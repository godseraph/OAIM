package com.advoa.orgtree;

import java.net.URL;

import javax.swing.ImageIcon;

import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.Workspace;
import org.jivesoftware.spark.component.tabbedPane.SparkTabbedPane;
import org.jivesoftware.spark.plugin.Plugin;

//展示OA的组织结构
public class OrgTreePlugin implements Plugin {

	private static ImageIcon organ_icon = null;

	public static ImageIcon getOrganIcon() {// 机构图标
		if (organ_icon == null) {
			ClassLoader cl = OrgTreePlugin.class.getClassLoader();
			URL imageURL = cl.getResource("images/organ.png");
			organ_icon = new ImageIcon(imageURL);
		}
		return organ_icon;
	}

	private static ImageIcon user_icon = null;

	public static ImageIcon getUserIcon() { // 用户叶子节点图标
		if (user_icon == null) {
			ClassLoader cl = OrgTreePlugin.class.getClassLoader();
			URL imageURL = cl.getResource("images/user.png");
			user_icon = new ImageIcon(imageURL);
		}
		return user_icon;
	}

	private static ImageIcon nolnOpen_icon = null;

	public static ImageIcon getNolnopenicon() { // 非叶子节点打开图标
		if (nolnOpen_icon == null) {
			ClassLoader cl = OrgTreePlugin.class.getClassLoader();
			URL imageURL = cl.getResource("images/folder_into.png");
			nolnOpen_icon = new ImageIcon(imageURL);
		}
		return nolnOpen_icon;
	}

	private static ImageIcon nolnClose_icon = null;

	public static ImageIcon getNolnCloseicon() { // 非叶子节点打开图标
		if (nolnClose_icon == null) {
			ClassLoader cl = OrgTreePlugin.class.getClassLoader();
			URL imageURL = cl.getResource("images/folder_lock.png");
			nolnClose_icon = new ImageIcon(imageURL);
		}
		return nolnClose_icon;
	}

	public void initialize() {
		Workspace workspace = SparkManager.getWorkspace();
		SparkTabbedPane tabbedPane = workspace.getWorkspacePane();

		OrgTree orgTreePanel = new OrgTree();// 机构树

		// Add own Tab.
		tabbedPane.addTab("邮政目录", OrgTreePlugin.getOrganIcon(), orgTreePanel);
	}

	@Override
	public void shutdown() {
	}

	// 是否可关闭
	@Override
	public boolean canShutDown() {
		return false;
	}

	// 卸载
	public void uninstall() {
	}

}