package com.ssm.songshangmen.entity;

import java.io.Serializable;
import java.sql.Date;


/**
 * 小蜜蜂
 * @author eternal
 *
 */

public class Bee implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**主键*/
	private String id;
	/**姓名*/
	private String name;
	/**性别 1男 2女*/
	private int sex;
	/**生日*/
	private Date birthday;
	/**身份证号*/
	private String idCard;
	/**编号*/
	private String number;
	/**总订单数*/
	private int totalOrder;
	/**区域ID*/
	private int areaId;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public int getTotalOrder() {
		return totalOrder;
	}
	public void setTotalOrder(int totalOrder) {
		this.totalOrder = totalOrder;
	}
	public int getAreaId() {
		return areaId;
	}
	public void setAreaId(int areaId) {
		this.areaId = areaId;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	
}
