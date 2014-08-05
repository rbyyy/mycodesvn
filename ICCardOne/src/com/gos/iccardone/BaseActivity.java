package com.gos.iccardone;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;

import com.gos.iccardone.httpoperation.GosHttpApplication;
import com.gos.iccardone.httpoperation.GosHttpOperation;

public class BaseActivity extends ActionBarActivity {
	protected SharedPreferences prefs;

	protected GosHttpApplication 	gosHttpApplication;
	protected GosHttpOperation 		gosHttpOperation;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		prefs = PreferenceManager.getDefaultSharedPreferences(this);

		gosHttpApplication = (GosHttpApplication) getApplication();

		gosHttpOperation = gosHttpApplication.getGosHttpOperation();
		
	}
}

