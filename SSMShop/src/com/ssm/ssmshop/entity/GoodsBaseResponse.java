package com.ssm.ssmshop.entity;

import java.util.List;

public class GoodsBaseResponse<T extends ApplicationEntity> implements ApplicationEntity {

	/**指令编号*/
	private int 	action;
	/**返回码*/
	private int 	code;
	/**orderid*/
	private	String	orderId;
	/**商店名称*/
	private	String	shopName;
	/**用户名称*/
	private	String	userName;
	/**用户电话*/
	private String	userPhone;
	/**用户地址*/
	private	String	userAddress;
	/**小蜜蜂姓名*/
	private String	beeName;
	/**返回结果*/
	private List<T> data;
	
	public int getAction() {
		return action;
	}
	public void setAction(int action) {
		this.action = action;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public List<T> getData() {
		return data;
	}
	public void setData(List<T> data) {
		this.data = data;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
	public String getUserAddress() {
		return userAddress;
	}
	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}
	public String getBeeName() {
		return beeName;
	}
	public void setBeeName(String beeName) {
		this.beeName = beeName;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	
}