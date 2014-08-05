package com.ssm.ssmbee;

import java.io.IOException;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.ssm.ssmbee.MainActivity;
import com.ssm.ssmbee.R;
import com.ssm.ssmbee.UserLoginActivity;
import com.ssm.ssmbee.entity.Auth;
import com.ssm.ssmbee.entity.BaseResponse;
import com.ssm.ssmbee.exception.BaseException;
import com.ssm.ssmbee.helper.SSMHelper;
import com.ssm.ssmbee.httpoperation.GosHttpApplication;

@SuppressLint("HandlerLeak")
public class UserLoginActivity extends BaseActivity {
	/**等待动画*/
	private		ProgressDialog			progressDialog;
	/**用户名*/
	private		EditText				userNameEditText;
	/**密码*/
	private		EditText				passWordEditText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		setContentView(R.layout.activity_userlogin);
		progressDialog = new ProgressDialog(UserLoginActivity.this);
		findview();
	}
	
	protected void findview() {
		userNameEditText = (EditText)findViewById(R.id.usernameEditText);
		passWordEditText = (EditText)findViewById(R.id.passwordEditText);
		
		((Button)findViewById(R.id.loginInButton)).setOnClickListener(new OnClickAction());
	}
	/**判断是否为空*/
	protected void isEmpty() {
		final String userNameString = userNameEditText.getText().toString();
		final String passWordString = passWordEditText.getText().toString();
		if (userNameString == null || userNameString.equals("")) {
			Toast.makeText(UserLoginActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
		}
		else if (passWordString == null || passWordString.equals("")) {
			Toast.makeText(UserLoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
		}
		else {
			SSMHelper.showProgressDialog(progressDialog);
			new Thread(){
				public void run() {
					userLogin(userNameString, passWordString);
				}
			}.start();
		}
	}
	/**点击事件*/
	class OnClickAction implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.loginInButton:
				isEmpty();
				break;
			default:
				break;
			}
		}
		
	}
	/**用户登录*/
	protected void userLogin(String userNameString, String passwordString) {
		try {
			String resIdString = SSMHelper.getSharePreStr(UserLoginActivity.this, GosHttpApplication.REGISTER_ID_STRING);
			BaseResponse<Auth> baseResponse = gosHttpOperation.invokerUserLogin(userNameString, passwordString, resIdString);
			int codeInt = baseResponse.getCode();
			if (codeInt == 1) {
				SSMHelper.putSharePre(UserLoginActivity.this, GosHttpApplication.USER_ID_STRING, baseResponse.getData().get(0).getRoleId());
				loginHandler.sendEmptyMessage(1);
			}
			else {
				loginHandler.sendEmptyMessage(0);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			loginHandler.sendEmptyMessage(0);
		} catch (BaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			loginHandler.sendEmptyMessage(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			loginHandler.sendEmptyMessage(0);
		}
	}
	
	private Handler loginHandler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				loginRequestFailed();
				break;
			case 1:
				loginRequestSuccessed();
				break;
			default:
				break;
			}
		}
	};
	/**登录请求失败*/
	protected void loginRequestFailed() {
		progressDialog.dismiss();
		Toast.makeText(UserLoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
	}
	/**登录请求成功*/
	protected void loginRequestSuccessed() {
		progressDialog.dismiss();
		Intent oneIntent = new Intent();
		oneIntent.setClass(UserLoginActivity.this, MainActivity.class);
		UserLoginActivity.this.startActivity(oneIntent);
		UserLoginActivity.this.finish();
	}
	
}
