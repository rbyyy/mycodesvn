package com.law.fangyuan;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MemberCenter extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
        setContentView(R.layout.member_center);
        AMenu.isLogin(MemberCenter.this);
        AApp.getInstance().add(this);
        Button stow_btn = (Button)findViewById(R.id.stow_btn);
        stow_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent member = new Intent();
				member.setClass(MemberCenter.this,NewsCollect.class);
				MemberCenter.this.startActivity(member);
				MemberCenter.this.finish();
			}
        	
        });
        TextView exit_btn = (TextView)findViewById(R.id.exit_btn);
        exit_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SharedPreferences sp = AMenu.getSharedPreferences(MemberCenter.this);
				SharedPreferences.Editor editor = sp.edit();
				editor.putString("cookie", null);
				editor.commit();
				Intent member = new Intent();
				member.setClass(MemberCenter.this,MemberLogin.class);
				MemberCenter.this.startActivity(member);
				MemberCenter.this.finish();
			}
        	
        });
		((ImageView) findViewById(R.id.ivTitleBtnLeft)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				MemberCenter.this.finish();
			}
			
		});
		new Request().execute();
	}
	
	private void Complete(String result){
		try {
			JSONObject json = (JSONObject) new JSONObject(result).getJSONObject("r");
			((TextView) findViewById(R.id.userid)).setText(json.getString("userid").toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	class Request extends AsyncTask<Object, Object, Object>{

		@Override
		protected Object doInBackground(Object... arg0) {
			// TODO Auto-generated method stub
			String result = "";
			Http http = new Http(MemberCenter.this);
			HashMap<String,String> cookies = AMenu.getCookies(MemberCenter.this);
			if(cookies.containsKey("DedeUserID")){
				result = http.GET("http://www.60886666.com/android/?m=getUser&mid="+cookies.get("DedeUserID"));
			}
			return result;
		}

		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Complete(result.toString());
		}		
	}

}