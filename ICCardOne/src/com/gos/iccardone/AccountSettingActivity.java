package com.gos.iccardone;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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
public class AccountSettingActivity extends BaseActivity {
	
	private TextView			accountInfoEditText;
	private Button				queryBalanceButton;
	private TextView			rebateTextView;
	private TextView			balanceTextView;
	private String				balanceString = "";
	private String				rebateString = "";
	private ProgressDialog 		mSaveDialog = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accountsetting);
		initView();
	}
	/***/
	protected void initView() {
		accountInfoEditText = (TextView)findViewById(R.id.accountInfoEditText);
		String cardIdString = GOSHelper.getSharePreStr(this, GosHttpApplication.USER_NAME_STRING);
		accountInfoEditText.setText(cardIdString);
//		accountInfoEditText.setFocusable(false);   
//		accountInfoEditText.setFocusableInTouchMode(false);   
//		accountInfoEditText.requestFocus();
		
		rebateTextView = (TextView)findViewById(R.id.rebateTextView);
		balanceTextView = (TextView)findViewById(R.id.balanceTextView);
		queryBalanceButton = (Button)findViewById(R.id.queryBalanceButton);
		queryBalanceButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isEmpty();
			}
		});
	}
	/**判断是否为空*/
	protected void isEmpty() {
		final String accountString = accountInfoEditText.getText().toString();
		if (accountString.equals("")) {
			Toast.makeText(this, "账号不能为空", Toast.LENGTH_SHORT).show();
		}
		else {
			mSaveDialog = ProgressDialog.show(AccountSettingActivity.this, "查询余额", "查询余额中，请稍等...", true);
			new Thread(){
				public void run() {
					queryBalanceHttp(accountString);
				}
			}.start();
		}
	}
	/**网络请求*/
	protected void queryBalanceHttp(String accountString) {
		try {
			String tokenString = GOSHelper.getSharePreStr(this, GosHttpApplication.USER_TOKEN_STRING);
			HttpResponse aHttpResponse = gosHttpOperation.invokerQueryBalanceResponse(accountString, tokenString);
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
					JSONArray balanceJsonArray = jsonObject.getJSONArray("data");
					for (int i = 0; i < balanceJsonArray.size(); i++) {
						JSONObject oneObject = balanceJsonArray.getJSONObject(i);
						balanceString = oneObject.getString("total");
						rebateString = oneObject.getString("profit");
						loginResultHandler.sendEmptyMessage(1);
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
		Toast.makeText(this, "查询余额失败", Toast.LENGTH_SHORT).show();
	}
	/**进入登录成功之后的界面*/
	protected void accessToView() {
		rebateTextView.setText(" "+rebateString);
		balanceTextView.setText(balanceString);
		mSaveDialog.dismiss();
		Toast.makeText(this, "查询余额成功", Toast.LENGTH_SHORT).show();
	}
	
}
