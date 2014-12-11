package com.advoa.sparkplugin;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.jivesoftware.spark.SoundManager;
import org.jivesoftware.spark.SparkManager;

public class OAAlertTask extends java.util.TimerTask {

	private static OAAlertPlugin plugin;
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
		preferences = new AdvOAPreferences();
		String str = null;
		String xmlStr = get(preferences.getUserName(),
				preferences.getPassword());
		boolean mailalert = getXML(xmlStr, "mailalert").equals("True");
		boolean gwalert = getXML(xmlStr, "gwalert").equals("True");
		boolean soundSelectionInChatRoom = preferences
				.getSoundSelectionInChatRoom();
		if (preferences.getBubbleSelection()) {
			if (mailalert && !gwalert) {
				str = "�������ʼ����ģ�";
				tip.setToolTip(str);
			} else if (gwalert && !mailalert) {
				str = "�����µĹ��Ĵ��죡";
				tip.setToolTip(str);
			} else if (mailalert && gwalert) {
				str = "�������ʼ����ģ�\r\n�����µĹ��Ĵ��죡";
				tip.setToolTip(str);
			}
			
		}
		if (soundSelectionInChatRoom){
			boolean gwau = getXML(xmlStr, "gwau").equals("no");
			if(!gwau && gwalert){
				soundManager.playClip(new File(cl.getResource(
						"sounds/"+getXML(xmlStr, "gwau")).getFile()));
			}
			boolean mailau = getXML(xmlStr, "mailau").equals("no");
			if(!mailau && mailalert){
				soundManager.playClip(new File(cl.getResource(
						"sounds/"+getXML(xmlStr, "mailau")).getFile()));
			}
			
		}
	}

	// ��ȡ����
	public String getXML(String xmlStr, String attribute) {
		Document document;
		try {
			document = DocumentHelper.parseText(xmlStr);
			return document.getRootElement().attribute(attribute)
					.getStringValue();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	//ͨ��xml��ȡurl
	public String getURIName() {
		String uriName = null;
		String h = "http://"; // uriName����ҳ��ͷ
		String openfireIP = null; // openfire��IP��ַ
		String fileName = ":9090/oapluginconfig.xml";
		String xmlURL = null;
		SAXReader URIsaxReader = new SAXReader();
		try {	

			openfireIP = SparkManager.getConnection().getHost();
			uriName = h + openfireIP + fileName;
			Document document = URIsaxReader.read(uriName);
			//Node node = document.selectSingleNode("//root/oaalert/oaurl");
			List<? extends Node> list = document.selectNodes("//root/oaalert/oaurl");
			for (Node node : list) {
				xmlURL=node.getText();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return xmlURL;
	}
	//����url
	public String getRealURL(String xmlURL) {
		String[] urls = xmlURL.split("#");
		String realurl = "";
		for (int i = 0; i < urls.length; i++) {
			if (urls[i].equals("username")) {
				urls[i] = preferences.getUserName();
			} else if (urls[i].equals("password")) {
				urls[i] = preferences.getPassword();
			}
			realurl += urls[i];
		}
		return realurl;
	}

	//��ȡ�ض�����
	public String get(String username, String password) {
		String xmlStr = getRealURL(getURIName());
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			// ����httpget.
			HttpGet httpget = new HttpGet(xmlStr);
			// ִ��get����.
			CloseableHttpResponse response = httpclient.execute(httpget);
			try {
				// ��ȡ��Ӧʵ��
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					xmlStr = EntityUtils.toString(entity);
				}
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// �ر�����,�ͷ���Դ
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return xmlStr;
	}

}