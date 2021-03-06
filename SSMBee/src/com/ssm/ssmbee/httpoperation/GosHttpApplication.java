package com.ssm.ssmbee.httpoperation;

import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.ssm.ssmbee.util.DebugConfig;
import com.ssm.ssmbee.constants.Constants.Config;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;

@SuppressWarnings("unused")
public class GosHttpApplication extends Application {
	private static final String TAG = "GosHttpOperation";
    private static final boolean DEBUG = DebugConfig.DEBUG;
	private GosHttpOperation gosHttpOperation;
	private String mVersion = null;//版本号
	public static final String PACKAGE_NAME = "com.ssm.ssmbee";//应用包名
	public static final String FIRST_ACCESS_STRING = "firstaccessstring";//是否为第一次进入
	public static final String USER_NAME_STRING = "usernamestring";//用户名
	public static final String USER_PASSWORD_STRING = "userpasswordstring";//密码
	public static final String USER_LOGIN_FLAG_STRING = "userloginflagstring";//用户登录标志
	public static final String USER_ID_STRING = "useridstring";//用户id
	public static final	String REGISTER_ID_STRING = "registeridstring";//推送id
	
	
	@Override
    public void onCreate() {
		if (Config.DEVELOPER_MODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDialog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyDeath().build());
		}
		super.onCreate();
		mVersion = getVersionString(this);
		loadGosHttpOperation();
		//initImageLoader(getApplicationContext());
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

    public static GosHttpOperation createMacyAndLarry(Context context) {
        String version = getVersionString(context);
        if (DebugConfig.USE_DEBUG_SERVER) {
            return new GosHttpOperation(GosHttpOperation.createHttpApi("", version));
        } else {
            return new GosHttpOperation(GosHttpOperation.createHttpApi(version));
        }
    }
    /**得到版本号*/
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
    
//    public static void initImageLoader(Context context) {
//		// This configuration tuning is custom. You can tune every option, you may tune some of them,
//		// or you can create default configuration by
//		//  ImageLoaderConfiguration.createDefault(this);
//		// method.
//		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
//				.threadPriority(Thread.NORM_PRIORITY - 2)
//				.denyCacheImageMultipleSizesInMemory()
//				.discCacheFileNameGenerator(new Md5FileNameGenerator())
//				.tasksProcessingOrder(QueueProcessingType.LIFO)
//				.writeDebugLogs() // Remove for release app
//				.build();
//		// Initialize ImageLoader with configuration.
//		ImageLoader.getInstance().init(config);
//	}
}
