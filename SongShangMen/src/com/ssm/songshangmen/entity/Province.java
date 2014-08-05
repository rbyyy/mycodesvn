package com.ssm.songshangmen.entity;

import java.io.Serializable;

/**
 * 省份
 * @author eternal
 *
 */
public class Province implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int id;
	/**省份ID*/
	private String provinceID;
	/**省份名*/
	private String province;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getProvinceID() {
		return provinceID;
	}
	public void setProvinceID(String provinceID) {
		this.provinceID = provinceID;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	
	

}
