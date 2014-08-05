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
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MemberLogin extends Activity {
	private ProgressDialog dialog;
	private List<NameValuePair> params;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.member_login);
        AApp.getInstance().add(this);
        params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("fmdo","login"));
        params.add(new BasicNameValuePair("dopost","login"));
        TextView login_btn = (TextView)findViewById(R.id.login_btn);
        login_btn.setOnClickListener(new OnClickListener(){

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String userid = ((EditText)MemberLogin.this.findViewById(R.id.userid)).getText().toString();
				params.add(new BasicNameValuePair("userid",URLEncoder.encode(userid)));
				String pwd = ((EditText)MemberLogin.this.findViewById(R.id.pwd)).getText().toString();
				params.add(new BasicNameValuePair("pwd",URLEncoder.encode(pwd)));
				new doLogin().execute();
			}
        	
        });
        TextView to_reg = (TextView)findViewById(R.id.to_reg);
        to_reg.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent member = new Intent();
				member.setClass(MemberLogin.this,MemberRegister.class);
				MemberLogin.this.startActivity(member);
				MemberLogin.this.finish();
			}
        	
        });
		((ImageView) findViewById(R.id.ivTitleBtnLeft)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				MemberLogin.this.finish();
			}
			
		});
	}
	
	Runnable loading = new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
	        dialog = new ProgressDialog(MemberLogin.this);
	        dialog.setMessage("µÇÂ½ÖÐ\u2026");
	        dialog.setIndeterminate(true);
	        dialog.setCancelable(true);
	        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
	        dialog.show();
		}
    	
    };
	
	@SuppressLint("NewApi")
	class doLogin extends AsyncTask<Object, Object, Object>{

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
			Http http = new Http(MemberLogin.this);
			return http.POST("http://www.60886666.com/android/login.php",params);
		}

		@SuppressLint("NewApi")
		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			dialog.dismiss();
			loginSuccess(result.toString());
		}		
	}
	
	private void loginSuccess(String result){
		try {
			Toast.makeText(MemberLogin.this, new JSONObject(result).getString("msg").toString(), Toast.LENGTH_LONG).show();
			Intent member = new Intent();
			member.setClass(MemberLogin.this,MemberCenter.class);
			MemberLogin.this.startActivity(member);
			MemberLogin.this.finish();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

}
