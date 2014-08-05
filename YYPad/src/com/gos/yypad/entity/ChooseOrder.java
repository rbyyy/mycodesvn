package com.gos.yypad.entity;

public class ChooseOrder implements ApplicationEntity {
	
	private String order_id;//订单ID  
	private String order_type;//订单类型
	private String business_name;//商品名称
	private String business_price;//商品价格
	private String business_number;//商品数量
	private String business_date;//商品选购日期
	
	//订单ID 
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	//订单类型
	public String getOrder_type() {
		return order_type;
	}
	public void setOrder_type(String order_type) {
		this.order_type = order_type;
	}
	//商品名称
	public String getBusiness_name() {
		return business_name;
	}
	public void setBusiness_name(String business_name) {
		this.business_name = business_name;
	}
	//商品价格
	public String getBusiness_price() {
		return business_price;
	}
	public void setBusiness_price(String business_price) {
		this.business_price = business_price;
	}
	//商品数量
	public String getBusiness_number() {
		return business_number;
	}
	public void setBusiness_number(String business_number) {
		this.business_number = business_number;
	}
	//商品选购日期
	public String getBusiness_date() {
		return business_date;
	}
	public void setBusiness_date(String business_date) {
		this.business_date = business_date;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((order_id == null) ? 0 : order_id.hashCode());
		result = prime * result + ((order_type == null) ? 0 : order_type.hashCode());
		result = prime * result + ((business_name == null) ? 0 : business_name.hashCode());
		result = prime * result + ((business_price == null) ? 0 : business_price.hashCode());
		result = prime * result + ((business_number == null) ? 0 : business_number.hashCode());
		result = prime * result + ((business_date == null) ? 0 : business_date.hashCode());
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
		ChooseOrder other = (ChooseOrder) obj;
		if (order_id == null) {
			if (other.order_id != null)
				return false;
		} else if (!order_id.equals(other.order_id))
			return false;
		
		if (order_type == null) {
			if (other.order_type != null)
				return false;
		} else if (!order_type.equals(other.order_type))
			return false;
		
		if (business_name == null) {
			if (other.business_name != null)
				return false;
		} else if (!business_name.equals(other.business_name))
			return false;
		
		if (business_price == null) {
			if (other.business_price != null)
				return false;
		} else if (!business_price.equals(other.business_price))
			return false;
		
		if (business_number == null) {
			if (other.business_number != null)
				return false;
		} else if (!business_number.equals(other.business_number))
			return false;
		
		if (business_date == null) {
			if (other.business_date != null)
				return false;
		} else if (!business_date.equals(other.business_date))
			return false;

		return true;
	}

	@Override
	public String toString() {
		return "ChooseOrder [order_id=" + order_id + ",order_type=" + order_type + ",business_name=" + business_name + ", business_price=" + business_price +", business_number=" + business_number + ",business_date=" + business_date + "]";
	}
	
}
