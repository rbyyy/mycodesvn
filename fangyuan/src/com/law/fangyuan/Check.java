package com.law.fangyuan;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.os.IBinder;

public class Check extends Service {
	public int interval = 3600; 
	private Timer timer;
	private String img;
	private FileCache file;
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		img = this.getFilesDir().toString()+"/splash.png";
		file = new FileCache(this);
		timer = new Timer();		
		timer.scheduleAtFixedRate(new TimerTask(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				check();
			}
			
		}, 0, 1000*interval);
	}
	
	
	
	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(timer != null){
			timer.cancel();
		}
	}
	
	public void check(){
		String result = get("http://www.60886666.com/android/?m=check");
		if(result.equals("")) return;
		try {
			JSONObject json = (JSONObject) new JSONObject(result).getJSONObject("r");
			if(json.has("splash_md5")){
				String splash_md5 = json.getString("splash_md5").toString();
				if(!splash_md5.equals(file.get("splash_update"))){
					JSONObject splash = (JSONObject) json.getJSONObject("splash");
					if(!splash.getString("img").toString().equals("")){
						download(splash.getString("img").toString(), img);
						file.set("splash_update",splash_md5);
					}
				}
			}
			if(json.has("news_md5")){
				String news_md5 = json.getString("news_md5").toString();
				if(!news_md5.equals(file.get("news_update"))){
					JSONObject news = (JSONObject) json.getJSONObject("news");
					showTitle(news.getString("title").toString(),
							news.getString("text").toString(),
							news.getString("classid").toString(),
							news.getString("id").toString());
					file.set("news_update",news_md5);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@SuppressWarnings("deprecation")
	public void showTitle(String title, String text, String classid, String id){
		showSound();
		NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		Notification n = new Notification(R.drawable.ic_launcher, title, System.currentTimeMillis());
		n.flags = Notification.FLAG_AUTO_CANCEL;
//		n.flags = Notification.FLAG_ONGOING_EVENT;
		Intent i = new Intent(this, NewsContent.class);
		Bundle b = new Bundle();
		b.putString("classid", classid);
		b.putString("id", id);
		b.putString("check", "check");
		i.putExtras(b);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent contentIntent = PendingIntent.getActivity(this, R.string.app_name, i, PendingIntent.FLAG_UPDATE_CURRENT);
		n.setLatestEventInfo(this, title, text, contentIntent);
		nm.notify(R.string.app_name, n);
	}
	
	@SuppressLint("NewApi")
	public void showSound(){
		SharedPreferences cfg = AMenu.getSharedPreferences(Check.this);
		if(!cfg.getBoolean("news_sound", true))return;
		final SoundPool sp= new SoundPool(3, AudioManager.STREAM_SYSTEM, 0);//第一个参数为同时播放数据流的最大个数，第二数据流类型，第三为声音质量
        final int music = sp.load(this, R.raw.prompt, 1); //把你的声音素材放到res/raw里，第2个参数即为资源文件，第3个为音乐的优先级
        sp.setOnLoadCompleteListener(new OnLoadCompleteListener(){

			@Override
			public void onLoadComplete(SoundPool arg0, int arg1, int arg2) {
				// TODO Auto-generated method stub
				sp.play(music, 1, 1, 0, 0, 1);
			}
        	
        });        
	}
	
    public String get(String httpUrl){
    	String result = "";
        try {
        	HttpGet httpGet = new HttpGet(httpUrl);
	        HttpResponse httpResponse = new DefaultHttpClient().execute(httpGet);   
	        if(httpResponse.getStatusLine().getStatusCode()==HttpStatus.SC_OK){ 
		        result = EntityUtils.toString(httpResponse.getEntity());
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
	
	@SuppressWarnings("resource")
	public void download(String url, String path){
    	try{
        	URL u = new URL(url);
        	URLConnection conn = u.openConnection();
        	conn.connect();
        	InputStream is = conn.getInputStream();
        	if(conn.getContentLength()>0 && is != null){
        		String tpath = path+".tmp";
        		FileOutputStream fos = new FileOutputStream(tpath);
        		int pos = 0;
        		byte buf[] = new byte[1024];
        		while(true){
        			pos = is.read(buf);
        			if(pos <0 ){
        				break;
        			}
        			fos.write(buf, 0, pos);
        		}
        		new File(tpath).renameTo(new File(path));
        	}
        	is.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
}
