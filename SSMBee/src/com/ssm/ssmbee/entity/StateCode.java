package com.ssm.ssmbee.entity;

import java.io.Serializable;

public class StateCode implements ApplicationEntity, Serializable {
	private static final long serialVersionUID = 1L;
	/**命令码*/
	private int action;
	/**是否成功状态*/
	private int code;
	/**返回结果数据*/
	private String data;
	
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	
	public int getAction() {
		return action;
	}
	public void setAction(int action) {
		this.action = action;
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	
}
