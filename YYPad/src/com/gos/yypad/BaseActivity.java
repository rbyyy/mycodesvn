package com.gos.yypad;

import java.util.UUID;

import com.gos.yypad.httpoperation.GosHttpApplication;
import com.gos.yypad.httpoperation.GosHttpOperation;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;



public abstract class BaseActivity extends FragmentActivity  {

	protected SharedPreferences prefs;

	protected GosHttpApplication gosHttpApplication;
	protected GosHttpOperation gosHttpOperation;

	private static final String IMAGE_CACHE_DIR = "images";
	
	protected 	ImageLoader imageLoader = ImageLoader.getInstance();
	protected   DisplayImageOptions options;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		prefs = PreferenceManager.getDefaultSharedPreferences(this);

		gosHttpApplication = (GosHttpApplication) getApplication();

		gosHttpOperation = gosHttpApplication.getGosHttpOperation();

		final DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.beforearrow).cacheInMemory(true).cacheOnDisc(true).build();  
        
	}
	
	protected String getId(){
		final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
		final String tmDeviceId;
		tmDeviceId = "" + tm.getDeviceId();
		UUID deviceUuid = new UUID(tmDeviceId.hashCode(), ((long)tmDeviceId.hashCode() << 32) | tmDeviceId.hashCode());
		String uniqueId = deviceUuid.toString().replace("-", "");
		return uniqueId;
	}
	
}
