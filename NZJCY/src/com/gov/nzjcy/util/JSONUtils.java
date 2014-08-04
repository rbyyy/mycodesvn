package com.gov.nzjcy.util;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import com.gov.nzjcy.entity.ApplicationEntity;
import com.gov.nzjcy.exception.BaseException;
import com.gov.nzjcy.exception.ParseException;
import com.gov.nzjcy.parser.Parser;

public class JSONUtils {
    
    private static final boolean DEBUG = DebugConfig.DEBUG;
    private static final Logger LOG = Logger.getLogger(Parser.class.getCanonicalName());
    
	public static ApplicationEntity consume(Parser<? extends ApplicationEntity> parser, String content)
        throws ParseException, BaseException, IOException {
        
    	if(DEBUG){
            LOG.log(Level.FINE, "http response: " + content);
            System.out.println("http response: " + content);
    	}
        
        try {
            JSONObject json = new JSONObject(content);
            if(json.has("error")){
            	throw new ParseException(((JSONObject)json.get("error")).getString("message"));
            }else{
            	return parser.parse(json);
            }
        } catch (JSONException ex) {
            throw new ParseException("Error parsing JSON response: " + ex.getMessage());
        }
    }
}