package com.gov.nzjcy.http;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.message.BasicNameValuePair;

import com.gov.nzjcy.entity.ApplicationEntity;
import com.gov.nzjcy.exception.BaseException;
import com.gov.nzjcy.parser.Parser;

public interface HttpApi {
	abstract public ApplicationEntity doHttpRequest(HttpRequestBase httpRequest,
            Parser<? extends ApplicationEntity> parser) throws ParseException, BaseException, IOException;

	abstract public HttpResponse doHttpResponse(HttpRequestBase httpRequestBase) throws IOException;
	
    abstract public HttpGet createHttpGet(String url, NameValuePair... nameValuePairs);

    abstract public HttpPost createHttpPost(String url, NameValuePair... nameValuePairs);
    
    abstract public HttpURLConnection createHttpURLConnectionPost(URL url, String boundary) throws ParseException, BaseException, IOException; 
    
}
