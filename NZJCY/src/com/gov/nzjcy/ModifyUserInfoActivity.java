package com.gov.nzjcy;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gov.nzjcy.exception.BaseException;
import com.gov.nzjcy.exception.ParseException;
import com.gov.nzjcy.helper.GOSHelper;
import com.gov.nzjcy.httpoperation.GosHttpApplication;

import android.app.AlertDialog;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class ModifyUserInfoActivity extends BaseActivity {
	/**打印标示*/
	private String	TAG = "ModifyUserInfoActivity";
	/** head 头部 的左侧菜单 按钮*/
	private LinearLayout	top_left_Layout;
	/** head 头部 的右侧菜单 按钮*/
	private ImageButton		top_right_headImageButton;
	/**head的标题*/
	private TextView		topTitleTextView;
	/**修改密码*/
	private EditText		modifyPasswordEditText;
	/**姓名*/
	private EditText		userInfoNameEditText;
	/**手机*/
	private EditText		userInfoPhoneEditText;
	/**固话*/
	private EditText		userInfoTelEditText;
	/**电子邮箱*/
	private EditText		userInfoEmailEditText;
	/**修改按钮*/
	private Button			modifyUserInfoButton;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modifyuserinfo);
		initView();
	}
	//初始化界面
	protected void initView() {
		top_left_Layout = (LinearLayout)findViewById(R.id.leftPopLinearLayout);//左边的返回按钮
		topTitleTextView = (TextView)findViewById(R.id.topTitleTextview);
		topTitleTextView.setText("修改资料");//标题
		top_right_headImageButton = (ImageButton)findViewById(R.id.loginImageButton);//用户登录按钮
		//左边返回按钮的点击事件
		top_left_Layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ModifyUserInfoActivity.this.finish();
			}
		});
		//隐藏右边的按钮
		top_right_headImageButton.setVisibility(View.INVISIBLE);
		modifyPasswordEditText = (EditText)findViewById(R.id.modifyPasswordEditText);
		userInfoNameEditText = (EditText)findViewById(R.id.userInfoNameEditText);
		userInfoPhoneEditText = (EditText)findViewById(R.id.userInfoPhoneEditText);
		userInfoTelEditText = (EditText)findViewById(R.id.userInfoTelEditText);
		userInfoEmailEditText = (EditText)findViewById(R.id.userInfoEmailEditText);
		modifyUserInfoButton = (Button)findViewById(R.id.modifyUserInfoButton);
		modifyUserInfoButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isEmpty();
			}
		});
		
	}
	//判断是否为空
	protected void isEmpty() {
		final String modifyPasswordString = modifyPasswordEditText.getText().toString().trim();
		final String userInfoNameString = userInfoNameEditText.getText().toString().trim();
		final String userInfoPhoneString = userInfoPhoneEditText.getText().toString().trim();
		final String userInfoTelString = userInfoTelEditText.getText().toString().trim();
		final String userInfoEmailString = userInfoEmailEditText.getText().toString().trim();
		if ((modifyPasswordString.equals("")) && (userInfoNameString.equals("")) && (userInfoPhoneString.equals(""))
				&&(userInfoTelString.equals(""))&&(userInfoEmailString.equals(""))) {
			Toast.makeText(ModifyUserInfoActivity.this, "请输入信息", Toast.LENGTH_SHORT).show();
		}
		else {
			new Thread()
			{
				public void run() {
					modifyPersonInfo(modifyPasswordString, userInfoNameString, userInfoPhoneString, userInfoTelString, userInfoEmailString);
				}
			}.start();
		}
		
	}
	/**修改个人资料*/
	protected void modifyPersonInfo(String passWordString, String userNameString, String telephoneString, String mobileString, String emailString) {
		try {
			String userIDString = GOSHelper.getSharePreStr(ModifyUserInfoActivity.this, GosHttpApplication.USER_ID_STRING);
			HttpResponse aString = gosHttpOperation.invokerSetUserInfoResponse(userIDString, passWordString, userNameString, telephoneString, mobileString, emailString);
			HttpEntity entity = aString.getEntity();
			InputStream is = entity.getContent();
			String reString = GOSHelper.convertStreamToString(is);
		
			if (reString == null || reString.equals("")) {
				
			}
			else {
				JSONObject jsonObject = JSON.parseObject(reString);
				JSONObject dataObject = jsonObject.getJSONObject("data");
				String stateString = dataObject.getString("state");
				if (stateString.equals("1")) {
					Log.v("测试依申请公开项", "测试依申请公开项成功");
					alertHandler.sendEmptyMessage(1);
				}
				else
				{
					Log.v("测试依申请公开项", "测试依申请公开项失败");
					alertHandler.sendEmptyMessage(0);
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
	//
	Handler alertHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 0:
				alertMsg("0");
				break;
			case 1:
				alertMsg("1");
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};
	public void alertMsg(String typeString) {
		if (typeString.equals("0")) {
			new AlertDialog.Builder(this).setTitle("温馨提示").
			setMessage("修改失败!").
			setPositiveButton("确定", null).show();
		}
		else {
			new AlertDialog.Builder(this).setTitle("温馨提示").
			setMessage("修改成功!").
			setPositiveButton("确定", null).show();
		}
	}
	
}
