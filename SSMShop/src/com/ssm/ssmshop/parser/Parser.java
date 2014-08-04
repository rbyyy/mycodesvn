package com.ssm.ssmshop.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.ssm.ssmshop.entity.ApplicationEntity;
import com.ssm.ssmshop.exception.ParseException;

public interface Parser<T extends ApplicationEntity> {
	
	public abstract T parse(JSONObject json) throws ParseException, JSONException;

}
