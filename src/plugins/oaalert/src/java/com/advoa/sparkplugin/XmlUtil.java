package com.advoa.sparkplugin;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.jivesoftware.spark.SparkManager;

public class XmlUtil {

	private String uriName = null;
	private String h = "http://"; // uriName的网页表头
	private String openfireIP = null; // openfire的IP地址
	private String fileName = ":9090/oapluginconfig.xml";
	private Document document;
	
	public XmlUtil(){
		SAXReader URIsaxReader = new SAXReader();
		openfireIP = SparkManager.getConnection().getHost();
		uriName = h + openfireIP + fileName;
		try {
			document = URIsaxReader.read(uriName);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	// 获取属性
	public String getXML(String xmlStr, String attribute) {
		Document document;
		try {
			document = DocumentHelper.parseText(xmlStr);
			return document.getRootElement().attribute(attribute)
					.getStringValue();
		} catch (DocumentException e) {
			e.printStackTrace();
		}

		return null;
	}

	//
	public List<? extends Node> getList(String path) {
		List<? extends Node> list = null;
		list = document.selectNodes(path);
		return list;
	}

	// 通过xml获取url
	public String getURIName(List<? extends Node> list, String attr) {
		String xmlURL = null;

		for (Node node : list) {
			Element element = (Element) node;

			xmlURL = node.getText();
		}
		return xmlURL;
	}

	// 获取属性值
	/**
	 * @param list
	 *            node集合
	 * @param attr
	 *            属性名
	 * @return 属性值得集合
	 */
	@SuppressWarnings("null")
	public Vector<String> getAttr(List<? extends Node> list, String attr) {
		Vector<String> attrValue = new Vector<String>();

		for (Node node : list) {
			Element element = (Element) node;
			element.elementIterator();
			attrValue.add(element.attribute(attr).getValue());
		}
		return attrValue;
	}

	// 获取某特定节点下的属性
	public Vector<String> getDownAttr(List<? extends Node> list, String upattr) {
		Vector<String> attrValue = new Vector<String>();
		for (Node node : list) {
			Element element = (Element) node;
			if (element.attribute("name").getValue().equals(upattr)) {
				Iterator<Element> iter = element.elementIterator();
				while (iter.hasNext()) {
					element = iter.next();
					if (element.getName().equals("loginurl")) {
						iter = element.elementIterator();
						while (iter.hasNext()) {
							attrValue.add(iter.next().attribute("name")
									.getValue());
						}
					}
				}
			}
		}
		return attrValue;
	}

	// 处理url并获取真实URL
	public String getRealURL(String xmlURL, AdvOAPreferences preferences) {
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

	// 获取目标节点之下的目标节点
	/**
	 * @param list
	 * @param upattr
	 * @param downattr
	 * @return
	 */
	public String getXMLText(List<? extends Node> list, String upattr,
			String downattr) {
		String url = "";
		for (Node node : list) {
			Element element = (Element) node;
			if (element.attribute("name").getValue().equals(upattr)) {
				Iterator<Element> iter = element.elementIterator();
				while (iter.hasNext()) {
					element = iter.next();
					if (element.getName().equals("loginurl")) {
						iter = element.elementIterator();
						while (iter.hasNext()) {
							element = iter.next();
							if (element.attribute("name").getValue()
									.equals(downattr)) {
								url = element.getText();
								return url;
							}
						}
					}
				}
			}
		}
		return url;
	}

	public String getXMLText(List<? extends Node> list, String attr) {
		String url = "";
		for (Node node : list) {
			Element element = (Element) node;
			if (element.attribute("name").getValue().equals(attr)) {
				Iterator<Element> iter = element.elementIterator();
				while (iter.hasNext()) {
					element = iter.next();
					if (element.getName().equals("oaurl")) {
						url = element.getText();
						return url;
					}
				}
			}
		}
		return url;
	}

	public String getTime(List<? extends Node> list, String upattr, String ele) {
		String time = "";
		for (Node node : list) {
			Element element = (Element) node;
			if (element.attribute("name").getValue().equals(upattr)) {
				Iterator<Element> iter = element.elementIterator();
				while (iter.hasNext()) {
					element = iter.next();
					if (element.getName().equals(ele)) {
						time = element.getText();
					}
				}
			}
		}
		return time;
	}
}
