package com.ssm.songshangmen.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.ssm.songshangmen.entity.ApplicationEntity;
import com.ssm.songshangmen.exception.ParseException;

public abstract class AbstractParser<T extends ApplicationEntity> implements Parser<T> {

    /** 
     * All derived parsers must implement parsing a JSONObject instance of themselves. 
     */
    public abstract T parse(JSONObject json) throws ParseException, JSONException;
    
}