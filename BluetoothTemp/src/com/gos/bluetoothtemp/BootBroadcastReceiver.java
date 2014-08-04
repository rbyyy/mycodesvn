package com.gos.bluetoothtemp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

public class BootBroadcastReceiver extends BroadcastReceiver {
	
	//重写onReceive方法 
	public void onReceive(Context context, Intent intent) {
		
		 if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) 
		    { 
		      Intent newIntent = new Intent(context, BusiessBlueToothActivity.class);
		      newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  //注意，必须添加这个标记，否则启动会失败 
		      context.startActivity(newIntent);   
		      //注意：如果是开机就要看到程序就使用startActivity,如果想开机启动一个服务就用startService    
		    }       
	}
}
