package com.gos.yypad.entity;

public class ShopList implements ApplicationEntity {
	private String shopcode;//门店ID  
	private String shopname;//门店名称
	private String shopmanager;//门店管理者名称
	private String shopaddress;//门店地址
	private String shopmobile;//联系电话
	private String shopopen;//是否启用，1启用，0未启用
	private String shopselected;//当前选择的店面

	public String getShopcode() {
		return shopcode;
	}

	public void setShopcode(String shopcode) {
		this.shopcode = shopcode;
	}

	public String getShopname() {
		return shopname;
	}

	public void setShopname(String shopname) {
		this.shopname = shopname;
	}

	public String getShopmanager() {
		return shopmanager;
	}

	public void setShopmanager(String shopmanager) {
		this.shopmanager = shopmanager;
	}

	public String getShopaddress() {
		return shopaddress;
	}

	public void setShopaddress(String shopaddress) {
		this.shopaddress = shopaddress;
	}

	public String getShopmobile() {
		return shopmobile;
	}

	public void setShopmobile(String shopmobile) {
		this.shopmobile = shopmobile;
	}

	public String getShopopen() {
		return shopopen;
	}

	public void setShopopen(String shopopen) {
		this.shopopen = shopopen;
	}

	public String getShopselected() {
		return shopselected;
	}

	public void setShopselected(String shopselected) {
		this.shopselected = shopselected;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((shopcode == null) ? 0 : shopcode.hashCode());
		result = prime * result + ((shopname == null) ? 0 : shopname.hashCode());
		result = prime * result + ((shopmanager == null) ? 0 : shopmanager.hashCode());
		result = prime * result + ((shopaddress == null) ? 0 : shopaddress.hashCode());
		result = prime * result + ((shopmobile == null) ? 0 : shopmobile.hashCode());
		result = prime * result + ((shopopen == null) ? 0 : shopopen.hashCode());
		result = prime * result + ((shopselected == null) ? 0 : shopselected.hashCode());
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
		ShopList other = (ShopList) obj;
		if (shopcode == null) {
			if (other.shopcode != null)
				return false;
		} else if (!shopcode.equals(other.shopcode))
			return false;
		
		if (shopname == null) {
			if (other.shopname != null)
				return false;
		} else if (!shopname.equals(other.shopname))
			return false;
		
		if (shopmanager == null) {
			if (other.shopmanager != null)
				return false;
		} else if (!shopmanager.equals(other.shopmanager))
			return false;
		
		if (shopaddress == null) {
			if (other.shopaddress != null)
				return false;
		} else if (!shopaddress.equals(other.shopaddress))
			return false;
		
		if (shopmobile == null) {
			if (other.shopmobile != null)
				return false;
		} else if (!shopmobile.equals(other.shopmobile))
			return false;
		
		if (shopopen == null) {
			if (other.shopopen != null)
				return false;
		} else if (!shopopen.equals(other.shopopen))
			return false;
		if (shopselected == null) {
			if (other.shopselected != null)
				return false;
		} else if (!shopselected.equals(other.shopselected))
			return false;
		
		return true;
	}

	@Override
	public String toString() {
		return "ShopList [shop_code=" + shopcode + ",shop_name=" + shopname + ",shop_manager=" + shopmanager + ", shop_address=" + shopaddress +", shop_mobile=" + shopmobile + ",shop_open=" + shopopen +",shop_selected="+ shopselected + "]";
	}

}
