package com.gos.bluetoothtemp;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.prefs.Preferences;

import cn.jpush.android.api.JPushInterface;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

@SuppressLint("HandlerLeak")
public class MainActivity extends ActionBarActivity {
	static final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
	private UUID uuid ;
	private static final String TAG = "BluetoothTest";	
	private static final boolean STATE_CONNECTED = true;
	
	private Button btnSearch, btnDis, btnConfirm, btnExit;
	private ToggleButton tbtnSwitch;
	
	private ListView lvBTDevices;
	private MyBlueToothDeviceAdapter adtDevices;
	private ArrayList<HashMap<String, Object>> lstDevices = new ArrayList<HashMap<String,Object>>();	
	private BluetoothAdapter btAdapt;
	
	public static BluetoothSocket socket = null;
	public static BluetoothSocket btSocket;
	public static AcceptThread serverThread;
	
	private ProgressDialog 	mProgressDialog = null;
	
	private String blueToothDeviceAddress = "";
	
	//public static boolean isForeground = false;
	
	private EditText severAddEditText;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setActionBar();//设置actionbar
		String gIDString = JPushInterface.getRegistrationID(this);
		Log.v("gid", gIDString);
		((TextView)findViewById(R.id.TextView001)).setText(gIDString);
		// Button 设置
		btnSearch = (Button) this.findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(new ClickEvent());
		
		btnConfirm = (Button)this.findViewById(R.id.btnConfirm);
		btnConfirm.setOnClickListener(new ClickEvent());
		
		btnExit = (Button) this.findViewById(R.id.btnExit);
		btnExit.setOnClickListener(new ClickEvent());
		
		severAddEditText = (EditText)findViewById(R.id.severAddEditText);
		 SharedPreferences serverAddressInfo = getSharedPreferences("serveraddress_info", 0);  
		 String serverAddressString = serverAddressInfo.getString("serveraddress", "");  
		 severAddEditText.setText(serverAddressString);

		
//		btnDis = (Button) this.findViewById(R.id.btnDis);
//		btnDis.setOnClickListener(new ClickEvent());

		// ToogleButton设置
		tbtnSwitch = (ToggleButton) this.findViewById(R.id.tbtnSwitch);
		tbtnSwitch.setOnClickListener(new ClickEvent());
		
		
		// ListView及其数据源 适配器
		lvBTDevices = (ListView) this.findViewById(R.id.lvDevices);
		adtDevices = new MyBlueToothDeviceAdapter();
		lvBTDevices.setAdapter(adtDevices);
		lvBTDevices.setOnItemClickListener(new ItemClickEvent());	
		
		
		btAdapt = BluetoothAdapter.getDefaultAdapter();// 初始化本机蓝牙功能
		uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
		

		
		if(btAdapt == null){
			Log.e(TAG, "No BlueToothDevice!");
			return;
		}
		else{
		
			if (btAdapt.getState() == BluetoothAdapter.STATE_OFF)// 读取蓝牙状态并显示
				{
					tbtnSwitch.setChecked(true);
					Toast.makeText(MainActivity.this, "蓝牙尚未打开,服务端需先打开蓝牙", Toast.LENGTH_LONG).show();
				}
			else if (btAdapt.getState() == BluetoothAdapter.STATE_ON){
				tbtnSwitch.setChecked(false);
			}
			
			// 注册Receiver来获取蓝牙设备相关的结果
			IntentFilter intent = new IntentFilter();
			intent.addAction(BluetoothDevice.ACTION_FOUND);// 用BroadcastReceiver来取得搜索结果
			intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
			intent.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
			intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
			registerReceiver(searchDevices, intent);
		
		}			
	}
	
	/**设置actionbar*/
	protected void setActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayUseLogoEnabled(true);
		actionBar.setTitle("设置");		
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == android.R.id.home) {
			Intent oneIntent = new Intent();
			oneIntent.setClass(MainActivity.this, BusiessBlueToothActivity.class);
			MainActivity.this.startActivity(oneIntent);
			MainActivity.this.finish();
		}
		return super.onOptionsItemSelected(item);
	}


	private void manageConnectedSocket() {
		//setTitle("检测到蓝牙接入！");
		btSocket=socket;
		//打开控制继电器实例
		Intent intent = new Intent();
		intent.setClass(MainActivity.this, BusiessBlueToothActivity.class);
		startActivity(intent);
	}
	

	private BroadcastReceiver searchDevices = new BroadcastReceiver() {
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
				if (lstDevices.indexOf(macString) == -1)// 防止重复添加
				{
					HashMap<String, Object> map = new HashMap<String, Object>();
					String deviceNamString = device.getName() == null?"":device.getName();
					map.put("deviceName", deviceNamString);
					map.put("deviceMac", device.getAddress());
					lstDevices.add(map); // 获取设备名称和mac地址
				}
					
				adtDevices.notifyDataSetChanged();//刷新蓝牙设备列表
			}
		}
	};


	
	protected void onDestroy() {
	    this.unregisterReceiver(searchDevices);
		super.onDestroy();
//		android.os.Process.killProcess(android.os.Process.myPid());
//		if (serverThread != null) {
//			serverThread.cancel();
//			//serverThread.destroy();
//		}
		
		
	}
	//注销
