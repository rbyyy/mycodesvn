package com.ssm.songshangmen.entity;

import java.io.Serializable;

public class Address implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int id;
	/**用户ID*/
	private String userId;
	/**收货地址*/
	private String address;
	/**收货人姓名*/
	private String name;
	/**收货人电话*/
	private String phone;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
}
