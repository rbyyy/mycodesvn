package com.law.fangyuan;

import java.util.Random;

import android.R.string;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MemberRegister extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
        setContentView(R.layout.member_reg);
        AApp.getInstance().add(this);
        InitEven();
        new createVerify().execute();
	}
	
	private void InitEven(){
        TextView to_login = (TextView)findViewById(R.id.to_login);
        to_login.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent member = new Intent();
				member.setClass(MemberRegister.this,MemberLogin.class);
				MemberRegister.this.startActivity(member);
				MemberRegister.this.finish();
			}
        	
        });
		((ImageView) findViewById(R.id.ivTitleBtnLeft)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				MemberRegister.this.finish();
			}
			
		});
        RelativeLayout to_phone_form = (RelativeLayout)findViewById(R.id.to_phone_form);
        to_phone_form.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				to_step(2);
				Button reg_verify_btn = (Button)MemberRegister.this.findViewById(R.id.reg_verify_btn);
		        reg_verify_btn.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						new sendVerify().execute();
					}
		        	
		        });
			}
        	
        });
	}
	
	private void to_step(int index){
		int i = 1;
		while( true ){
			int id = getResources().getIdentifier("step_"+Integer.toString(i), "id", this.getPackageName());
			if(findViewById(id) == null) break;
			if(i == index){
				((LinearLayout)findViewById(id)).setVisibility(View.VISIBLE);
			}else{
				((LinearLayout)findViewById(id)).setVisibility(View.GONE);
			}
			i++;
		}

	}
	
	class createVerify extends AsyncTask<Object, Object, Object>{

		@Override
		protected Object doInBackground(Object... arg0) {
			// TODO Auto-generated method stub
			Http http = new Http(MemberRegister.this);
			return http.GET("http://www.60886666.com/android/reg.php?step=1");
		}
	
	}
	
	class sendVerify extends AsyncTask<Object, Object, Object>{

		@Override
		protected Object doInBackground(Object... arg0) {
			// TODO Auto-generated method stub
			Http http = new Http(MemberRegister.this);
			EditText reg_phone = (EditText)MemberRegister.this.findViewById(R.id.reg_phone);
			return http.GET("http://www.60886666.com/android/reg.php?step=2&mobile="+reg_phone.getText());
		}

		@SuppressLint("NewApi")
		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Toast.makeText(MemberRegister.this, result.toString(), Toast.LENGTH_LONG).show();
			if(result.toString().equals("验证码已经发送，请注意查收！")){
				((LinearLayout)MemberRegister.this.findViewById(R.id.reg_phone_form)).setVisibility(View.GONE);
				((LinearLayout)MemberRegister.this.findViewById(R.id.reg_verify_form)).setVisibility(View.VISIBLE);
				EditText check_verify = (EditText)MemberRegister.this.findViewById(R.id.check_verify);
				Random r = new Random();
				double rDouble = r.nextDouble()*1000000;
				Long rLong = (long)rDouble;
				String rString = rLong.toString();
				check_verify.setText(rString);
				Button check_verify_btn = (Button)MemberRegister.this.findViewById(R.id.check_verify_btn);
				check_verify_btn.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						new checkVerify().execute();
					}
		        	
		        });
			}
		}		
	}
	
	class checkVerify extends AsyncTask<Object, Object, Object>{

		@Override
		protected Object doInBackground(Object... arg0) {
			// TODO Auto-generated method stub
			Http http = new Http(MemberRegister.this);
			EditText check_verify = (EditText)MemberRegister.this.findViewById(R.id.check_verify);
			return http.GET("http://www.60886666.com/android/reg.php?step=3&code="+check_verify.getText());
		}

		@SuppressLint("NewApi")
		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Toast.makeText(MemberRegister.this, result.toString(), Toast.LENGTH_LONG).show();
			if(result.toString().equals("验证码正确！")){
				to_step(3);
				Button do_reg_btn = (Button)MemberRegister.this.findViewById(R.id.do_reg_btn);
				do_reg_btn.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						EditText pwd = (EditText)MemberRegister.this.findViewById(R.id.pwd);
						EditText pwd2 = (EditText)MemberRegister.this.findViewById(R.id.pwd2);
						if(pwd.getText().toString().equals(pwd2.getText().toString())){
							new register().execute();
						}else{
							Toast.makeText(MemberRegister.this, "两次密码不一样", Toast.LENGTH_LONG).show();
						}
					}
		        	
		        });
			}
		}		
	}
	
	class register extends AsyncTask<Object, Object, Object>{

		@Override
		protected Object doInBackground(Object... arg0) {
			// TODO Auto-generated method stub
			Http http = new Http(MemberRegister.this);
			EditText userid = (EditText)MemberRegister.this.findViewById(R.id.userid);
			EditText pwd = (EditText)MemberRegister.this.findViewById(R.id.pwd);
			EditText mobile = (EditText)MemberRegister.this.findViewById(R.id.reg_phone);
			EditText emailEditText = (EditText)MemberRegister.this.findViewById(R.id.emailEditText);
			String oneString = userid.getText().toString();
			String twoString = pwd.getText().toString();
			String threeString = mobile.getText().toString();
			String emailString = emailEditText.getText().toString();
			
			return http.GET("http://www.60886666.com/android/reg.php?step=4&userid="+userid.getText()+"&password="+pwd.getText()+"&mobile="+mobile.getText()+"&email="+emailEditText.getText());
		}

		@SuppressLint("NewApi")
		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Toast.makeText(MemberRegister.this, result.toString(), Toast.LENGTH_LONG).show();
			if(result.toString().equals("注册成功！")){
				Intent member = new Intent();
				member.setClass(MemberRegister.this,MemberCenter.class);
				MemberRegister.this.startActivity(member);
				MemberRegister.this.finish();
			}
		}		
	}

}
