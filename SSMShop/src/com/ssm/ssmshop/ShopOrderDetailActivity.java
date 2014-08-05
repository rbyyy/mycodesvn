package com.ssm.ssmshop;

import java.io.IOException;
import java.util.ArrayList;

import com.ssm.ssmshop.adapter.OrderDetailAdapter;
import com.ssm.ssmshop.entity.Goods;
import com.ssm.ssmshop.entity.GoodsBaseResponse;
import com.ssm.ssmshop.entity.StateCode;
import com.ssm.ssmshop.exception.BaseException;
import com.ssm.ssmshop.helper.SSMHelper;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.net.ParseException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class ShopOrderDetailActivity extends BaseActivity {
	
	/**等待动画*/
	private		ProgressDialog				progressDialog;
	/**订单id*/
	private		String						orderIdString;
	/**订单列表*/
	private		ArrayList<Goods>			orderArrayList = new ArrayList<Goods>();
	/**姓名*/
	private		TextView					userNameTextView;
	/**地址*/
	private		TextView					userAddressTextView;
	/**电话*/
	private		TextView					userPhoneTextView;
	/**订单id*/
	private		String						orderIdOneString;
	/**用户姓名*/
	private		String						userNameString;
	/**用户地址*/
	private		String						userAddressString;
	/**用户电话*/
	private		String						userPhoneString;
	/**商店订单数据源*/
	private		OrderDetailAdapter			adapter;
	/**列表*/
	private		ListView					shopOrderDetailListView;
	/**提交按钮*/
	private		Button						confirmOrderButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBar();
		setContentView(R.layout.activity_shoporderdetail);
		findView();
		orderIdString = getIntent().getStringExtra("orderId");
		progressDialog = new ProgressDialog(ShopOrderDetailActivity.this);
		SSMHelper.showProgressDialog(progressDialog);
		new Thread(){
			public void run() {
				requestShopOrder(orderIdString);
			}
		}.start();
	}
	/**设置actionbar*/
	protected void setActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayUseLogoEnabled(true);
		actionBar.setLogo(R.drawable.ico_backbee);
		actionBar.setTitle("订单详情");
		actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.title_bar));		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == android.R.id.home) {
			ShopOrderDetailActivity.this.finish();
		}
		return super.onOptionsItemSelected(item);
	}
	/**初始化视图*/
	protected void findView() {
		userNameTextView = (TextView)findViewById(R.id.userNameTextView);
		userAddressTextView = (TextView)findViewById(R.id.userAddressTextView);
		userPhoneTextView = (TextView)findViewById(R.id.userPhoneTextView);
		confirmOrderButton = (Button)findViewById(R.id.confirmOrderButton);
	}
	/**获取订单*/
	protected void requestShopOrder(String orderIdString) {
		try {
			GoodsBaseResponse<Goods> baseResponse = gosHttpOperation.invokerObtainShopOrderDetail(orderIdString);
			int codeInt = baseResponse.getCode();
			if (codeInt == 1) {
				orderIdOneString = baseResponse.getOrderId();
				userNameString = baseResponse.getUserName();
				userAddressString = baseResponse.getUserAddress();
				userPhoneString = baseResponse.getUserPhone();
				orderArrayList = (ArrayList<Goods>)baseResponse.getData();
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
	protected void loginRequestFailed(int num) {
		progressDialog.dismiss();
		if (num == 0) {
			Toast.makeText(ShopOrderDetailActivity.this, "获取订单失败", Toast.LENGTH_SHORT).show();
		}
		else if (num == 3) {
			Toast.makeText(ShopOrderDetailActivity.this, "确认订单失败", Toast.LENGTH_SHORT).show();
		}
		
	}
	/**登录请求成功*/
	protected void loginRequestSuccessed(int num) {
		//Toast.makeText(OrderCountActivity.this, "获取订单成功", Toast.LENGTH_SHORT).show();
//		orderPreviewAdapter = new OrderPreviewAdapter(ShopOrderDetailActivity.this, orderPreviewList);
//		orderCountListView.setAdapter(orderPreviewAdapter);
		
		if (num == 1) {
			userNameTextView.setText(userNameString);
			userAddressTextView.setText(userAddressString);
			userPhoneTextView.setText(userPhoneString);
			adapter = new OrderDetailAdapter(this, orderArrayList);
			shopOrderDetailListView = (ListView)findViewById(R.id.shopOrderDetailListView);
			shopOrderDetailListView.setAdapter(adapter);
			confirmOrderButton.setVisibility(View.VISIBLE);
			confirmOrderButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					SSMHelper.showProgressDialog(progressDialog);
					new Thread(){
						public void run() {
							requestConfirmOrder(orderIdOneString);
						}
					}.start();
				}
			});
			progressDialog.dismiss();
		}
		else if (num == 2) {
			progressDialog.dismiss();
			Toast.makeText(ShopOrderDetailActivity.this, "确认订单成功", Toast.LENGTH_SHORT).show();
			ShopOrderDetailActivity.this.finish();
		}
	}
	/**确认订单*/
	protected void requestConfirmOrder(String orderIdString) {
		try {
			StateCode baseResponse = gosHttpOperation.invokerShopAuditOrder(orderIdString);
			int codeInt = baseResponse.getCode();
			if (codeInt == 1) {
				loginHandler.sendEmptyMessage(2);
			}
			else {
				loginHandler.sendEmptyMessage(3);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			loginHandler.sendEmptyMessage(3);
		} catch (BaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			loginHandler.sendEmptyMessage(3);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			loginHandler.sendEmptyMessage(3);
		}
	}
	
	
}
