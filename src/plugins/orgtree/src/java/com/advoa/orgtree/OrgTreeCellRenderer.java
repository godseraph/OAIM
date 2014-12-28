package com.advoa.orgtree;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.InputEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.component.JiveTreeNode;

//机构节点
public class OrgTreeCellRenderer extends DefaultTreeCellRenderer {

	/**
* 
*/
	private static final long serialVersionUID = 1759820655239259659L;
	private Object value;
	private String openfireIP = SparkManager.getConnection().getHost();;

	private static final Icon nolIcon = new ImageIcon(OrgTreeCellRenderer.class
			.getClassLoader().getResource("images/offline.png"));

	private static final Icon onlineIcon = new ImageIcon(
			OrgTreeCellRenderer.class.getClassLoader().getResource(
					"images/online.png"));

	private static final Icon openIcon = new ImageIcon(
			OrgTreeCellRenderer.class.getClassLoader().getResource(
					"images/folder_into.png"));

	private static final Icon closeIcon = new ImageIcon(
			OrgTreeCellRenderer.class.getClassLoader().getResource(
					"images/folder_lock.png"));

	/**
	 * Empty Constructor.
	 */
	public OrgTreeCellRenderer() {
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		this.value = value;

		final Component c = super.getTreeCellRendererComponent(tree, value,
				selected, expanded, leaf, row, hasFocus);

		// 得到每个节点的TreeNode
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		setClosedIcon(closeIcon);
		setOpenIcon(openIcon);

		if (node.isRoot()) {
			setIcon(openIcon);
		} else if (!node.isLeaf()) {
			if(expanded){
				setIcon(openIcon);
			}else{
				setIcon(closeIcon);
			}
			// setOpenIcon(onlineIcon);
		}

		// 得到每个节点的text
		String str = node.toString();

		String strUrl = "http://"+openfireIP+":9090/plugins/presence/status?jid="
				+ str + "@oaim&type=xml";

		if (node.isLeaf()) {
			switch (IsUserOnLine(strUrl)) {
			case 0:
				setIcon(nolIcon);
				break;
			case 1:
				setIcon(onlineIcon);
				break;
			case 2:
				setIcon(nolIcon);
				break;
			default:
				break;
			}
		}

		return c;
	}

	// 取得图标
	private Icon getCustomIcon() {
		if (value instanceof JiveTreeNode) {
			JiveTreeNode node = (JiveTreeNode) value;
			return node.getClosedIcon();
		}
		return null;
	}

	// 关闭的图标
	public Icon getClosedIcon() {
		return getCustomIcon();
	}

	public Icon getDefaultClosedIcon() {
		return getCustomIcon();
	}

	// 叶子的图标
	public Icon getDefaultLeafIcon() {
		return getCustomIcon();
	}

	public Icon getDefaultOpenIcon() {
		return getCustomIcon();
	}

	public Icon getLeafIcon() {
		return getCustomIcon();
	}

	// 打开的图标
	public Icon getOpenIcon() {
		return getCustomIcon();
	}


	public static short IsUserOnLine(String strUrl) {
		short shOnLineState = 0; // 0-不存在- 1-在线- 2-不在线-
		try {
			URL oUrl = new URL(strUrl);
			URLConnection oConn = oUrl.openConnection();
			if (oConn != null) {
				BufferedReader oIn = new BufferedReader(new InputStreamReader(
						oConn.getInputStream()));
				if (null != oIn) {
					String strFlag = oIn.readLine();
					oIn.close();

					if (strFlag.indexOf("type=\"unavailable\"") >= 0) {
						shOnLineState = 2;
					}
					if (strFlag.indexOf("type=\"error\"") >= 0) {
						shOnLineState = 0;
					} else if (strFlag.indexOf("priority") >= 0
							|| strFlag.indexOf("id=\"") >= 0) {
						shOnLineState = 1;
					}
				}
			}
		} catch (Exception e) {
		}

		return shOnLineState;
	}
}