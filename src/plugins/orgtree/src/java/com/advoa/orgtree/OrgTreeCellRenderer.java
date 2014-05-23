package com.advoa.orgtree;

import java.awt.Component;
import java.awt.Font;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.jivesoftware.spark.component.JiveTreeNode;

//机构节点
public class OrgTreeCellRenderer extends DefaultTreeCellRenderer {

/**
* 
*/
private static final long serialVersionUID = 1759820655239259659L;
private Object value;

    /**
     * Empty Constructor.
     */
    public OrgTreeCellRenderer() {
    }

    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        this.value = value;

        final Component c = super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);


        //设置图标
        setIcon(getCustomIcon());

        //用中文字体解决乱码问题
        // Root Nodes are always bold
        JiveTreeNode node = (JiveTreeNode)value;
        if (node.getAllowsChildren()) {
            setFont(new Font("宋体", Font.BOLD, 13));
        }
        else {
            setFont(new Font("宋体", Font.PLAIN, 13));
        }


        return c;
    }

    //取得图标
    private Icon getCustomIcon() {
        if (value instanceof JiveTreeNode) {
            JiveTreeNode node = (JiveTreeNode)value;
            return node.getClosedIcon();
        }
        return null;
    }

    //关闭的图标
    public Icon getClosedIcon() {
        return getCustomIcon();
    }

    public Icon getDefaultClosedIcon() {
        return getCustomIcon();
    }

    //叶子的图标
    public Icon getDefaultLeafIcon() {
        return getCustomIcon();
    }

    public Icon getDefaultOpenIcon() {
        return getCustomIcon();
    }

    public Icon getLeafIcon() {
        return getCustomIcon();
    }

    //打开的图标
    public Icon getOpenIcon() {
        return getCustomIcon();
    }

}