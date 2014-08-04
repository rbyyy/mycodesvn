package com.ssm.songshangmen.entity;

import java.io.Serializable;

/**
 * 区域
 * @author eternal
 *
 */
public class Area implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int id;
	/**区域ID*/
	private String areaID;
	/**区域名字*/
	private String area;
	/**所在城市ID*/
	private String father;
	/**经度*/
	private String longitude;
	/**纬度*/
	private String latitude;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAreaID() {
		return areaID;
	}
	public void setAreaID(String areaID) {
		this.areaID = areaID;
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
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	
}
