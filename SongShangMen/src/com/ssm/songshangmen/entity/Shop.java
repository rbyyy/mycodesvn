package com.ssm.songshangmen.entity;

import java.io.Serializable;

/**
 * 商家
 * @author eternal
 *
 */
public class Shop implements ApplicationEntity, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**主键*/
	private String id;
	/**店名*/
	private String name;
	/**详细地址*/
	private String address;
	/**小区、区域*/
	private int areaId;
	/**商店类型 1外卖 2便利店 3服务 三大分类，包含二级分类*/
	private int type;
	/**固定电话*/
	private String phone;
	/**营业时间*/
	private String businessHours;
	/**是否营业中 0否 1是*/
	private int openStatus;
	/**图片*/
	private String picture;
	/**等级*/
	private int level;
	/**属性、标签*/
	private String tag;
	/**活动*/
	private String activity;
	/**距离*/
	private double distance;
	/**预计送达时间*/
	private String sendTime;
	/**超过多少钱免费送货*/
	private double sendLimit;
	/**配送费*/
	private double sendFee;
	/**访问量*/
	private int pageView;
	/**经度*/
	private double longitude;
	/**纬度*/
	private double latitude;
	/**是否已审核*/
	private int examine;
	/**总订单数*/
	private int totalOrder;
	/**公告*/
	private String notice;
	
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getAreaId() {
		return areaId;
	}
	public void setAreaId(int areaId) {
		this.areaId = areaId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getBusinessHours() {
		return businessHours;
	}
	public void setBusinessHours(String businessHours) {
		this.businessHours = businessHours;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getActivity() {
		return activity;
	}
	public void setActivity(String activity) {
		this.activity = activity;
	}
	public String getSendTime() {
		return sendTime;
	}
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	public double getSendLimit() {
		return sendLimit;
	}
	public void setSendLimit(double sendLimit) {
		this.sendLimit = sendLimit;
	}
	public int getPageView() {
		return pageView;
	}
	public void setPageView(int pageView) {
		this.pageView = pageView;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public int getOpenStatus() {
		return openStatus;
	}
	public void setOpenStatus(int openStatus) {
		this.openStatus = openStatus;
	}
	public int getExamine() {
		return examine;
	}
	public void setExamine(int examine) {
		this.examine = examine;
	}
	public int getTotalOrder() {
		return totalOrder;
	}
	public void setTotalOrder(int totalOrder) {
		this.totalOrder = totalOrder;
	}
	public String getNotice() {
		return notice;
	}
	public void setNotice(String notice) {
		this.notice = notice;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public double getSendFee() {
		return sendFee;
	}
	public void setSendFee(double sendFee) {
		this.sendFee = sendFee;
	}
	
}
