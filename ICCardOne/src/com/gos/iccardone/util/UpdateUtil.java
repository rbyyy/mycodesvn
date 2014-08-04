package com.gos.iccardone.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gos.iccardone.R;




import java.io.IOException;
import java.io.InputStream;



import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;



import com.gos.iccardone.exception.BaseException;
import com.gos.iccardone.exception.ParseException;
import com.gos.iccardone.helper.GOSHelper;
import com.gos.iccardone.httpoperation.GosHttpApplication;
import com.gos.iccardone.httpoperation.GosHttpOperation;
import com.gos.iccardone.httpoperation.Http;

public class UpdateUtil {
	
	
	private Boolean isshow;
	private Activity activity;
	private ProgressDialog dialog=null;
	private String version,url,name,path;
	protected GosHttpApplication 	gosHttpApplication;
	protected GosHttpOperation 		gosHttpOperation;
	
	public UpdateUtil(Activity a){
		isshow = true;
		activity=a;
		gosHttpApplication = (GosHttpApplication) activity.getApplication();
		gosHttpOperation = gosHttpApplication.getGosHttpOperation();
		new Thread(){
			public void run() {
				operatinRequest();
			}
		}.start();
		//new getVersion().execute();
	}
	
	public UpdateUtil(Activity a,Boolean b){
		isshow = b;
		activity=a;
		gosHttpApplication = (GosHttpApplication) activity.getApplication();
		gosHttpOperation = gosHttpApplication.getGosHttpOperation();
		new Thread(){
			public void run() {
				operatinRequest();
			}
		}.start();
//		new getVersion().execute();
	}
	
	public void checkVersion(String result){
		if(result.toString().compareTo(GosHttpApplication.getVersionName(activity)) > 0){
			name = "lefubao.apk";
			path = Environment.getExternalStorageDirectory().toString()+"/"+name;
			doUpdateDailog();
		}else if(isshow){
			noUpdateDailog();
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

//	Runnable loading = new Runnable(){
//
//		@SuppressWarnings("deprecation")
//		@Override
//		public void run() {
//			// TODO Auto-generated method stub
//	        dialog = new ProgressDialog(activity);
//	        dialog.setMessage(activity.getString(R.string.checking));
//	        dialog.setIndeterminate(true);
//	        dialog.setCancelable(true);
//	        dialog.setButton(activity.getString(R.string.cancal), new DialogInterface.OnClickListener() {
//				
//				@Override
//				public void onClick(DialogInterface arg0, int arg1) {
//					// TODO Auto-generated method stub
//					dialog.cancel();
//				}
//			});
//	        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//	        dialog.show();
//		}
//    	
//    };
	
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
	
	/**网络请求*/
    protected void operatinRequest() {
    	try {
			HttpResponse aHttpResponse = gosHttpOperation.invokerSoftwareUpdateResponse();
			HttpEntity entity = aHttpResponse.getEntity();
			InputStream is = entity.getContent();
			String reString = GOSHelper.convertStreamToString(is);
			if (reString == null || reString.equals("")) {
				loginResultHandler.sendEmptyMessage(0);
			}
			else {
				com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(reString);
				String codeString = jsonObject.getString("code");
				if (codeString.equals("1")) {
					JSONArray responeArray = jsonObject.getJSONArray("data");

					if (responeArray.size() > 0) {
						for (int i = 0; i < responeArray.size(); i++) {
							JSONObject oneObject = responeArray.getJSONObject(i);
							version = oneObject.getString("version");
							url = oneObject.getString("url");
						}
						loginResultHandler.sendEmptyMessage(1);
					}
					else {
						loginResultHandler.sendEmptyMessage(0);
					}
				}
				else {
					loginResultHandler.sendEmptyMessage(0);
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
			loginResultHandler.sendEmptyMessage(0);
		} catch (BaseException e) {
			e.printStackTrace();
			loginResultHandler.sendEmptyMessage(0);
		} catch (IOException e) {
			e.printStackTrace();
			loginResultHandler.sendEmptyMessage(0);
		}
	}
    /**登录结果处理handler*/
	@SuppressLint("HandlerLeak")
	Handler loginResultHandler = new Handler()
	{
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				loginFailed();
				break;
			case 1:
				accessToView();
				break;
			default:
				break;
			}
		}
	};
	/**登录失败的显示*/
	protected void loginFailed() {
		
	}
	/**进入登录成功之后的界面*/
	protected void accessToView() {
		checkVersion(version);
	}
	
}
