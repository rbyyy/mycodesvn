package com.gov.nzjcy;

import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gov.nzjcy.httpoperation.GosHttpApplication;
import com.gov.nzjcy.exception.BaseException;
import com.gov.nzjcy.exception.ParseException;
import com.gov.nzjcy.helper.GOSHelper;
import com.gov.nzjcy.util.MD5Util;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;

public class UserLoginActivity extends BaseActivity {
	
	/**打印标示*/
	private String	TAG = "AskForOpenActivity";
	/** head 头部 的左侧菜单 按钮*/
	private LinearLayout	top_left_Layout;
	/** head 头部 的右侧菜单 按钮*/
	private ImageButton		top_right_headImageButton;
	/**head的标题*/
	private TextView		topTitleTextView;
	/**tabhost的定义*/
	private TabHost 		tabHost;
	/**登陆项*/
	private LinearLayout	topUserLoginUpLinearLayout;
	/**注册项*/
	private LinearLayout	topUserLoginInLinearLayout;
	/**用户名*/
	private TextView		loginUpUserNameTextView;
	/**密码*/
	private TextView		loginUpUserPasswordTextView;
	/**登录按钮*/
	private Button			loginupButton;
	/**注册时用户名*/
	private EditText		loginInUserNameEditText;
	/**注册时密码*/
	private EditText		loginInPasswordOneEditText;
	/**注册时确认密码*/
	private EditText		loginInPasswordTwoEditText;
	/**注册按钮*/
	private Button			loginInButton;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_userlogin);
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
				UserLoginActivity.this.finish();
			}
		});
		//隐藏右边的按钮
		top_right_headImageButton.setVisibility(View.INVISIBLE);
		//右边登录按钮的点击事件
