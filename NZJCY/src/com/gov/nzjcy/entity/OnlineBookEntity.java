package com.gov.nzjcy.entity;


public class OnlineBookEntity implements ApplicationEntity {
	private String userIdString;//用户id
	private String SQR_shenfen;//申请人身份 
	private String SQR_shiwusuoName;//事务所名称
	private String SQR_isLower;//职业律师,0否，1是
	private String SQR_IsFromHelpCenter;//法律援助中心指派，0否，1是
	private String SQR_IsNeedHide;//需要回避，0否，1是
	private String SQR_realName; //姓名
	private String SQR_IDCardNumber;//身份证号
	private String SQR_mobile;//移动电话号
	private String BG_Realname;//被告姓名
	private String BG_Sex;//被告性别
	private String BG_Shenfenzheng;//被告身份证
	private String BG_Birthday;//被告出生年月
	private String BG_Jianchayuan;//检察院名称
	private String BG_Jieduan;//阶段
	private String BG_Cuoshi;//措施
	private String BG_Anyou;//案由
	private String WTR_Realname;//委托人姓名
	private String WTR_Shenfenzheng;//委托人身份证
	private String Type;//分类
	private String Description;//详细信息

//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + ((SQR_shenfen == null) ? 0 : SQR_shenfen.hashCode());
//		result = prime * result + ((SQR_shiwusuoName == null) ? 0 : SQR_shiwusuoName.hashCode());
//		result = prime * result + ((SQR_isLower == null) ? 0 : SQR_isLower.hashCode());
//		result = prime * result + ((SQR_IsFromHelpCenter == null) ? 0 : SQR_IsFromHelpCenter.hashCode());
//		result = prime * result + ((SQR_IsNeedHide == null) ? 0 : SQR_IsNeedHide.hashCode());
//		result = prime * result + ((SQR_realName == null) ? 0 : SQR_realName.hashCode());
//		result = prime * result + ((SQR_IDCardNumber == null) ? 0 : SQR_IDCardNumber.hashCode());
//		result = prime * result + ((SQR_mobile == null) ? 0 : SQR_mobile.hashCode());
//		result = prime * result + ((BG_Realname == null) ? 0 : BG_Realname.hashCode());
//		result = prime * result + ((BG_Sex == null) ? 0 : BG_Sex.hashCode());
//		result = prime * result + ((BG_Shenfenzheng == null) ? 0 : BG_Shenfenzheng.hashCode());
//		result = prime * result + ((BG_Jianchayuan == null) ? 0 : BG_Jianchayuan.hashCode());
//		result = prime * result + ((BG_Jieduan == null) ? 0 : BG_Jieduan.hashCode());
//		result = prime * result + ((BG_Cuoshi == null) ? 0 : BG_Cuoshi.hashCode());
//		
//		result = prime * result + ((BG_Anyou == null) ? 0 : BG_Anyou.hashCode());
//		result = prime * result + ((WTR_Realname == null) ? 0 : WTR_Realname.hashCode());
//		result = prime * result + ((WTR_Shenfenzheng == null) ? 0 : WTR_Shenfenzheng.hashCode());
//		result = prime * result + ((Type == null) ? 0 : Type.hashCode());
//		result = prime * result + ((Description == null) ? 0 : Description.hashCode());
//		
//		return result;
//	}
	
	public String getSQR_shenfen() {
		return SQR_shenfen;
	}

	public void setSQR_shenfen(String sQR_shenfen) {
		SQR_shenfen = sQR_shenfen;
	}

	public String getSQR_shiwusuoName() {
		return SQR_shiwusuoName;
	}

	public void setSQR_shiwusuoName(String sQR_shiwusuoName) {
		SQR_shiwusuoName = sQR_shiwusuoName;
	}

	public String getSQR_isLower() {
		return SQR_isLower;
	}

	public void setSQR_isLower(String sQR_isLower) {
		SQR_isLower = sQR_isLower;
	}

	public String getSQR_IsFromHelpCenter() {
		return SQR_IsFromHelpCenter;
	}

