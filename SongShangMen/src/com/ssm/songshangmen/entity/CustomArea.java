package com.ssm.songshangmen.entity;

import java.io.Serializable;

/**
 * 自定义区域
 * @author eternal
 *
 */
public class CustomArea implements Serializable {

	private static final long serialVersionUID = 1L;
	/**主键ID*/
	private int id;
	/**小区名字*/
	private String area;
	/**所在区域ID*/
	private String father;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getFather() {
		return father;
	}
	public void setFather(String father) {
		this.father = father;
	}
	
}
