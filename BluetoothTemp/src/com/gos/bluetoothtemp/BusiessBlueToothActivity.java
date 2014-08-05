package com.gos.bluetoothtemp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import u.aly.S;
import cn.jpush.android.api.JPushInterface;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.gos.bluetoothtemp.MainActivity.AcceptThread;
import com.gos.bluetoothtemp.helper.GOSHelper;
import com.gos.bluetoothtemp.http.Http;
import com.gos.ui.VerticalProgressBar;
import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


@SuppressLint({ "DefaultLocale", "SimpleDateFormat" })
public class BusiessBlueToothActivity  extends ActionBarActivity{	
	
	static final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
	private UUID uuid ;
	private BluetoothAdapter btAdapt;
	public static BluetoothSocket socket = null;
	public static BluetoothSocket btSocket;
	public static AcceptThread serverThread;
	private ProgressDialog 	mProgressDialog = null;
	
	public static boolean isRecording = false;// 线程控制标记
	
	private Button btBack, btRefresh, btSend;//releaseCtrl
	
	private OutputStream outStream = null;
	
	private TextView _txtRead, _tempShowTextView, bigTextView, smallTextView;//,_txtSend
	
	private ConnectedThread manageThread;
	
	private Handler mHandler;
	
	private String blueToothDeviceString = "";
	
	private ProgressDialog dialog = null;
	private ProgressDialog dialogTwo = null;
	/**当前选择的类型*/
	private int 			currentSelectItem = 0;
	/**大仓名称集合*/
	private String[] 		bigStrings = new String[]{};
	private ArrayList<Object> bigArrayList = new ArrayList<Object>();
	/**点击*/
	private RadioOnClick OnClick = new RadioOnClick(0);
	/**小仓名称集合*/
	private String[] 		smallStrings = new String[]{};
	private ArrayList<Object> smallArrayList = new ArrayList<Object>();
	/**大小仓id组合*/
	private String[]		bigSmallStrings = new String[]{};
	private ArrayList<Object> bigSmallArrayList = new ArrayList<Object>();
	/**点击*/
	private RadioOnClick OneOnClick;
	
	public LocationClient mLocationClient = null;  
	public BDLocationListener myListener = new MyLocationListener(); 
	
	private LocationMode tempMode = LocationMode.Battery_Saving;
	private String tempcoor="gcj02";
	/**GIS*/
	private String GISString = "";
	/**温度信息*/
	private String bluetoothTempOneString = "";
	/**设备唯一编号*/
	private String gIDString = "";
	/***/
	private String canbinIdString = "";
	/**大仓选择号*/
	private int	   bigSelectNumber = 0;
	/**小仓选择号*/
	private int	   smallSelectNumber = 0;
	/**温度类型*/
	private TextView	tempTypeTextView;
	/**拌合站*/
	private TextView	mixTempTypeTextView;
	/**温度类型集合*/
	private	ArrayList<HashMap<String, Object>>	tempTypeArrayList = new ArrayList<HashMap<String, Object>>();
	/**全部拌合站类型集合*/
	private ArrayList<Object> allMixArrayList = new ArrayList<Object>();
	/**拌合站集合*/
	private ArrayList<HashMap<String, Object>>  mixTempTypeArrayList = new ArrayList<HashMap<String,Object>>();
	/**温度类型数组*/
	private String[] 	tempTypeStrings = new String[]{};
	/**温度类型选择*/
	private int			tempTypeSelect = 0;
	/**拌合站*/
	private	String[]	mixTempStrings = new String[]{};
	/**拌合站选择*/
	private int			mixTempSelect = 0;
	/**报警按钮*/
	private	TextView	baojingTextView;
	/**报警颜色标志*/
	private ImageView	baojingImageView;
	/**标志*/
	private boolean 	flag = false;
	/**退出时间*/
	private long 		exitTime=0;
	/***/
	private LinearLayout	mixTempLinearLayout;
	
	
	private BroadcastReceiver mDataChangedReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			flag = true;
			yujingImageChange();
			
			if (flag) {
				baojingImageView.setImageResource(R.drawable.image_red);
			}
		}
	};
	
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_businessbluetooth);
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		
		SharedPreferences serverAddressInfo = getSharedPreferences("serveraddress_info", 0);  
		tempTypeSelect = serverAddressInfo.getInt("tempType", 0); 
		 
		
		//setTitle("返回前需先关闭socket连接");
		registerReceiver(mDataChangedReceiver, new IntentFilter("android.intent.action.MAIN"));
		//获取蓝牙的地址
		blueToothDeviceString = getIntent().getStringExtra("deviceMac");
		
		findMyView();
		setMyViewListener(); 
		
		gIDString = JPushInterface.getRegistrationID(this);
		
		
		if (blueToothDeviceString != null) {
			btSocket = MainActivity.btSocket;
			deviceConnected();
		}
		else {
			btAdapt = BluetoothAdapter.getDefaultAdapter();// 初始化本机蓝牙功能
			uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
			if(btAdapt == null){
				return;
			}
			else{
				if (btAdapt.getState() == BluetoothAdapter.STATE_OFF)// 读取蓝牙状态并显示
					{
						btAdapt.enable();
						try {
							Thread.sleep(5 * 1000);//延时5s
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} //暂停1秒钟
					}
				else if (btAdapt.getState() == BluetoothAdapter.STATE_ON){
				
				}
			}
			
			// 注册Receiver来获取蓝牙设备相关的结果
			IntentFilter intent = new IntentFilter();
			intent.addAction(BluetoothDevice.ACTION_FOUND);// 用BroadcastReceiver来取得搜索结果
			intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
			intent.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
			intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
			registerReceiver(searchDevicesOne, intent);
			btAdapt.startDiscovery();
		}
		
