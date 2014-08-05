package com.gos.yypad.entity;

import java.util.ArrayList;
import java.util.Collection;

public class Group <T extends ApplicationEntity> extends ArrayList<T> implements ApplicationEntity {

    private static final long serialVersionUID = 1L;

    private String mType;
    
    private String current_page;
    
    private String total_pages;
    
    public String getCurrent_page() {
		return current_page;
	}

	public void setCurrent_page(String current_page) {
		this.current_page = current_page;
	}

	public String getTotal_pages() {
		return total_pages;
	}

	public void setTotal_pages(String total_pages) {
		this.total_pages = total_pages;
	}

	public Group() {
        super();
    }
    
    public Group(Collection<T> collection) {
        super(collection);
    }

    public void setType(String type) {
        mType = type;
    }

    public String getType() {
        return mType;
    }
}
