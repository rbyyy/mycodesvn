package com.gos.bluetoothtemp.data;

public class TempAlarm {
	
	private String temp_msg_id;//消息id
	private String temp_tag;//温度报警标志
	private String temp_content;//温度报警内容
	private String temp_date;//温度报警日期
	private String temp_type;//温度类型
	private String temp_deal;//是否已处理
	
	
	public String getTemp_msg_id() {
		return temp_msg_id;
	}
	public void setTemp_msg_id(String temp_msg_id) {
		this.temp_msg_id = temp_msg_id;
	}
	
	public String getTemp_tag() {
		return temp_tag;
	}
	public void setTemp_tag(String temp_tag) {
		this.temp_tag = temp_tag;
	}
	
	public String getTemp_content() {
		return temp_content;
	}
	public void setTemp_content(String temp_content) {
		this.temp_content = temp_content;
	}
	
	public String getTemp_date() {
		return temp_date;
	}
	public void setTemp_date(String temp_date) {
		this.temp_date = temp_date;
	}
	
	public String getTemp_type() {
		return temp_type;
	}
	public void setTemp_type(String temp_type) {
		this.temp_type = temp_type;
	}
	public String getTemp_deal() {
		return temp_deal;
	}
	public void setTemp_deal(String temp_deal) {
		this.temp_deal = temp_deal;
	}


}
