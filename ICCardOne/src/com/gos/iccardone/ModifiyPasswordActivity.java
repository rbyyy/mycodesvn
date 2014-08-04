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

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class ModifiyPasswordActivity extends BaseActivity {
	
	private TextView			accountEditText;
	private EditText			oldPwdEditText;
	private EditText			newPwdEditText;
	private EditText			confirmNewPwdEditText;
	private ProgressDialog 		mSaveDialog = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modifypassword);
		initView();
	}
	/***/
	protected void initView() {
		accountEditText = (TextView)findViewById(R.id.userAccountEditText);
		String cardIdString = GOSHelper.getSharePreStr(this, GosHttpApplication.USER_NAME_STRING);
		accountEditText.setText(cardIdString);
//		accountEditText.setFocusable(false);   
//		accountEditText.setFocusableInTouchMode(false);   
//		accountEditText.requestFocus();
		
		oldPwdEditText = (EditText)findViewById(R.id.oldPwdEditText);
		newPwdEditText = (EditText)findViewById(R.id.newPwdEditText);
		confirmNewPwdEditText = (EditText)findViewById(R.id.confirmNewPwdEditText);
		Button confirmButton = (Button)findViewById(R.id.modifyPwdButton);
		confirmButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isEmpty();
			}
		});
	}
	/**判断是否为空*/
	protected void isEmpty() {
		final String accountString = accountEditText.getText().toString();
		final String oldPwdString = oldPwdEditText.getText().toString();
		final String newPwdString = newPwdEditText.getText().toString();
		final String confirmNewPwdString = confirmNewPwdEditText.getText().toString();
		
		if (accountString.equals("")) {
			Toast.makeText(this, "账号不能为空", Toast.LENGTH_SHORT).show();
		}
		else if (oldPwdString.equals("")) {
			Toast.makeText(this, "旧密码不能为空", Toast.LENGTH_SHORT).show();
		}
		else if (newPwdString.equals("")) {
			Toast.makeText(this, "新密码不能为空", Toast.LENGTH_SHORT).show();
		}
		else if (confirmNewPwdString.equals("")) {
			Toast.makeText(this, "确认新密码不能为空", Toast.LENGTH_SHORT).show();
		}
		else if (!confirmNewPwdString.equals(newPwdString)) {
			Toast.makeText(this, "新密码和确认新密码不相同", Toast.LENGTH_SHORT).show();
		}
		else {
			mSaveDialog = ProgressDialog.show(ModifiyPasswordActivity.this, "修改密码", "修改密码中，请稍等...", true); 
			new Thread(){
				public void run() {
					modifyPasswordHttp(accountString, oldPwdString, confirmNewPwdString);
				}
			}.start();
		}
		
	}
	/**网络请求*/
	protected void modifyPasswordHttp(String accountString, String oldPwdString,
			 String confirmNewPwdString) {
		try {
			String tokenString = GOSHelper.getSharePreStr(this, GosHttpApplication.USER_TOKEN_STRING);
			HttpResponse aHttpResponse = gosHttpOperation.invokerModifyPasswordRespone(accountString, confirmNewPwdString, oldPwdString, tokenString);
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
				accessToView();
				break;
			default:
				break;
			}
		}
	};
	/**登录失败的显示*/
	protected void loginFailed() {
		mSaveDialog.dismiss();
		Toast.makeText(this, "修改密码失败", Toast.LENGTH_SHORT).show();
	}
	/**进入登录成功之后的界面*/
	protected void accessToView() {
		accountEditText.setText("");
		oldPwdEditText.setText("");
		newPwdEditText.setText("");
		confirmNewPwdEditText.setText("");
		mSaveDialog.dismiss();
		Toast.makeText(this, "修改密码成功", Toast.LENGTH_SHORT).show();
	}
}
