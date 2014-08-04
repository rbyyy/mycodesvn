package com.law.fangyuan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AutoRun extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){
			Intent in = new Intent(Intent.ACTION_RUN);
			in.setClass(context, Check.class);
			context.startService(in);
		}
	}

}
