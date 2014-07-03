package com.advoa.orgtree;

import javax.swing.tree.*;

public class OrgTreeNode extends DefaultMutableTreeNode implements
		java.io.Serializable {

	private static final long serialVersionUID = -5358854185627562145L;
	private String unitid;// 机构ID
	private String unitname; // 机构名称
	private String superunitid1; // 上一级机构名称
	private Boolean visited;// 是否已访问
	private String type; // 类型
	private String eMail; // 用户的邮箱

	public OrgTreeNode(String s, String eMail) {
		super(s);
		this.eMail = eMail;
	}

	public OrgTreeNode(String s) {
		super(s);
		this.eMail = null;
	}

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

	public String geteMail() {
		return eMail;
	}
}