package com.ssm.songshangmen.activity;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ssm.songshangmen.R;
import com.ssm.songshangmen.entity.Auth;
import com.ssm.songshangmen.entity.BaseResponse;
import com.ssm.songshangmen.exception.BaseException;
import com.ssm.songshangmen.exception.ParseException;
import com.ssm.songshangmen.helper.SSMHelper;
import com.ssm.songshangmen.httpoperation.GosHttpApplication;

@SuppressLint("HandlerLeak")
public class UserLoginUpActivity extends BaseActivity {
	
	/**等待动画*/
	private		ProgressDialog			progressDialog;
	/**手机号*/
	private		EditText				userNameEditText;
	/**密码*/
	private		EditText				passWordEditText;
	/**确认密码*/
	private		EditText				confirmPassWordEditText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_userloginup);
		progressDialog = new ProgressDialog(UserLoginUpActivity.this);
		findview();
	}
	
	protected void findview() {
		LinearLayout leftBackLinearLayout = (LinearLayout)findViewById(R.id.leftBackLinearLayout);
		leftBackLinearLayout.setVisibility(View.VISIBLE);
		leftBackLinearLayout.setOnClickListener(new OnClickAction());
		((TextView)findViewById(R.id.title)).setText("用户注册");
		
		userNameEditText = (EditText)findViewById(R.id.usernameEditText);
		passWordEditText = (EditText)findViewById(R.id.passwordEditText);
		confirmPassWordEditText = (EditText)findViewById(R.id.confirmPasswordEditText);
		
		((Button)findViewById(R.id.loginUpButton)).setOnClickListener(new OnClickAction());
	}
	/**判断是否为空*/
	protected void isEmpty() {
		final String userNameString = userNameEditText.getText().toString();
		final String passWordString = passWordEditText.getText().toString();
		final String confirmPasswordString = confirmPassWordEditText.getText().toString();
		if (userNameString == null || userNameString.equals("")) {
			Toast.makeText(UserLoginUpActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
		}
		else if (passWordString == null || passWordString.equals("")) {
			Toast.makeText(UserLoginUpActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
		}
		else if (confirmPasswordString == null || confirmPasswordString.equals("")) {
			Toast.makeText(UserLoginUpActivity.this, "确认密码不能为空", Toast.LENGTH_SHORT).show();
		}
		else if (!confirmPasswordString.equals(passWordString)) {
			Toast.makeText(UserLoginUpActivity.this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
		}
		else {
			SSMHelper.showProgressDialog(progressDialog);
			new Thread(){
				public void run() {
					userRegister(userNameString, passWordString);
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
			case R.id.leftBackLinearLayout:
				UserLoginUpActivity.this.finish();
				break;
			case R.id.loginUpButton:
				isEmpty();
				break;
			default:
				break;
			}
		}
	}
	/**用户注册*/
	protected void userRegister(String userNameString, String passWordString) {
		try {
			BaseResponse<Auth> baseResponse = gosHttpOperation.invokerUserRegister(userNameString, passWordString, userNameString);
			int codeInt = baseResponse.getCode();
			if (codeInt == 1) {
				SSMHelper.putSharePre(UserLoginUpActivity.this, GosHttpApplication.USER_ID_STRING, baseResponse.getData().get(0).getRoleId());
				msgHandler.sendEmptyMessage(1);
			}
			else {
				msgHandler.sendEmptyMessage(0);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msgHandler.sendEmptyMessage(0);
		} catch (BaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msgHandler.sendEmptyMessage(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msgHandler.sendEmptyMessage(0);
		}
	}
	/**结果处理*/
	private Handler msgHandler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				requestDidFailed();
				break;
			case 1:
				requestDidSuccessed();
				break;
			default:
				break;
			}
		}
	};
	/**网络请求错误*/
	protected void requestDidFailed() {
		progressDialog.dismiss();
		Toast.makeText(UserLoginUpActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
	}
	/**网络请求成功*/
	protected void requestDidSuccessed() {
		userNameEditText.setText("");
		passWordEditText.setText("");
		confirmPassWordEditText.setText("");
		progressDialog.dismiss();
		Toast.makeText(UserLoginUpActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
	}
	
}
