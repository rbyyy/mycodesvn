package com.ssm.songshangmen.entity;

import java.io.Serializable;

/**
 * 订单
 * @author eternal
 *
 */
public class Order implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**主键*/
	private String id;
	/**商店ID*/
	private String shopId;
	/**买家ID*/
	private String userId;
	/**小蜜蜂ID*/
	private String beeId;
	/**订单商品,可以包含多种商品，以json格式存储*/
	private String goods;
	/**是否已付款*/
	private int isPaid;
	/**订单状态  1未发货  2已发货  3已接收 3商家取消订单*/
	private int orderStatus;
	/**总金额*/
	private double totalPrice;
	/**其他费用*/
	private double otherCost;
	/**日期*/
	private String date;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getBeeId() {
		return beeId;
	}
	public void setBeeId(String beeId) {
		this.beeId = beeId;
	}
	public String getGoods() {
		return goods;
	}
	public void setGoods(String goods) {
		this.goods = goods;
	}
	public int getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}
	public double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public double getOtherCost() {
		return otherCost;
	}
	public void setOtherCost(double otherCost) {
		this.otherCost = otherCost;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public int getIsPaid() {
		return isPaid;
	}
	public void setIsPaid(int isPaid) {
		this.isPaid = isPaid;
	}
	
}