//	protected void cancelSever() {
//	    this.unregisterReceiver(searchDevices);
////		android.os.Process.killProcess(android.os.Process.myPid());
//		serverThread.cancel();
//		serverThread.destroy();
//	}
//	
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
			Log.e(TAG, " BT connection established, data transfer link open.");
			blueToothDeviceAddress = address;
			SharedPreferences serverAddressInfo = getSharedPreferences("serveraddress_info", 0);  
			serverAddressInfo.edit().putString("serveraddress", severAddEditText.getText().toString()).commit();  
			serverAddressInfo.edit().putString("bluetoothid", blueToothDeviceAddress).commit();
			msgHandler.sendEmptyMessage(0);//隐藏进度条
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, " Connection failed.", e);	
			//Toast.makeText(getApplicationContext(), "连接失败", Toast.LENGTH_SHORT);
			setTitle("连接失败..");
		}
	}
	
	/**点击*/
	class ItemClickEvent implements AdapterView.OnItemClickListener {
		
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			if (severAddEditText.getText().toString().equals("")) {
				Toast.makeText(MainActivity.this, "请填写服务器地址", Toast.LENGTH_SHORT).show();
			}
			else {
				mProgressDialog = ProgressDialog.show(MainActivity.this, "蓝牙连接", "连接蓝牙中，请稍等...");
				final String address = lstDevices.get(arg2).get("deviceMac").toString();
				new Thread(){
					public void run() {
						connectBlueToothDevice(address);//连接蓝牙
					}
				}.start();
			}
			
		}
		
	}
	/**
	 * 隐藏progressbar
	 * */
	Handler msgHandler = new Handler(){
		@Override
  	  public void handleMessage(Message msg) {
  	   // TODO Auto-generated method stub
			super.handleMessage(msg);
			mProgressDialog.dismiss();
			//打开业务界面
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, BusiessBlueToothActivity.class);
			intent.putExtra("deviceMac", blueToothDeviceAddress);
			startActivity(intent);
			MainActivity.this.finish();
  	  }
	};
	/**
	 * 按钮点击事件
	 * */
	private	class ClickEvent implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			if (v == btnSearch)// 搜索蓝牙设备，在BroadcastReceiver显示结果
			{
				if (btAdapt.getState() == BluetoothAdapter.STATE_OFF) {// 如果蓝牙还没开启
					Toast.makeText(MainActivity.this, "请先打开蓝牙", 1000).show();
					return;
				}
				setTitle("本机蓝牙地址：" + btAdapt.getAddress());
				lstDevices.clear();
				btAdapt.startDiscovery();
			} else if (v == tbtnSwitch) {// 本机蓝牙启动/关闭
				if (tbtnSwitch.isChecked() == false)				
				{
					btAdapt.enable();
				    try {
						Thread.sleep(1 * 1000);//延时5s
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} //暂停1秒钟
					//服务端监听
//					serverThread=new AcceptThread();
//					serverThread.start();
					Toast.makeText(MainActivity.this, "蓝牙监听已打开", 1000).show();
				}		
				
				else if (tbtnSwitch.isChecked() == true)
					btAdapt.disable();
			} 
