package com.law.fangyuan;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddComment {
	private Activity activity;
	private ProgressDialog dialog;
	private List<NameValuePair> params;
	
	public AddComment(final Activity activity) {
		this.activity = activity;
		Bundle bundle = activity.getIntent().getExtras();
		params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("aid", bundle.getString("id")));
		params.add(new BasicNameValuePair("fid", "0"));//ÆÀÂÛ¸¸id
		params.add(new BasicNameValuePair("dopost","send"));
		params.add(new BasicNameValuePair("comtype","comments"));
		params.add(new BasicNameValuePair("isconfirm","yes"));
		params.add(new BasicNameValuePair("type","ajax"));
		params.add(new BasicNameValuePair("android","1"));
		params.add(new BasicNameValuePair("notuser","1"));
//		params.add(new BasicNameValuePair("username","1"));
//		params.add(new BasicNameValuePair("pwd","1"));
//		params.add(new BasicNameValuePair("typeid","1"));//
//		params.add(new BasicNameValuePair("quotemsg","1"));//
		((Button) activity.findViewById(R.id.add)).setOnClickListener(new OnClickListener(){

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String saytext = ((EditText)activity.findViewById(R.id.saytext)).getText().toString();
//				params.add(new BasicNameValuePair("msg",saytext));
				params.add(new BasicNameValuePair("msg",URLEncoder.encode(saytext)));
				new Request().execute();
			}
			
		});
	}
	
	public void complete(String result){
		try {
			Toast.makeText(activity.getApplicationContext(), new JSONObject(result).getString("r").toString(), Toast.LENGTH_LONG).show();
			Bundle bundle = activity.getIntent().getExtras();
			Intent intent = new Intent();
			Bundle b = new Bundle();
			b.putString("classid", bundle.getString("classid"));
			b.putString("id", bundle.getString("id"));
			intent.putExtras(b);
			intent.setClass(activity, CommentList.class);
			activity.startActivity(intent);
			activity.finish();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	Runnable loading = new Runnable(){

		@SuppressWarnings("deprecation")
		@Override
		public void run() {
			// TODO Auto-generated method stub
	        dialog = new ProgressDialog(activity);
	        dialog.setMessage(activity.getString(R.string.releaseing));
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
	
	@SuppressLint("NewApi")
	class Request extends AsyncTask<Object, Object, Object>{

		@SuppressLint("NewApi")
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			loading.run();
		}

		@Override
		protected Object doInBackground(Object... arg0) {
			// TODO Auto-generated method stub
			Http http = new Http(activity);
			return http.POST("http://www.60886666.com/android/?m=feedback",params);
		}

		@SuppressLint("NewApi")
		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			dialog.dismiss();
			complete(result.toString());
		}		
	}
}
