package com.ssm.songshangmen.entity;

import java.io.Serializable;

/**
 * 城市
 * @author eternal
 *
 */
public class City implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int id;
	/**城市ID*/
	private String cityID;
	/**城市名*/
	private String city;
	/**所在省份ID*/
	private String father;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCityID() {
		return cityID;
	}
	public void setCityID(String cityID) {
		this.cityID = cityID;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getFather() {
		return father;
	}
	public void setFather(String father) {
		this.father = father;
	}
	
	
}
