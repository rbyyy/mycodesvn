package com.ssm.songshangmen.util;

import com.ssm.songshangmen.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.WindowManager;

import com.ssm.songshangmen.httpoperation.GosHttpApplication;

public class UpdateUtil {
	
	
	private Boolean isshow;
	private Activity activity;
	private ProgressDialog dialog=null;
	private String url,name,path;
	
	public UpdateUtil(Activity a){
		isshow = true;
		activity=a;
		//new getVersion().execute();
	}
	
	public UpdateUtil(Activity a,Boolean b){
		isshow = b;
		activity=a;
		//new getVersion().execute();
	}
	
	public void checkVersion(String result){
		try {
			JSONObject json = (JSONObject) new JSONObject(result).getJSONObject("r");
			if(json.has("name")){
				if(json.getString("version").toString().compareTo(GosHttpApplication.getVersionName(activity)) > 0){
					url = json.getString("url").toString();
					name = json.getString("name").toString();
					path = Environment.getExternalStorageDirectory().toString()+"/"+name;
					doUpdateDailog();
				}else if(isshow){
					noUpdateDailog();
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Boolean getType(){
		try {
			Class.forName("android.app.DownloadManager");
			return true;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return false;
		}
	}
	
	public void download(){
//		if(getType()){
//			new Download1(activity,url);
//		}else{
			Intent intent = new Intent(Intent.ACTION_RUN);
			intent.setClass(activity, Download.class);
			intent.putExtra("url", url);
			intent.putExtra("name", name);
			intent.putExtra("path", path);
			activity.startService(intent);
//		}
	}
	
	public void noUpdateDailog(){
		new AlertDialog.Builder(activity)
		.setIcon(android.R.drawable.ic_notification_overlay)
		.setTitle(R.string.update)
		.setMessage(R.string.update_cancel_msg)
		.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
    		public void onClick(DialogInterface dialog, int whichButton) {
    		/* User clicked OK so do some stuff */
    			dialog.dismiss();
    		}
		})
		.show();
	}
	
	public void doUpdateDailog(){
		new AlertDialog.Builder(activity)
		.setIcon(android.R.drawable.ic_notification_overlay)
		.setTitle(R.string.update)
		.setMessage(R.string.update_msg)
		.setPositiveButton(R.string.download, new DialogInterface.OnClickListener() {
    		public void onClick(DialogInterface dialog, int whichButton) {
    		/* User clicked OK so do some stuff */
    			dialog.dismiss();
    			download();
    		}
		})
		.setNegativeButton(R.string.cancal, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
			
		})
		.show();
	}

	Runnable loading = new Runnable(){

		@SuppressWarnings("deprecation")
		@Override
		public void run() {
			// TODO Auto-generated method stub
	        dialog = new ProgressDialog(activity);
	        dialog.setMessage(activity.getString(R.string.checking));
	        dialog.setIndeterminate(true);
	        dialog.setCancelable(true);
	        dialog.setButton(activity.getString(R.string.cancal), new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					dialog.cancel();
				}
			});
	        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
	        dialog.show();
		}
    	
    };
	
//	class getVersion extends AsyncTask<Object, Object, Object>{
//
//		@Override
//		protected void onPreExecute() {
//			// TODO Auto-generated method stub
//			super.onPreExecute();
//			if(isshow){
//				loading.run();
//			}
//		}
//		
//		@Override
//		protected Object doInBackground(Object... arg0) {
//			// TODO Auto-generated method stub
//			Http http = new Http(activity);
//			return http.GET("http://www.60886666.com/android/?m=update");
//		}
//
//		@Override
//		protected void onPostExecute(Object result) {
//			// TODO Auto-generated method stub
//			super.onPostExecute(result);
//			if(dialog!=null&&dialog.isShowing()){
//				dialog.dismiss();
//			}
//			checkVersion(result.toString());
//		}
//		
//	}
	
}
