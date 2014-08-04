package com.ssm.ssmshop;

import java.io.IOException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.JSONParser;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.ssm.ssmshop.entity.Auth;
import com.ssm.ssmshop.entity.BaseResponse;
import com.ssm.ssmshop.entity.StateCode;
import com.ssm.ssmshop.exception.BaseException;
import com.ssm.ssmshop.helper.SSMHelper;
import com.ssm.ssmshop.httpoperation.GosHttpApplication;
import com.ssm.ssmshop.httpoperation.GosHttpOperation;

import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.ParseException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi")
public class MainActivity extends BaseActivity {
	/**退出事件间隔*/
	private long 				exitTime	=	0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBar();
		setContentView(R.layout.activity_main);
		
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}
	/**设置actionbar*/
	protected void setActionBar() {
		ActionBar actionBar = getSupportActionBar();
		//actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayUseLogoEnabled(true);
		actionBar.setLogo(R.drawable.ico_backbee);
		actionBar.setTitle("我的商铺");
		actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.title_bar));		
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		MenuItem searchItem = menu.add("搜索");
		searchItem.setIcon(R.drawable.tab_btn_search);
		searchItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		searchItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		else if (id == android.R.id.home) {
			
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		
		/**商铺名称*/
		private		TextView				shopNameTextView;
		/**修改按钮*/
		private		TextView				modifyTextView;
		/**上传图片*/
		private		Button					upLoadLogoButton;
		/**上传地理信息*/
		private		ImageView				uploadGPSInfoImageView;
		/**订单数*/
		private		FrameLayout				orderCountFrameLayout;
		/**订单数显示*/
		private		TextView				orderCountTextView;
		/**待付款*/
		private		TextView				obligationTextView;
		/**出售中*/
		private		TextView				onOfferTextView;
		/**待指派配送*/
		private		TextView				assignSendTextView;
		/**访问量*/
		private		TextView				visitsTextView;
		/**活动*/
		private		TextView				activityTextView;
		/**定位*/
		public 		LocationClient 			mLocationClient = null; 
		public 		BDLocationListener 		myListener = new MyLocationListener(); 		
		private 	LocationMode 			tempMode = LocationMode.Battery_Saving;
		private 	String 					tempcoor="gcj02";
		/**GIS*/
		private 	String 					GISString = "";
		/**logo图片*/
		private 	ImageView				logoImageView;
		
		protected GosHttpApplication 		gosHttpApplication;
		protected GosHttpOperation 			gosHttpOperation;
		
		private static int RESULT_LOAD_IMAGE = 1;
		/**等待动画*/
		private		ProgressDialog			progressDialog;
		/**结果*/
		private		String					resultString = "";

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			gosHttpApplication = (GosHttpApplication)getActivity().getApplication();
			gosHttpOperation = gosHttpApplication.getGosHttpOperation();
			findView(rootView);
			progressDialog = new ProgressDialog(getActivity());
			SSMHelper.showProgressDialog(progressDialog);
			new Thread(){
				public void run() {
					requestShopDetail("");
				}
			}.start();
			
			return rootView;
		}
		
		protected void findView(View v) {
			shopNameTextView = (TextView)v.findViewById(R.id.shopNameTextView);
			
			modifyTextView = (TextView)v.findViewById(R.id.modifyTextView);
			modifyTextView.setOnClickListener(new OnClickAction());
			logoImageView = (ImageView)v.findViewById(R.id.logoImageView);
			logoImageView.setOnClickListener(new OnClickAction());
			upLoadLogoButton = (Button)v.findViewById(R.id.upLoadLogoButton);
			upLoadLogoButton.setOnClickListener(new OnClickAction());
			uploadGPSInfoImageView = (ImageView)v.findViewById(R.id.uploadGPSInfoImageView);
			uploadGPSInfoImageView.setOnClickListener(new OnClickAction());
			
			orderCountFrameLayout = (FrameLayout)v.findViewById(R.id.orderCountFrameLayout);
			orderCountFrameLayout.setOnClickListener(new OnClickAction());
			orderCountTextView = (TextView)v.findViewById(R.id.orderCountTextView);
			obligationTextView = (TextView)v.findViewById(R.id.obligationTextView);
			onOfferTextView = (TextView)v.findViewById(R.id.onOfferTextView);
			assignSendTextView = (TextView)v.findViewById(R.id.assignSendTextView);
			visitsTextView = (TextView)v.findViewById(R.id.visitsTextView);
			activityTextView = (TextView)v.findViewById(R.id.activityTextView);
		}
		
		/**点击事件*/
		class OnClickAction implements android.view.View.OnClickListener{

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.modifyTextView:
					showDialog_Layout(getActivity());
					break;
				case R.id.logoImageView:
//					Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//					startActivityForResult(i, RESULT_LOAD_IMAGE);
					break;
				case R.id.upLoadLogoButton:
					
					break;
				case R.id.uploadGPSInfoImageView:
					requestLoc();
					break;
				case R.id.orderCountFrameLayout:
					Intent oneIntent = new Intent();
					oneIntent.setClass(getActivity(), OrderCountActivity.class);
					getActivity().startActivity(oneIntent);
					break;
				default:
					break;
				}	
			}
		}
		/**图库调用*/
		@Override
		public void onActivityResult(int requestCode, int resultCode, Intent data) {
		    super.onActivityResult(requestCode, resultCode, data);
		 
		    if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
		        Uri selectedImage = data.getData();
		        String[] filePathColumn = { MediaStore.Images.Media.DATA };
		 
		        Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
		        cursor.moveToFirst();
		 
		        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		        String picturePath = cursor.getString(columnIndex);
		        cursor.close();
		        logoImageView.setBackground(null);
		        logoImageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
		    }
		 
		        // String picturePath contains the path of selected Image
		}
		
		//显示基于Layout的AlertDialog  
	    protected void showDialog_Layout(Context context) {  
	        LayoutInflater inflater = LayoutInflater.from(context);  
	        final View textEntryView = inflater.inflate(  
	                R.layout.dialoglayout, null);  
	        final EditText edtInput = (EditText)textEntryView.findViewById(R.id.edtInput);  
	        final AlertDialog.Builder builder = new AlertDialog.Builder(context);  
	        builder.setCancelable(false);  
	        builder.setIcon(R.drawable.ico_backbee);  
	        builder.setTitle("修改商铺名称");  
	        builder.setView(textEntryView);  
	        builder.setPositiveButton("确认",  
	                new DialogInterface.OnClickListener() {  
	                    public void onClick(DialogInterface dialog, int whichButton) {  
	                        shopNameTextView.setText(edtInput.getText().toString()); 
	                    }  
	                });  
	        builder.setNegativeButton("取消",  
	                new DialogInterface.OnClickListener() {  
	                    public void onClick(DialogInterface dialog, int whichButton) {  
	                          
	                    }  
	                });  
	        builder.show();  
	    }
	    /**请求商店详细信息*/
		protected void requestShopDetail(String shopIdString) {
			try {
				String userIdString = SSMHelper.getSharePreStr(getActivity(), GosHttpApplication.USER_ID_STRING);
				StateCode baseResponse = gosHttpOperation.invokerObtainShopDetail(userIdString);
				int codeInt = baseResponse.getCode();
				if (codeInt == 1) {
					resultString = baseResponse.getData();
					loginHandler.sendEmptyMessage(1);
				}
				else {
					loginHandler.sendEmptyMessage(0);
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				loginHandler.sendEmptyMessage(0);
			} catch (BaseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				loginHandler.sendEmptyMessage(0);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				loginHandler.sendEmptyMessage(0);
			}
		}
		
		private Handler loginHandler = new Handler(){
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
					loginRequestFailed();
					break;
				case 1:
					loginRequestSuccessed();
					break;
				default:
					break;
				}
			}
		};
		/**登录请求失败*/
		protected void loginRequestFailed() {
			progressDialog.dismiss();
			Toast.makeText(getActivity(), "登录失败", Toast.LENGTH_SHORT).show();
		}
		/**登录请求成功*/
		protected void loginRequestSuccessed() {
			if (!resultString.equals("")) {
				JSONObject  jsonObject = JSON.parseObject(resultString.replace("[", "").replace("]", ""));
				String 		totalCountString = jsonObject.getInteger("totalCount").toString();
				String		readyPaymentCount = jsonObject.getInteger("readyPaymentCount").toString();
				String		activityCount = jsonObject.getInteger("activityCount").toString();
				String		pageView = jsonObject.getInteger("pageView").toString();
				String		goodsCount = jsonObject.getInteger("goodsCount").toString();
				String		readySendCount = jsonObject.getInteger("readySendCount").toString();

				orderCountTextView.setText(totalCountString);
				obligationTextView.setText(readyPaymentCount);
				onOfferTextView.setText(goodsCount);
				assignSendTextView.setText(readySendCount);
				visitsTextView.setText(pageView);
				activityTextView.setText(activityCount);
			}
			progressDialog.dismiss();
		}
	    
	    /**
	     * 获取gis信息
	     * */
	    protected void requestLoc() {
	    	mLocationClient = ((GosHttpApplication)getActivity().getApplication()).mLocationClient;
			
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
	    /***/
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
//			int span=1000;
			try {
				//span = Integer.valueOf(frequence.getText().toString());
			} catch (Exception e) {
				// TODO: handle exception
			}
			//option.setScanSpan(5000);//设置发起定位请求的间隔时间为5000ms
			option.setIsNeedAddress(true);
			mLocationClient.setLocOption(option);
		}
	   
	}

	//按两次返回键退出
	 @Override
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
	     if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
	     	if ((System.currentTimeMillis() - exitTime) > 2000) {
	             Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
	             exitTime = System.currentTimeMillis();
	         } else {
	             MainActivity.this.finish();
	             System.exit(0);
	         }
	         return true;
	     }
	     return super.onKeyDown(keyCode, event);
	 }

}
