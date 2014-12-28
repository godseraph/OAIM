/**
 * $RCSfile: ,v $
 * $Revision: $
 * $Date: $
 * 
 * Copyright (C) 2004-2011 Jive Software. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.advoa.sparkplugin;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JApplet;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.dom4j.Element;
import org.dom4j.Node;
import org.jivesoftware.Spark;
import org.jivesoftware.spark.component.VerticalFlowLayout;
import org.jivesoftware.spark.util.ResourceUtils;

public class AdvOAPreferenceDialog extends JPanel implements ActionListener {
	private static final long serialVersionUID = -1836601903928057855L;

	private JLabel serverLabel = new JLabel("服务器：");
	private JLabel oauserLabel = new JLabel();
	private JLabel passwordLabel = new JLabel();
	private JLabel loginLabel = new JLabel("登录地址：");
	private JLabel mailLabel = new JLabel("新邮件提醒声音选择：");
	private JLabel gwLabel = new JLabel("待办件提醒声音选择：");

	private JTextField oauserField;
	private JPasswordField passwordField;
	private JComboBox<String> serverBox;
	private JComboBox<String> loginBox;
	private JComboBox<String> mailSound;
	private JComboBox<String> gwSound;
	//private JCheckBox alertEnabled;
	private JCheckBox bubbleCheck;
	private JCheckBox soundCheck;
	private JCheckBox buttonCheck;
	private JPanel infoPanel;
	private JPanel alertPanel;
	private JPanel soundPanel;
	private Properties props;
	private File configFile;
	ArrayList<String> languages;
	private XmlUtil util;

	@SuppressWarnings("null")
	public AdvOAPreferenceDialog() {
		props = new Properties();
		try {
			props.load(new FileInputStream(getConfigFile()));
		} catch (IOException e) {
			// Can't load ConfigFile
		}

		util = new XmlUtil();
		List<? extends Node> serverlist = util
				.getList("//root/oaalert/servers/oaserver");

		String[] sound = { "蜂鸣", "短促音", "鸟鸣", "门铃", "击打", "深海探测", "招呼", "风琴",
				"短鸣", "静音" };
		infoPanel = new JPanel();
		alertPanel = new JPanel();
		soundPanel = new JPanel();

		serverBox = new JComboBox<String>(util.getAttr(serverlist, "name"));

		loginBox = new JComboBox<String>(util.getDownAttr(serverlist, serverBox
				.getSelectedItem().toString()));
		mailSound = new JComboBox<String>(sound);
		gwSound = new JComboBox<String>(sound);
		soundCheck = new JCheckBox();
		buttonCheck = new JCheckBox();
		oauserField = new JTextField();
		passwordField = new JPasswordField();
		infoPanel.setLayout(new GridBagLayout());
		alertPanel.setLayout(new GridBagLayout());
		soundPanel.setLayout(new GridBagLayout());
		oauserField.setText(props.getProperty("username"));
		passwordField.setText(props.getProperty("password"));

		bubbleCheck = new JCheckBox();
		bubbleCheck.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setBubbleSelection(bubbleCheck.isSelected());
			}
		});
		soundCheck.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setSoundSelection(soundCheck.isSelected());
			}
		});

		serverBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				loginBox.removeAllItems();
				Vector<String> items = util.getDownAttr(
						util.getList("//root/oaalert/servers/oaserver"),
						serverBox.getSelectedItem().toString());
				for (int i = 0; i < items.size(); i++) {
					loginBox.addItem(util.getDownAttr(
							util.getList("//root/oaalert/servers/oaserver"),
							serverBox.getSelectedItem().toString()).get(i));
				}
			}
		});

		// 基本信息
		infoPanel.add(serverLabel, new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		infoPanel.add(serverBox, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		infoPanel.add(oauserLabel, new GridBagConstraints(0, 1, 3, 1, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		infoPanel.add(oauserField, new GridBagConstraints(5, 1, 1, 1, 1.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5,
						5, 5, 300), 150, 0));
		infoPanel.add(passwordLabel, new GridBagConstraints(0, 2, 3, 1, 0.0,
				0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		infoPanel.add(passwordField, new GridBagConstraints(5, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 300), 150, 0));
		infoPanel.add(loginLabel, new GridBagConstraints(0, 3, 3, 1, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		infoPanel.add(loginBox, new GridBagConstraints(5, 3, 3, 1, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));

		// 弹窗提醒
		alertPanel.add(bubbleCheck, new GridBagConstraints(0, 0, 2, 1, 1.0,
				1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		alertPanel.add(buttonCheck, new GridBagConstraints(0, 1, 2, 1, 1.0,
				1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));

		// 声音提醒
		soundPanel.add(soundCheck, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		soundPanel.add(mailLabel, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		soundPanel.add(mailSound, new GridBagConstraints(3, 1, 2, 1, 1.0, 1.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		soundPanel.add(gwLabel, new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		soundPanel.add(gwSound, new GridBagConstraints(3, 2, 2, 1, 1.0, 1.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));

		ResourceUtils.resLabel(oauserLabel, oauserField, "用户名：");
		ResourceUtils.resLabel(passwordLabel, passwordField, "口令：");

		ResourceUtils.resButton(bubbleCheck, "是否开启弹窗提醒");
		ResourceUtils.resButton(soundCheck, "是否开启声音提醒");
		ResourceUtils.resButton(buttonCheck, "是否显示OA登录按钮");

		setLayout(new VerticalFlowLayout());

		infoPanel.setBorder(BorderFactory.createTitledBorder("基本信息"));
		alertPanel.setBorder(BorderFactory.createTitledBorder("弹窗提醒"));
		soundPanel.setBorder(BorderFactory.createTitledBorder("声音提醒"));

		add(infoPanel);
		add(alertPanel);
		add(soundPanel);
	}

	private File getConfigFile() {
		// TODO Auto-generated method stub
		if (configFile == null)
			configFile = new File(Spark.getSparkUserHome(),
					"spellchecking.properties");

		return configFile;
	}

	public void updateUI(boolean enable) {
		if (enable) {
			bubbleCheck.setSelected(true);
			soundCheck.setSelected(true);
			bubbleCheck.setEnabled(true);
			soundCheck.setEnabled(true);
		} else {
			bubbleCheck.setSelected(false);
			soundCheck.setSelected(false);
			bubbleCheck.setEnabled(false);
			soundCheck.setEnabled(false);
		}
	}


	// 获取用户名
	public String getUserName() {
		return oauserField.getText();
	}

	public void setUserName(String name) {
		oauserField.setText(name);
	}

	public boolean getButtonCheck() {
		return buttonCheck.isSelected();
	}

	public void setButtonCheck(boolean show) {
		buttonCheck.setSelected(show);
	}

	//
	public String getServer() {
		return serverBox.getSelectedItem().toString();
	}

	public void setServer(String server) {
		serverBox.setSelectedItem(server);
	}

	//
	public String getLogin() {
		return loginBox.getSelectedItem().toString();
	}

	public void setLogin(String login) {
		loginBox.setSelectedItem(login);
	}

	//
	public String getMailSound() {
		return mailSound.getSelectedItem().toString();
	}

	public void setMailSound(String mail) {
		mailSound.setSelectedItem(mail);
	}

	//
	public String getGwSound() {
		return gwSound.getSelectedItem().toString();
	}

	public void setGwSound(String gw) {
		gwSound.setSelectedItem(gw);
	}

	public String getPassword() {
		return String.valueOf(passwordField.getPassword());
	}

	public void setPassword(String password) {
		passwordField.setText(password);
	}

	// 获取声音选项
	public boolean getSoundSelection() {
		return soundCheck.isSelected();
	}

	public void setSoundSelection(boolean show) {
		soundCheck.setSelected(show);
		mailSound.setEnabled(show);
		gwSound.setEnabled(show);
	}


	// 气泡
	public boolean getBubbleSelection() {
		return bubbleCheck.isSelected();
	}

	public void setBubbleSelection(boolean ignore) {
		bubbleCheck.setSelected(ignore);
		buttonCheck.setEnabled(ignore);
	}
	

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
	}
}
