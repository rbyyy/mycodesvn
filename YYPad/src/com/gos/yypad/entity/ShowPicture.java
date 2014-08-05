package com.gos.yypad.entity;

public class ShowPicture implements ApplicationEntity {
	private String pictureId;//图片ID  
	private String areaCode;//区域ID
	private String moduleCode;//模块code
	private String moduleName;//模块者名称
	private String picUrl;//图片地址
	private String date;//时间
	private String picDescribe;//图片描述

	public String getPictureId() {
		return pictureId;
	}

	public void setPictureId(String pictureId) {
		this.pictureId = pictureId;
	}
	
	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	
	public String getModuleCode() {
		return moduleCode;
	}

	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	public String getPicDescribe() {
		return picDescribe;
	}

	public void setPicDescribe(String picDescribe) {
		this.picDescribe = picDescribe;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pictureId == null) ? 0 : pictureId.hashCode());
		result = prime * result + ((areaCode == null) ? 0 : areaCode.hashCode());
		result = prime * result + ((moduleCode == null) ? 0 : moduleCode.hashCode());
		result = prime * result + ((moduleName == null) ? 0 : moduleName.hashCode());
		result = prime * result + ((picUrl == null) ? 0 : picUrl.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((picDescribe == null) ? 0 : picDescribe.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ShowPicture other = (ShowPicture) obj;
		if (pictureId == null) {
			if (other.pictureId != null)
				return false;
		} else if (!pictureId.equals(other.pictureId))
			return false;
		
		if (areaCode == null) {
			if (other.areaCode != null)
				return false;
		} else if (!areaCode.equals(other.areaCode))
			return false;
		
		if (moduleCode == null) {
			if (other.moduleCode != null)
				return false;
		} else if (!moduleCode.equals(other.moduleCode))
			return false;
		
		if (moduleName == null) {
			if (other.moduleName != null)
				return false;
		} else if (!moduleName.equals(other.moduleName))
			return false;
		
		if (picUrl == null) {
			if (other.picUrl != null)
				return false;
		} else if (!picUrl.equals(other.picUrl))
			return false;
		
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		
		if (picDescribe == null) {
			if (other.picDescribe != null)
				return false;
		} else if (!picDescribe.equals(other.picDescribe))
			return false;
		
		return true;
	}

	@Override
	public String toString() {
		return "ShowPicture [picture_id=" + pictureId + ",area_code=" + areaCode + ",module_code="+ moduleCode + ",module_name=" + moduleName + 
				", picture_url=" + picUrl +", picture_date=" + date + ",picture_describe=" + picDescribe + "]";
	}

}