//		
//		new getTempClassifyInfo().execute();//请求温度类型
//		new getStorageBin().execute();//请求大小仓数据

	}
	/**设备连接*/
	protected void deviceConnected() {
		//接收线程启动
		manageThread=new ConnectedThread();
		mHandler  = new MyHandler();
		manageThread.Start();
		
		sendMessage("685555A0A0000116");
		setTitle("发送指令成功");
		
		requestLoc();//请求地理坐标
		SharedPreferences serverAddressInfo = getSharedPreferences("serveraddress_info", 0);  
		String serverAddressString = serverAddressInfo.getString("serveraddress", "");  
		if (!serverAddressString.equals("")) {
			new getStorageBin().execute();//请求大小仓数据
			new getTempClassifyInfo().execute();//请求温度类型
		}
	}
	/**搜索设备*/
	private BroadcastReceiver searchDevicesOne = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			Bundle b = intent.getExtras();
			Object[] lstName = b.keySet().toArray();
			
			// 显示所有收到的消息及其细节
			for (int i = 0; i < lstName.length; i++) {
				String keyName = lstName[i].toString();
				Log.e(keyName, String.valueOf(b.get(keyName)));
			}
			//搜索设备时，取得设备的MAC地址
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				//String str= device.getName() + "|" + device.getAddress();
				String macString = device.getAddress();
				SharedPreferences serverAddressInfo = getSharedPreferences("serveraddress_info", 0);  
				 final String bluetoothIdString = serverAddressInfo.getString("bluetoothid", "");
				if (macString.equals(bluetoothIdString)) {
					blueToothDeviceString = bluetoothIdString;
					_txtRead.setText(blueToothDeviceString);//mac地址
					mProgressDialog = ProgressDialog.show(BusiessBlueToothActivity.this, "蓝牙连接", "连接蓝牙中，请稍等...");
					new Thread(){
						public void run() {
							connectBlueToothDevice(bluetoothIdString);//连接蓝牙
						}
					}.start();
					
				}
			}
		}
	};
	
	protected void connectBlueToothDevice(String address) {
		//服务端监听
//		serverThread=new AcceptThread();
//		serverThread.start();
		Log.e("address",address);
		
	    uuid = UUID.fromString(SPP_UUID);
		Log.e("uuid",uuid.toString());
		
		BluetoothDevice btDev = btAdapt.getRemoteDevice(address);//"00:11:00:18:05:45"

		Method m;
		try {
			m = btDev.getClass().getMethod("createRfcommSocket", new Class[] {int.class});
			btSocket = (BluetoothSocket) m.invoke(btDev, Integer.valueOf(1));
		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchMethodException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		//取消发现	
		btAdapt.cancelDiscovery();
		try {
			//btSocket = btDev.createRfcommSocketToServiceRecord(uuid);				
			btSocket.connect();				
			Log.e("one", " BT connection established, data transfer link open.");
			msgHandler.sendEmptyMessage(0);//隐藏进度条
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e("one", " Connection failed.", e);	
			//Toast.makeText(getApplicationContext(), "连接失败", Toast.LENGTH_SHORT);
			setTitle("连接失败..");
		}
	}
	/**
	 * 隐藏progressbar
	 * */
	Handler msgHandler = new Handler(){
		@Override
  	  public void handleMessage(Message msg) {
  	   // TODO Auto-generated method stub
			mProgressDialog.dismiss();
			deviceConnected();
			super.handleMessage(msg);
  	  }
	};

	private void findMyView() {	
		
		//releaseCtrl=(Button)findViewById(R.id.button1);
		bigTextView = (TextView) findViewById(R.id.bigTextView);
		bigTextView.setOnClickListener(new RadioClickListener());
		smallTextView = (TextView) findViewById(R.id.smallTextView);
		smallTextView.setOnClickListener(new RadioClickListener());
		tempTypeTextView = (TextView) findViewById(R.id.tempTypeTextView);
		tempTypeTextView.setOnClickListener(new RadioClickListener());
		mixTempTypeTextView = (TextView) findViewById(R.id.mixTempTextView);
		mixTempTypeTextView.setOnClickListener(new RadioClickListener());
		
		mixTempLinearLayout = (LinearLayout)findViewById(R.id.mixTempLinearLayout);
		mixTempLinearLayout.setVisibility(View.INVISIBLE);
		
		baojingTextView = (TextView)findViewById(R.id.baojingTextView);
		baojingImageView = (ImageView)findViewById(R.id.warningImageView);
		
		btBack = (Button) findViewById(R.id.button2);
		btRefresh = (Button)findViewById(R.id.btRefresh);
		btSend = (Button) findViewById(R.id.btSend);		
       
		_txtRead = (TextView) findViewById(R.id.etShow);
		_txtRead.setText(blueToothDeviceString);//mac地址
		_tempShowTextView = (TextView)findViewById(R.id.tempShowTextView);//温度显示
		//_txtSend = (EditText) findViewById(R.id.etSend);
		//_txtSend.setText("685555A0A0000116");
	}
	/**
	 * 预警颜色变换
	 * */
	protected void yujingImageChange() {
		if (flag) {
			SharedPreferences serverAddressInfo = getSharedPreferences("serveraddress_info", 0);  
			serverAddressInfo.edit().putInt("temp", 1).commit();
//			baojingImageView.setBackgroundResource(R.drawable.image_red);
		}
		else {
			baojingImageView.setBackgroundResource(R.drawable.image_green);
		}
	}
	
	/**弹出框*/
	class RadioClickListener implements OnClickListener {
		   @Override
		   public void onClick(View v) {
			   switch (v.getId()) {
					case R.id.bigTextView:
						currentSelectItem = 0;
						OneOnClick = new RadioOnClick(bigSelectNumber);
						AlertDialog ad =new AlertDialog.Builder(BusiessBlueToothActivity.this).setTitle("")
					    .setSingleChoiceItems(bigStrings, OnClick.getIndex(),new RadioOnClick(bigSelectNumber)).create();
					    ad.show();
						break;
					case R.id.smallTextView:
						currentSelectItem = 1;
						OneOnClick = new RadioOnClick(smallSelectNumber);
						AlertDialog adOne =new AlertDialog.Builder(BusiessBlueToothActivity.this).setTitle("")
					    .setSingleChoiceItems(smallStrings, OneOnClick.getIndex(),new RadioOnClick(smallSelectNumber)).create();
					    adOne.show();
						break;
					case R.id.tempTypeTextView:
						currentSelectItem = 2;
						OneOnClick = new RadioOnClick(tempTypeSelect);
						AlertDialog adTwo =new AlertDialog.Builder(BusiessBlueToothActivity.this).setTitle("")
					    .setSingleChoiceItems(tempTypeStrings, OneOnClick.getIndex(),new RadioOnClick(tempTypeSelect)).create();
						adTwo.show();
						break;
					case R.id.mixTempTextView:
						currentSelectItem = 3;
						OneOnClick = new RadioOnClick(mixTempSelect);
						AlertDialog adThree =new AlertDialog.Builder(BusiessBlueToothActivity.this).setTitle("")
					    .setSingleChoiceItems(mixTempStrings, OneOnClick.getIndex(),new RadioOnClick(mixTempSelect)).create();
						adThree.show();
						break;
					default:
						break;
			   }
		   }
	}
	/**弹出框点击事件*/
	class RadioOnClick implements DialogInterface.OnClickListener{
	   private int index = 0;
		
	   public RadioOnClick(int index){
		   //this.index = index;
		   if(currentSelectItem == 0)
			{
				bigSelectNumber = index;
			}
			else if (currentSelectItem == 1) {
				smallSelectNumber = index;
			}
			else if (currentSelectItem == 2) {
				tempTypeSelect = index;
			} 
			else {
				mixTempSelect = index;
			}
	   }
	   public void setIndex(int index){
		   
		   //this.index=index;
		   if(currentSelectItem == 0)
			{
				bigSelectNumber = index;
			}
			else if (currentSelectItem == 1) {
				smallSelectNumber = index;
			}
			else if (currentSelectItem == 2) {
				tempTypeSelect = index;
			} 
			else {
				mixTempSelect = index;
			}
	   }
	   public int getIndex(){
		   if(currentSelectItem == 0)
			{
			    index = bigSelectNumber;
			}
			else if (currentSelectItem == 1) {
				index = smallSelectNumber;
			}
			else if (currentSelectItem == 2) {
				tempTypeSelect = index;
			} 
			else {
				mixTempSelect = index;
			}
		   return index;
	   }
	 
	   @SuppressWarnings("unchecked")
	public void onClick(DialogInterface dialog, int whichButton){
		   setIndex(whichButton);
			switch (currentSelectItem) {
			   	case 0:
			   		bigTextView.setText(bigStrings[bigSelectNumber]);	
			   		@SuppressWarnings("unchecked")
					ArrayList<HashMap<String, Object>> smallOneArrayList = (ArrayList<HashMap<String, Object>>) smallArrayList.get(bigSelectNumber);
	    			ArrayList<Object> tmpOneArrayList = new ArrayList<Object>();
	    			ArrayList<Object> tmpTwoArrayList = new ArrayList<Object>();
	    			for (int i = 0; i < smallOneArrayList.size(); i++) {
	    				tmpOneArrayList.add(smallOneArrayList.get(i).get("smallBin"));
	    				tmpTwoArrayList.add(smallOneArrayList.get(i).get("canbinID"));
					}
	    			
	    			smallStrings = new String[tmpOneArrayList.size()];
	    			smallStrings = tmpOneArrayList.toArray(smallStrings);
	    			
	    			bigSmallStrings = new String[tmpTwoArrayList.size()];
	    			bigSmallStrings = tmpTwoArrayList.toArray(bigSmallStrings);
	    			
	    			smallTextView.setText(smallStrings[0]);
			   		break;
			   	case 1:
			   		smallTextView.setText(smallStrings[smallSelectNumber]);
			   		break;
			   	case 2:
			   		SharedPreferences serverAddressInfo = getSharedPreferences("serveraddress_info", 0);  
					serverAddressInfo.edit().putInt("tempType", tempTypeSelect).commit();
			   		tempTypeTextView.setText(tempTypeStrings[tempTypeSelect]);
			   		
			   		mixTempTypeArrayList = (ArrayList<HashMap<String, Object>>) allMixArrayList.get(tempTypeSelect);
	    			ArrayList<Object> tmpOneArrayList1 = new ArrayList<Object>();
	    			String oneString = "";
	    			for (int i = 0; i < mixTempTypeArrayList.size(); i++) {
	    				oneString += mixTempTypeArrayList.get(i).get("subTempType");
	    				tmpOneArrayList1.add(mixTempTypeArrayList.get(i).get("subTempType"));
					}
	    			if (oneString.equals("")) {
	    				mixTempLinearLayout.setVisibility(View.INVISIBLE);
					}
	    			else {
	    				mixTempLinearLayout.setVisibility(View.VISIBLE);
	    				mixTempStrings = new String[tmpOneArrayList1.size()];
		    			mixTempStrings = tmpOneArrayList1.toArray(mixTempStrings);
				   		
				   		mixTempTypeTextView.setText(mixTempStrings[0]);
					}
	    			
			   		break;
			   	case 3:
			   		mixTempTypeTextView.setText(mixTempStrings[mixTempSelect]);
			   		break;
			    default:
				    break;
			}
		   dialog.dismiss();
		   
	   }
	 }
	

	private void setMyViewListener() {

		//releaseCtrl.setOnClickListener(new ClickEvent());
		baojingTextView.setOnClickListener(new ClickEvent());
		btBack.setOnClickListener(new ClickEvent());
		btRefresh.setOnClickListener(new ClickEvent());
		btSend.setOnClickListener(new ClickEvent());
		
	}




	@Override
	public void onDestroy()  
    {  
		if (mDataChangedReceiver != null) {
			unregisterReceiver(mDataChangedReceiver);
		}
		
		try {
			if (blueToothDeviceString != null) {
				btSocket.close();//MainActivity.
				//android.os.Process.killProcess(android.os.Process.myPid());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
        if (searchDevicesOne != null) {
         	 //unregisterReceiver(searchDevicesOne);//注释掉
  		}
        
        super.onDestroy();  

    }
	
	 
	public void onResume() {
		super.onResume();
		SharedPreferences serverAddressInfo = getSharedPreferences("serveraddress_info", 0);  
		int bjInfo = serverAddressInfo.getInt("temp", 0); 
		 if (bjInfo == 1) {
			 baojingImageView.setImageResource(R.drawable.image_red);
		 }
		 else {
			 baojingImageView.setImageResource(R.drawable.image_green);
		}
		
		MobclickAgent.onResume(this);
	}
		
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
		
	private	class ClickEvent implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			
//			if (v == releaseCtrl)// 释放连接
//			{
//				try {
//					MainActivity.btSocket.close();
//					manageThread.Stop();
//					//testBlueTooth.serverThread.cancel();					
//					//Toast.makeText(getApplicationContext(), "socket连接已关闭", Toast.LENGTH_SHORT);
//					setTitle("socket连接已关闭");
//				} catch (IOException e) {
//					//Log .e(TAG,"ON RESUME: Unable to close socket during connection failure", e2);
//					//Toast.makeText(getApplicationContext(), "关闭连接失败", Toast.LENGTH_SHORT);
//					setTitle("关闭连接失败");
//				}
//				
//			} 
//			else 
		   if (v == btBack) {// 返回
			   if (blueToothDeviceString != null) {
				   try {
						btSocket.close();//MainActivity.
						manageThread.Stop();
						//testBlueTooth.serverThread.cancel();					
						//Toast.makeText(getApplicationContext(), "socket连接已关闭", Toast.LENGTH_SHORT);
						setTitle("socket连接已关闭");
						Intent oneIntent = new Intent();
					    oneIntent.setClass(BusiessBlueToothActivity.this, MainActivity.class);
					    BusiessBlueToothActivity.this.startActivity(oneIntent);
					    BusiessBlueToothActivity.this.finish();
					} catch (IOException e) {
						//Log .e(TAG,"ON RESUME: Unable to close socket during connection failure", e2);
						//Toast.makeText(getApplicationContext(), "关闭连接失败", Toast.LENGTH_SHORT);
						setTitle("关闭连接失败");
					}
			   }
			   else {
				    Intent oneIntent = new Intent();
				    oneIntent.setClass(BusiessBlueToothActivity.this, MainActivity.class);
				    BusiessBlueToothActivity.this.startActivity(oneIntent);
				    BusiessBlueToothActivity.this.finish();
			   }   
			} 
			else if (v == btRefresh){
				if (blueToothDeviceString == null) {
					Toast.makeText(BusiessBlueToothActivity.this, "没有连接蓝牙设备,请先连接蓝牙设备", Toast.LENGTH_LONG).show();
				}
				else {
					sendMessage("685555A0A0000116");
					setTitle("发送指令成功");
					requestLoc();
				}
				
			}
			else if (v == btSend) {
				if (blueToothDeviceString == null) {
					Toast.makeText(BusiessBlueToothActivity.this, "没有连接蓝牙设备,请先连接蓝牙设备", Toast.LENGTH_LONG).show();
				}
				else {
					new getUploadInfo().execute();
				}
			}
			else if (v == baojingTextView) {
				Intent oneIntent = new Intent(BusiessBlueToothActivity.this, TempAlarmClassifyActivity.class);
				BusiessBlueToothActivity.this.startActivity(oneIntent);
			}
		}
	}

	
   public static void setEditTextEnable(TextView view,Boolean able){
       // view.setTextColor(R.color.read_only_color);   //设置只读时的文字颜色
        if (view instanceof android.widget.EditText){
            view.setCursorVisible(able);      //设置输入框中的光标不可见
            view.setFocusable(able);           //无焦点
            view.setFocusableInTouchMode(able);     //触摸时也得不到焦点
        }
   }
	/**
	 * 发送指令
	 * */
	public void sendMessage(String message) {
		
		//控制模块
		try {
			outStream = btSocket.getOutputStream();//MainActivity.
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//Log.e(TAG, "ON RESUME: Output stream creation failed.", e);
			Toast.makeText(getApplicationContext(), " Output stream creation failed.", Toast.LENGTH_SHORT).show();
			
		}
		
		byte[] msgBuffer = null;		
		msgBuffer = GOSHelper.hexStringToBytes(message);		

		try {
			outStream.write(msgBuffer);				
			//Toast.makeText(getApplicationContext(), "发送数据中..", Toast.LENGTH_SHORT);
			//setTitle("成功发送指令:"+message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//Log.e(TAG, "ON RESUME: Exception during write.", e);
			Toast.makeText(getApplicationContext(), "发送数据失败", Toast.LENGTH_SHORT).show();
			
		}
		
	}

	/**
	 * 管理蓝牙连接类
	 * */
	class ConnectedThread extends Thread {
	
			private InputStream inStream = null;// 蓝牙数据输入流
			private long wait;
			private Thread thread;
					
			public ConnectedThread() {
				isRecording = false;
				this.wait=50;
				thread =new Thread(new ReadRunnable());
			}
	
			public void Stop() {
				isRecording = false;			
				//thread.stop();
				//State bb = thread.getState();
				}
			
			public void Start() {
				isRecording = true;
				State aa = thread.getState();
				if(aa==State.NEW){
				thread.start();
				}else thread.resume();
			}
			
			private class ReadRunnable implements Runnable {
				private ArrayList<String> outputCodeArrayList = new ArrayList<String>();
			public void run() {
				while (isRecording) {
					
					try {					
						inStream = btSocket.getInputStream();//MainActivity.						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						//Log.e(TAG, "ON RESUME: Output stream creation failed.", e);
						Toast.makeText(getApplicationContext(), " input stream creation failed.", Toast.LENGTH_SHORT).show();
						
					}						
					//char[]dd= new  char[40]; 		                      
					int length=20;
					byte[] temp = new byte[length];
					//String readStr="";
					//keep listening to InputStream while connected
					if (inStream!= null) {
					try{
						int len = inStream.read(temp,0,length-1);	
						Log.e("available", String.valueOf(len));
						//setTitle("available"+len);
						if (len > 0) {
							byte[] btBuf = new byte[len];
							System.arraycopy(temp, 0, btBuf, 0, btBuf.length);	
							byte b = btBuf[0];
							int bint = Integer.parseInt(String.valueOf(b));
							String a = Integer.toHexString(bint);
							if (a.equals("68")) {//开始接收
								outputCodeArrayList.removeAll(outputCodeArrayList);
							}
							else {//回传的数据集
								String mString = GOSHelper.bytesToHexString(btBuf);
								outputCodeArrayList.add("68"+mString);
								mHandler.obtainMessage(01,len,-1,outputCodeArrayList).sendToTarget();
							} 			            
						}			             
			             Thread.sleep(wait);// 延时一定时间缓冲数据
						}catch (Exception e) {
							// TODO Auto-generated catch block
							mHandler.sendEmptyMessage(00);
						}				
					}
				}
			}
		}	
	}
	/**
	 * 处理回传过来的结果
	 * */
	 @SuppressLint("HandlerLeak")
	private class MyHandler extends Handler{ 
    	@SuppressLint("HandlerLeak")
		@Override		    
        public void dispatchMessage(Message msg) { 
    		switch(msg.what){
    		case 00:
    			isRecording=false;
//    			_txtRead.setText("");
//    			_txtRead.setHint("socket连接已关闭");
    			//_txtRead.setText("inStream establishment Failed!");
    			break;
    			
    		case 01:
    			@SuppressWarnings("unchecked")
				ArrayList<String> info = (ArrayList<String>) msg.obj;
    			strConvertToTemp(info.get(0));
    			break;    			

            default:	            
                break;
    		}
		}
	} 
	 /**
	  * 温度换算
	  * */
	 protected void strConvertToTemp(String tempHexString) {
		 String[] tempStrings = tempHexString.split("68");

		for (int i = 1; i < tempStrings.length; i++) {
			String flagString = tempStrings[i];
			if (flagString.substring(0, 4).equals("3333")) {
				String tempFlagString = "";
				if (flagString.subSequence(4, 6).equals("01")) {
					tempFlagString = "";
				}
				else {
					tempFlagString = "-";
				}
				String tempString = flagString.substring(6, 10);
				Long tempInt = Long.parseLong(tempString, 16);
				float tempFloat = (float)tempInt;
				String tempOneString = String.valueOf(tempFloat/100);
				String tempTwoString = tempFlagString + tempOneString;

				final VerticalProgressBar progressHorizontal = (VerticalProgressBar) findViewById(R.id.progress_bar);
		         
		         progressHorizontal.setProgress((int) (tempInt/100));
		         
		         
		         _tempShowTextView.setText("");
		         bluetoothTempOneString = tempTwoString;
		         _tempShowTextView.append(tempTwoString+"℃");
			}
			else if (flagString.substring(0, 4).equals("5555")) {
				
			} 
		}
	}

	/**读取仓位数据*/
	class getStorageBin extends AsyncTask<Object, Object, Object>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			loading.run();
		}
		
		@Override
		protected Object doInBackground(Object... arg0) {
			// TODO Auto-generated method stub
			Http http = new Http(BusiessBlueToothActivity.this);
			JSONObject paramJsonObject2 = new JSONObject();
	    	paramJsonObject2.put("comStr", "1003");//命令    	
	    	JSONObject paramOne = new JSONObject();
	    	paramOne.put("data", paramJsonObject2);
	    	
	    	String query = URLEncodedUtils.format(Http.stripNulls(new BasicNameValuePair("data", paramOne.toString())), HTTP.UTF_8);
	    	SharedPreferences serverAddressInfo = getSharedPreferences("serveraddress_info", 0);  
			String serverAddressString = serverAddressInfo.getString("serveraddress", "");
//			String serverAddressString = "116.255.209.108:8011";
			if (!serverAddressString.equals("")) {
				try{
					String httpString = "http://"+serverAddressString+"/getInterface.aspx?" + query;
			    	return http.GET(httpString);
				}
				catch(Exception exception)
				{
					Log.v("tag", "tag");
				}
				
			}
	    	return "";
		}

		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(dialog!=null){
				dialog.dismiss();
			}
			String resultString = result.toString();
			if ((!result.equals(""))&&(!resultString.startsWith("<html>"))) {
				parseStorageBinData(resultString);
			}
			else {
				Toast.makeText(BusiessBlueToothActivity.this, "服务器地址不正确", Toast.LENGTH_SHORT).show();
			}
			
		}
		
	}
	

	
	Runnable loading = new Runnable(){

		@SuppressWarnings("deprecation")
		@Override
		public void run() {
			// TODO Auto-generated method stub
	        dialog = new ProgressDialog(BusiessBlueToothActivity.this);
	        dialog.setMessage("获取仓位数据中，请稍等...");
	        dialog.setIndeterminate(true);
	        dialog.setCanceledOnTouchOutside(false);
//	        dialog.setButton("取消", new DialogInterface.OnClickListener() {
//				
//				@Override
//				public void onClick(DialogInterface arg0, int arg1) {
//					// TODO Auto-generated method stub
//					dialog.cancel();
//				}
//			});
	        //dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
	        dialog.show();
		}
    	
    };
    /**
     * 上报信息提示
     * */
    Runnable loadingOne = new Runnable(){

		@SuppressWarnings("deprecation")
		@Override
		public void run() {
			// TODO Auto-generated method stub
	        dialog = new ProgressDialog(BusiessBlueToothActivity.this);
	        dialog.setMessage("上报数据中，请稍等...");
	        dialog.setIndeterminate(true);
	        dialog.setCanceledOnTouchOutside(false);
//	        dialog.setButton("取消", new DialogInterface.OnClickListener() {
//				
//				@Override
//				public void onClick(DialogInterface arg0, int arg1) {
//					// TODO Auto-generated method stub
//					dialog.cancel();
//				}
//			});
	        //dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
	        dialog.show();
		}
    	
    };
    /**
     * 对仓位数据进行解析
     * */
    protected void parseStorageBinData(String resultString) {
    	if (resultString.equals("")) {
    		Dialog alertDialog = new AlertDialog.Builder(this). 
                    setTitle("提示"). 
                    setMessage("获取数据失败"). 
                    setIcon(R.drawable.ic_launcher). 
                    create(); 
            alertDialog.show(); 
		}
    	else {
    		JSONObject jsonObject = JSONObject.parseObject(resultString);
    		JSONObject dataObject = jsonObject.getJSONObject("data");
    		String stateString = dataObject.getString("state");
    		if (stateString.equals("1")) {
    			JSONArray infoArray = dataObject.getJSONArray("info");
    			for (int i = 0; i < infoArray.size(); i++) {
    				JSONObject oneObject = infoArray.getJSONObject(i);
    				//bigSmallArrayList.add(oneObject.getString("canbinID"));
    				bigArrayList.add(oneObject.getString("largeBin"));
    				JSONArray infoTwoArray = oneObject.getJSONArray("info");
    				ArrayList<HashMap<String, Object>> oneArrayList = new ArrayList<HashMap<String,Object>>();
    				for (int j = 0; j < infoTwoArray.size(); j++) {
    					JSONObject twoObject = infoTwoArray.getJSONObject(j);
	
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put("canbinID", twoObject.get("canbinID"));
						map.put("smallBin", twoObject.get("smallBin"));
						oneArrayList.add(map);
					}
    				if (oneArrayList.size()>0) {
    					smallArrayList.add(oneArrayList);
					}
    			}
    			bigStrings = new String[bigArrayList.size()];
    			bigStrings =  bigArrayList.toArray(bigStrings);
    			@SuppressWarnings("unchecked")
				ArrayList<HashMap<String, Object>> smallOneArrayList = (ArrayList<HashMap<String, Object>>) smallArrayList.get(0);
    			ArrayList<Object> tmpOneArrayList = new ArrayList<Object>();
    			ArrayList<Object> tmpTwoArrayList = new ArrayList<Object>();
    			for (int i = 0; i < smallOneArrayList.size(); i++) {
    				tmpOneArrayList.add(smallOneArrayList.get(i).get("smallBin"));
    				tmpTwoArrayList.add(smallOneArrayList.get(i).get("canbinID"));
				}
    			
    			smallStrings = new String[tmpOneArrayList.size()];
    			smallStrings = tmpOneArrayList.toArray(smallStrings);
    			
    			bigSmallStrings = new String[tmpTwoArrayList.size()];
    			bigSmallStrings = tmpTwoArrayList.toArray(bigSmallStrings);
    			
    			bigTextView.setText(bigStrings[0]);
    			smallTextView.setText(smallStrings[0]);
    		}
    		else {
    			Dialog alertDialog = new AlertDialog.Builder(this). 
                        setTitle("提示"). 
                        setMessage("获取数据失败"). 
                        setIcon(R.drawable.ic_launcher). 
                        create(); 
                alertDialog.show();
    		}
		}
	}
    
    /**
     * 获取温度类型信息提示
     * */
    Runnable loadingTwo = new Runnable(){

		@SuppressWarnings("deprecation")
		@Override
		public void run() {
			// TODO Auto-generated method stub
	        dialogTwo = new ProgressDialog(BusiessBlueToothActivity.this);
	        dialogTwo.setMessage("获取温度类型数据中，请稍等...");
	        dialogTwo.setIndeterminate(true);
	        dialogTwo.setCanceledOnTouchOutside(false);
//	        dialog.setButton("取消", new DialogInterface.OnClickListener() {
//				
//				@Override
//				public void onClick(DialogInterface arg0, int arg1) {
//					// TODO Auto-generated method stub
//					dialog.cancel();
//				}
//			});
	        //dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
	        dialogTwo.show();
		}
    	
    };
    
    /**获取温度类型信息*/
    class getTempClassifyInfo extends AsyncTask<Object, Object, Object>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			loadingTwo.run();
		}
		
		@Override
		protected Object doInBackground(Object... arg0) {
			// TODO Auto-generated method stub
			Http http = new Http(BusiessBlueToothActivity.this);
			JSONObject paramJsonObject2 = new JSONObject();
	    	paramJsonObject2.put("comStr", "1004");//命令
	    	JSONObject paramOne = new JSONObject();
	    	paramOne.put("data", paramJsonObject2);
	    	
	    	String query = URLEncodedUtils.format(Http.stripNulls(new BasicNameValuePair("data", paramOne.toString())), HTTP.UTF_8);
	    	SharedPreferences serverAddressInfo = getSharedPreferences("serveraddress_info", 0);  
			String serverAddressString = serverAddressInfo.getString("serveraddress", "");
//	    	String serverAddressString = "116.255.209.108:8011";
			if (!serverAddressString.equals("")) {
				try {
					String httpString = "http://"+ serverAddressString + "/getInterface.aspx?" + query;
			    	return http.GET(httpString);
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}
//			String httpString = "http://116.255.209.108:8011/getInterface.aspx?" + query;
//	    	return http.GET(httpString);
			
	    	return "";
		}

		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(dialog!=null){
				dialogTwo.dismiss();
			}
			
			String resultString = result.toString();
			if ((!result.equals(""))&&(!resultString.startsWith("<html>"))) {
				parseTempClassifyData(resultString);
			}
			else {
				Toast.makeText(BusiessBlueToothActivity.this, "服务器地址不正确", Toast.LENGTH_SHORT).show();
			}
			
		}	
	} 
    /**温度类型数据解析*/
    @SuppressWarnings("unchecked")
	protected void parseTempClassifyData(String resultString) {
    	if (resultString.equals("")) {
    		Dialog alertDialog = new AlertDialog.Builder(this). 
                    setTitle("提示"). 
                    setMessage("获取数据失败"). 
                    setIcon(R.drawable.ic_launcher). 
                    create(); 
            alertDialog.show(); 
		}
    	else {
    		JSONObject jsonObject = JSONObject.parseObject(resultString);
    		JSONObject dataObject = jsonObject.getJSONObject("data");
    		String stateString = dataObject.getString("state");
    		if (stateString.equals("1")) {
    			JSONArray infoArray = dataObject.getJSONArray("info");
    			for (int i = 0; i < infoArray.size(); i++) {
    				JSONObject oneObject = infoArray.getJSONObject(i);
    				//bigSmallArrayList.add(oneObject.getString("canbinID"));
    				HashMap<String, Object> maps = new HashMap<String, Object>();
    				maps.put("tempTypeID", oneObject.get("tempTypeID"));
    				maps.put("tempType", oneObject.get("tempType"));
    				tempTypeArrayList.add(maps);
    				
    				JSONArray infoTwoArray = oneObject.getJSONArray("info");   				
    				ArrayList<HashMap<String, Object>> oneArrayList = new ArrayList<HashMap<String,Object>>();
    				for (int j = 0; j < infoTwoArray.size(); j++) {
    					JSONObject twoObject = infoTwoArray.getJSONObject(j);
	
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put("subTempTypeID", twoObject.get("subTempTypeID"));
						map.put("subTempType", twoObject.get("subTempType"));
						oneArrayList.add(map);
					}
    				if (oneArrayList.size() > 0) {
						allMixArrayList.add(oneArrayList);
					}

    			}
    			tempTypeStrings = new String[tempTypeArrayList.size()];
    			ArrayList<Object> tempArrayList = new ArrayList<Object>();
    			for (int i = 0; i < tempTypeArrayList.size(); i++) {
					tempArrayList.add(tempTypeArrayList.get(i).get("tempType"));
				}
    			tempTypeStrings =  tempArrayList.toArray(tempTypeStrings);
    			
    			mixTempTypeArrayList = (ArrayList<HashMap<String,Object>>)allMixArrayList.get(0);
    			mixTempStrings = new String[mixTempTypeArrayList.size()];
    			ArrayList<Object> tmpOneArrayList = new ArrayList<Object>();
    			String oneString = "";
    			for (int i = 0; i < mixTempTypeArrayList.size(); i++) {
    				oneString += "";
    				tmpOneArrayList.add(mixTempTypeArrayList.get(i).get("subTempType"));
				}
    			if (oneString.equals("")) {
    				mixTempLinearLayout.setVisibility(View.INVISIBLE);
				}
    			else {
    				mixTempLinearLayout.setVisibility(View.VISIBLE);
    				mixTempStrings = tmpOneArrayList.toArray(mixTempStrings);
    				mixTempTypeTextView.setText(mixTempStrings[0]);
				}
    			
    			tempTypeTextView.setText(tempTypeStrings[0]);
    			
    		}
    		else {
    			Dialog alertDialog = new AlertDialog.Builder(this). 
                        setTitle("提示"). 
                        setMessage("获取数据失败"). 
                        setIcon(R.drawable.ic_launcher). 
                        create(); 
                alertDialog.show();
    		}
		}
	}
    
    /**上报数据*/
    class getUploadInfo extends AsyncTask<Object, Object, Object>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			loadingOne.run();
		}
		
		@Override
		protected Object doInBackground(Object... arg0) {
			// TODO Auto-generated method stub
			Http http = new Http(BusiessBlueToothActivity.this);
			JSONObject paramJsonObject2 = new JSONObject();
	    	paramJsonObject2.put("comStr", "1002");//命令
	    	
	    	if (bigSmallStrings.length > 0) {
	    		canbinIdString = bigSmallStrings[smallSelectNumber];
			}
	    	paramJsonObject2.put("canbinID", canbinIdString);//命令  
	    	paramJsonObject2.put("largeBin", bigTextView.getText().toString().trim());//大仓信息 
	    	paramJsonObject2.put("smallBin", smallTextView.getText().toString().trim());//小仓信息 
	    	  
	    	String tempTypeIdString = tempTypeArrayList.get(tempTypeSelect).get("tempTypeID").toString();
	    	String mixTempTypeString = mixTempTypeArrayList.get(mixTempSelect).get("subTempTypeID").toString();
	    	String tempResult = "";
	    	if (mixTempTypeString.equals("")) {
				tempResult = tempTypeIdString;
			}
	    	else {
				tempResult = tempTypeIdString+","+mixTempTypeString;
			}
	    	paramJsonObject2.put("temperatureType", tempResult);//温度类型 
	    	paramJsonObject2.put("temperature", bluetoothTempOneString);//温度数值
	    	String dateString = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
	    	paramJsonObject2.put("dateStamp", dateString);//日期
	    	paramJsonObject2.put("GIS", GISString);//地理信息
	    	paramJsonObject2.put("UUID", gIDString);//手机唯一标识信息
	    	
	    	JSONObject paramOne = new JSONObject();
	    	paramOne.put("data", paramJsonObject2);
	    	
	    	String query = URLEncodedUtils.format(Http.stripNulls(new BasicNameValuePair("data", paramOne.toString())), HTTP.UTF_8);
	    	SharedPreferences serverAddressInfo = getSharedPreferences("serveraddress_info", 0);  
			String serverAddressString = serverAddressInfo.getString("serveraddress", "");  
			if (!serverAddressString.equals("")) {
				try {
					String httpString = "http://"+serverAddressString + "/getInterface.aspx?" + query;
			    	return http.GET(httpString);
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}
	    	return "";
		}

		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(dialog!=null){
				dialog.dismiss();
			}
			
			String resultString = result.toString();
			if ((!result.equals(""))&&(!resultString.startsWith("<html>"))) {
				uploadDataParsse(resultString);
			}
			else {
				Toast.makeText(BusiessBlueToothActivity.this, "服务器地址不正确", Toast.LENGTH_SHORT).show();
			}
			
		}
		
	}
    /**
     * 对上报的结果进行分析
     * */
    protected void uploadDataParsse(String resultString) {
    	if (resultString.equals("")) {
    		Dialog alertDialog = new AlertDialog.Builder(this). 
                    setTitle("提示"). 
                    setMessage("上报数据失败"). 
                    setIcon(R.drawable.ic_launcher). 
                    create(); 
            alertDialog.show(); 
		}
    	else {
    		JSONObject jsonObject = JSONObject.parseObject(resultString);
    		JSONObject dataObject = jsonObject.getJSONObject("data");
    		String stateString = dataObject.getString("state");
    		if (stateString.equals("1")) {
    			Dialog alertDialog = new AlertDialog.Builder(this). 
                        setTitle("提示"). 
                        setMessage("上报数据成功"). 
                        setIcon(R.drawable.ic_launcher). 
                        create(); 
                alertDialog.show();
    		}
    		else {
    			Dialog alertDialog = new AlertDialog.Builder(this). 
                        setTitle("提示"). 
                        setMessage("上报数据失败"). 
                        setIcon(R.drawable.ic_launcher). 
                        create(); 
                alertDialog.show();
    		}
		}
	}
    
    /**
     * 获取gis信息
     * */
    protected void requestLoc() {
    	mLocationClient = ((LocationApplication)getApplication()).mLocationClient;
		
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Battery_Saving);//设置定位模式
		option.setCoorType("gcj02");//返回的定位结果是百度经纬度,默认值gcj02
		//option.setScanSpan(5000);//设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true);//返回的定位结果包含地址信息
		option.setNeedDeviceDirect(true);//返回的定位结果包含手机机头的方向
		mLocationClient.setLocOption(option);
		
		mLocationClient.registerLocationListener( myListener );  //注册监听函数  
		
		mLocationClient.start();
		
		//mLocationClient.requestLocation();
	}
    /**地理位置监听*/
	public class MyLocationListener implements BDLocationListener {  
		@Override  
		public void onReceiveLocation(BDLocation location) {  
		    if (location == null)  
		        return ;  
		    StringBuffer sb = new StringBuffer(256);  
		    sb.append("time : ");  
		    sb.append(location.getTime());  
		    sb.append("\nerror code : ");  
		    sb.append(location.getLocType());  
		    sb.append("\nlatitude : ");  
		    sb.append(location.getLatitude());  
		    sb.append("\nlontitude : ");  
		    sb.append(location.getLongitude());  
		    sb.append("\nradius : ");  
		    sb.append(location.getRadius());  
		    if (location.getLocType() == BDLocation.TypeGpsLocation){  
		        sb.append("\nspeed : ");  
		        sb.append(location.getSpeed());  
		        sb.append("\nsatellite : ");  
		        sb.append(location.getSatelliteNumber());  
		    } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){  
		        sb.append("\naddr : ");  
		        sb.append(location.getAddrStr());  
		    }   
		    Log.v("infosb", sb.toString());
		    GISString = location.getLatitude() + "," + location.getLongitude();
		    //logMsg(sb.toString());  
		    }  
	}
	
	private void InitLocation(){
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(tempMode);//设置定位模式
		option.setCoorType(tempcoor);//返回的定位结果是百度经纬度，默认值gcj02
//		int span=1000;
		try {
			//span = Integer.valueOf(frequence.getText().toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
		//option.setScanSpan(5000);//设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);
	}
    
	//按两次返回键退出
	 @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
        	if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                BusiessBlueToothActivity.this.finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

	
}