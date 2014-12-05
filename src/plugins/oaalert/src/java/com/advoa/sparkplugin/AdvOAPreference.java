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

import java.awt.EventQueue;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import org.jivesoftware.spark.preference.Preference;

/**
 * Class used to acquire the Preferences set for this plugin by the User
 */
public class AdvOAPreference implements Preference {
	public static String NAMESPACE = "AdvOA";
	private AdvOAPreferenceDialog dialog;
	private AdvOAPreferences preferences;

	public AdvOAPreference() {
		preferences = new AdvOAPreferences();
		try {
			if (EventQueue.isDispatchThread()) {
				dialog = new AdvOAPreferenceDialog();
			} else {
				EventQueue.invokeAndWait(new Runnable() {

					@Override
					public void run() {
						dialog = new AdvOAPreferenceDialog();
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Returns the Preferences
	 * 
	 * @return {@link AdvOAPreference}
	 */
	public AdvOAPreferences getPreferences() {
		return preferences;
	}

	public void commit() {
		preferences.setSpellCheckerEnabled(dialog.isSpellCheckingEnabled());//是否开启
		preferences.setBubbleSelection(dialog.getBubbleSelection());//气泡
		preferences.setSoundSelectionInChatRoom(dialog.getSoundSelection());//声音
		preferences.setStatus(dialog.getBubbleSelection());
		preferences.setUserName(dialog.getUserName());
		preferences.setPassword(dialog.getPassword());
		preferences.save();
		
	}

	public Object getData() {
		return preferences;
	}

	public String getErrorMessage() {
		return null;
	}

	public JComponent getGUI() {
		return dialog;
	}

	public Icon getIcon() {
		ClassLoader cl = getClass().getClassLoader();
		return new ImageIcon(cl.getResource("images/magnet.png"));
	}

	public String getListName() {
		return "办公系统";
	}

	public String getNamespace() {
		return NAMESPACE;
	}

	public String getTitle() {
		return "协同办公系统设置";
	}

	public String getTooltip() {
		return "设置与协同办公系统的关联参数";
	}

	public boolean isDataValid() {
		return true;
	}

	public void load() {
		dialog.setSpellCheckingEnabled(preferences.isSpellCheckerEnabled());
		dialog.setSoundSelection(preferences.getSoundSelectionInChatRoom());
		dialog.setBubbleSelection(preferences.getBubbleSelection());
	}

	public void shutdown() {

	}

}
