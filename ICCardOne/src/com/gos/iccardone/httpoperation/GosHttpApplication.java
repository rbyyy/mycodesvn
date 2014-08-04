package com.gos.iccardone.httpoperation;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;

import com.gos.iccardone.util.DebugConfig;

@SuppressWarnings("unused")
public class GosHttpApplication extends Application {
	private static final String TAG = "GosHttpOperation";
    private static final boolean DEBUG = DebugConfig.DEBUG;
	private GosHttpOperation gosHttpOperation;
	private String mVersion = null;
	public static final String PACKAGE_NAME = "com.gos.iccardone"; //这个记住一定要修改
	
//	public static final String SERVER_ADDRESS_STRING = "server_address_str";//服务器地址
	public static final String FIRST_ACCESS_STRING = "firstaccessstring";//是否为第一次进入
	public static final String USER_NAME_STRING = "usernamestring";//用户名
	public static final String USER_PASSWORD_STRING = "userpasswordstring";//密码
	public static final String USER_TOKEN_STRING = "usertokenstring";//用户token
	
	public static final String REMEMBER_PASSWORD_FLAG = "rememberpasswordflag";
//	public static final String USER_ID_STRING = "useridstring";//用户id
	
	@Override
    public void onCreate() {
		
		if (DebugConfig.DEVELOPER_MODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDialog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyDeath().build());
		}
		super.onCreate();
		mVersion = getVersionString(this);
		loadGosHttpOperation();
	}
	
    public GosHttpOperation getGosHttpOperation() {
		return gosHttpOperation;
	}
    
    private void loadGosHttpOperation() {
        if (!DebugConfig.USE_DEBUG_SERVER) {
        	gosHttpOperation = new GosHttpOperation(GosHttpOperation.createHttpApi("", mVersion));
        } else {
        	gosHttpOperation = new GosHttpOperation(GosHttpOperation.createHttpApi(mVersion));
        }
    }

//    public static GosHttpOperation createMacyAndLarry(Context context) {
//        String version = getVersionString(context);
//        if (DebugConfig.USE_DEBUG_SERVER) {
//            return new GosHttpOperation(GosHttpOperation.createHttpApi("", version));
//        } else {
//            return new GosHttpOperation(GosHttpOperation.createHttpApi(version));
//        }
//    }
    
    private static String getVersionString(Context context) {
        // Get a version string for the app.
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(PACKAGE_NAME, 0);
            return PACKAGE_NAME + ":" + String.valueOf(pi.versionCode);
        } catch (NameNotFoundException e) {
            if (DEBUG) Log.d(TAG, "Could not retrieve package info", e);
            throw new RuntimeException(e);
        }
    }
    
    /**得到版本名*/
    public static String getVersionName(Activity a) {
		String versionName = "";
		try {
			// ---get the package info---
			PackageManager pm = a.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(a.getPackageName(), 0);
			versionName = pi.versionName;
			if (versionName == null || versionName.length() <= 0) {
				return "";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return versionName;
	}
    
}
