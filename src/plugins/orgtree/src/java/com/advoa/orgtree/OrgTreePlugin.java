package com.advoa.orgtree;

import java.net.URL;

import javax.swing.ImageIcon;

import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.Workspace;
import org.jivesoftware.spark.component.tabbedPane.SparkTabbedPane;
import org.jivesoftware.spark.plugin.Plugin;

//չʾOA����֯�ṹ
public class OrgTreePlugin implements Plugin {

	private static ImageIcon organ_icon = null;

	public static ImageIcon getOrganIcon() {// ����ͼ��
		if (organ_icon == null) {
			ClassLoader cl = OrgTreePlugin.class.getClassLoader();
			URL imageURL = cl.getResource("images/organ.png");
			organ_icon = new ImageIcon(imageURL);
		}
		return organ_icon;
	}

	private static ImageIcon user_icon = null;

	public static ImageIcon getUserIcon() { // �û�Ҷ�ӽڵ�ͼ��
		if (user_icon == null) {
			ClassLoader cl = OrgTreePlugin.class.getClassLoader();
			URL imageURL = cl.getResource("images/user.png");
			user_icon = new ImageIcon(imageURL);
		}
		return user_icon;
	}

	private static ImageIcon nolnOpen_icon = null;

	public static ImageIcon getNolnopenicon() { // ��Ҷ�ӽڵ��ͼ��
		if (nolnOpen_icon == null) {
			ClassLoader cl = OrgTreePlugin.class.getClassLoader();
			URL imageURL = cl.getResource("images/folder_into.png");
			nolnOpen_icon = new ImageIcon(imageURL);
		}
		return nolnOpen_icon;
	}

	private static ImageIcon nolnClose_icon = null;

	public static ImageIcon getNolnCloseicon() { // ��Ҷ�ӽڵ��ͼ��
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

		OrgTree orgTreePanel = new OrgTree();// ������

		// Add own Tab.
		tabbedPane.addTab("����Ŀ¼", OrgTreePlugin.getOrganIcon(), orgTreePanel);
	}

	@Override
	public void shutdown() {
	}

	// �Ƿ�ɹر�
	@Override
	public boolean canShutDown() {
		return false;
	}

	// ж��
	public void uninstall() {
	}

}