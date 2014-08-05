package com.ssm.songshangmen.entity;

import java.io.Serializable;

/**
 * 商品
 * @author eternal
 *
 */
public class Goods implements ApplicationEntity,Serializable {

	private static final long serialVersionUID = 1L;

	/**主键*/
	private int id;
	/**商品名称*/
	private String name;
	/**商品类型*/
	private int type;
	/**商品价格*/
	private double price;
	/**商品打折后价格*/
	private double salePrice;
	/**商品图片*/
	private String picture;
	/**商品描述*/
	private String description;
	/**商店ID*/
	private String shopId;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