	public void setSQR_IsFromHelpCenter(String sQR_IsFromHelpCenter) {
		SQR_IsFromHelpCenter = sQR_IsFromHelpCenter;
	}

	public String getSQR_IsNeedHide() {
		return SQR_IsNeedHide;
	}

	public void setSQR_IsNeedHide(String sQR_IsNeedHide) {
		SQR_IsNeedHide = sQR_IsNeedHide;
	}

	public String getSQR_realName() {
		return SQR_realName;
	}

	public void setSQR_realName(String sQR_realName) {
		SQR_realName = sQR_realName;
	}

	public String getSQR_IDCardNumber() {
		return SQR_IDCardNumber;
	}

	public void setSQR_IDCardNumber(String sQR_IDCardNumber) {
		SQR_IDCardNumber = sQR_IDCardNumber;
	}

	public String getSQR_mobile() {
		return SQR_mobile;
	}

	public void setSQR_mobile(String sQR_mobile) {
		SQR_mobile = sQR_mobile;
	}

	public String getBG_Realname() {
		return BG_Realname;
	}

	public void setBG_Realname(String bG_Realname) {
		BG_Realname = bG_Realname;
	}

	public String getBG_Sex() {
		return BG_Sex;
	}

	public void setBG_Sex(String bG_Sex) {
		BG_Sex = bG_Sex;
	}

	public String getBG_Shenfenzheng() {
		return BG_Shenfenzheng;
	}

	public void setBG_Shenfenzheng(String bG_Shenfenzheng) {
		BG_Shenfenzheng = bG_Shenfenzheng;
	}

	public String getBG_Jianchayuan() {
		return BG_Jianchayuan;
	}

	public void setBG_Jianchayuan(String bG_Jianchayuan) {
		BG_Jianchayuan = bG_Jianchayuan;
	}

	public String getBG_Jieduan() {
		return BG_Jieduan;
	}

	public void setBG_Jieduan(String bG_Jieduan) {
		BG_Jieduan = bG_Jieduan;
	}

	public String getBG_Cuoshi() {
		return BG_Cuoshi;
	}

	public void setBG_Cuoshi(String bG_Cuoshi) {
		BG_Cuoshi = bG_Cuoshi;
	}

	public String getBG_Anyou() {
		return BG_Anyou;
	}

	public void setBG_Anyou(String bG_Anyou) {
		BG_Anyou = bG_Anyou;
	}

	public String getWTR_Realname() {
		return WTR_Realname;
	}

	public void setWTR_Realname(String wTR_Realname) {
		WTR_Realname = wTR_Realname;
	}

	public String getWTR_Shenfenzheng() {
		return WTR_Shenfenzheng;
	}

