package com.law.fangyuan;

import java.io.File;

import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Download1 {
	
	Context context;
	DownloadManager manager;
	String DOWNLOAD_FOLDER_NAME = "fangyuan";
    String DOWNLOAD_FILE_NAME   = "fangyuan.apk";

	@SuppressWarnings({ "static-access", "deprecation" })
	public Download1(Context context,String url){
		this.context = context;		
		manager =(DownloadManager)context.getSystemService(context.DOWNLOAD_SERVICE);
		context.registerReceiver(new DownloadCompleteReceiver(), new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
		//创建下载请求
		DownloadManager.Request down = new DownloadManager.Request(Uri.parse(url));
		//设置允许使用的网络类型，这里是移动网络和wifi都可以
		down.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE|DownloadManager.Request.NETWORK_WIFI);
		//禁止发出通知，既后台下载
		down.setShowRunningNotification(false);
		//设置下载后文件存放的位置
		down.setDestinationInExternalPublicDir(DOWNLOAD_FOLDER_NAME, DOWNLOAD_FILE_NAME);
		down.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		down.setTitle(context.getString(R.string.app_name));
		down.setVisibleInDownloadsUi(false);
		manager.enqueue(down);
	}
	
	private void install(Context context, String filePath){
		File file = new File(filePath);
		if (file != null && file.length() > 0 && file.exists() && file.isFile()) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setDataAndType(Uri.parse("file://"+file.toString()), "application/vnd.android.package-archive");
			context.startActivity(intent);
		}
	}
	
    //接受下载完成后的intent  
    class DownloadCompleteReceiver extends BroadcastReceiver {  
        @Override  
        public void onReceive(Context context, Intent intent) {  
            if(intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)){  
                long downId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                Query query = new Query();
                query.setFilterById(downId);
                Cursor cur = manager.query(query);
                if(cur.moveToFirst()){
                	int columnIndex = cur.getColumnIndex(DownloadManager.COLUMN_STATUS);
                	if (DownloadManager.STATUS_SUCCESSFUL == cur.getInt(columnIndex)) {
                        String apkFilePath = new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath()).append(File.separator)
                                .append(DOWNLOAD_FOLDER_NAME)
                                .append(File.separator)
                                .append(DOWNLOAD_FILE_NAME)
                                .toString();
                        install(context,apkFilePath);
                	}
                }
            }
        }
    }

}
