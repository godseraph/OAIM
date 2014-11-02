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
        tip.setToolTip(new ImageIcon("test.jpg"),"您有新的待办件！\r\n您有新的邮件！");
        ClassLoader cl = OAAlertPlugin.class.getClassLoader();
        URL soundURL = cl.getResource("sounds/alert.au");
        SparkManager.getSoundManager().playClip(new File(soundURL.getFile()));
        
	}  
} 