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
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.jivesoftware.Spark;
import org.jivesoftware.spark.component.VerticalFlowLayout;
import org.jivesoftware.spark.util.ResourceUtils;

public class AdvOAPreferenceDialog extends JPanel implements
	ActionListener {
    private static final long serialVersionUID = -1836601903928057855L;

    private JLabel oauserLabel = new JLabel();
	private JLabel passwordLabel = new JLabel();
	private JTextField oauserField;
	private JPasswordField passwordField;
	private JCheckBox alertEnabled;
	private JCheckBox bubble;
	private JCheckBox sound;
	private JPanel spellPanel;
	private JPanel setPanel;
	private Properties props;
	private File configFile;
	ArrayList<String> languages;

	public AdvOAPreferenceDialog() {
		props = new Properties();
		try {
			props.load(new FileInputStream(getConfigFile()));
		} catch (IOException e) {
			// Can't load ConfigFile
		}
		spellPanel = new JPanel();
		setPanel = new JPanel();
		alertEnabled = new JCheckBox();
		sound = new JCheckBox();
		oauserField = new JTextField();
		passwordField = new JPasswordField();
		spellPanel.setLayout(new GridBagLayout());
		setPanel.setLayout(new GridBagLayout());
		oauserField.setText(props.getProperty("username"));
		passwordField.setText(props.getProperty("password"));

		bubble = new JCheckBox();
		bubble.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setBubbleSelection(bubble.isSelected());
			}
		});

		alertEnabled.addActionListener(this);

		spellPanel.add(oauserLabel, new GridBagConstraints(1, 0, 3, 1, 0.0,
				0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		spellPanel.add(oauserField, new GridBagConstraints(5, 0, 1, 1, 1.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 300), 150, 0));
		spellPanel.add(passwordLabel, new GridBagConstraints(1, 1, 3, 1, 0.0,
				0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		spellPanel.add(passwordField, new GridBagConstraints(5, 1, 1, 1, 1.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 300), 150, 0));

		setPanel.add(alertEnabled, new GridBagConstraints(0, 0, 2, 1, 1.0, 1.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		setPanel.add(sound, new GridBagConstraints(0, 1, 2, 1, 1.0, 1.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		setPanel.add(bubble, new GridBagConstraints(0, 2, 2, 1, 1.0, 1.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));

		ResourceUtils.resButton(alertEnabled, "是否启用");
		ResourceUtils.resLabel(oauserLabel, oauserField, "用户名:");
		ResourceUtils.resLabel(passwordLabel, passwordField, "口令:");

		ResourceUtils.resButton(bubble, "气泡");
		ResourceUtils.resButton(sound, "声音");

		setLayout(new VerticalFlowLayout());
		spellPanel.setBorder(BorderFactory.createTitledBorder("基本信息"));
		setPanel.setBorder(BorderFactory.createTitledBorder("提醒设置"));
		add(spellPanel);
		add(setPanel);
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
			bubble.setSelected(true);
			sound.setSelected(true);
			bubble.setEnabled(true);
			sound.setEnabled(true);
		} else {
			bubble.setSelected(false);
			sound.setSelected(false);
			bubble.setEnabled(false);
			sound.setEnabled(false);
		}
	}

	public void setSpellCheckingEnabled(boolean enable) {
		alertEnabled.setSelected(enable);
		updateUI(enable);
	}

	// 获取用户名
	public String getUserName() {
		return oauserField.getText();
	}

	public void setUserName(String name) {
		oauserField.setText(name);
	}

	public String getPassword() {
		return String.valueOf(passwordField.getPassword());
	}

	public void setPassword(String password) {
		passwordField.setText(password);
	}

	// 获取声音选项
	public boolean getSoundSelection() {
		return sound.isSelected();
	}

	public void setSoundSelection(boolean show) {
		sound.setSelected(show);
	}

	// 是否开启
	public boolean isSpellCheckingEnabled() {
		return alertEnabled.isSelected();
	}

	// 气泡
	public boolean getBubbleSelection() {
		return bubble.isSelected();
	}

	public void setBubbleSelection(boolean ignore) {
		bubble.setSelected(ignore);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		updateUI(alertEnabled.isSelected());
	}
}
