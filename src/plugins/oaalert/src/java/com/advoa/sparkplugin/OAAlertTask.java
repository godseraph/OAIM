package com.advoa.sparkplugin;

import java.io.File;
import java.io.FileWriter;
import java.net.URL;

import javax.swing.ImageIcon;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.dom4j.Element;
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
		System.out.println("taskrun");
		OAAlertToolTip tip = new OAAlertToolTip();
		ClassLoader cl = OAAlertPlugin.class.getClassLoader();
		AdvOAPreferences preferences = new AdvOAPreferences();
		URL soundURL = null;
		if (preferences.getBubbleSelection()) {
			if (getXML("mailalert").equals("True")) {
				tip.setToolTip(new ImageIcon("test.jpg"), "�����µĴ������");
				if (preferences.getSoundSelectionInChatRoom()) {
					soundURL = cl.getResource("sounds/" + getXML("mailau"));
					soundManager.playClip(new File(soundURL.getFile()));
				} else {
					try {
						soundManager.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			if (getXML("gwalert").equals("True")) {
				tip.setToolTip(new ImageIcon("test.jpg"), "�����µ��ʼ���");
				if (preferences.getSoundSelectionInChatRoom()) {
					soundURL = cl.getResource("sounds/" + getXML("gwau"));
					soundManager.playClip(new File(soundURL.getFile()));
				} else {
					try {
						soundManager.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				// soundManager.playClip(new File(soundURL.getFile()));
				// setXML("gwalert", false);
			}
		}else{
			System.out.println("ֹͣ������");
		}
		// soundURL = cl.getResource("sounds/ps.au");
		// SparkManager.getSoundManager().playClip(new
		// File(soundURL.getFile()));
	}

	// ��ȡ����
	public String getXML(String attribute) {
		String uriName = null;
		String h = "http://"; // uriName����ҳ��ͷ
		String openfireIP = null; // openfire��IP��ַ
		String fileName = ":9090/oapluginconfig.xml";
		SAXReader URIsaxReader = new SAXReader();
		try {
			openfireIP = SparkManager.getConnection().getHost();
			uriName = h + openfireIP + fileName;
			Document document = URIsaxReader.read(uriName);
			Node node = document.selectSingleNode("//root/oaalert/oaurl");
			if (node != null) {
				alerturl = node.getText();
			}
			if (alerturl != null && !alerturl.isEmpty()) {
				return URIsaxReader.read(alerturl).getRootElement()
						.attribute(attribute).getStringValue();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	// ��������
	public void setXML(String attribute, Boolean value) {
		String uriName = null;
		String h = "http://"; // uriName����ҳ��ͷ
		String openfireIP = null; // openfire��IP��ַ
		String fileName = ":9090/oapluginconfig.xml";
		SAXReader URIsaxReader = new SAXReader();
		try {
			openfireIP = SparkManager.getConnection().getHost();
			uriName = h + openfireIP + fileName;
			Document document = URIsaxReader.read(uriName);
			Node node = document.selectSingleNode("//root/oaalert/oaurl");
			if (node != null) {
				alerturl = node.getText();
			}
			if (alerturl != null && !alerturl.isEmpty()) {
				URIsaxReader.read(alerturl).getRootElement()
						.attribute(attribute).setValue("False");
			}
			try {
				/** ��document�е�����д���ļ��� */
				XMLWriter writer = new XMLWriter(new FileWriter(new File(
						fileName)));
				writer.write(document);
				writer.close();
				/** ִ�гɹ�,�践��1 */
				// return Value = 1;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}