package com.gos.yypad.entity;

public class ProductList implements ApplicationEntity {
	private String 	productId;//产品ID  
	private String 	productName;//产品名称
	private String 	productPicurl;//产品图片链接
	private String 	productPrice;//产品价格  
	private String 	productPremark;//产品备注
	private String 	productOperatorname;//产品操作系统
	private String 	productIson;//产品是否在售 
	private String	productClassPath;//产品所属类别
	
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
	
	public String getProductPicurl() {
		return productPicurl;
	}
	public void setProductPicurl(String productPicurl) {
		this.productPicurl = productPicurl;
	}
	
	public String getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(String productPrice) {
		this.productPrice = productPrice;
	}
	
	public String getProductPremark() {
		return productPremark;
	}
	public void setProductPremark(String productPremark) {
		this.productPremark = productPremark;
	}
	
	public String getProductOperatorname() {
		return productOperatorname;
	}
	public void setProductOperatorname(String productOperatorname) {
		this.productOperatorname = productOperatorname;
	}
	
	public String getProductIson() {
		return productIson;
	}
	public void setProductIson(String productIson) {
		this.productIson = productIson;
	}
	
	public String getProductClassPath() {
		return productClassPath;
	}
	public void setProductClassPath(String productClassPath) {
		this.productClassPath = productClassPath;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((productId == null) ? 0 : productId.hashCode());
		result = prime * result + ((productName == null) ? 0 : productName.hashCode());
		result = prime * result + ((productPicurl == null) ? 0 : productPicurl.hashCode());
		result = prime * result + ((productPrice == null) ? 0 : productPrice.hashCode());
		result = prime * result + ((productPremark == null) ? 0 : productPremark.hashCode());
		result = prime * result + ((productOperatorname == null) ? 0 : productOperatorname.hashCode());
		result = prime * result + ((productIson == null) ? 0 : productIson.hashCode());
		result = prime * result + ((productClassPath == null) ? 0 : productClassPath.hashCode());
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
		ProductList other = (ProductList) obj;
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
		
		if (productPicurl == null) {
			if (other.productPicurl != null)
				return false;
		} else if (!productPicurl.equals(other.productPicurl))
			return false;
		
		if (productPrice == null) {
			if (other.productPrice != null)
				return false;
		} else if (!productPrice.equals(other.productPrice))
			return false;
		
		if (productPremark == null) {
			if (other.productPremark != null)
				return false;
		} else if (!productPremark.equals(other.productPremark))
			return false;
		
		if (productOperatorname == null) {
			if (other.productOperatorname != null)
				return false;
		} else if (!productOperatorname.equals(other.productOperatorname))
			return false;
		
		if (productIson == null) {
			if (other.productIson != null)
				return false;
		} else if (!productIson.equals(other.productIson))
			return false;
		
		if (productClassPath == null) {
			if (other.productClassPath != null)
				return false;
		} else if (!productClassPath.equals(other.productClassPath))
			return false;
		
		return true;
	}

	@Override
	public String toString() {
		return "ProductList [product_id=" + productId + ",product_name=" + productName + ",product_picurl=" + productPicurl + ",product_price=" + productPrice +",product_premark=" + productPremark +","
				+ "product_operatorname=" + productPremark +",product_ison=" + productIson + ",product_classpath=" + productClassPath + "]";
	}

}
