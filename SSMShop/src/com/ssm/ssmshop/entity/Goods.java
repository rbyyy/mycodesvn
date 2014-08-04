package com.ssm.ssmshop.entity;

import java.io.Serializable;

import com.ssm.ssmshop.entity.ApplicationEntity;

public class Goods implements ApplicationEntity,Serializable {

	private static final long serialVersionUID = 1L;

	/**商品名称*/
	private String 	name;
	/**商品价格*/
	private String 	price;
	/**商品类型*/
	private String 	number;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}

}
