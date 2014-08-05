package com.ssm.ssmbee;

import cn.jpush.android.api.JPushInterface;

import com.ssm.ssmbee.helper.SSMHelper;
import com.ssm.ssmbee.httpoperation.GosHttpApplication;
import com.ssm.ssmbee.httpoperation.GosHttpOperation;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

public class BaseActivity extends ActionBarActivity {
	protected SharedPreferences 	prefs;
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
