package com.gov.nzjcy;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
/**
 * Created by chongzi on 14-4-8.
 */
public class NetworkTool {
    /**
     * 获取网址内容
     * @param url
     * @return
     * @throws Exception
     */
    public static String getContent(String url) throws Exception{
        StringBuilder sb = new StringBuilder();

        HttpClient client = new DefaultHttpClient();
        HttpParams httpParams = client.getParams();
        //设置网络超时参数
        HttpConnectionParams.setConnectionTimeout(httpParams, 30000);
        HttpConnectionParams.setSoTimeout(httpParams, 30000);
        //
        HttpGet httpGet=new HttpGet(url);
        HttpResponse response;
        try {
             response = client.execute(httpGet);
        }catch(Exception e){
            response=null;
            Log.e("getServerVerCode", e.getMessage());}
        if(response!=null) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"), 8192);

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                reader.close();
            }
        }
        return sb.toString();
    }
}
