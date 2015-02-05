package com.advoa.sparkplugin;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;

import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.jivesoftware.MainWindow;
import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.plugin.Plugin;

public class OAAlertPlugin implements Plugin {

	private int alerttimer;
	private AdvOAPreference preference;
	private AdvOAPreferences preferences;
	private ScheduledThreadPoolExecutor timer = new ScheduledThreadPoolExecutor(
			5);
	// private Timer timer = new Timer();
	private OAAlertTask task = OAAlertTask.getInstance(this);
	private XmlUtil util;

	public void initialize() {
		util = new XmlUtil();
		final MainWindow mainWindow = SparkManager.getMainWindow();
		preferences = new AdvOAPreferences();

		JMenuItem oaMenu = new JMenuItem("OAµÇÂ¼");
		mainWindow.getJMenuBar().getMenu(0).add(oaMenu, 0);

		oaMenu.addActionListener(new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent event) {
				toOA(util.getRealURL(util.getXMLText(
						util.getList("//root/oaalert/servers/oaserver"),
						preferences.getServerSelection(),
						preferences.getLoginSelection()), preferences));
			}

		});
		preference = AdvOAManager.getInstance().getAdvOAPreference();
		SparkManager.getPreferenceManager().addPreference(preference);

		getIni();
		bubbleRun();
	}

	public void toOA(String url) {
		try {
			Process ps = null;
			Runtime rn = Runtime.getRuntime();
			ps = rn.exec("reg query HKEY_CLASSES_ROOT\\Applications\\iexplore.exe\\shell\\open\\command");
			ps.getOutputStream().close();
			InputStreamReader i = new InputStreamReader(ps.getInputStream());
			String line;
			BufferedReader ir = new BufferedReader(i);
			while ((line = ir.readLine()) != null) {
				if (line.contains("\"")) {
					String[] str = line.split("\"");
					rn.exec(str[1] + " " + url);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void shutdown() {
	}

	// ÊÇ·ñ¿É¹Ø±Õ
	@Override
	public boolean canShutDown() {
		return true;
	}

	// Ð¶ÔØ
	public void uninstall() {
	}

	public void getIni() {
		try {
			alerttimer = Integer.parseInt(util.getTime(
					util.getList("//root/oaalert/servers/oaserver"),
					preferences.getServerSelection(), "timer"));
		} catch (NumberFormatException e) {
			alerttimer = 5;
		}
		if (alerttimer < 2)
			alerttimer = 2;
	}

	public void bubbleRun() {
		try {
			timer.scheduleWithFixedDelay(task, 1, 60 * alerttimer,
					TimeUnit.SECONDS);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {

	}
}