package com.advoa.sparkplugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import sun.audio.*;

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

public class OAAlertTask implements Runnable {

	private static OAAlertPlugin plugin;
	private static OAAlertTask instance;
	private static SoundManager soundManager = SparkManager.getSoundManager();
	private static AdvOAPreferences preferences;
	private Map<String, String> soundMap = new HashMap<String, String>();

	private OAAlertTask(OAAlertPlugin oaAlertPlugin) {
		plugin = oaAlertPlugin;
		soundMap.put("短促音", "sound0.au");
		soundMap.put("鸟鸣", "sound1.au");
		soundMap.put("门铃", "sound2.au");
		soundMap.put("击打", "sound4.au");
		soundMap.put("深海探测", "sound5.au");
		soundMap.put("招呼", "sound6.au");
		soundMap.put("风琴", "sound7.au");
		soundMap.put("短鸣", "sound8.au");
		soundMap.put("蜂鸣", "sound9.au");
		soundMap.put("静音", "no");
	}

	public static synchronized OAAlertTask getInstance(OAAlertPlugin plugin) {
		if (instance == null) {
			instance = new OAAlertTask(plugin);
		}
		return instance;
	}

	@Override
	public void run() {
		preferences = new AdvOAPreferences();
		try {
			String userName = preferences.getUserName();
			if (!userName.equals("")) {
				OAAlertToolTip tip = new OAAlertToolTip();
				ClassLoader cl = OAAlertPlugin.class.getClassLoader();
				String str = null;
				String xmlStr = get(userName, preferences.getPassword());
				boolean mailalert = getXML(xmlStr, "mailalert").equals("True");
				boolean gwalert = getXML(xmlStr, "gwalert").equals("True");
				boolean soundSelectionInChatRoom = preferences
						.getSoundSelectionInChatRoom();
				if (mailalert && !gwalert) {
					str = "  您有新邮件待阅！";
					tip.setToolTip(str);
				} else if (gwalert && !mailalert) {
					str = "  您有新的公文待办！";
					tip.setToolTip(str);
				} else if (mailalert && gwalert) {
					str = "  您有新邮件待阅！\n  您有新的公文待办！";
					tip.setToolTip(str);
				}
				if (soundSelectionInChatRoom) {
					String gw_au = soundMap.get(preferences
							.getGwSoundSelection());
					if (!gw_au.equals("no") && gwalert) {
						AudioPlayer.player.start(cl
								.getResourceAsStream("sounds/" + gw_au));
					}
					String mail_au = soundMap.get(preferences
							.getMailSoundSelection());
					if (!mail_au.equals("no") && mailalert) {
						AudioPlayer.player.start(cl
								.getResourceAsStream("sounds/" + mail_au));
					}

				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			try {
				throw e;
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	// 获取属性
	public String getXML(String xmlStr, String attribute) {
		Document document;
		String attr = null;
		try {
			document = DocumentHelper.parseText(xmlStr);
			attr = document.getRootElement().attribute(attribute)
					.getStringValue();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "用户名或密码错误！");
		}
		return attr;
	}

	// 通过xml获取url
	public String getURIName() {
		String uri = null;
		try {
			XmlUtil util = new XmlUtil();
			uri = util.getXMLText(
					util.getList("//root/oaalert/servers/oaserver"),
					preferences.getServerSelection());
			return uri;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return uri;
	}

	// 处理url
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

	// 获取重定向结果
	public String get(String username, String password) {
		String xmlStr = getRealURL(getURIName());
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			// 创建httpget.
			HttpGet httpget = new HttpGet(xmlStr);
			// 执行get请求.
			CloseableHttpResponse response = httpclient.execute(httpget);
			try {
				// 获取响应实体
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
			// 关闭连接,释放资源
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return xmlStr;
	}

}