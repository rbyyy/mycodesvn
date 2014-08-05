package com.gos.yypad.entity;

public class ProductName implements ApplicationEntity {
	private String productId;//产品ID  
	private String productName;//区域ID
	private String productPathid;//模块code
	
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	public String getProductPathid() {
		return productPathid;
	}
	public void setProductPathid(String productPathid) {
		this.productPathid = productPathid;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((productId == null) ? 0 : productId.hashCode());
		result = prime * result + ((productName == null) ? 0 : productName.hashCode());
		result = prime * result + ((productPathid == null) ? 0 : productPathid.hashCode());
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
		ProductName other = (ProductName) obj;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		
		if (productName == null) {
			if (other.productName != null)
				return false;
		} else if (!productName.equals(other.productName))
			return false;
		
		if (productPathid == null) {
			if (other.productPathid != null)
				return false;
		} else if (!productPathid.equals(other.productPathid))
			return false;
		
		return true;
	}

	@Override
	public String toString() {
		return "ProductName [product_id=" + productId + ",product_name=" + productName + ",product_pathid=" + productPathid + "]";
	}
	
}
