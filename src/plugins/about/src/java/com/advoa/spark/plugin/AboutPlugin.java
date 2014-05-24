package com.advoa.spark.plugin;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.Workspace;
import org.jivesoftware.spark.component.tabbedPane.SparkTabbedPane;
import org.jivesoftware.spark.plugin.Plugin;

public class AboutPlugin implements Plugin {

	private static ImageIcon  plugin_icon=null;        
	public static ImageIcon    getPluginIcon(){
		//���ͼ��
		if(plugin_icon==null){            
			ClassLoader  cl=AboutPlugin.class.getClassLoader();            
			URL   imageURL=cl.getResource("images/icon.gif");            
			plugin_icon=new   ImageIcon(imageURL);        
		}        
		return  plugin_icon;    
	}
	@Override
	public void initialize() {
		//�����ʼ��
		Workspace workspace = SparkManager.getWorkspace();
		SparkTabbedPane tabbedPane = workspace.getWorkspacePane();
		JTextPane textPane = new JTextPane();
		Document doc = textPane.getDocument();
		SimpleAttributeSet attrSet = new SimpleAttributeSet();
		// ��ɫ
		StyleConstants.setForeground(attrSet, Color.RED);
		// �Ӵ�
		StyleConstants.setBold(attrSet, true);
		// �����С
		StyleConstants.setFontSize(attrSet, 25);
		StyleConstants.setBackground(attrSet, Color.YELLOW);
		String content = "Hello World";
		try {
			doc.insertString(doc.getLength(), content, attrSet);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		// Add own Tab.
		tabbedPane.addTab("����", getPluginIcon(), textPane);
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean canShutDown() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void uninstall() {
		// TODO Auto-generated method stub

	}

}
