package com.ssm.ssmshop.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.ssm.ssmshop.entity.ApplicationEntity;
import com.ssm.ssmshop.exception.ParseException;

public abstract class AbstractParser<T extends ApplicationEntity> implements Parser<T> {

    /** 
     * All derived parsers must implement parsing a JSONObject instance of themselves. 
     */
    public abstract T parse(JSONObject json) throws ParseException, JSONException;
    
}