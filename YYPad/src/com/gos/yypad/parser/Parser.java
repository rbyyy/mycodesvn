package com.gos.yypad.parser;


import org.json.JSONException;
import org.json.JSONObject;

import com.gos.yypad.entity.ApplicationEntity;
import com.gos.yypad.exception.ParseException;

public interface Parser<T extends ApplicationEntity> {
	
	public abstract T parse(JSONObject json) throws ParseException, JSONException;
	
    
}
