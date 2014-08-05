package com.gos.yypad.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.gos.yypad.entity.ApplicationEntity;
import com.gos.yypad.exception.ParseException;

public abstract class AbstractParser<T extends ApplicationEntity> implements Parser<T> {

    /** 
     * All derived parsers must implement parsing a JSONObject instance of themselves. 
     */
    public abstract T parse(JSONObject json) throws ParseException, JSONException;
    
}