package com.ssm.songshangmen.activity;

import cn.jpush.android.api.JPushInterface;

import com.ssm.songshangmen.helper.SSMHelper;
import com.ssm.songshangmen.httpoperation.GosHttpApplication;
import com.ssm.songshangmen.httpoperation.GosHttpOperation;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class BaseActivity extends FragmentActivity {
	protected SharedPreferences prefs;

	protected GosHttpApplication 	gosHttpApplication;
	protected GosHttpOperation 		gosHttpOperation;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        
        String gIDString = JPushInterface.getRegistrationID(this);
        SSMHelper.putSharePre(BaseActivity.this, GosHttpApplication.REGISTER_ID_STRING, gIDString);
        Log.v("123", "测试"+gIDString);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);

		gosHttpApplication = (GosHttpApplication) getApplication();

		gosHttpOperation = gosHttpApplication.getGosHttpOperation();
		
	}
}
