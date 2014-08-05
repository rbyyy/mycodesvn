package com.law.fangyuan;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

public class AsyncBitmapLoader {
	
	private String path = null;
	private static AsyncBitmapLoader instance;

	public static AsyncBitmapLoader getInstance(Context context){
		if(null==instance){
			instance = new AsyncBitmapLoader(context);
		}
		return instance;
	}
	
	public AsyncBitmapLoader(Context context){
		path = context.getFilesDir().toString()+"/img/";
		File dir = new File(path);
		if(!dir.exists()){ dir.mkdirs();}
	}
	
	@SuppressLint("HandlerLeak")
	public void load(final ImageView iv, final String imageURL, final ImageCallBack imageCallBack){
		final String bitmapName = MD5(imageURL);
		if(new File(path + bitmapName).exists()){
			try {
				Drawable d = Drawable.createFromStream(new FileInputStream(path + bitmapName), "src");
				imageCallBack.imageLoad(iv, d);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				imageCallBack.imageLoad(iv, null);
				e.printStackTrace();
			}
			return;
		}
		
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if(new File(path + bitmapName).exists()){
					try {
						Drawable d = Drawable.createFromStream(new FileInputStream(path + bitmapName), "src");
						imageCallBack.imageLoad(iv, d);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						imageCallBack.imageLoad(iv, null);
						e.printStackTrace();
					}
				}
			}
		};
		
		new Thread() {
			
			@SuppressWarnings("resource")
			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpURLConnection conn;
				try {
					conn = (HttpURLConnection)((new URL(imageURL)).openConnection());
		        	InputStream is = conn.getInputStream();
		        	if(conn.getContentLength()>0 && is != null){
		        		String tpath = path + bitmapName +".tmp";
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
		        		new File(tpath).renameTo(new File(path + bitmapName));
		        	}
		        	handler.sendEmptyMessage(0);
		        	is.close();
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
		return;
	}
	
	public String MD5(String instr) {
		String s = null;
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9','a', 'b', 'c', 'd', 'e', 'f' };
		try {
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			md.update(instr.getBytes());
			byte tmp[] = md.digest();
			char str[] = new char[16 * 2];
			int k = 0;
			for (int i = 0; i < 16; i++) {
				byte byte0 = tmp[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			s = new String(str);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s.toLowerCase(Locale.ENGLISH);
	}
	
	public interface ImageCallBack	{
		public void imageLoad(ImageView view, Drawable d);
	}
}
