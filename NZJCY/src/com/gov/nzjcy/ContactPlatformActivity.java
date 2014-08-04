package com.gov.nzjcy;

import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gov.nzjcy.exception.BaseException;
import com.gov.nzjcy.exception.ParseException;
import com.gov.nzjcy.helper.GOSHelper;
import com.gov.nzjcy.httpoperation.GosHttpApplication;
import com.gov.nzjcy.util.MD5Util;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class ContactPlatformActivity extends BaseActivity {
	
	/** head 头部 的左侧菜单 按钮*/
	private LinearLayout	top_left_Layout;
	/** head 头部 的右侧菜单 按钮*/
	private ImageButton		top_right_headImageButton;
	/**head的标题*/
	private TextView		topTitleTextView;
	/**用户名*/
	private EditText		userNamEditText;
	/**密码*/
	private EditText		passwordEditText;
	/**登录按钮*/
	private Button			loginUpButton;
	
	private ProgressDialog 							mSaveDialog = null;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contactplatform);
		String nodeString = "人大代表联络平台使用须知："
				+ "\n使用说明: "
				+ "\n1、欢迎各位人大代表、政协委员使用该联络平台，使用时请通过用户名及密码登陆本系统。"
				+ "\n2、请妥善保管好自己的用户名及密码，遗忘密码请与南召检察院办公室负责人联系。"
				+ "\n3、使用过程中如有疑问，请及时与南召检察院信息中心联系。";
		
		new AlertDialog.Builder(this).setTitle("使用须知").
		setMessage(nodeString).
		setPositiveButton("确定", null).show();
		
		initView();
	}
	//初始化界面
	protected void initView() {
		top_left_Layout = (LinearLayout)findViewById(R.id.leftPopLinearLayout);//左边的返回按钮
		topTitleTextView = (TextView)findViewById(R.id.topTitleTextview);
		topTitleTextView.setText("我");//标题
		top_right_headImageButton = (ImageButton)findViewById(R.id.loginImageButton);//用户登录按钮
		//左边返回按钮的点击事件
		top_left_Layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ContactPlatformActivity.this.finish();
			}
		});
		//隐藏右边的按钮
		top_right_headImageButton.setVisibility(View.INVISIBLE);
		//右边登录按钮的点击事件
//				top_right_headImageButton.setOnClickListener(new OnClickListener() {
//					
//					@Override
//					public void onClick(View v) {
//						// TODO Auto-generated method stub
//
//					}
//				});
		userNamEditText = (EditText)findViewById(R.id.contactLoginUpUserNameEditText);
		passwordEditText = (EditText)findViewById(R.id.contactLoginUpPasswordEditText);
		loginUpButton = (Button)findViewById(R.id.contactLoginUpButton);
		loginUpButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				decideInput();
			}
		});
	}
	/**判断输入*/
	protected void decideInput() {
		final String userNameString = userNamEditText.getText().toString().trim();
		final String passwordString = passwordEditText.getText().toString().trim();
		if ((userNameString.equals("")) || (userNameString == null)) {
			Toast.makeText(ContactPlatformActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
		}
		else if ((passwordString.equals("")) || (passwordString == null)) {
			Toast.makeText(ContactPlatformActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
		}
		else {
   		    mSaveDialog = ProgressDialog.show(ContactPlatformActivity.this, "登录","正在登录中，请稍等...", true); 
   		    mSaveDialog.setCanceledOnTouchOutside(false);
			new Thread()
			{
				public void run() {
					userLoginUp(userNameString, passwordString);
				}
			}.start();
		}
	}
	/**登录接口
     * @throws NoSuchAlgorithmException */
    protected void userLoginUp(String usernameString, String passwordString) {
    	try {
    		String loginupPasswordMd5String = MD5Util.MD5(passwordString).substring(8, 24);
			HttpResponse aString = gosHttpOperation.invokerUserLoginUpResponse(usernameString, loginupPasswordMd5String);
			HttpEntity entity = aString.getEntity();
			InputStream is = entity.getContent();
			String reString = GOSHelper.convertStreamToString(is);
		
			if (reString == null || reString.equals("")) {
				loginResultHandler.sendEmptyMessage(0);
			}
			else {
				JSONObject jsonObject = JSON.parseObject(reString);
				JSONObject dataObject = jsonObject.getJSONObject("data");
				String stateString = dataObject.getString("state");
				if (stateString.equals("1")) {
					JSONArray responeArray = dataObject.getJSONArray("responseList");
					String aString2 = responeArray.toJSONString();
					if (aString2 == null) {
						loginResultHandler.sendEmptyMessage(0);
					}
					if (responeArray.size() > 0) {
						for (int i = 0; i < responeArray.size(); i++) {
							JSONObject oneObject = responeArray.getJSONObject(i);
							String userIdString = oneObject.getString("id");
							String userTypeString = oneObject.getString("userType");
							if (userTypeString.equals("5")) {
								GOSHelper.putSharePre(ContactPlatformActivity.this, GosHttpApplication.USER_RDDB_ID_STRING, userIdString);
								loginResultHandler.sendEmptyMessage(1);
							}
							else {
								loginResultHandler.sendEmptyMessage(0);
							}
						}
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
    /**登录注册之后跳转到的页面*/
    @SuppressLint("HandlerLeak")
	Handler loginResultHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:
				showLoginResult(1);
				break;
			case 0:
				showLoginResult(0);
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};
	/**跳转界面*/
	protected void showLoginResult(int loginResultInt) {
		if (loginResultInt == 0) {
			Toast.makeText(ContactPlatformActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
			mSaveDialog.dismiss();
		}
		else if (loginResultInt == 1) {
			mSaveDialog.dismiss();
			Intent loginResultIntent = new Intent(ContactPlatformActivity.this, ContactPlatformOneActivity.class);
			ContactPlatformActivity.this.startActivity(loginResultIntent);
		}
	}
	
	
}