	public void setWTR_Shenfenzheng(String wTR_Shenfenzheng) {
		WTR_Shenfenzheng = wTR_Shenfenzheng;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getBG_Birthday() {
		return BG_Birthday;
	}

	public void setBG_Birthday(String bG_Birthday) {
		BG_Birthday = bG_Birthday;
	}

	public String getUserIdString() {
		return userIdString;
	}

	public void setUserIdString(String userIdString) {
		this.userIdString = userIdString;
	}

//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		OnlineBookEntity other = (OnlineBookEntity) obj;
//		
//		if (SQR_shenfen == null) {
//			if (other.SQR_shenfen != null)
//				return false;
//		} else if (!SQR_shenfen.equals(other.SQR_shenfen))
//			return false;
//		if (SQR_shiwusuoName == null) {
//			if (other.SQR_shiwusuoName != null)
//				return false;
//		} else if (!SQR_shiwusuoName.equals(other.SQR_shiwusuoName))
//			return false;
//		if (SQR_isLower == null) {
//			if (other.SQR_isLower != null)
//				return false;
//		} else if (!SQR_isLower.equals(other.SQR_isLower))
//			return false;
//		if (SQR_IsFromHelpCenter == null) {
//			if (other.SQR_IsFromHelpCenter != null)
//				return false;
//		} else if (!SQR_IsFromHelpCenter.equals(other.SQR_IsFromHelpCenter))
//			return false;
//		if (SQR_IsNeedHide == null) {
//			if (other.SQR_IsNeedHide != null)
//				return false;
//		} else if (!SQR_IsNeedHide.equals(other.SQR_IsNeedHide))
//			return false;
//		if (SQR_IDCardNumber == null) {
//			if (other.SQR_IDCardNumber != null)
//				return false;
//		} else if (!SQR_IDCardNumber.equals(other.SQR_IDCardNumber))
//			return false;
//		if (SQR_mobile == null) {
//			if (other.SQR_mobile != null)
//				return false;
//		} else if (!SQR_mobile.equals(other.SQR_mobile))
//			return false;
//		if (BG_Realname == null) {
//			if (other.BG_Realname != null)
//				return false;
//		} else if (!BG_Realname.equals(other.BG_Realname))
//			return false;
//		if (BG_Sex == null) {
//			if (other.BG_Sex != null)
//				return false;
//		} else if (!BG_Sex.equals(other.BG_Sex))
//			return false;
//		if (BG_Shenfenzheng == null) {
//			if (other.BG_Shenfenzheng != null)
//				return false;
//		} else if (!BG_Shenfenzheng.equals(other.BG_Shenfenzheng))
//			return false;
//		if (BG_Jianchayuan == null) {
//			if (other.BG_Jianchayuan != null)
//				return false;
//		} else if (!BG_Jianchayuan.equals(other.BG_Jianchayuan))
//			return false;
//		if (BG_Jieduan == null) {
//			if (other.BG_Jieduan != null)
//				return false;
//		} else if (!BG_Jieduan.equals(other.BG_Jieduan))
//			return false;
//		if (BG_Cuoshi == null) {
//			if (other.BG_Cuoshi != null)
//				return false;
//		} else if (!BG_Cuoshi.equals(other.BG_Cuoshi))
//			return false;
//		if (BG_Anyou == null) {
//			if (other.BG_Anyou != null)
//				return false;
//		} else if (!BG_Anyou.equals(other.BG_Anyou))
//			return false;
//		if (WTR_Realname == null) {
//			if (other.WTR_Realname != null)
//				return false;
//		} else if (!WTR_Realname.equals(other.WTR_Realname))
//			return false;
//		if (WTR_Shenfenzheng == null) {
//			if (other.WTR_Shenfenzheng != null)
//				return false;
//		} else if (!WTR_Shenfenzheng.equals(other.WTR_Shenfenzheng))
//			return false;
//		if (Type == null) {
//			if (other.Type != null)
//				return false;
//		} else if (!Type.equals(other.Type))
//			return false;
//		if (Description == null) {
//			if (other.Description != null)
//				return false;
//		} else if (!Description.equals(other.Description))
//			return false;
//		
//		return true;
//	}
//
//	@Override
//	public String toString() {
//		return "AskForOpenEntity [SQR_shenfen=" + SQR_shenfen + ",SQR_shiwusuoName=" + SQR_shiwusuoName + 
//				",SQR_isLower=" + SQR_isLower +
//				", SQR_IsFromHelpCenter=" + SQR_IsFromHelpCenter +", SQR_IsNeedHide=" + SQR_IsNeedHide + 
//				",SQR_realName=" + SQR_realName + ",SQR_IDCardNumber="+ SQR_IDCardNumber +
//				",SQR_mobile="+ SQR_mobile + ",BG_Realname="+ BG_Realname + 
//				",BG_Sex="+ BG_Sex + ",BG_Shenfenzheng="+ BG_Shenfenzheng + 
//				",BG_Jianchayuan="+ BG_Jianchayuan + ",BG_Jieduan="+ BG_Jieduan + ",BG_Cuoshi="+ BG_Cuoshi +
//				",BG_Anyou="+ BG_Anyou + ",WTR_Realname="+ WTR_Realname + ",WTR_Shenfenzheng="+ WTR_Shenfenzheng + 
//				",Type="+ Type + ",Description="+ Description + "]";
//	}

}
