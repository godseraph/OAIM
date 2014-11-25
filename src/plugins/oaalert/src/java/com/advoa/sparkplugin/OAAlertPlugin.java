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
	
	
	public void initialize() {
		preference = AdvOAManager.getInstance()
			    .getAdvOAPreference();
		    SparkManager.getPreferenceManager().addPreference(preference);
		System.out.println("OAAlertPlugin init");
		getIni();
		if(pluginEnabled){
			   Timer timer = new Timer();  
			   timer.schedule(new OAAlertTask(this), 1000, 1000*60*alerttimer);  
		}
	}

	@Override
	public void shutdown() {
	}

	// 是否可关闭
	@Override
	public boolean canShutDown() {
		return false;
	}

	// 卸载
	public void uninstall() {
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

}