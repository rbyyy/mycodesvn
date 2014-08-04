package com.gos.lyfx;

import java.util.Set;

import com.example.bluetoothprinter.BlueToothService;
import com.example.bluetoothprinter.BlueToothService.OnReceiveDataHandleEvent;
import com.gos.lyfx.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class PrinterReceiptActivity extends Activity {
	private BlueToothService mBTService = null;
	public  Handler mhandler;
	
	
	public static boolean isRecording = false;// 线程控制标记

	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;
	private Button controlButton;//打开或关闭蓝牙
	private Thread bt_update = null;//状态更新线程
	private boolean updateflag = true;//更新标志
	private Button bt_matches;// 配对蓝牙
	private ListView deviceList;// 设备列表
	private ArrayAdapter<String> mPairedDevicesArrayAdapter = null;// 已配对
	private ArrayAdapter<String> mNewDevicesArrayAdapter = null;// 新搜索列表
	private BluetoothAdapter mBluetoothAdapter = null;
	private Set<BluetoothDevice> devices;
	private Button bt_scan;// 扫描设备
	public Handler handler = null;

	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_printerreceipt);
		
		((ImageButton)findViewById(R.id.leftNaviButton)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
		((TextView)findViewById(R.id.topTitleView)).setText("销售同步");
		
		mhandler = new Handler() {
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case MESSAGE_STATE_CHANGE:// 蓝牙连接状态
					switch (msg.arg1) {
					case BlueToothService.STATE_CONNECTED:// 已经连接
						break;
					case BlueToothService.STATE_CONNECTING:// 正在连接
						break;
					case BlueToothService.STATE_LISTEN:
					case BlueToothService.STATE_NONE:
						break;
					case BlueToothService.SUCCESS_CONNECT:
						Toast.makeText(
								PrinterReceiptActivity.this,
								"连接成功", Toast.LENGTH_SHORT).show();

						break;
					case BlueToothService.FAILED_CONNECT:
						Toast.makeText(
								PrinterReceiptActivity.this,
								"连接失败", Toast.LENGTH_SHORT)
								.show();
						break;
					case BlueToothService.LOSE_CONNECT:
						Toast.makeText(
								PrinterReceiptActivity.this,
								"断开连接", Toast.LENGTH_SHORT).show();
						break;
					}
					break;
				case MESSAGE_READ:
					// sendFlag = false;//缓冲区已满
					break;
				case MESSAGE_WRITE:// 缓冲区未满
					// sendFlag = true;
					break;

				}
			}
		};
		
		mBTService = new BlueToothService(this, mhandler);// 创建对象的时候必须有一个是Handler类型
		checkDeviceHaveBluetooth();
		openOrCloseBluetooth();
		disConnect();
		checkMatchDevice();
		scanBluetoothDevice();
		listClickAction();
		printWord();
		sendOrder();

	}
	/**检测设备是否含有蓝牙功能*/
	protected void checkDeviceHaveBluetooth() {
		// 点击检查设备是否含有蓝牙功能
		Button checkButton = (Button) findViewById(R.id.checkButton);
		checkButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mBTService.HasDevice()) {
					Toast.makeText(
							PrinterReceiptActivity.this,
							"有蓝牙设备", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(
							PrinterReceiptActivity.this,
							"没有蓝牙设备", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	/**打开或关闭蓝牙功能*/
	protected void openOrCloseBluetooth() {
		// 点击打开或者关闭蓝牙设备
		controlButton = (Button) findViewById(R.id.openButton);
		if (mBTService.IsOpen()) {// 判断蓝牙是否打开
			controlButton.setText("打开蓝牙");
		}
		controlButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mBTService.IsOpen()) {// 判断蓝牙是否打开
					if (mBTService.getState() == mBTService.STATE_CONNECTED) {
						mBTService.DisConnected();
					}
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					mBTService.CloseDevice();
				} else {
					mBTService.OpenDevice();
				}

			}
		});
		
		// 更新按钮状态
		bt_update = new Thread() {
			public void run() {
				while (updateflag) {
					if (mBTService.IsOpen()) {// 判断蓝牙是否打开
						controlButton.post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								controlButton.setText("关闭蓝牙");
							}
						});
					} else {
						controlButton.post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								controlButton.setText("打开蓝牙");
							}
						});

					}

				}
			}
		};

		bt_update.start();
		
	}
	/**断开蓝牙连接*/
	protected void disConnect(){
		Button bt_disconnect = (Button) findViewById(R.id.disconnectButton);
		bt_disconnect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mBTService.getState() == mBTService.STATE_CONNECTED) {
					mBTService.DisConnected();
				}
			}
		});
	}
	/**查看已配对的设备*/
	protected void checkMatchDevice() {
		mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this,
				R.layout.device_name);
		deviceList = (ListView) findViewById(R.id.lv_device);
		// 查看已配对蓝牙
		bt_matches = (Button) findViewById(R.id.bt_matches);
		bt_matches.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!mBTService.IsOpen()) {
					mBTService.OpenDevice();
					return;
				}
				deviceList.setAdapter(mPairedDevicesArrayAdapter);
				mPairedDevicesArrayAdapter.clear();
				devices = mBTService.GetBondedDevice();
				if (devices.size() > 0) {

					for (BluetoothDevice device : devices) {
						mPairedDevicesArrayAdapter.add(device.getName() + "\n"
								+ device.getAddress());
					}
				} else {
					String noDevices = "没有已配对的设备";
					mPairedDevicesArrayAdapter.add(noDevices);
				}
			}
		});
	}
	/**扫描设备*/
	@SuppressLint("HandlerLeak")
	protected void scanBluetoothDevice() {
		
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 0:

					break;
				case 1:// 扫描完毕
						// progressDialog.cancel();
					mBTService.StopScan();

					Toast.makeText(
							PrinterReceiptActivity.this,
							"扫描完成", Toast.LENGTH_SHORT).show();
					break;
				case 2:// 停止扫描
					Toast.makeText(
							PrinterReceiptActivity.this,
							"停止扫描", Toast.LENGTH_SHORT).show();
					break;
				}
			}
		};
		
		deviceList = (ListView) findViewById(R.id.lv_device);
		mNewDevicesArrayAdapter = new ArrayAdapter<String>(this,
				R.layout.device_name);
		// 扫描所有区设备

		bt_scan = (Button) findViewById(R.id.bt_scan);
		bt_scan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 先判断是否正在扫描

				if (!mBTService.IsOpen()) {// 判断蓝牙是否打开
					mBTService.OpenDevice();
					return;
				}
				if (mBTService.GetScanState() == mBTService.STATE_SCANING)
					return;

				mNewDevicesArrayAdapter.clear();
				devices = mBTService.GetBondedDevice();

				if (devices.size() > 0) {

					for (BluetoothDevice device : devices) {
						mNewDevicesArrayAdapter.add(device.getName() + "\n"
								+ device.getAddress());
					}
				}
				deviceList.setAdapter(mNewDevicesArrayAdapter);
				new Thread() {
					public void run() {
						mBTService.ScanDevice();
					}
				}.start();

			}

		});

		mBTService.setOnReceive(new OnReceiveDataHandleEvent() {

			@Override
			public void OnReceive(BluetoothDevice device) {
				// TODO Auto-generated method stub
				if (device != null) {
					mNewDevicesArrayAdapter.add(device.getName() + "\n"
							+ device.getAddress());
				} else {
					Message msg = new Message();
					msg.what = 1;
					handler.sendMessage(msg);

				}
			}
		});
		
		
	}
	/**点击事件*/
	protected void listClickAction() {
		deviceList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 获取蓝牙物理地址
				if (!mBTService.IsOpen()) {// 判断蓝牙是否打开
					mBTService.OpenDevice();
					return;
				}
				if (mBTService.GetScanState() == mBTService.STATE_SCANING) {
					Message msg = new Message();
					msg.what = 2;
					handler.sendMessage(msg);
				}
				if (mBTService.getState() == mBTService.STATE_CONNECTING) {
					return;
				}

				String info = ((TextView) view).getText().toString();
				String address = info.substring(info.length() - 17);
				mBTService.DisConnected();
				mBTService.ConnectToDevice(address);// 连接蓝牙

			}
		});
	}
	/**打印文字*/
	protected void printWord() {
		Button bt_print = (Button)findViewById(R.id.printButton);
		bt_print.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mBTService.getState() != mBTService.STATE_CONNECTED) {
					Toast.makeText(
							PrinterReceiptActivity.this,
							"设备没有连接", 2000).show();
					return;
				}
				String message = "\n\n\n\n"
						+ " 产品名     数量    单价    金额\n"
						+ "可口可乐       4     2.5      10\n"
						+ "可口可乐       4     2.5      10\n"
						+ "可口可乐       4     2.5      10\n"
						+ "                       总价:30元\n"
						+ "店名:天明路利好店\n"
						+ "店面余额:2200元\n"
						+ "洛阳好思源有限责任公司\n"
						+ "销售员:贾思明\n"
						+ "日期:2014.6.5 14:32\n\n\n";
				byte[] bt = new byte[3];
				bt[0] = 27;
				bt[1] = 56;
				bt[2] = 2;// 1,2//设置字体大小
				mBTService.write(bt);
				mBTService.PrintCharacters(message);
			}
		});
	}
	/**发送指令*/
	protected void sendOrder() {
		Button bt_order = (Button) findViewById(R.id.sendOrderButton);
		bt_order.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mBTService.getState() != mBTService.STATE_CONNECTED) {
					Toast.makeText(
							PrinterReceiptActivity.this,
							"没有连接设备", 2000).show();
					return;
				}
				byte[] msgBuffer = null;		
				msgBuffer = hexStringToBytes("685555A0A0000116");	
//				byte[] send = new byte[10];
				mBTService.SendOrder(msgBuffer);
			}
		});
	}

	/** 
	 * Convert hex string to byte[] 
	 * @param hexString the hex string 
	 * @return byte[] 
	 */  
	public static byte[] hexStringToBytes(String hexString) {  
	    if (hexString == null || hexString.equals("")) {  
	        return null;  
	    }  
	    hexString = hexString.toUpperCase();  
	    int length = hexString.length() / 2;  
	    char[] hexChars = hexString.toCharArray();  
	    byte[] d = new byte[length];  
	    for (int i = 0; i < length; i++) {  
	        int pos = i * 2;  
	        d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));  
	    }  
	    return d;  
	} 
	/** 
	 * Convert char to byte 
	 * @param c char 
	 * @return byte 
	 */  
	 public static byte charToByte(char c) {  
	    return (byte) "0123456789ABCDEF".indexOf(c);  
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	public void onBackPressed() {
		if (bt_update != null) {
			updateflag = false;
			bt_update = null;
		}
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (mBTService != null) {
			mBTService.DisConnected();
			mBTService = null;
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PrinterReceiptActivity.this.finish();
	}
	
	
}
