package com.ssm.songshangmen.entity;

import java.io.Serializable;

/**
 * 商品种类
 * @author eternal
 *
 */
public class GoodsType implements ApplicationEntity, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int id;
	/**名称*/
	private String name;
	/**所诉商店*/
	private String shopId;
	/**该商品当前选择的数量*/
	private int	   number;
	
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
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	
}
