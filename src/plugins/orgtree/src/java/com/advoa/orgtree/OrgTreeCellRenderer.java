package com.advoa.orgtree;

import java.awt.Component;
import java.awt.Font;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.jivesoftware.spark.component.JiveTreeNode;

//�����ڵ�
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


        //����ͼ��
        setIcon(getCustomIcon());

        //��������������������
        // Root Nodes are always bold
        JiveTreeNode node = (JiveTreeNode)value;
        if (node.getAllowsChildren()) {
            setFont(new Font("����", Font.BOLD, 13));
        }
        else {
            setFont(new Font("����", Font.PLAIN, 13));
        }


        return c;
    }

    //ȡ��ͼ��
    private Icon getCustomIcon() {
        if (value instanceof JiveTreeNode) {
            JiveTreeNode node = (JiveTreeNode)value;
            return node.getClosedIcon();
        }
        return null;
    }

    //�رյ�ͼ��
    public Icon getClosedIcon() {
        return getCustomIcon();
    }

    public Icon getDefaultClosedIcon() {
        return getCustomIcon();
    }

    //Ҷ�ӵ�ͼ��
    public Icon getDefaultLeafIcon() {
        return getCustomIcon();
    }

    public Icon getDefaultOpenIcon() {
        return getCustomIcon();
    }

    public Icon getLeafIcon() {
        return getCustomIcon();
    }

    //�򿪵�ͼ��
    public Icon getOpenIcon() {
        return getCustomIcon();
    }

}