package com.ssm.songshangmen.entity;

import java.util.List;

public class BaseResponse<T extends ApplicationEntity> implements ApplicationEntity {

	/**指令编号*/
	private int action;
	/**返回码*/
	private int code;
	/**总页码(针对商品)*/
	private int	totalPage;
	/**返回结果*/
	private List<T> data;
	
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
	public List<T> getData() {
		return data;
	}
	public void setData(List<T> data) {
		this.data = data;
	}
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	
}