//			top_right_headImageButton.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//
//				}
//			});

		initTabHost();
		topUserLoginUpLinearLayout = (LinearLayout)findViewById(R.id.topUserLoginUpLinearLayout);
		topUserLoginInLinearLayout = (LinearLayout)findViewById(R.id.topUserLoginInLinearLayout);
		
		loginUpUserNameTextView = (TextView)findViewById(R.id.userLoginUpUserNameEditText);
		loginUpUserPasswordTextView = (TextView)findViewById(R.id.userLoginUpPasswordEditText);
		loginupButton = (Button)findViewById(R.id.userLoginUpButton);
		loginupButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String oneFlagString = "0";
				String twoFlagString = "0";
				final String loginUpUserNameString = loginUpUserNameTextView.getText().toString();
				final String loginUpPasswordString = loginUpUserPasswordTextView.getText().toString();
				
				if ((loginUpUserNameString != null) && !loginUpUserNameString.equals("")) {
					oneFlagString = "1";
				}
				else {
					Toast.makeText(UserLoginActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
				}
				if ((loginUpPasswordString.toString() != null) && !loginUpPasswordString.equals("")) {
					twoFlagString = "1";
				}
				else {
					Toast.makeText(UserLoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
				}
				if (oneFlagString.equals("1") && twoFlagString.equals("1")) {
					new Thread()
					{
						public void run() {
							userLoginUp(loginUpUserNameString, loginUpPasswordString);
						}
					}.start();
				}
				
			}
		});
		
		loginInUserNameEditText = (EditText)findViewById(R.id.userLoginInUserNameEditText);
		loginInPasswordOneEditText = (EditText)findViewById(R.id.userLoginInPasswordEditText);
		loginInPasswordTwoEditText = (EditText)findViewById(R.id.userLoginInConfirmPasswordEditText);
		loginInButton = (Button)findViewById(R.id.userLoginInButton);
		loginInButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String oneFlagString = "0";
				String twoFlagString = "0";
				String threeFlagString = "0";
				final String loginInUserNameString = loginInUserNameEditText.getText().toString();
				final String loginInPasswordOneString = loginInPasswordOneEditText.getText().toString();
				final String loginInPasswordTwoString = loginInPasswordTwoEditText.getText().toString();
				if ((loginInUserNameString != null) && !loginInUserNameString.equals("")) {
					oneFlagString = "1";
				}
				else {
					Toast.makeText(UserLoginActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
				}
				if ((loginInPasswordOneString.toString() != null) && !loginInPasswordOneString.equals("")) {
					twoFlagString = "1";
				}
				else {
					Toast.makeText(UserLoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
				}
				if (twoFlagString.equals("1") && loginInPasswordTwoString.toString() != null && !loginInPasswordTwoString.equals("")) {
					if (loginInPasswordOneString.equals(loginInPasswordTwoString)) {
						threeFlagString = "1";
					}
					else {
						Toast.makeText(UserLoginActivity.this, "两次输入的密码不相同", Toast.LENGTH_SHORT).show();
					}
				}
				else {
					Toast.makeText(UserLoginActivity.this, "两次输入的密码不相同", Toast.LENGTH_SHORT).show();
				}
				if (oneFlagString.equals("1") && twoFlagString.equals("1") && threeFlagString.equals("1")) {
					new Thread()
					{
						public void run() {
							userLoginIn(loginInUserNameString, loginInPasswordTwoString);
						}
					}.start();
				}
			}
		});
		
	}
	//初始化tabhost
	protected void initTabHost() {
		tabHost = (TabHost)findViewById(R.id.topUserLoginTabHost);  
        tabHost.setup();  
        TabWidget tabWidget = tabHost.getTabWidget();  
        
        tabHost.addTab(tabHost.newTabSpec("tab1")  
                .setIndicator("登陆", getResources().getDrawable(R.drawable.ic_launcher))  
                .setContent(R.id.userLoginView1));   
          
        tabHost.addTab(tabHost.newTabSpec("tab2")  
                .setIndicator("注册")  
                .setContent(R.id.userLoginView2));  
          
        final int tabs = tabWidget.getChildCount();  
        Log.v(TAG, "***tabWidget.getChildCount() : " + tabs);  
        
        //注意这个就是改变Tabhost默认样式的地方，一定将这部分代码放在上面这段代码的下面，不然样式改变不了
        updateTab(tabHost);
//	        for (int i =0; i < tabWidget.getChildCount(); i++) {  
//	         //修改Tabhost高度和宽度
//	         tabWidget.getChildAt(i).getLayoutParams().height = 60;  
//	         tabWidget.getChildAt(i).getLayoutParams().width = 65;
//	         //修改显示字体大小
//	         TextView tv = (TextView) tabWidget.getChildAt(i).findViewById(android.R.id.title);
//	         tv.setTextSize(15);
//	         //tv.setTextColor(this.getResources().getColorStateList(android.R.color.white));
//	        }
        
        tabHost.setOnTabChangedListener(new OnTabChangedListener()); // 选择监听器
	}
	/**tab变化时的监听*/
	class OnTabChangedListener implements OnTabChangeListener {

		  @Override
		  public void onTabChanged(String tabId) {
			   tabHost.setCurrentTabByTag(tabId);
			   System.out.println("tabid " + tabId);
			   System.out.println("curreny after: " + tabHost.getCurrentTabTag());
			   Log.v(TAG, "tabid " + tabId);
			   Log.v(TAG, "curreny after: " + tabHost.getCurrentTabTag());
			   updateTab(tabHost);
			   switch (tabHost.getCurrentTab()) {
			   	 case 0:
			   		 	topUserLoginUpLinearLayout.setVisibility(View.VISIBLE);
			   		 	topUserLoginInLinearLayout.setVisibility(View.INVISIBLE);
			   		 break;
			   	 case 1:
			   		 	topUserLoginUpLinearLayout.setVisibility(View.INVISIBLE);
			   		 	topUserLoginInLinearLayout.setVisibility(View.VISIBLE);
			   		 break;
			   	 default:
			   		 break;
			   }
		  }
	}
	/**
     * 更新Tab标签的颜色，和字体的颜色
     * @param tabHost
     */
    private void updateTab(final TabHost tabHost) {
        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            View view = tabHost.getTabWidget().getChildAt(i);
            //修改Tabhost高度和宽度
            view.getLayoutParams().height = 60;  
            view.getLayoutParams().width = 65;
            
            TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextSize(15);
            tv.setTypeface(Typeface.SERIF, 2); // 设置字体和风格
            if (tabHost.getCurrentTab() == i) {//选中
                //view.setBackgroundDrawable(getResources().getDrawable(R.drawable.category_current));//选中后的背景 
            	int defColor = Color.rgb(66, 139, 202);
            	tv.setTextColor(defColor);
            } else {//不选中
                //view.setBackgroundDrawable(getResources().getDrawable(R.drawable.category_bg));//非选择的背景
                tv.setTextColor(this.getResources().getColorStateList(android.R.color.black));
            }
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
							GOSHelper.putSharePre(UserLoginActivity.this, GosHttpApplication.USER_NAME_STRING, usernameString);
							GOSHelper.putSharePre(UserLoginActivity.this, GosHttpApplication.USER_PASSWORD_STRING, passwordString);
							GOSHelper.putSharePre(UserLoginActivity.this, GosHttpApplication.USER_ID_STRING, userIdString);
							GOSHelper.putSharePre(UserLoginActivity.this, GosHttpApplication.USER_TYPE_STRING, userTypeString);
							loginResultHandler.sendEmptyMessage(1);
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
		} catch (BaseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    /**注册接口*/
    protected void userLoginIn(String usernameString, String passwordString) {
    	try {
    		String loginupPasswordMd5String = MD5Util.MD5(passwordString).substring(8, 24);
			HttpResponse aString = gosHttpOperation.invokerUserRegisteredResponse(usernameString, loginupPasswordMd5String);
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
					
					if (responeArray.size() > 0) {
						for (int i = 0; i < responeArray.size(); i++) {
							JSONObject oneObject = responeArray.getJSONObject(i);
							String userIDString = oneObject.getString("userID").trim();
							GOSHelper.putSharePre(UserLoginActivity.this, GosHttpApplication.USER_NAME_STRING, usernameString);
							GOSHelper.putSharePre(UserLoginActivity.this, GosHttpApplication.USER_PASSWORD_STRING, passwordString);
							GOSHelper.putSharePre(UserLoginActivity.this, GosHttpApplication.USER_ID_STRING, userIDString);
							loginResultHandler.sendEmptyMessage(1);
						}
					}
					
				}
				else {
					loginResultHandler.sendEmptyMessage(0);
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (BaseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
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
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};
	/**跳转界面*/
	protected void showLoginResult(int loginResultInt) {
		if (loginResultInt == 0) {
			Toast.makeText(UserLoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
		}
		else if (loginResultInt == 1) {
			Intent loginResultIntent = new Intent(UserLoginActivity.this, UserLoginResultActivity.class);
			UserLoginActivity.this.startActivity(loginResultIntent);
		}
	}
	
}
