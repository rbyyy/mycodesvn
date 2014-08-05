package com.ssm.songshangmen.activity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.Protectable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ssm.songshangmen.R;
import com.ssm.songshangmen.adapter.OrderGoodsListAdapter;
import com.ssm.songshangmen.database.OrderDao;
import com.ssm.songshangmen.entity.OrderGoods;
import com.ssm.songshangmen.entity.OrderGoodsList;
import com.ssm.songshangmen.entity.StateCode;
import com.ssm.songshangmen.exception.BaseException;
import com.ssm.songshangmen.exception.ParseException;
import com.ssm.songshangmen.helper.SSMHelper;
import com.ssm.songshangmen.httpoperation.GosHttpApplication;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint({ "SimpleDateFormat", "HandlerLeak" })
public class OrderCommitActivity extends BaseActivity implements ActivityCallBridge.OnMethodCallback  {
	/**订单数据*/
	private    	OrderDao				orderDao;
	/**函数回调*/
	private 	ActivityCallBridge 		mBridge;
	/**订单数据*/
	private		ArrayList<OrderGoods> 	orderGoodsArrayList = new ArrayList<OrderGoods>();
	/**数据源*/
	private		OrderGoodsListAdapter 	goodsListAdapter = null;
	/**姓名*/
	private		EditText				userNameEditText;
	/**手机*/
	private		EditText				phoneNumberEditText;
	/**地址*/
	private		EditText				addressEditText;
	/**留言*/
	private		EditText				leaveWordEditText;
	/**提交订单*/
	private		Button					commitOrderButton;
	/**返回地址码*/
	private 	String					addressCodeString = "";
	/**商店id*/
	private		String					shopIdString;
	/**总价*/		
	private		String					totalPrice;
	/**等待动画*/
	private		ProgressDialog			progressDialog;
	
