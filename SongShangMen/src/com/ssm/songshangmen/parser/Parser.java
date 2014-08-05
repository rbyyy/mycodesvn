package com.ssm.songshangmen.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.ssm.songshangmen.entity.ApplicationEntity;
import com.ssm.songshangmen.exception.ParseException;

public interface Parser<T extends ApplicationEntity> {
	
	public abstract T parse(JSONObject json) throws ParseException, JSONException;

}
