package com.ssm.songshangmen.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ssm.songshangmen.R;
import com.ssm.songshangmen.activity.ShopMenuActivity.buttonClickAction;
import com.ssm.songshangmen.entity.Auth;
import com.ssm.songshangmen.entity.BaseResponse;
import com.ssm.songshangmen.entity.Goods;
import com.ssm.songshangmen.entity.GoodsType;
import com.ssm.songshangmen.entity.OrderGoodsList;
import com.ssm.songshangmen.entity.Shop;
import com.ssm.songshangmen.entity.StateCode;
import com.ssm.songshangmen.exception.BaseException;
import com.ssm.songshangmen.exception.ParseException;
import com.ssm.songshangmen.helper.SSMHelper;
import com.ssm.songshangmen.httpoperation.GosHttpApplication;
import com.ssm.songshangmen.parser.LoginUpAndInParser;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class UserLoginActivity extends BaseActivity {
	
	/**等待动画*/
	private		ProgressDialog			progressDialog;
	/**用户名*/
	private		EditText				userNameEditText;
	/**密码*/
	private		EditText				passWordEditText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_userlogin);
		progressDialog = new ProgressDialog(UserLoginActivity.this);
		findview();
	}
	
	protected void findview() {
		LinearLayout leftBackLinearLayout = (LinearLayout)findViewById(R.id.leftBackLinearLayout);
		leftBackLinearLayout.setVisibility(View.VISIBLE);
		leftBackLinearLayout.setOnClickListener(new OnClickAction());
		((TextView)findViewById(R.id.title)).setText("用户登录");
		
		userNameEditText = (EditText)findViewById(R.id.usernameEditText);
		passWordEditText = (EditText)findViewById(R.id.passwordEditText);
		
		((Button)findViewById(R.id.loginInButton)).setOnClickListener(new OnClickAction());
		((Button)findViewById(R.id.loginUpButton)).setOnClickListener(new OnClickAction());
	}
	/**判断是否为空*/
	protected void isEmpty() {
		final String userNameString = userNameEditText.getText().toString();
		final String passWordString = passWordEditText.getText().toString();
		if (userNameString == null || userNameString.equals("")) {
			Toast.makeText(UserLoginActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
		}
		else if (passWordString == null || passWordString.equals("")) {
			Toast.makeText(UserLoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
		}
		else {
			SSMHelper.showProgressDialog(progressDialog);
			new Thread(){
				public void run() {
					userLogin(userNameString, passWordString);
				}
			}.start();
		}
	}
	/**点击事件*/
	class OnClickAction implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.leftBackLinearLayout:
				UserLoginActivity.this.finish();
				break;
			case R.id.loginUpButton:
				Intent oneIntent = new Intent();
				oneIntent.setClass(UserLoginActivity.this, UserLoginUpActivity.class);
				UserLoginActivity.this.startActivity(oneIntent);
				break;
			case R.id.loginInButton:
				isEmpty();
				break;
			default:
				break;
			}
		}
		
	}
	
	protected void netRequest() {
//		try {
//			//StateCode stateCode = gosHttpOperation.invokerPhoneAuthCode("1261893");
////			BaseResponse<Shop> baseResponse = gosHttpOperation.invokerObtainShopListByType("1", "1");
////			List<Shop> shopList = baseResponse.getData();
////			String shopName = shopList.get(0).getName();
//			Log.v("123", shopName+"类");
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (BaseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
	}
	/**获取手机验证码*/
	protected void obtainPhoneAuth() {
		try {
			StateCode stateCode = gosHttpOperation.invokerPhoneAuthCode("1261893");
			int codeInt = stateCode.getCode();
			Log.v("123", codeInt+"类");
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
	/**检测手机验证码*/
	protected void verifyPhoneAuth() {
		try {
			StateCode stateCode = gosHttpOperation.invokerVerifyPhoneAuthCode("1261893", "123456");
			int codeInt = stateCode.getCode();
			Log.v("123", codeInt+"类");
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
	/**用户注册*/
	protected void userRegister() {
		try {
			BaseResponse<Auth> baseResponse = gosHttpOperation.invokerUserRegister("abc", "123456", "1261893");
			int codeInt = baseResponse.getCode();
			Log.v("123", codeInt+"类");
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
	
	/**用户登录*/
	protected void userLogin(String userNameString, String passwordString) {
		try {
			String resIdString = SSMHelper.getSharePreStr(UserLoginActivity.this, GosHttpApplication.REGISTER_ID_STRING);
			BaseResponse<Auth> baseResponse = gosHttpOperation.invokerUserLogin(userNameString, passwordString, resIdString);
			int codeInt = baseResponse.getCode();
			if (codeInt == 1) {
				SSMHelper.putSharePre(UserLoginActivity.this, GosHttpApplication.USER_ID_STRING, baseResponse.getData().get(0).getRoleId());
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
		Toast.makeText(UserLoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
	}
	/**登录请求成功*/
	protected void loginRequestSuccessed() {
		progressDialog.dismiss();
		UserLoginActivity.this.finish();
	}
	
	/**修改密码*/
	protected void modifyPassword() {
		try {
			StateCode stateCode = gosHttpOperation.invokerModifyPassword("ed48ab1695c4446e81695ec45a4045bb", "12345678");
			int codeInt = stateCode.getCode();
			Log.v("123", codeInt+"类");
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
	
	/**获取个人信息*/
	protected void requestPersonInfo() {
		try {
			StateCode stateCode = gosHttpOperation.invokerObtainUserInfo("e238d0e2c63a4c5d8a8cb7450de425f2");
			int codeInt = stateCode.getCode();
			Log.v("123", codeInt+"类");
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
	/**获取周围3000米之内的商户*/
	protected void requestShopByDistance() {
		try {
			BaseResponse<Shop> shopList = gosHttpOperation.invokerObtainShopListByDistance("1", "116.417384", "39.920178");
			int codeInt = shopList.getCode();
			Log.v("123", codeInt+"类");
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
	/**商店详细信息*/
	protected void requestShopDetailInfo() {
		try {
			BaseResponse<Shop> shopList = gosHttpOperation.invokerObtainShopDetailByShopId("a0eacfdd913c46c2a583e61b8e60e2cc");
			int codeInt = shopList.getCode();
			Log.v("123", codeInt+"类");
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
	/**猜你喜欢*/
	protected void guessYouLike() {
		try {
			BaseResponse<Shop> shopList = gosHttpOperation.invokerGuessYourLike("116.417384", "39.920178");
			int codeInt = shopList.getCode();
			Log.v("123", codeInt+"类");
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
	/**获取商品的种类*/
	protected void requestGoodsType() {
		try {
			BaseResponse<GoodsType> goodsType = gosHttpOperation.invokerObtainGoodsType("a0eacfdd913c46c2a583e61b8e60e2cc");
			int codeInt = goodsType.getCode();
			Log.v("123", codeInt+"类");
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
	/**获取商品的列表*/
	protected void requestGoodsList() {
		try {
			BaseResponse<Goods> goods = gosHttpOperation.invokerObtainGoodsList("a0eacfdd913c46c2a583e61b8e60e2cc", "1", "1");
			int codeInt = goods.getCode();
			Log.v("123", codeInt+"类");
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
	/**添加收货地址*/
	protected void addAddress() {
		try {
			StateCode addAddressStateCode = gosHttpOperation.invokerAddUserReceiptAddress("e238d0e2c63a4c5d8a8cb7450de425f2", "金源第一城", "测试", "12456");
			int codeInt = addAddressStateCode.getCode();
			String addressString = addAddressStateCode.getData().replace("[", "").replace("]", "");
			JSONObject jsonObject = JSON.parseObject(addressString);
			String addressIdString = jsonObject.getString("addressId");
			Log.v("123", codeInt+addressIdString);
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
	/**添加订单*/
	protected void addOrder() {
		try {
			List<OrderGoodsList> orderGoodsLists = new ArrayList<OrderGoodsList>();
			OrderGoodsList orderGoodsList = new OrderGoodsList();
			orderGoodsList.setGoodsName("可乐");
			orderGoodsList.setGoodsNumber("1");
			orderGoodsList.setGoodsPrice("4");
			orderGoodsLists.add(orderGoodsList);
			StateCode stateCode = gosHttpOperation.invokerAddOrder("a0eacfdd913c46c2a583e61b8e60e2cc", "e238d0e2c63a4c5d8a8cb7450de425f2", "1", orderGoodsLists, "0", "1", "100", "2014-7-11");
			int codeInt = stateCode.getCode();
			Log.v("123", codeInt+"类");
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
	
}
