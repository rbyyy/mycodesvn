package com.ssm.ssmbee.entity;

import java.io.Serializable;

public class OrderMenu implements ApplicationEntity, Serializable {

	/**
	 * 版本号
	 */
	private static final long serialVersionUID = 1L;
	
	private	int		orderStatus;
	private	String	orderId;
	private String	shopName;
	private String	shopAddress;
	private String  shopPhone;
	private String  buyName;
	private String  buyAddress;
	private String	buyPhone;
	private String	dateString;
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	public int getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}
	
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	
	public String getShopAddress() {
		return shopAddress;
	}
	public void setShopAddress(String shopAddress) {
		this.shopAddress = shopAddress;
	}
	
	public String getShopPhone() {
		return shopPhone;
	}
	public void setShopPhone(String shopPhone) {
		this.shopPhone = shopPhone;
	}
	
	public String getBuyName() {
		return buyName;
	}
	public void setBuyName(String buyName) {
		this.buyName = buyName;
	}
	
	public String getBuyAddress() {
		return buyAddress;
	}
	public void setBuyAddress(String buyAddress) {
		this.buyAddress = buyAddress;
	}
	
	public String getBuyPhone() {
		return buyPhone;
	}
	public void setBuyPhone(String buyPhone) {
		this.buyPhone = buyPhone;
	}
	
	public String getDateString() {
		return dateString;
	}
	public void setDateString(String dateString) {
		this.dateString = dateString;
	}
	
}
