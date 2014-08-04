package com.gov.nzjcy.entity;

public class SetAppealEntity implements ApplicationEntity {
	private String userIdString;//用户id
	private String setAppealUserName;//用户姓名
	private String setAppealBirthday;//出生年月
	private String setAppealSex;//性别0女，1男
	private String setAppealJiGuan;//籍贯
	private String company;//工作单位
	private String minzu; //民族
	private String address;//地址
	private String mobile;//电话
	private String IDCardNumber;//身份证号
	private String email;//电子邮件
	private String anjianType;//案件类别
	private String description;//申诉详细信息
	
	
	public String getSetAppealBirthday() {
		return setAppealBirthday;
	}
	public void setSetAppealBirthday(String setAppealBirthday) {
		this.setAppealBirthday = setAppealBirthday;
	}
	
	public String getSetAppealSex() {
		return setAppealSex;
	}
	public void setSetAppealSex(String setAppealSex) {
		this.setAppealSex = setAppealSex;
	}
	
	public String getSetAppealJiGuan() {
		return setAppealJiGuan;
	}
	public void setSetAppealJiGuan(String setAppealJiGuan) {
		this.setAppealJiGuan = setAppealJiGuan;
	}
	
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	
	public String getMinzu() {
		return minzu;
	}
	public void setMinzu(String minzu) {
		this.minzu = minzu;
	}
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	public String getIDCardNumber() {
		return IDCardNumber;
	}
	public void setIDCardNumber(String iDCardNumber) {
		IDCardNumber = iDCardNumber;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getAnjianType() {
		return anjianType;
	}
	public void setAnjianType(String anjianType) {
		this.anjianType = anjianType;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSetAppealUserName() {
		return setAppealUserName;
	}
	public void setSetAppealUserName(String setAppealUserName) {
		this.setAppealUserName = setAppealUserName;
	}
	public String getUserIdString() {
		return userIdString;
	}
	public void setUserIdString(String userIdString) {
		this.userIdString = userIdString;
	}
	
}
