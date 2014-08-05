package com.ssm.ssmbee;

import java.io.IOException;
import java.util.ArrayList;

import com.ssm.ssmbee.adapter.BeeQiangListAdapter;
import com.ssm.ssmbee.entity.BaseResponse;
import com.ssm.ssmbee.entity.OrderMenu;
import com.ssm.ssmbee.entity.StateCode;
import com.ssm.ssmbee.exception.BaseException;
import com.ssm.ssmbee.helper.SSMHelper;
import com.ssm.ssmbee.httpoperation.GosHttpApplication;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.net.ParseException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

public class BeeQiangActivity extends BaseActivity implements BeeQiangCallBridge.OnMethodCallback {
	
	/**等待动画*/
	private		ProgressDialog			progressDialog;
	/**抢单数据列表*/
	private		ListView				grabListView;
	/**数据*/
	private		ArrayList<OrderMenu>	grabArrayList;
	/**数据源*/
	private		BeeQiangListAdapter		adapter;
	/**函数回调*/
	private 	BeeQiangCallBridge 		mBridge;
	/**下订单的位置*/
	private		int						beeQiangOrderIndex = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_beeqiang);
		progressDialog = new ProgressDialog(BeeQiangActivity.this);
		setActionBar();
		SSMHelper.showProgressDialog(progressDialog);
		new Thread(){
			public void run() {
				requestAreaOrder("1");
			}
		}.start();
		mBridge = BeeQiangCallBridge.getInstance();
        mBridge.setOnMethodCallback(this);
	}
	/**设置actionbar*/
	protected void setActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayUseLogoEnabled(true);
		actionBar.setLogo(R.drawable.ico_backbee);
		actionBar.setTitle("抢单");
		actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.title_bar));		
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == android.R.id.home) {
			BeeQiangActivity.this.finish();
		}
		return super.onOptionsItemSelected(item);
	}
	/**数据填充*/
	protected void initData() {
		grabArrayList = new ArrayList<OrderMenu>();
		for (int i = 0; i < 5; i++) {
			OrderMenu orderMenu = new OrderMenu();
			orderMenu.setShopName("店名");
			orderMenu.setShopAddress("店的地址");
			orderMenu.setShopPhone("0371-66666666");
			orderMenu.setBuyName("刘成");
			orderMenu.setBuyAddress("买家地址");
			orderMenu.setBuyPhone("13443434343");
			grabArrayList.add(orderMenu);
		}
	}
	/**数据展示*/
	protected void dataShow() {
		adapter = new BeeQiangListAdapter(this, grabArrayList);
		grabListView = (ListView)findViewById(R.id.grabListView);
		grabListView.setAdapter(adapter);
	}
	/**请求小蜜蜂订单数据*/
	protected void requestAreaOrder(String areaIdString) {
		try {
			BaseResponse<OrderMenu> baseResponse = gosHttpOperation.invokerObtainAreaOrder(areaIdString);
			int codeInt = baseResponse.getCode();
			if (codeInt == 1) {
				grabArrayList = (ArrayList<OrderMenu>)baseResponse.getData();
				beeOrderHandler.sendEmptyMessage(1);
			}
			else {
				beeOrderHandler.sendEmptyMessage(0);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			beeOrderHandler.sendEmptyMessage(0);
		} catch (BaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			beeOrderHandler.sendEmptyMessage(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			beeOrderHandler.sendEmptyMessage(0);
		}
	}
	
	@SuppressLint("HandlerLeak")
	private Handler beeOrderHandler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
			case 3:
				loginRequestFailed(msg.what);
				break;
			case 1:
			case 2:
				loginRequestSuccessed(msg.what);
				break;
			default:
				break;
			}
		}
	};
	/**登录请求失败*/
	protected void loginRequestFailed(int codeInt) {
		progressDialog.dismiss();
		switch (codeInt) {
		case 0:
			Toast.makeText(BeeQiangActivity.this, "获取订单失败", Toast.LENGTH_SHORT).show();
			break;
		case 1:
			Toast.makeText(BeeQiangActivity.this, "抢单失败", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}
	/**登录请求成功*/
	protected void loginRequestSuccessed(int codeInt) {
		//Toast.makeText(BeeQiangActivity.this, "成功", Toast.LENGTH_SHORT).show();
		progressDialog.dismiss();
		switch (codeInt) {
		case 1:
			dataShow();
			break;
		case 2:
			if (beeQiangOrderIndex >= 0) {
				grabArrayList.remove(beeQiangOrderIndex);
				adapter.notifyDataSetChanged();
			}
			Toast.makeText(BeeQiangActivity.this, "抢单成功", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}
	@Override
	public void doMethod(int position, String orderId) {
		// TODO Auto-generated method stub
		SSMHelper.showProgressDialog(progressDialog);
		beeQiangOrderIndex = position;
		final String orderIdString = orderId;
		new Thread(){
			public void run() {
				requestQiangOrder(orderIdString);
			}
		}.start();
	}
	/**抢订单的请求*/
	protected void requestQiangOrder(String orderId) {
		try {
			String beeIdString = SSMHelper.getSharePreStr(BeeQiangActivity.this, GosHttpApplication.USER_ID_STRING);
			StateCode baseResponse = gosHttpOperation.invokerBeeGrabOrder(beeIdString, orderId);
			int codeInt = baseResponse.getCode();
			if (codeInt == 1) {
				beeOrderHandler.sendEmptyMessage(2);
			}
			else {
				beeOrderHandler.sendEmptyMessage(3);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			beeOrderHandler.sendEmptyMessage(3);
		} catch (BaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			beeOrderHandler.sendEmptyMessage(3);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			beeOrderHandler.sendEmptyMessage(3);
		}
	}
	
	
	
}
