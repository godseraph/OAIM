package com.advoa.spark.plugin;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;
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

	@Override
	public void initialize() {
		//插件初始化
		Workspace workspace = SparkManager.getWorkspace();
		SparkTabbedPane tabbedPane = workspace.getWorkspacePane();
		JTextPane textPane = new JTextPane();
		Document doc = textPane.getDocument();
		SimpleAttributeSet attrSet = new SimpleAttributeSet();
		// 颜色
		StyleConstants.setForeground(attrSet, Color.RED);
		// 加粗
		StyleConstants.setBold(attrSet, true);
		// 字体大小
		StyleConstants.setFontSize(attrSet, 25);
		StyleConstants.setBackground(attrSet, Color.YELLOW);
		String content = "Hello World";
		try {
			doc.insertString(doc.getLength(), content, attrSet);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		// Add own Tab.
		tabbedPane.addTab("介绍", new Icon() {
			 @ Override
			public void paintIcon(Component c, Graphics g, int x, int y) {
				// TODO Auto-generated method stub
			}
			 @ Override
			public int getIconWidth() {
				// TODO Auto-generated method stub
				return 0;
			}
			 @ Override
			public int getIconHeight() {
				// TODO Auto-generated method stub
				return 0;
			}
		}, textPane);
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
