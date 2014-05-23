package com.advoa.orgtree;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingWorker;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.component.JiveTreeNode;
import org.jivesoftware.spark.component.Tree;
import org.jivesoftware.spark.util.log.Log;

//组织机构树
public class OrgTree extends JPanel {
    /**
     * 
     */
    private static final long serialVersionUID = 5238403584089521528L;

    private final Tree orgTree;// 机构树控件
    private final OrgTreeNode rootNode = new OrgTreeNode("组织");// 根节点
    private final DefaultTreeModel treeModel;// 树模型

    // 初始化机构树
    public OrgTree() {

        // 根节点
        rootNode.setUnitid(null);
        rootNode.setSuperunitid1(null);
        rootNode.setVisited(false);
        rootNode.setAllowsChildren(true);// 允许有子节点
        rootNode.setIcon(OrgTreePlugin.getOrganIcon());// 图标

        // 机构树
        orgTree = new Tree(rootNode);
        orgTree.setShowsRootHandles(true); // 显示根结点左边的控制手柄
        orgTree.collapseRow(0); // 初始时只显示根结点
        orgTree.setCellRenderer(new OrgTreeCellRenderer());
        // 覆盖树展开事件,进行异步加载
        orgTree.addTreeExpansionListener(new TreeExpansionListener() {
            @Override
            public void treeExpanded(TreeExpansionEvent event) {
                // 首先获取展开的结点
                final OrgTreeNode expandNode = (OrgTreeNode) event.getPath()
                        .getLastPathComponent();

                // 判断该节点是否已经被访问过
                // 是——无需到数据库中读取、什么事也不做
                // 否——开始异步加载
                if (!expandNode.getVisited()) {
                    expandNode.setVisited(true); // 先改变visited字段的状态
                    orgTree.setEnabled(false); // 暂时禁用JTree

                    // 使用swingworker框架
                    new SwingWorker<Long, Void>() {
                        @Override
                        protected Long doInBackground() throws Exception {
                            return asynchLoad(expandNode);
                        }

                        @Override
                        protected void done() {
                            treeModel.removeNodeFromParent(expandNode
                                    .getFirstLeaf()); // 加载完毕后要删除“载入中...”结点
                            treeModel.nodeStructureChanged(expandNode); // 通知视图结构改变

                            orgTree.setEnabled(true);//重新启用JTree
                        }
                    }.execute();

                }
            }

            @Override
            public void treeCollapsed(TreeExpansionEvent event) {
            }
        });

        treeModel = (DefaultTreeModel) orgTree.getModel();// 树模型

        //排版
        setLayout(new BorderLayout());

        final JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.white);

        final JScrollPane treeScroller = new JScrollPane(orgTree);// 滚动条
        treeScroller.setBorder(BorderFactory.createEmptyBorder());
        panel.add(treeScroller, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5,
                        5, 5, 5), 0, 0));// 设置边界

        add(panel, BorderLayout.CENTER);

        rootNode.add(new OrgTreeNode("加载中..."));// 用于显示正在加载
    }

    // 从数据表中读取expandNode的子结点.返回值为处理时长
    private long asynchLoad(OrgTreeNode expandNode) {
        long handleTime = 0L; // 本次异步加载的处理时长
        long start = System.currentTimeMillis(); // 开始处理的时刻
        try {
            Thread.sleep(1000); // sleep一段时间以便看清楚整个过程

            JSONArray childJSON = this.getOrgTreeJSON(expandNode.getUnitid());
            if (childJSON != null && childJSON.size() > 0) {

                for (int i = 0, s = childJSON.size(); i < s; i++) {
                    JSONObject u = childJSON.getJSONObject(i);
                    OrgTreeNode node = new OrgTreeNode(u.getString("unitname"));
                    node.setUnitid(u.getString("unitid"));
                    if (u.containsKey("superunitid1")) {
                        node.setSuperunitid1(u.getString("superunitid1"));
                    }
                    node.setType(u.getString("type"));
                    if ("unit".equals(node.getType())) {
                        node.setAllowsChildren(true);// 机构
                    } else if ("staff".equals(node.getType())) {
                        node.setAllowsChildren(false);// 人员
                        if (u.containsKey("loginid")) {
                            node.setLoginid(u.getString("loginid"));// 登陆账号
                        }
                    }

                    node.setVisited(false);
                    node.setAllowsChildren(true);// 允许有子节点
                    if ("unit".equals(node.getType())) {// 机构
                        node.setIcon(OrgTreePlugin.getOrganIcon());// 图标
                    } else if ("staff".equals(node.getType())) {// 人员
                        node.setIcon(OrgTreePlugin.getUserIcon());// 图标
                    }
                    expandNode.add(node);
                    if ("unit".equals(node.getType())) {
                        node.add(new OrgTreeNode("加载中..."));// 用于显示正在加载
                    }
                }
            }

        } catch (Exception ex) {
            Log.error("", ex);
        } finally {
            handleTime = System.currentTimeMillis() - start; // 计算出处理时长
        }
        return handleTime;

    }

    private JSONArray getOrgTreeJSON(String unitid) {// 取得返回组织架构
        JSONArray result = new JSONArray();
        HttpClient httpClient = new HttpClient();
        GetMethod getMethod = new GetMethod(this.getOrgUrl(unitid));
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler());
        try {
            httpClient.executeMethod(getMethod);

            byte[] responseBody = getMethod.getResponseBody();
            String responseMsg = new String(responseBody, "GBK");
            result = JSONArray.fromObject(responseMsg);
        } catch (HttpException e) {
            // TODO Auto-generated catch block
            Log.error("", e);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.error("", e);
        } finally {

            getMethod.releaseConnection();
        }

        return result;
    }

    private String getOrgUrl(String unitid) {// 取得返回组织架构的url
        String host = SparkManager.getConnection().getHost();
        StringBuffer url = new StringBuffer("http://");
        url.append(host);
        url.append(":9090/plugins/orgtree/orgtreeservlet");
        if (unitid != null) {
            url.append("?unitid=");
            url.append(unitid);
        }
        return url.toString();
    }
 
    
}