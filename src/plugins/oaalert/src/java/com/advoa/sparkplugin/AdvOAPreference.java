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

import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.preference.Preference;

/**
 * Class used to acquire the Preferences set for this plugin by the User
 */
public class AdvOAPreference implements Preference {
	public static String NAMESPACE = "AdvOA";
	private AdvOAPreferenceDialog dialog;
	private AdvOAPreferenceDialogTest dialogTest;
	private AdvOAPreferences preferences;
	private OAAlertPlugin alert;

	public AdvOAPreference() {
		preferences = new AdvOAPreferences();
		try {
			if (EventQueue.isDispatchThread()) {
				dialogTest = new AdvOAPreferenceDialogTest();
			} else {
				EventQueue.invokeAndWait(new Runnable() {

					@Override
					public void run() {
						dialogTest = new AdvOAPreferenceDialogTest();						
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
		//boolean alertFlag = preferences.getBubbleSelection();
		preferences.setSpellCheckerEnabled(dialogTest.isSpellCheckingEnabled());//是否开启
		preferences.setBubbleSelection(dialogTest.getBubbleSelection());//气泡
		preferences.setSoundSelectionInChatRoom(dialogTest
				.getSoundSelection());//声音
		preferences.setUserName(dialogTest.getUserName());
		preferences.setPassword(dialogTest.getPassword());
		//AdvOAManager.getInstance().loadDictionary(dialog.getSelectedLanguage());
		preferences.save();
//		if(dialogTest.getBubbleSelection()){
//			alert = new OAAlertPlugin();
//			alert.initialize();
//		}
//		if(!dialogTest.getSoundSelection()){
//			if(preferences.getSoundSelectionInChatRoom())
//				try {
//					SparkManager.getSoundManager().wait();
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//		}else{
//			if(!preferences.getSoundSelectionInChatRoom()){
//				SparkManager.getSoundManager().notify();
//			}
//			
//		}
		
	}

	public Object getData() {
		return preferences;
	}

	public String getErrorMessage() {
		return null;
	}

	public JComponent getGUI() {
		return dialogTest;
	}

	public Icon getIcon() {
		ClassLoader cl = getClass().getClassLoader();
		return new ImageIcon(cl.getResource("images/magnet.png"));
	}

	public String getListName() {
		return "OA设置";
	}

	public String getNamespace() {
		return NAMESPACE;
	}

	public String getTitle() {
		return "OA设置";
	}

	public String getTooltip() {
		return "设置与OA关联的参数";
	}

	public boolean isDataValid() {
		return true;
	}

	public void load() {
		dialogTest.setSpellCheckingEnabled(preferences.isSpellCheckerEnabled());
		dialogTest.setSoundSelection(preferences
				.getSoundSelectionInChatRoom());
		dialogTest.setBubbleSelection(preferences.getBubbleSelection());
	}

	public void shutdown() {

	}

}
