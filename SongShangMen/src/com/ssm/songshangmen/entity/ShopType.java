package com.ssm.songshangmen.entity;

import java.io.Serializable;

/**
 * 商品类型
 * @author eternal
 *
 */
public class ShopType implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int id;
	/**商品类型*/
	private String name;
	/**所属商店*/
	private String shopId;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	
}
