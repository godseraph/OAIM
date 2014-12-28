package com.advoa.sparkplugin;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Timer;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;

import org.jivesoftware.MainWindow;
import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.plugin.Plugin;

public class OAAlertPlugin implements Plugin {

	private int alerttimer;
	private AdvOAPreference preference;
	
	private Timer timer = new Timer();
	private OAAlertTask task = OAAlertTask.getInstance(this);
	private XmlUtil util;

	public void initialize() {
		util = new XmlUtil();
		final MainWindow mainWindow = SparkManager.getMainWindow();

		JMenuItem oaMenu = new JMenuItem("OA��¼");
		mainWindow.getJMenuBar().getMenu(0).add(oaMenu, 0);

		oaMenu.addActionListener(new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent event) {
				AdvOAPreferences preferences = new AdvOAPreferences();
				try {
					Runtime.getRuntime()
							.exec("cmd /c start iexplore "
									+ util.getRealURL(
											util.getXMLText(
													util.getList("//root/oaalert/servers/oaserver"),
													preferences
															.getServerSelection(),
													preferences
															.getLoginSelection()),
											preferences));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		});
		preference = AdvOAManager.getInstance().getAdvOAPreference();
		SparkManager.getPreferenceManager().addPreference(preference);

		getIni();
		bubbleRun();
	}

	@Override
	public void shutdown() {
	}

	// �Ƿ�ɹر�
	@Override
	public boolean canShutDown() {
		return true;
	}

	// ж��
	public void uninstall() {
	}

	public void getIni() {
		try {
			AdvOAPreferences preferences = new AdvOAPreferences();
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
			timer.schedule(task, 1000, 1000 * 6 * alerttimer);
			//preferences.setStatus(true);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}
	public static void main(String[] args) {
		
	}
}