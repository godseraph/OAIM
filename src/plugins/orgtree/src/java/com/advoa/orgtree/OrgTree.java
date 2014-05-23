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

//��֯������
public class OrgTree extends JPanel {
    /**
     * 
     */
    private static final long serialVersionUID = 5238403584089521528L;

    private final Tree orgTree;// �������ؼ�
    private final OrgTreeNode rootNode = new OrgTreeNode("��֯");// ���ڵ�
    private final DefaultTreeModel treeModel;// ��ģ��

    // ��ʼ��������
    public OrgTree() {

        // ���ڵ�
        rootNode.setUnitid(null);
        rootNode.setSuperunitid1(null);
        rootNode.setVisited(false);
        rootNode.setAllowsChildren(true);// �������ӽڵ�
        rootNode.setIcon(OrgTreePlugin.getOrganIcon());// ͼ��

        // ������
        orgTree = new Tree(rootNode);
        orgTree.setShowsRootHandles(true); // ��ʾ�������ߵĿ����ֱ�
        orgTree.collapseRow(0); // ��ʼʱֻ��ʾ�����
        orgTree.setCellRenderer(new OrgTreeCellRenderer());
        // ������չ���¼�,�����첽����
        orgTree.addTreeExpansionListener(new TreeExpansionListener() {
            @Override
            public void treeExpanded(TreeExpansionEvent event) {
                // ���Ȼ�ȡչ���Ľ��
                final OrgTreeNode expandNode = (OrgTreeNode) event.getPath()
                        .getLastPathComponent();

                // �жϸýڵ��Ƿ��Ѿ������ʹ�
                // �ǡ������赽���ݿ��ж�ȡ��ʲô��Ҳ����
                // �񡪡���ʼ�첽����
                if (!expandNode.getVisited()) {
                    expandNode.setVisited(true); // �ȸı�visited�ֶε�״̬
                    orgTree.setEnabled(false); // ��ʱ����JTree

                    // ʹ��swingworker���
                    new SwingWorker<Long, Void>() {
                        @Override
                        protected Long doInBackground() throws Exception {
                            return asynchLoad(expandNode);
                        }

                        @Override
                        protected void done() {
                            treeModel.removeNodeFromParent(expandNode
                                    .getFirstLeaf()); // ������Ϻ�Ҫɾ����������...�����
                            treeModel.nodeStructureChanged(expandNode); // ֪ͨ��ͼ�ṹ�ı�

                            orgTree.setEnabled(true);//��������JTree
                        }
                    }.execute();

                }
            }

            @Override
            public void treeCollapsed(TreeExpansionEvent event) {
            }
        });

        treeModel = (DefaultTreeModel) orgTree.getModel();// ��ģ��

        //�Ű�
        setLayout(new BorderLayout());

        final JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.white);

        final JScrollPane treeScroller = new JScrollPane(orgTree);// ������
        treeScroller.setBorder(BorderFactory.createEmptyBorder());
        panel.add(treeScroller, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5,
                        5, 5, 5), 0, 0));// ���ñ߽�

        add(panel, BorderLayout.CENTER);

        rootNode.add(new OrgTreeNode("������..."));// ������ʾ���ڼ���
    }

    // �����ݱ��ж�ȡexpandNode���ӽ��.����ֵΪ����ʱ��
    private long asynchLoad(OrgTreeNode expandNode) {
        long handleTime = 0L; // �����첽���صĴ���ʱ��
        long start = System.currentTimeMillis(); // ��ʼ�����ʱ��
        try {
            Thread.sleep(1000); // sleepһ��ʱ���Ա㿴�����������

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
                        node.setAllowsChildren(true);// ����
                    } else if ("staff".equals(node.getType())) {
                        node.setAllowsChildren(false);// ��Ա
                        if (u.containsKey("loginid")) {
                            node.setLoginid(u.getString("loginid"));// ��½�˺�
                        }
                    }

                    node.setVisited(false);
                    node.setAllowsChildren(true);// �������ӽڵ�
                    if ("unit".equals(node.getType())) {// ����
                        node.setIcon(OrgTreePlugin.getOrganIcon());// ͼ��
                    } else if ("staff".equals(node.getType())) {// ��Ա
                        node.setIcon(OrgTreePlugin.getUserIcon());// ͼ��
                    }
                    expandNode.add(node);
                    if ("unit".equals(node.getType())) {
                        node.add(new OrgTreeNode("������..."));// ������ʾ���ڼ���
                    }
                }
            }

        } catch (Exception ex) {
            Log.error("", ex);
        } finally {
            handleTime = System.currentTimeMillis() - start; // ���������ʱ��
        }
        return handleTime;

    }

    private JSONArray getOrgTreeJSON(String unitid) {// ȡ�÷�����֯�ܹ�
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

    private String getOrgUrl(String unitid) {// ȡ�÷�����֯�ܹ���url
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