	private		TextView 				textView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ordercommit);
		progressDialog = new ProgressDialog(OrderCommitActivity.this);
		orderDao = new OrderDao(this);
		shopIdString = getIntent().getStringExtra("shopid");
		totalPrice = getIntent().getStringExtra("totalprice");
		findView();
		setGoodsList();
		mBridge = ActivityCallBridge.getInstance();
        mBridge.setOnMethodCallback(this);
	}
	
	protected void findView() {
		LinearLayout leftBackLinearLayout = (LinearLayout)findViewById(R.id.leftBackLinearLayout);
		leftBackLinearLayout.setVisibility(View.VISIBLE);
		leftBackLinearLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				OrderCommitActivity.this.finish();
			}
		});
		((TextView)findViewById(R.id.title)).setText("订单提交");
		userNameEditText = (EditText)findViewById(R.id.orderUserNameEditText);
		addressEditText = (EditText)findViewById(R.id.orderAddressEditText);
		phoneNumberEditText = (EditText)findViewById(R.id.orderPhoneEditText);
		leaveWordEditText = (EditText)findViewById(R.id.leaveWordsEditText);
		commitOrderButton = (Button)findViewById(R.id.commitOrderButton);
		commitOrderButton.setOnClickListener(new OnClickAction());
		textView = new TextView(this);
	}
	/**点击事件*/
	class OnClickAction implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.commitOrderButton:
				isEmpty();
				break;

			default:
				break;
			}
		}
		
	}
	/**为空判断*/
	protected void isEmpty() {
		final String userNameString = userNameEditText.getText().toString();
		final String addressString = addressEditText.getText().toString();
		final String phoneString = phoneNumberEditText.getText().toString();
		if (userNameString == null || userNameString.equals("")) {
			Toast.makeText(OrderCommitActivity.this, "姓名不能为空", Toast.LENGTH_SHORT).show();
		}
		else if (addressString == null || addressString.equals("")) {
			Toast.makeText(OrderCommitActivity.this, "送货地址不能为空", Toast.LENGTH_SHORT).show();
		}
		else if (phoneString == null || phoneString.equals("")) {
			Toast.makeText(OrderCommitActivity.this, "电话不能为空", Toast.LENGTH_SHORT).show();
		}
		else {
			accessIntoView(userNameString, addressString, phoneString);
		}
	}
	/**跳转*/
	protected void accessIntoView(final String userNameString, final String addressString, final String phoneString) {
		if (!SSMHelper.getSharePreStr(OrderCommitActivity.this, GosHttpApplication.USER_ID_STRING).equals("")) {
			SSMHelper.showProgressDialog(progressDialog);
			new Thread(){
				public void run() {
					commitAddAddress(userNameString, addressString, phoneString);
					if (!addressCodeString.equals("")) {
						commitOrderAction();
					}
				}
			}.start();
		}
		else {
			Intent oneIntent = new Intent(OrderCommitActivity.this, UserLoginActivity.class);
			OrderCommitActivity.this.startActivity(oneIntent);
		}
	}
	/**添加地址*/
	protected void commitAddAddress(String userNameString, String userAddressString, String userPhoneString) {
		try {
			String useridString = SSMHelper.getSharePreStr(this, GosHttpApplication.USER_ID_STRING);
			StateCode addAddressStateCode = gosHttpOperation.invokerAddUserReceiptAddress(useridString, userAddressString, userNameString, userPhoneString);
			int codeInt = addAddressStateCode.getCode();
			if (codeInt == 1) {
				String addressString = addAddressStateCode.getData().replace("[", "").replace("]", "");
				JSONObject jsonObject = JSON.parseObject(addressString);
				addressCodeString = jsonObject.getString("addressId");
			}
			else {
				handler.sendEmptyMessage(0);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			handler.sendEmptyMessage(0);
		} catch (BaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			handler.sendEmptyMessage(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			handler.sendEmptyMessage(0);
		}
	}
	/**提交订单*/
	protected void commitOrderAction() {
		try {
			String userIdString = SSMHelper.getSharePreStr(OrderCommitActivity.this, GosHttpApplication.USER_ID_STRING);
			List<OrderGoodsList> orderGoodsLists = new ArrayList<OrderGoodsList>();
			for (int i = 0; i < orderGoodsArrayList.size(); i++) {
				OrderGoodsList orderGoodsList = new OrderGoodsList();
				orderGoodsList.setGoodsName(orderGoodsArrayList.get(i).getName());
				orderGoodsList.setGoodsNumber(String.valueOf(orderGoodsArrayList.get(i).getGoodsNumber()));
				orderGoodsList.setGoodsPrice(String.valueOf(orderGoodsArrayList.get(i).getPrice()));
				orderGoodsLists.add(orderGoodsList);
			}
			String dateString = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			StateCode stateCode = gosHttpOperation.invokerAddOrder(orderGoodsArrayList.get(0).getShopId(), userIdString, addressCodeString, orderGoodsLists, "0", "1", totalPrice, dateString);
			int codeInt = stateCode.getCode();
			if (codeInt == 1) {
				handler.sendEmptyMessage(1);
			}
			else {
				handler.sendEmptyMessage(0);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**handler*/
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				requestFailed();
				break;
			case 1:
				requestSuccessed();
				break;
			default:
				break;
			}
		}
	};
	/**网络请求失败*/
	protected void requestFailed() {
		progressDialog.dismiss();
		Toast.makeText(OrderCommitActivity.this, "提交订单失败", Toast.LENGTH_SHORT).show();
	}
	/**网络请求成功*/
	protected void requestSuccessed() {
		progressDialog.dismiss();
		orderDao.deleteAllOrderGoods("ssm");
		Toast.makeText(OrderCommitActivity.this, "提交订单成功", Toast.LENGTH_SHORT).show();
		OrderCommitActivity.this.finish();
	}
	
	/**商品列表*/
	protected void setGoodsList() {
		orderGoodsArrayList = orderDao.readOrderGoodsList("ssm");
		double totalPriceDouble = 0;
		for (int i = 0; i < orderGoodsArrayList.size(); i++) {
			OrderGoods orderGoods = orderGoodsArrayList.get(i);
			totalPriceDouble += orderGoods.getPrice()*orderGoods.getGoodsNumber();
		}
		totalPrice = String.valueOf(totalPriceDouble);
		
		if (orderGoodsArrayList.size() <= 0) {
			OrderCommitActivity.this.finish();
		}
		else {
			
			goodsListAdapter = new OrderGoodsListAdapter(OrderCommitActivity.this, orderGoodsArrayList);
			ListView orderGoodsListView = (ListView)findViewById(R.id.orderGoodsListView);
			orderGoodsListView.removeFooterView(textView);
//			RelativeLayout.LayoutParams layoutParams =new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);  
//			layoutParams.setMargins(20, 0, 0, 0);  
//			textView.setLayoutParams(layoutParams);
			textView.setText(totalPrice);
			orderGoodsListView.addFooterView(textView);
			orderGoodsListView.setAdapter(goodsListAdapter);
			SSMHelper.setListViewHeightBasedOnChildren(orderGoodsListView);
		}
	}

	@Override
	public void doMethod(int menuPosition, int dishNumber, final int dishPosition) {
		// TODO Auto-generated method stub
		int totalNumber = orderGoodsArrayList.get(dishPosition).getGoodsNumber()+dishNumber;
		if (totalNumber>=1) {
			orderDao.updateNumberById("ssm", orderGoodsArrayList.get(dishPosition).getGoodsId(), 
					orderGoodsArrayList.get(dishPosition).getShopId(), orderGoodsArrayList.get(dishPosition).getType(), totalNumber);
			setGoodsList();
		}
		else {
			Dialog alertDialog = new AlertDialog.Builder(this).
					setTitle("温馨提醒").
					setMessage("是否要删除此条菜单?").
				    setNegativeButton("取消", new DialogInterface.OnClickListener() {
					        
					        @Override
					        public void onClick(DialogInterface dialog, int which) {
					        	// TODO Auto-generated method stub
					        	
					        }
					}).
					setPositiveButton("确定", new DialogInterface.OnClickListener() {
				        
				        @Override
				        public void onClick(DialogInterface dialog, int which) {
				        	// TODO Auto-generated method stub
				        	orderDao.deleteOrderGoodsByGoodsId("ssm", orderGoodsArrayList.get(dishPosition));
				        	setGoodsList();
				        	
				        }
				    }).create();
			alertDialog.show();
		}
		
	}
	
}
