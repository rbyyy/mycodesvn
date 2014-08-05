package com.ssm.ssmshop.http;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;

import com.ssm.ssmshop.entity.ApplicationEntity;
import com.ssm.ssmshop.exception.BaseException;
import com.ssm.ssmshop.parser.Parser;

public interface HttpApi {
	abstract public ApplicationEntity doHttpRequest(HttpRequestBase httpRequest,
            Parser<? extends ApplicationEntity> parser) throws ParseException, BaseException, IOException;
	
    abstract public HttpGet createHttpGet(String url, NameValuePair... nameValuePairs);

    abstract public HttpPost createHttpPost(String url, String requestData);
    
    abstract public HttpURLConnection createHttpURLConnectionPost(URL url, String boundary) throws ParseException, BaseException, IOException; 
    
}
