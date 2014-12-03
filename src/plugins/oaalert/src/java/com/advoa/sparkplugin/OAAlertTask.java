package com.advoa.sparkplugin;

import java.io.File;
import java.net.URL;

import javax.swing.ImageIcon;

import org.jivesoftware.spark.SparkManager;

public class OAAlertTask extends java.util.TimerTask{  
	
	private OAAlertPlugin plugin;
	
	public OAAlertTask(OAAlertPlugin oaAlertPlugin) {
		plugin=oaAlertPlugin;
	}

	@Override  
	public void run() {
		OAAlertToolTip tip = new OAAlertToolTip();
        tip.setToolTip(new ImageIcon("test.jpg"),"�����µĴ������\r\n�����µ��ʼ���");
        ClassLoader cl = OAAlertPlugin.class.getClassLoader();
        URL soundURL = cl.getResource("sounds/alert.au");
        SparkManager.getSoundManager().playClip(new File(soundURL.getFile()));
        
	}  
} 