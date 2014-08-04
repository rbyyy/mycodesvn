package com.ssm.ssmbee.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.ssm.ssmbee.entity.ApplicationEntity;
import com.ssm.ssmbee.exception.ParseException;

public interface Parser<T extends ApplicationEntity> {
	
	public abstract T parse(JSONObject json) throws ParseException, JSONException;

}
