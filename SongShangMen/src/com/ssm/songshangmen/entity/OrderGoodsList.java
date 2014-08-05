package com.ssm.songshangmen.entity;

import java.io.Serializable;

public class OrderGoodsList implements ApplicationEntity, Serializable {
	private static final long serialVersionUID = 1L;

	/**商品名称*/
	private String goodsName;
	/**商品价格*/
	private String goodsPrice;
	/**商品数量*/
	private String goodsNumber;
	
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	
	public String getGoodsPrice() {
		return goodsPrice;
	}
	public void setGoodsPrice(String goodsPrice) {
		this.goodsPrice = goodsPrice;
	}
	
	public String getGoodsNumber() {
		return goodsNumber;
	}
	public void setGoodsNumber(String goodsNumber) {
		this.goodsNumber = goodsNumber;
	}
	
}
