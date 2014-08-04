package com.gos.bluetoothtemp.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.gos.bluetoothtemp.helper.GOSHelper;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

public class Http {
	public Context context;
	public SharedPreferences sp;
	public DefaultHttpClient httpclient;

	public Http(Context context) {
		this.context = context;
		httpclient = new DefaultHttpClient();
		sp = GOSHelper.getSharedPreferences(context);
	}

	public boolean isConnection() {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = manager.getActiveNetworkInfo();
		if (networkinfo == null || !networkinfo.isAvailable()) {
			return false;
		}
		return true;
	}

	public String GET(String httpUrl) {
		String result = "";
		if (!isConnection())
			return result;
		try {
			HttpGet httpGet = new HttpGet(httpUrl);
			httpGet.setHeader("Cookie", sp.getString("cookie", null));
			HttpResponse httpResponse = httpclient.execute(httpGet);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = EntityUtils.toString(httpResponse.getEntity());
				setCookies();
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public String POST(String httpUrl, List<NameValuePair> params) {
		String result = "";
		if (!isConnection())
			return result;
		try {
			HttpPost httpPost = new HttpPost(httpUrl);
			httpPost.setHeader("Cookie", sp.getString("cookie", null));
			httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse httpResponse = httpclient.execute(httpPost);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = EntityUtils.toString(httpResponse.getEntity());
				setCookies();
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public void setCookies() {
		List<Cookie> cookies = httpclient.getCookieStore().getCookies();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < cookies.size(); i++) {
			Cookie cookie = cookies.get(i);
			String cookieName = cookie.getName();
			String cookieValue = cookie.getValue();
			if (!TextUtils.isEmpty(cookieName)
					&& !TextUtils.isEmpty(cookieValue)) {
				sb.append(cookieName + "=");
				sb.append(cookieValue + ";");
			}
		}
		if(sb.toString().equals(""))return;
		SharedPreferences.Editor edit = sp.edit();
		edit.putString("cookie", sb.toString());
		edit.commit();
	}
	
	public static List<NameValuePair> stripNulls(NameValuePair...nameValuePairs){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        for(int i = 0; i < nameValuePairs.length; i++){
            NameValuePair param = nameValuePairs[i];
            if(param.getValue() != null){
                System.out.println("Param: " + param);
                params.add(param);
            }
        }
        return params;
    }

}
