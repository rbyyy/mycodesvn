package com.ssm.ssmbee.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.ssm.ssmbee.entity.ApplicationEntity;
import com.ssm.ssmbee.exception.ParseException;

public abstract class AbstractParser<T extends ApplicationEntity> implements Parser<T> {

    /** 
     * All derived parsers must implement parsing a JSONObject instance of themselves. 
     */
    public abstract T parse(JSONObject json) throws ParseException, JSONException;
    
}