package com.advoa.orgtree;

import org.jivesoftware.spark.component.JiveTreeNode;

//����Node
public class OrgTreeNode extends JiveTreeNode implements java.io.Serializable {

	public OrgTreeNode(String unitname) {
		super(unitname);
	}

	/**
* 
*/
	private static final long serialVersionUID = -5358854185627562145L;
	private String unitid;// ����ID
	private String unitname; // ��������
	private String superunitid1; // ��һ����������
	private Boolean visited;// �Ƿ��ѷ���
	private String type; // ����
	private String loginid;// ��½�˺�

	public String getUnitid() {
		return unitid;
	}

	public void setUnitid(String unitid) {
		this.unitid = unitid;
	}

	public String getUnitname() {
		return unitname;
	}

	public void setUnitname(String unitname) {
		this.unitname = unitname;
	}

	public String getSuperunitid1() {
		return superunitid1;
	}

	public void setSuperunitid1(String superunitid1) {
		this.superunitid1 = superunitid1;
	}

	public Boolean getVisited() {
		return visited;
	}

	public void setVisited(Boolean visited) {
		this.visited = visited;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLoginid() {
		return loginid;
	}

	public void setLoginid(String loginid) {
		this.loginid = loginid;
	}

}