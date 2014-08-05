package com.gos.iccardone.http;

import com.loopj.android.http.*;

public class RestClient {
    private static final String BASE_URL = "";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }
    
    public static void setTimeOut(int timeout){
    	client.setTimeout(timeout);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
    	System.out.println(BASE_URL + relativeUrl);
        return BASE_URL + relativeUrl;
    }
}