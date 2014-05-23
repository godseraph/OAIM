package com.advoa.orgtree;

import org.jivesoftware.spark.component.JiveTreeNode;

//机构Node
public class OrgTreeNode extends JiveTreeNode implements java.io.Serializable {

	public OrgTreeNode(String unitname) {
		super(unitname);
	}

	/**
* 
*/
	private static final long serialVersionUID = -5358854185627562145L;
	private String unitid;// 机构ID
	private String unitname; // 机构名称
	private String superunitid1; // 上一级机构名称
	private Boolean visited;// 是否已访问
	private String type; // 类型
	private String loginid;// 登陆账号

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