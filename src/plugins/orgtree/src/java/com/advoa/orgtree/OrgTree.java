package com.advoa.orgtree;

import javax.swing.*;

import java.awt.event.*;
import java.awt.*;

import javax.swing.tree.*;
import javax.swing.event.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.spark.SparkManager;

//组织机构树
public class OrgTree extends JPanel implements TreeModelListener,
		ActionListener, java.io.Serializable {
	private static final long serialVersionUID = 5238403584089521528L;
	private JPopupMenu menu = null;
	private JMenuItem itemAdd = null, itemSendMess = null;
	private JTree tree1 = null;
	private DefaultTreeModel treeModel = null;
	private OrgTreeNode Root = new OrgTreeNode("邮政地址目录");
	private OrgTreeNode sonTreeNode = null;
	SAXReader saxReader = null, URIsaxReader = null;;

	// 初始化机构树
	OrgTree() {
		try {
			saxReader = new SAXReader();
			Document document = saxReader.read(URIName());
			Element root = document.getRootElement();
			if (root != null) {
				Iterator<Element> iter = root.elementIterator();
				if (iter.hasNext()) {
					while (iter.hasNext()) {
						Element sonElement = (Element) iter.next();
						sonTreeNode = createTreeNode(sonElement);
						Root.add(sonTreeNode);
					}
				} else {
					JOptionPane.showMessageDialog(null, "此XML文件只有一个根节点！");
				}
			} else {
				JOptionPane.showMessageDialog(null, "此XML文件是一个空文件！");
			}
		} catch (DocumentException e) {
		}
		JPanel panel = new JPanel();
		menu = new JPopupMenu();
		itemAdd = new JMenuItem("添加联系人");
		itemSendMess = new JMenuItem("发送即时信息");
		menu.add(itemAdd);
		menu.add(itemSendMess);
		itemAdd.addActionListener(this);
		itemSendMess.addActionListener(this);
		tree1 = new JTree(Root);
		tree1.setEditable(false);
		tree1.setRootVisible(true);
		tree1.putClientProperty("JTree.lineStyle", "Angle");
		tree1.addMouseListener(new MouseHandle());
		
		treeModel = (DefaultTreeModel) tree1.getModel();
		treeModel.addTreeModelListener(this);
		tree1.setVisible(true);
		
//		DefaultTreeCellRenderer render = (DefaultTreeCellRenderer) tree1
//				.getCellRenderer();
		final OrgTreeCellRenderer render = new OrgTreeCellRenderer();
		//render.setDisabledIcon();
		render.setLeafIcon(OrgTreePlugin.getUserIcon());// 设置用户的图标
		render.setOpenIcon(OrgTreePlugin.getNolnopenicon());
		render.setClosedIcon(OrgTreePlugin.getNolnCloseicon());
		tree1.setCellRenderer(render);		
		
		tree1.addTreeExpansionListener(new TreeExpansionListener() {
			
			@Override
			public void treeExpanded(TreeExpansionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void treeCollapsed(TreeExpansionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		// 排版
		setLayout(new BorderLayout());
		panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.setBackground(Color.white);

		final JScrollPane treeScroller = new JScrollPane(tree1);// 滚动条
		treeScroller.setBorder(BorderFactory.createEmptyBorder());
		panel.add(treeScroller, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5,
						5, 5, 5), 0, 0));// 设置边界
		add(panel, BorderLayout.CENTER);
	}

	public static void main(String args[]) {
		new OrgTree();
	}

	public void treeNodesChanged(TreeModelEvent e) {
	}

	public void treeNodesInserted(TreeModelEvent e) {
	}

	public void treeNodesRemoved(TreeModelEvent e) {
	}

	public void treeStructureChanged(TreeModelEvent e) {
	}

	//鼠标点击事件
	class MouseHandle extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			try {
				if (e.getModifiers() == InputEvent.BUTTON3_MASK) {
					menu.show(tree1, e.getX(), e.getY());
				}
				
			} catch (NullPointerException ne) {

			}
		}
		// public void mouseClicked(MouseEvent e){}
		// public void mouseReleased(MouseEvent e) {}
		// public void mouseEntered(MouseEvent e) {}
		// public void mouseExited(MouseEvent e) {}
	}

	public void actionPerformed(ActionEvent e) {
		TreePath treepath = tree1.getSelectionPath();
		if (treepath != null) {
			OrgTreeNode selectionNode = (OrgTreeNode) treepath
					.getLastPathComponent();
			String eMail = (String) selectionNode.geteMail();
			OperateLinkMan operateLinkMan = new OperateLinkMan(e, itemAdd,
					itemSendMess);
			operateLinkMan.search(eMail);
			// System.out.println(operateLinkMan.getJID());
			// Presence presence = new Presence(Presence.Type.available);
			// presence.setStatus("Gone fishing");
			// System.out.println(presence.getFrom());
			// ConnectionUtils.getConnection().sendPacket(presence);
		} else {
			JOptionPane.showMessageDialog(null, "树的路径为空！");
		}
	}

	public OrgTreeNode createTreeNode(Element root) {
		Attribute nameAttr = null, emailAttr = null;
		OrgTreeNode hasCreNode = null, parentNode = null, leafNode = null;
		Iterator<Element> iter = root.elementIterator();
		if (iter.hasNext()) {
			nameAttr = root.attribute("n");
			String name = nameAttr.getValue();
			parentNode = new OrgTreeNode(name);
			while (iter.hasNext()) {
				Element sonElement = (Element) iter.next();
				hasCreNode = createTreeNode(sonElement);
				parentNode.add(hasCreNode);
			}
			return parentNode;
		} else {
			nameAttr = root.attribute("n");
			emailAttr = root.attribute("m");
			if (nameAttr != null && emailAttr != null) {
				String name = nameAttr.getValue();
				String email = emailAttr.getValue();
				leafNode = new OrgTreeNode(name, email);
			}
			return leafNode;
		}
	}

	public String URIName() {
		String uriName = null;
		String h = "http://"; // uriName的网页表头
		String openfireIP = null; // openfire的IP地址
		String fileName = ":9090/oapluginconfig.xml";
		String xmlURL = null;
		URIsaxReader = new SAXReader();
		try {

			openfireIP = SparkManager.getConnection().getHost();
			uriName = h + openfireIP + fileName;
			Document document = URIsaxReader.read(uriName);
			List<? extends Node> list = document.selectNodes("//root/orgtree");
			for (Node node : list) {
				xmlURL = node.getText();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return xmlURL;
	}

	
}
