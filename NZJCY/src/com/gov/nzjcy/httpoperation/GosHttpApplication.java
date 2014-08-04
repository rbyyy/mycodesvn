package com.gov.nzjcy.httpoperation;

import com.gov.nzjcy.util.DebugConfig;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.gov.nzjcy.constants.Constants.Config;

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
	private String mVersion = null;
	public static final String PACKAGE_NAME = "com.gov.nzjcy";
	
//	public static final String SERVER_ADDRESS_STRING = "server_address_str";//服务器地址
//	public static final String SHOP_CODE_STRING = "shopcodeString";//店面code
//	public static final String FIRST_ACCESS_STRING = "firstaccessstring";//是否为第一次进入
	public static final String USER_NAME_STRING = "usernamestring";//用户名
	public static final String USER_PASSWORD_STRING = "userpasswordstring";//密码
	public static final String USER_ID_STRING = "useridstring";//用户id
	public static final String USER_TYPE_STRING = "usertypestring";//用户type
	public static final String USER_RDDB_ID_STRING = "userrddbidstring";//人大代表用户id
	
	@Override
    public void onCreate() {
		if (Config.DEVELOPER_MODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDialog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyDeath().build());
		}
		super.onCreate();
		mVersion = getVersionString(this);
		loadGosHttpOperation();
		initImageLoader(getApplicationContext());
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
    
    public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}
}