//				else if (v == btnDis)// 本机可以被搜索
//				{
//					Intent discoverableIntent = new Intent(
//							BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
//					discoverableIntent.putExtra(
//							BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
//					startActivity(discoverableIntent);
//				} 
			else if (v == btnExit) {
//				try {
//					if (btSocket != null)
//						btSocket.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
				Intent oneIntent = new Intent(MainActivity.this, AboutActivity.class);
				MainActivity.this.startActivity(oneIntent);
				//MainActivity.this.finish();
				
			}
			else if (v == btnConfirm) {
				try {
					if (btSocket != null)
						btSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				Intent oneIntent = new Intent(MainActivity.this, TempAlarmClassifyActivity.class);
				MainActivity.this.startActivity(oneIntent);
			}
		}

	}
	/**
	 *数据源
	 * */
	class MyBlueToothDeviceAdapter extends BaseAdapter
	{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return lstDevices.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			BluetoothDevicesHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(getApplication()).inflate(R.layout.item_bluetoothdeviceslist, null);
				holder = new BluetoothDevicesHolder();
				holder.blueToothNameTextView = (TextView)convertView.findViewById(R.id.blueToothNameTextView);
				holder.blueToothMacTextView = (TextView)convertView.findViewById(R.id.blueToothMacTextView);
				convertView.setTag(holder);
			}
			else {
				holder = (BluetoothDevicesHolder)convertView.getTag();
			}
//			String oneString = lstDevices.get(position).get("deviceMac").toString();
//			SharedPreferences serverAddressInfo = getSharedPreferences("serveraddress_info", 0);  
//			final String bluetoothAddressString = serverAddressInfo.getString("bluetoothid", "");  
//			 if (oneString.equals(bluetoothAddressString)) {
//				 mProgressDialog = ProgressDialog.show(MainActivity.this, "蓝牙连接", "连接蓝牙中，请稍等...");
//				 new Thread(){
//						public void run() {
//							connectBlueToothDevice(bluetoothAddressString);//连接蓝牙
//						}
//					}.start();
//			}
			//蓝牙名称
			holder.blueToothNameTextView.setText(lstDevices.get(position).get("deviceName").toString());
			//蓝牙mac地址
			holder.blueToothMacTextView.setText(lstDevices.get(position).get("deviceMac").toString());
			
			return convertView;
		}
		
	}
	/**
	 * holder
	 * */
	public final class BluetoothDevicesHolder
	{
		public TextView		blueToothNameTextView;
		public TextView		blueToothMacTextView;
	}
	 
	
	/**
	 * 与服务端建立连接
	 * */
	class AcceptThread extends Thread {
		private final BluetoothServerSocket serverSocket;
	    public AcceptThread() {
	        // Use a temporary object that is later assigned to mmServerSocket,
	        // because mmServerSocket is final	
	    	BluetoothServerSocket tmp=null;
	    	try {
				//tmp = btAdapt.listenUsingRfcommWithServiceRecord("MyBluetoothApp", uuid);
				
				Log.e(TAG, "++BluetoothServerSocket established!++");
				Method listenMethod = btAdapt.getClass().getMethod("listenUsingRfcommOn", new Class[]{int.class});
				tmp = (BluetoothServerSocket) listenMethod.invoke(btAdapt, Integer.valueOf( 1));
				
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			serverSocket=tmp;
	}    
    
    public void run() {
        // Keep listening until exception occurs or a socket is returned
       	//mState!=STATE_CONNECTED
        while(true) {
//            try {
//                socket = serverSocket.accept();
//                Log.e(TAG, "++BluetoothSocket established! DataLink open.++");
//            } catch (IOException e) {
//                break;
//            }
            // If a connection was accepted
            if (socket != null) {
                // Do work to manage the connection (in a separate thread)
                manageConnectedSocket();     
                try {
					serverSocket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                break;
            }
        }
    } 
	/** Will cancel the listening socket, and cause the thread to finish */
    public void cancel() {
	        try {
	            serverSocket.close();
	        } catch (IOException e) { }
	    }
	}
	
	protected String getId(){
		final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
		final String tmDeviceId;
		tmDeviceId = "" + tm.getDeviceId();
		UUID deviceUuid = new UUID(tmDeviceId.hashCode(), ((long)tmDeviceId.hashCode() << 32) | tmDeviceId.hashCode());
		String uniqueId = deviceUuid.toString().replace("-", "");
		return uniqueId;
	}


	
//	public void onResume() {
//		super.onResume();
//		MobclickAgent.onResume(this);
//	}
//	
//	public void onPause() {
//		super.onPause();
//		MobclickAgent.onPause(this);
//	}
	
	//屏蔽back键
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0){
			Intent oneIntent = new Intent();
			oneIntent.setClass(MainActivity.this, BusiessBlueToothActivity.class);
			MainActivity.this.startActivity(oneIntent);
			MainActivity.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
}