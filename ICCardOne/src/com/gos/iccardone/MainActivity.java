package com.gos.iccardone;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


import com.gos.iccardone.exception.BaseException;
import com.gos.iccardone.exception.ParseException;
import com.gos.iccardone.helper.GOSHelper;
import com.gos.iccardone.httpoperation.GosHttpApplication;
import com.gos.iccardone.util.UpdateUtil;

import android.R.bool;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class MainActivity extends BaseActivity {
	private long 				exitTime=0;
	private EditText			userNameEditText;
	private EditText			passwordEditText;
	private ProgressDialog 		mSaveDialog = null;
	private CheckBox			rememberCheckBox;
	private Boolean				rememberFlag = false;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		new UpdateUtil(MainActivity.this, false);
		if (GOSHelper.getSharePreBoolean(MainActivity.this, GosHttpApplication.REMEMBER_PASSWORD_FLAG)) {
			rememberFlag = true;
		}
		else {
			rememberFlag = false;
		}
		initView();
	}
	/***/
	protected void initView() {
		userNameEditText = (EditText)findViewById(R.id.userNameEditText);
		if (rememberFlag) {
			userNameEditText.setText(GOSHelper.getSharePreStr(MainActivity.this, GosHttpApplication.USER_NAME_STRING));
		}
		
		passwordEditText = (EditText)findViewById(R.id.passwordEditText);
		rememberCheckBox = (CheckBox)findViewById(R.id.rememberCheckBox);
		if (rememberFlag) {
			rememberCheckBox.setChecked(true);
		}
		else {
			rememberCheckBox.setChecked(false);
		}
		rememberCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					rememberFlag = true;
					GOSHelper.putSharePre(MainActivity.this, GosHttpApplication.REMEMBER_PASSWORD_FLAG, true);
				}
				else {
					rememberFlag = false;
					GOSHelper.putSharePre(MainActivity.this, GosHttpApplication.REMEMBER_PASSWORD_FLAG, false);
				}
			}
		});
		Button loginButton = (Button)findViewById(R.id.loginUpButton);
		loginButton.setOnClickListener(new MyOnClickListener());
	}
	
	protected class MyOnClickListener implements OnClickListener
	{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			isEmpty();
		}
	}
	/**判断输入是否为空*/
	protected void isEmpty() {
		final String usernameString = userNameEditText.getText().toString();
		final String passwordString = passwordEditText.getText().toString();
		if (usernameString.equals("")) {
			Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
		}
		else if (passwordString.equals("")) {
			Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
		}
		else {
			GOSHelper.putSharePre(MainActivity.this, GosHttpApplication.USER_NAME_STRING, usernameString);
			GOSHelper.putSharePre(MainActivity.this, GosHttpApplication.USER_PASSWORD_STRING, passwordString);
			mSaveDialog = ProgressDialog.show(MainActivity.this, "用户登录", "正在登录中，请稍等...", true);
			new Thread()
			{
				public void run() {
					loginInOperation("HAE7RKOA", "70143234");
				}
			}.start();
		}
	}
	/**网络操作*/
	protected void loginInOperation(String usernameString, String passwordString) {
		try {
			HttpResponse aHttpResponse = gosHttpOperation.invokerUserLoginUpResponse(usernameString, passwordString);
			HttpEntity entity = aHttpResponse.getEntity();
			InputStream is = entity.getContent();
			String reString = GOSHelper.convertStreamToString(is);
			if (reString == null || reString.equals("")) {
				loginResultHandler.sendEmptyMessage(0);
			}
			else {
				JSONObject jsonObject = JSON.parseObject(reString);
				String stateString = jsonObject.getString("code");
				if (stateString.equals("1")) {
					String tokenString = jsonObject.getString("data");
					GOSHelper.putSharePre(this, GosHttpApplication.USER_TOKEN_STRING, tokenString);
					loginResultHandler.sendEmptyMessage(1);
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
	Handler loginResultHandler = new Handler()
	{
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				loginFailed();
				break;
			case 1:
				iccardLoginThread();
				break;
			default:
				break;
			}
		}
	};
	/**登录失败的显示*/
	protected void loginFailed() {
		mSaveDialog.dismiss();
		Toast.makeText(this, "登录失败，卡号或密码错误", Toast.LENGTH_SHORT).show();
	}
	/**操作iccard登录*/
	protected void iccardLoginThread() {
		new Thread(){
			public void run() {
				icCardLoginInOperation();
			}
		}.start();
	}
	/**ICCard登录*/
	/**网络操作*/
	protected void icCardLoginInOperation() {
		try {
			String cardIdString = userNameEditText.getText().toString();
			String passwordString = passwordEditText.getText().toString();
			String tokenString = GOSHelper.getSharePreStr(this, GosHttpApplication.USER_TOKEN_STRING);
			HttpResponse aHttpResponse = gosHttpOperation.invokerICCardLoginResponse(cardIdString, passwordString, tokenString);
			HttpEntity entity = aHttpResponse.getEntity();
			InputStream is = entity.getContent();
			String reString = GOSHelper.convertStreamToString(is);
			if (reString == null || reString.equals("")) {
				loginResultHandler.sendEmptyMessage(0);
			}
			else {
				JSONObject jsonObject = JSON.parseObject(reString);
				String stateString = jsonObject.getString("code");
				if (stateString.equals("1")) {
					GOSHelper.putSharePre(this, GosHttpApplication.USER_NAME_STRING, cardIdString);
					GOSHelper.putSharePre(this, GosHttpApplication.USER_PASSWORD_STRING, passwordString);
					loginResultOneHandler.sendEmptyMessage(1);
				}
				else {
					loginResultOneHandler.sendEmptyMessage(0);
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
			loginResultOneHandler.sendEmptyMessage(0);
		} catch (BaseException e) {
			e.printStackTrace();
			loginResultOneHandler.sendEmptyMessage(0);
		} catch (IOException e) {
			e.printStackTrace();
			loginResultOneHandler.sendEmptyMessage(0);
		}
	}
	/**登录结果处理handler*/
	Handler loginResultOneHandler = new Handler()
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
	
	
	/**进入登录成功之后的界面*/
	protected void accessToView() {
		mSaveDialog.dismiss();
		Intent oneIntent = new Intent(MainActivity.this, BusinessActivity.class);
		MainActivity.this.startActivity(oneIntent);
		MainActivity.this.finish();
	}
	//按两次返回键退出
	 @Override
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
     if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
     	if ((System.currentTimeMillis() - exitTime) > 2000) {
             Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
             exitTime = System.currentTimeMillis();
         } else {
             MainActivity.this.finish();
             System.exit(0);
         }
         return true;
     }
     return super.onKeyDown(keyCode, event);
 }
	
}
