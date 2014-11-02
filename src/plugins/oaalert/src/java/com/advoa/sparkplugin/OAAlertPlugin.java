package com.advoa.sparkplugin;

import java.util.List;
import java.util.Timer;
import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.plugin.Plugin;



public class OAAlertPlugin implements Plugin {

	private boolean	pluginEnabled=false;
	private String alerturl;
	private int alerttimer;
	
	
	public void initialize() {
		System.out.println("OAAlertPlugin init");
		//getIni();
		alerttimer=1;
		pluginEnabled=true;
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
		String xmlURL = null;
		SAXReader URIsaxReader = new SAXReader();
		try {

			openfireIP = SparkManager.getConnection().getHost();
			uriName = h + openfireIP + fileName;
			Document document = URIsaxReader.read(uriName);
			List<? extends Node> list = document.selectNodes("//root/orgtree");
			for (Node node : list) {
				xmlURL=node.getText();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}