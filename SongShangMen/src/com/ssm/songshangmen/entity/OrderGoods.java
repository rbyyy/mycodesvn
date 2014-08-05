package com.ssm.songshangmen.entity;

import java.io.Serializable;

public class OrderGoods implements Serializable {
	private static final long serialVersionUID = 1L;

	/**主键*/
	private int goodsId;
	/**商品名称*/
	private String name;
	/**商品类型*/
	private int type;
	/**商品价格*/
	private double price;
	/**商品打折后价格*/
	private double salePrice;
	/**商店ID*/
	private String shopId;
	/**商品数量*/
	private int goodsNumber;
	

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(double salePrice) {
		this.salePrice = salePrice;
	}
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	public int getGoodsNumber() {
		return goodsNumber;
	}
	public void setGoodsNumber(int goodsNumber) {
		this.goodsNumber = goodsNumber;
	}
	public int getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}
	
}
