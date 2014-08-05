package com.gos.bluetoothtemp;

import com.alibaba.fastjson.JSONObject;
import com.gos.bluetoothtemp.data.TempAlarm;
import com.gos.bluetoothtemp.data.TempAlarmDao;

import cn.jpush.android.api.JPushInterface;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class JPushReciverServer extends BroadcastReceiver {
	
	private	TempAlarmDao			tempAlarmDao;
	
	@SuppressLint("SimpleDateFormat")
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
		Bundle bundle = intent.getExtras();
        Log.d("tag", "onReceive - " + intent.getAction());
         
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
             
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            System.out.println("收到了自定义消息。消息内容是：" + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            String title = bundle.getString(JPushInterface.EXTRA_TITLE);
            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);

            // 自定义消息不会展示在通知栏，完全要开发者写代码去处理
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            System.out.println("收到了通知");
            tempAlarmDao = new TempAlarmDao(context);
            TempAlarm tempAlarm = new TempAlarm();
            //String dateString = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
            String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
            String content = bundle.getString(JPushInterface.EXTRA_ALERT);
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            JSONObject jsonObject = JSONObject.parseObject(extras);
    		String dateString = jsonObject.getString("dateStamp");
    		String msgTypeString = jsonObject.getString("msgTypeID");
    		String msgIDString = jsonObject.getString("msgID");
    		tempAlarm.setTemp_msg_id(msgIDString);
            tempAlarm.setTemp_tag("");
            tempAlarm.setTemp_date(dateString);
            tempAlarm.setTemp_content(content);
            tempAlarm.setTemp_type(msgTypeString);
            
            tempAlarmDao.insert("temp", tempAlarm);
            Log.v("tag", extras);
//            Intent oneIntent = new Intent();
//            oneIntent.setClass(context, BusiessBlueToothActivity.class);
//            oneIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            context.startActivity(oneIntent);
            context.sendBroadcast(new Intent("android.intent.action.MAIN"));
            // 在这里可以做些统计，或者做些其他工作
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            System.out.println("用户点击打开了通知");
            // 在这里可以自己写代码去定义用户点击后的行为
            Intent i = new Intent(context, TempAlarmClassifyActivity.class);  //自定义打开的界面
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
   
        } else {
            Log.d("test", "Unhandled intent - " + intent.getAction());
        }

	}

}
