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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

import org.jivesoftware.Spark;

public class AdvOAPreferences {
	private Properties props;
	private File configFile;

	public AdvOAPreferences() {
		this.props = new Properties();

		try {
			props.load(new FileInputStream(getConfigFile()));
		} catch (IOException e) {
			// Can't load ConfigFile
		}

	}

	public File getConfigFile() {
		if (configFile == null)
			configFile = new File(Spark.getSparkUserHome(),
					"spellchecking.properties");

		return configFile;
	}

	public void save() {
		try {
			props.store(new FileOutputStream(getConfigFile()), "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 设置状态
	public void setStatus(boolean status) {
		setBoolean("status", status);
	}

	// 获取状态信息
	public boolean getStatus() {
		return getBoolean("status", false);
	}

	// 保存用户名
	public void setUserName(String userName) {
		props.setProperty("username", userName);
	}

	public String getUserName() {
		return props.getProperty("username");
	}

	// 保存密码
	public void setPassword(String password) {
		
		Blowfish bf = new Blowfish("AudO6l0swyzXDnk");
		if(!password.equals((props.getProperty("password"))))
				props.setProperty("password", bf.encrypt(password));
	}

	public String getPassword() {
		Blowfish bf = new Blowfish("AudO6l0swyzXDnk");
		return bf.decrypt(props.getProperty("password"));
	}

	public void setSpellLanguage(String name) {
		props.setProperty("selectedSpellLanguage", name);
	}

	public String getSpellLanguage() {
		return props.getProperty("selectedSpellLanguage", Locale.getDefault()
				.getLanguage());
	}
	
	//是否设置
	public void setSpellCheckerEnabled(boolean enabled) {
		setBoolean("spellCheckerEnabled", enabled);
	}

	public boolean isSpellCheckerEnabled() {
		return getBoolean("spellCheckerEnabled", false);
	}

	public void setAutoSpellCheckerEnabled(boolean enabled) {
		setBoolean("autoSpellCheckerEnabled", enabled);
	}

	public boolean isAutoSpellCheckerEnabled() {
		return getBoolean("autoSpellCheckerEnabled", false);
	}
	
	//设置声音
	public boolean getSoundSelectionInChatRoom() {
		return getBoolean("showSoundSelectionInChatRoom", false);
	}

	public void setSoundSelectionInChatRoom(boolean value) {
		setBoolean("showSoundSelectionInChatRoom", value);
	}

	private boolean getBoolean(String property, boolean defaultValue) {
		return Boolean.parseBoolean(props.getProperty(property,
				Boolean.toString(defaultValue)));
	}

	private void setBoolean(String property, boolean value) {
		props.setProperty(property, Boolean.toString(value));
	}

	public boolean getBubbleSelection() {
		return getBoolean("BubbleSelection", false);
	}

	// 气泡
	public void setBubbleSelection(boolean bubble) {
		setBoolean("BubbleSelection", bubble);
	}
}
