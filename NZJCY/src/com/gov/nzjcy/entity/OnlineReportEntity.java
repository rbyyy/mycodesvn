package com.gov.nzjcy.entity;

public class OnlineReportEntity implements ApplicationEntity {
	private String userIdString;//用户id
	private String OnlineReportUserName;//用户姓名  
	private String OnlineReportIDCardNumber;//身份证信息
	private String OnlineReportMobile;//电话信息
	private String OnlineReportTel;//座机
	private String OnlineReportAddress;//地址
	private String OnlineReportEmail; //邮箱
	private String BJB_Person_Type;//被举报人类型（0职务犯罪线索，1违法违纪干警）暂定
	private String BJB_Person_Realname;//被举报人姓名
	private String BJB_Person_Sex;//被举报人性别0女，1男
	private String BJB_Person_CompanyNameOrBumen;//被举报人部门
	private String BJB_Person_Zhiji;//被举报人职级
	private String BJB_Person_Zhiwu;//被举报人职务
	private String BJB_Person_Address;//被举报人地址信息
	private String BJB_Person_Anyou;//被举报案由
	private String BJB_Description;//举报详细情

	
	public String getOnlineReportIDCardNumber() {
		return OnlineReportIDCardNumber;
	}

	public void setOnlineReportIDCardNumber(String onlineReportIDCardNumber) {
		OnlineReportIDCardNumber = onlineReportIDCardNumber;
	}

	public String getOnlineReportMobile() {
		return OnlineReportMobile;
	}

	public void setOnlineReportMobile(String onlineReportMobile) {
		OnlineReportMobile = onlineReportMobile;
	}

	public String getOnlineReportTel() {
		return OnlineReportTel;
	}

	public void setOnlineReportTel(String onlineReportTel) {
		OnlineReportTel = onlineReportTel;
	}

	public String getOnlineReportAddress() {
		return OnlineReportAddress;
	}

	public void setOnlineReportAddress(String onlineReportAddress) {
		OnlineReportAddress = onlineReportAddress;
	}

	public String getOnlineReportEmail() {
		return OnlineReportEmail;
	}

	public void setOnlineReportEmail(String onlineReportEmail) {
		OnlineReportEmail = onlineReportEmail;
	}

	public String getBJB_Person_Type() {
		return BJB_Person_Type;
	}

	public void setBJB_Person_Type(String bJB_Person_Type) {
		BJB_Person_Type = bJB_Person_Type;
	}

	public String getBJB_Person_Realname() {
		return BJB_Person_Realname;
	}

	public void setBJB_Person_Realname(String bJB_Person_Realname) {
		BJB_Person_Realname = bJB_Person_Realname;
	}

	public String getBJB_Person_Sex() {
		return BJB_Person_Sex;
	}

	public void setBJB_Person_Sex(String bJB_Person_Sex) {
		BJB_Person_Sex = bJB_Person_Sex;
	}

	public String getBJB_Person_CompanyNameOrBumen() {
		return BJB_Person_CompanyNameOrBumen;
	}

	public void setBJB_Person_CompanyNameOrBumen(
			String bJB_Person_CompanyNameOrBumen) {
		BJB_Person_CompanyNameOrBumen = bJB_Person_CompanyNameOrBumen;
	}

	public String getBJB_Person_Zhiji() {
		return BJB_Person_Zhiji;
	}

	public void setBJB_Person_Zhiji(String bJB_Person_Zhiji) {
		BJB_Person_Zhiji = bJB_Person_Zhiji;
	}

	public String getBJB_Person_Zhiwu() {
		return BJB_Person_Zhiwu;
	}

	public void setBJB_Person_Zhiwu(String bJB_Person_Zhiwu) {
		BJB_Person_Zhiwu = bJB_Person_Zhiwu;
	}

	public String getBJB_Person_Address() {
		return BJB_Person_Address;
	}

	public void setBJB_Person_Address(String bJB_Person_Address) {
		BJB_Person_Address = bJB_Person_Address;
	}

	public String getBJB_Person_Anyou() {
		return BJB_Person_Anyou;
	}

	public void setBJB_Person_Anyou(String bJB_Person_Anyou) {
		BJB_Person_Anyou = bJB_Person_Anyou;
	}

	public String getBJB_Description() {
		return BJB_Description;
	}

	public void setBJB_Description(String bJB_Description) {
		BJB_Description = bJB_Description;
	}

	public String getOnlineReportUserName() {
		return OnlineReportUserName;
	}

	public void setOnlineReportUserName(String onlineReportUserName) {
		OnlineReportUserName = onlineReportUserName;
	}

	public String getUserIdString() {
		return userIdString;
	}

	public void setUserIdString(String userIdString) {
		this.userIdString = userIdString;
	}
}
