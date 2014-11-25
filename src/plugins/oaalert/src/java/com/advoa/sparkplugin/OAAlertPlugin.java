package com.advoa.sparkplugin;

import java.util.List;
import java.util.Timer;

import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.plugin.Plugin;
import org.jivesoftware.spellchecker.SpellcheckManager;

public class OAAlertPlugin implements Plugin {

	private boolean	pluginEnabled=false;
	private String alerturl;
	private int alerttimer;
	private AdvOAPreference preference;
	private AdvOAPreferences preferences;
	private Timer timer;
	private OAAlertTask task = OAAlertTask.getInstance(this);
	
	public void initialize() {
		preference = AdvOAManager.getInstance()
			    .getAdvOAPreference();
		SparkManager.getPreferenceManager().addPreference(preference);
		preferences = new AdvOAPreferences();
		System.out.println("OAAlertPlugin init");
		timer = new Timer();
		getIni();
		bubbleRun();
	}

	@Override
	public void shutdown() {
	}

	// 是否可关闭
	@Override
	public boolean canShutDown() {
		return true;
	}

	// 卸载
	public void uninstall() {
	}
	
	//设置pluginEnabled属性
	public void setPluginEnabled(boolean pluginEnabled) {
		this.pluginEnabled = pluginEnabled;
	}
	
	public void getIni() {
		String uriName = null;
		String h = "http://"; // uriName的网页表头
		String openfireIP = null; // openfire的IP地址
		String fileName = ":9090/oapluginconfig.xml";
		SAXReader URIsaxReader = new SAXReader();
		try {

			openfireIP = SparkManager.getConnection().getHost();
			uriName = h + openfireIP + fileName;
			Document document = URIsaxReader.read(uriName);
			Node node = document.selectSingleNode("//root/oaalert/oaurl");
			if (node!=null) {
				alerturl=node.getText();
			}
			if(alerturl!=null&&!alerturl.isEmpty())
				pluginEnabled=true;
			node = document.selectSingleNode("//root/oaalert/timer");
			if (node!=null) {
				try {
					alerttimer=Integer.parseInt(node.getText());
				} catch (NumberFormatException e) {
					alerttimer=5;
				}
				if(alerttimer<2)	alerttimer=2;
			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
//	public void refresh(){
//		try {
//			if(preferences.getBubbleSelection()){
//				bubbleRun();
//			}else if(preferences.getStatus()&&!preferences.getBubbleSelection()){
//				task.cancel();
//				preferences.setStatus(false);
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
//		
//	}
	
	public void bubbleRun(){
		try {					
			timer.schedule(task, 1000, 1000*60*alerttimer); 
			preferences.setStatus(true);
			preferences.save();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
}