package com.advoa.sparkplugin;

import java.io.File;
import java.net.URL;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.jivesoftware.spark.SoundManager;
import org.jivesoftware.spark.SparkManager;

public class OAAlertTask extends java.util.TimerTask {

	private static OAAlertPlugin plugin;
	private static String alerturl;
	private static OAAlertTask instance;
	private static SoundManager soundManager = SparkManager.getSoundManager();
	private static AdvOAPreferences preferences;

	private OAAlertTask(OAAlertPlugin oaAlertPlugin) {
		plugin = oaAlertPlugin;
	}

	public static synchronized OAAlertTask getInstance(OAAlertPlugin plugin) {
		if (instance == null) {
			instance = new OAAlertTask(plugin);
		}
		return instance;
	}

	@Override
	public void run() {
		OAAlertToolTip tip = new OAAlertToolTip();
		ClassLoader cl = OAAlertPlugin.class.getClassLoader();
		AdvOAPreferences preferences = new AdvOAPreferences();
		String str = null;
		Document document = getDocument();
		boolean mailalert = getXML(document, "mailalert").equals("True");
		boolean gwalert = getXML(document, "gwalert").equals("True");
		boolean soundSelectionInChatRoom = preferences
				.getSoundSelectionInChatRoom();
		File soundFile = new File(cl.getResource("sounds/" + getXML(document,"mailau"))
				.getFile());
		File soundFile2 = new File(cl.getResource("sounds/" + getXML(document,"gwau"))
				.getFile());
		if (preferences.getBubbleSelection()) {
			if (mailalert && !gwalert) {
				str = "您有新邮件待阅！";
				tip.setToolTip(str);
				if (soundSelectionInChatRoom)
					soundManager.playClip(soundFile);
			} else if (gwalert && !mailalert) {
				str = "您有新的公文待办！";
				tip.setToolTip(str);
				if (soundSelectionInChatRoom)
					soundManager.playClip(soundFile2);
			} else if (mailalert && gwalert) {
				str = "您有新邮件待阅！\r\n您有新的公文待办！";
				tip.setToolTip(str);
				if (soundSelectionInChatRoom) {
					soundManager.playClip(soundFile);
					soundManager.playClip(soundFile2);
				}

			}

		}
	}

	// 获取属性
	public String getXML(Document document, String attribute) {
		SAXReader URIsaxReader = new SAXReader();
		Node node = document.selectSingleNode("//root/oaalert/oaurl");
		if (node != null) {
			alerturl = node.getText();
		}
		if (alerturl != null && !alerturl.isEmpty()) {
			try {
				return URIsaxReader.read(alerturl).getRootElement()
						.attribute(attribute).getStringValue();
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	public Document getDocument() {
		String uriName = null;
		String h = "http://"; // uriName的网页表头
		String openfireIP = null; // openfire的IP地址
		String fileName = ":9090/oapluginconfig.xml";
		SAXReader URIsaxReader = new SAXReader();
		openfireIP = SparkManager.getConnection().getHost();
		uriName = h + openfireIP + fileName;
		Document document = null;
		try {
			document = URIsaxReader.read(uriName);
			return document;
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return document;
	}

}