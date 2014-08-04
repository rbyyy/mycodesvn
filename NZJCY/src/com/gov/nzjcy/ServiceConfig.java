package com.gov.nzjcy;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public class ServiceConfig {
    private static final String TAG = "Config";

    public static final String UPDATE_SERVER = "http://m.nzjcy.gov.cn:81/";
    public static final String UPDATE_APKNAME = "NZJCY.apk";
    public static final String UPDATE_VERJSON = "APPver.txt";
    public static final String UPDATE_SAVENAME = "NZJCY.apk";


    public static int getVerCode(Context context) {
        return Integer.parseInt(context.getString(R.string.app_versionCode));
        /*int verCode = -1;
        try {
            verCode = context.getPackageManager().getPackageInfo(
                    "cn.gov.nzcjy.app", 0).versionCode;
        } catch (NameNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }
        return verCode;*/
    }

    public static String getVerName(Context context) {
        return context.getString(R.string.app_versionName);
        /*String verName = "";
        try {
            verName = context.getPackageManager().getPackageInfo(
                    "cn.gov.nzcjy.app", 0).versionName;
        } catch (NameNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }
        return verName;*/

    }
    @SuppressWarnings("unused")
	private String getVersionName(Context context) throws Exception
    {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(),0);
        String version = packInfo.versionName;
        return version;
    }
    public static String getAppName(Context context) {
        String verName = context.getResources()
                .getText(R.string.app_name).toString();
        return verName;
    }
}