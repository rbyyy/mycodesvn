package com.gov.nzjcy.parser;


import org.json.JSONException;
import org.json.JSONObject;

import com.gov.nzjcy.entity.ApplicationEntity;
import com.gov.nzjcy.exception.ParseException;

public interface Parser<T extends ApplicationEntity> {
	
	public abstract T parse(JSONObject json) throws ParseException, JSONException;
	
    
}
