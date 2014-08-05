package com.gos.yypad;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

public class BootBroadcastReceiver extends BroadcastReceiver {
	
	private PackageManager pm; 
	//重写onReceive方法 
	public void onReceive(Context context, Intent intent) {
		//后边的XXX.class就是要启动的服务 
////		Intent service = new Intent(context,XXXclass); 
////		context.startService(service); 
//		 Log.v("TAG", "开机时应用自动启动.....");  
//		//启动应用，参数为需要自动启动的应用的包名
//		 pm = context.getPackageManager(); 
//		 Intent intent1 = pm.getLaunchIntentForPackage("com.gos.yypad");
//		 context.startActivity(intent1 );
////		 Intent i = new Intent(context, Splash.class);     
////	     i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
////	     context.startActivity(i); 
		
		 if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) 
		    { 
		      Intent newIntent = new Intent(context, LauncherActivity.class);
		      newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  //注意，必须添加这个标记，否则启动会失败 
		      context.startActivity(newIntent);   
		      //注意：如果是开机就要看到程序就使用startActivity,如果想开机启动一个服务就用startService    
		    }       
	}
}
