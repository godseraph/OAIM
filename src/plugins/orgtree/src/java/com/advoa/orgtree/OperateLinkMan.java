package com.advoa.orgtree;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.ReportedData;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.ReportedData.Row;
import org.jivesoftware.smackx.packet.DiscoverInfo;
import org.jivesoftware.smackx.packet.DiscoverItems;
import org.jivesoftware.smackx.search.UserSearchManager;
import org.jivesoftware.spark.ChatManager;
import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.search.Searchable;
import org.jivesoftware.spark.ui.ChatContainer;
import org.jivesoftware.spark.ui.ChatRoom;
import org.jivesoftware.spark.ui.RosterDialog;
import org.jivesoftware.spark.util.SwingWorker;
import org.jivesoftware.spark.util.log.Log;

public class OperateLinkMan implements Searchable {
	private Collection<String> searchServices = null;
	private String serviceName = null;
	private UserSearchManager searchManager;
	private ReportedData data = null;
	private Form searchForm;
	private ActionEvent e;
	private MouseEvent Me;
	private JMenuItem itemAdd = null;
	private JMenuItem itemSendMess = null;

	// �˹�����Ϊ�˵���Ҽ����ֵĲ˵�׼����
	public OperateLinkMan(final ActionEvent e, JMenuItem itemAdd,
			JMenuItem itemSendMess) {
		loadSearchServices();
		this.itemAdd = itemAdd;
		this.itemSendMess = itemSendMess;
		this.e = e;
	}

	// �˹��캯����Ϊ��˫����������Ի���׼����
	public OperateLinkMan(final MouseEvent Me) {
		loadSearchServices();
		this.Me = Me;
		this.itemAdd = null;
		this.itemSendMess = null;
		this.e = null;
	}

	public void search(final String treeNodeEmail) {
		SwingWorker worker = new SwingWorker() {
			public Object construct() {
				// On initialization, find search service.
				if (searchServices == null) {
					loadSearchServices();
				}
				if (searchServices == null) {
					JOptionPane.showMessageDialog(null, "������Ϊ�գ�");
				} else {
					for (String searchService : searchServices)
						serviceName = searchService;
					// searchResults.clearTable();
					try {
						searchManager = new UserSearchManager(
								SparkManager.getConnection());
						searchForm = searchManager.getSearchForm(serviceName);
						Form answerForm = searchForm.createAnswerForm();
						answerForm.setAnswer("Email", true);
						answerForm.setAnswer("search", treeNodeEmail);
						data = searchManager.getSearchResults(answerForm,
								serviceName);
					} catch (XMPPException e) {
						Log.error("Unable to load search service.", e);
					}
				}
				return data;
			}

			public void finished() {

				Iterator<Row> rows = data.getRows();
				if (rows.hasNext()) {
					if (e.getSource() == itemAdd) {
						addUsers(data);
					}
					if (e.getSource() == itemSendMess
							|| Me.getClickCount() == 2) {
						linkUsers(data);
					}
				} else {
					JOptionPane.showMessageDialog(null, "���ݿ���û�д��˵���Ϣ��");
				}
			}
		};
		worker.start();
	}

	private void loadSearchServices() {
		try {
			searchServices = getServices();
		} catch (Exception e) {
			Log.error("Unable to load search services.", e);
		}
	}

	private Collection<String> getServices() throws Exception {
		final Set<String> searchServices = new HashSet<String>();
		ServiceDiscoveryManager discoManager = ServiceDiscoveryManager
				.getInstanceFor(SparkManager.getConnection());
		DiscoverItems items = SparkManager.getSessionManager()
				.getDiscoveredItems();
		Iterator<DiscoverItems.Item> iter = items.getItems();
		while (iter.hasNext()) {
			DiscoverItems.Item item = iter.next();
			try {
				DiscoverInfo info;
				try {
					info = discoManager.discoverInfo(item.getEntityID());
				} catch (XMPPException e) {
					// Ignore Case
					continue;
				}
				if (info.containsFeature("jabber:iq:search")) {
					// Check that the search service belongs to user searches
					// (and not room searches or other searches)
					for (Iterator<DiscoverInfo.Identity> identities = info
							.getIdentities(); identities.hasNext();) {
						DiscoverInfo.Identity identity = identities.next();
						if ("directory".equals(identity.getCategory())
								&& "user".equals(identity.getType())) {
							searchServices.add(item.getEntityID());
						}
					}
				}
			} catch (Exception e) {
				// No info found.
				break;
			}
		}
		return searchServices;
	}

	public Icon getIcon() {
		return null;
	}

	public String getName() {
		return null;
	}

	public String getDefaultText() {
		return null;
	}

	public String getToolTip() {
		return null;
	}

	public String getFirstValue(ReportedData.Row row, String key) {
		try {
			final Iterator<String> rows = row.getValues(key);
			while (rows.hasNext()) {
				return rows.next();
			}
		} catch (Exception e) {
			Log.error("Error retrieving the first value.", e);
		}
		return null;
	}

	public void addUsers(ReportedData data) {

		Iterator<Row> rows = data.getRows();
		if (rows.hasNext()) {
			Row row = rows.next();
			RosterDialog dialog = new RosterDialog();
			dialog.setDefaultNickname(getFirstValue(row, "Username"));
			dialog.setDefaultJID(getFirstValue(row, "jid"));
			dialog.showRosterDialog();
		} else {
			JOptionPane.showMessageDialog(null, "data��������ֻ���������ƣ�û��ʵ�ʵ���ֵ��");
		}
	}

	public void linkUsers(ReportedData data) {
		Iterator<Row> rows = data.getRows();
		String jid = null;
		String username = null;
		if (rows.hasNext()) {
			Row row = rows.next();
			jid = getFirstValue(row, "jid");
			username = getFirstValue(row, "Username");
			ChatManager chatManager = SparkManager.getChatManager();
			ChatRoom chatRoom = chatManager.createChatRoom(jid, username,
					username);
			ChatContainer chatRooms = chatManager.getChatContainer();
			chatRooms.activateChatRoom(chatRoom);
		} else {
			JOptionPane.showMessageDialog(null, "data��������ֻ���������ƣ�û��ʵ�ʵ���ֵ����");
		}
	}

}