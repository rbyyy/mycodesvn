package com.gov.nzjcy.entity;


public class AskForOpenEntity implements ApplicationEntity {
	private String realName;//用户姓名  
	private String address;//常住地址
	private String IDcardNumber;//身份证号
	private String sex;//性别，0女，1男
	private String email;//邮箱地址
	private String mobile;//移动电话
	private String tel;//座机
	private String mailingAddress;//邮寄地址 
	private String conDescription;//公开项类型
	private String description;//内容描述
	private String usefulDescription;//用途描述
	private String medium;//介质
	private String getMode;//获取方式
	private String userID;//用户ID

	
	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getIDcardNumber() {
		return IDcardNumber;
	}

	public void setIDcardNumber(String iDcardNumber) {
		IDcardNumber = iDcardNumber;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getMailingAddress() {
		return mailingAddress;
	}

	public void setMailingAddress(String mailingAddress) {
		this.mailingAddress = mailingAddress;
	}

	public String getConDescription() {
		return conDescription;
	}

	public void setConDescription(String conDescription) {
		this.conDescription = conDescription;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUsefulDescription() {
		return usefulDescription;
	}

	public void setUsefulDescription(String usefulDescription) {
		this.usefulDescription = usefulDescription;
	}

	public String getMedium() {
		return medium;
	}

	public void setMedium(String medium) {
		this.medium = medium;
	}

	public String getGetMode() {
		return getMode;
	}

	public void setGetMode(String getMode) {
		this.getMode = getMode;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}
	
//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + ((realName == null) ? 0 : realName.hashCode());
//		result = prime * result + ((address == null) ? 0 : address.hashCode());
//		result = prime * result + ((IDcardNumber == null) ? 0 : IDcardNumber.hashCode());
//		result = prime * result + ((sex == null) ? 0 : sex.hashCode());
//		result = prime * result + ((email == null) ? 0 : email.hashCode());
//		result = prime * result + ((mobile == null) ? 0 : mobile.hashCode());
//		result = prime * result + ((tel == null) ? 0 : tel.hashCode());
//		result = prime * result + ((mailingAddress == null) ? 0 : mailingAddress.hashCode());
//		result = prime * result + ((conDescription == null) ? 0 : conDescription.hashCode());
//		result = prime * result + ((description == null) ? 0 : description.hashCode());
//		result = prime * result + ((usefulDescription == null) ? 0 : usefulDescription.hashCode());
//		result = prime * result + ((medium == null) ? 0 : medium.hashCode());
//		result = prime * result + ((getMode == null) ? 0 : getMode.hashCode());
//		result = prime * result + ((userID == null) ? 0 : userID.hashCode());
//		return result;
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		AskForOpenEntity other = (AskForOpenEntity) obj;
//		
//		if (realName == null) {
//			if (other.realName != null)
//				return false;
//		} else if (!realName.equals(other.realName))
//			return false;
//		
//		if (address == null) {
//			if (other.address != null)
//				return false;
//		} else if (!address.equals(other.address))
//			return false;
//		
//		if (IDcardNumber == null) {
//			if (other.IDcardNumber != null)
//				return false;
//		} else if (!IDcardNumber.equals(other.IDcardNumber))
//			return false;
//		
//		if (sex == null) {
//			if (other.sex != null)
//				return false;
//		} else if (!sex.equals(other.sex))
//			return false;
//		
//		if (email == null) {
//			if (other.email != null)
//				return false;
//		} else if (!email.equals(other.email))
//			return false;
//		
//		if (mobile == null) {
//			if (other.mobile != null)
//				return false;
//		} else if (!mobile.equals(other.mobile))
//			return false;
//		if (tel == null) {
//			if (other.tel != null)
//				return false;
//		} else if (!tel.equals(other.tel))
//			return false;
//		if (mailingAddress == null) {
//			if (other.mailingAddress != null)
//				return false;
//		} else if (!mailingAddress.equals(other.mailingAddress))
//			return false;
//		if (conDescription == null) {
//			if (other.conDescription != null)
//				return false;
//		} else if (!conDescription.equals(other.conDescription))
//			return false;
//		if (description == null) {
//			if (other.description != null)
//				return false;
//		} else if (!description.equals(other.description))
//			return false;
//		if (usefulDescription == null) {
//			if (other.usefulDescription != null)
//				return false;
//		} else if (!usefulDescription.equals(other.usefulDescription))
//			return false;
//		if (medium == null) {
//			if (other.medium != null)
//				return false;
//		} else if (!medium.equals(other.medium))
//			return false;
//		if (getMode == null) {
//			if (other.getMode != null)
//				return false;
//		} else if (!getMode.equals(other.getMode))
//			return false;
//		if (userID == null) {
//			if (other.userID != null)
//				return false;
//		} else if (!userID.equals(other.userID))
//			return false;
//		
//		return true;
//	}
//
//	@Override
//	public String toString() {
//		return "AskForOpenEntity [real_name=" + realName + ",address=" + address + ",IDcardNumber=" + IDcardNumber +
//				", sex=" + sex +", email=" + email + ",mobile=" + mobile + ",tel="+ tel +",mailingAddress="+ mailingAddress +
//				",conDescription="+ conDescription + ",description="+ description + ",usefulDescription="+ usefulDescription + 
//				",medium="+ medium + ",getMode="+ getMode + ",userID="+ userID + "]";
//	}

}
