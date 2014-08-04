package com.gos.iccardone.parser;


import org.json.JSONException;
import org.json.JSONObject;

import com.gos.iccardone.entity.ApplicationEntity;
import com.gos.iccardone.exception.ParseException;

public interface Parser<T extends ApplicationEntity> {
	
	public abstract T parse(JSONObject json) throws ParseException, JSONException;
	
    
}
