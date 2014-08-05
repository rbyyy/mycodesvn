package com.ssm.ssmshop.entity;

import java.io.Serializable;

public class OrderPreview implements ApplicationEntity, Serializable {

	/**
	 * 版本号
	 */
	private static final long serialVersionUID = 1L;
	
	private	int		orderStatus;
	private	String	orderId;
	private String  buyName;
	private String  buyAddress;
	private String	buyPhone;
	private String  buyTotalPrice;
	
	public int getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
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
	public String getBuyTotalPrice() {
		return buyTotalPrice;
	}
	public void setBuyTotalPrice(String buyTotalPrice) {
		this.buyTotalPrice = buyTotalPrice;
	}
	
}
