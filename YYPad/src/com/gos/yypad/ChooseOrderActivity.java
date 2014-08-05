package com.gos.yypad;


import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gos.yypad.database.ChooseOrderDao;
import com.gos.yypad.entity.ChooseOrder;
import com.gos.yypad.entity.Group;
import com.gos.yypad.exception.BaseException;
import com.gos.yypad.exception.ParseException;
import com.gos.yypad.helper.GOSHelper;
import com.gos.yypad.httpoperation.GosHttpAPIInvoker;
import com.gos.yypad.httpoperation.GosHttpApplication;
import com.gos.yypad.httpoperation.GosHttpOperation;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat")
public class ChooseOrderActivity extends BaseActivity {
	
	private final static String TAG = "ChooseOrderActivity";
	private ProgressDialog 	    mSaveDialog = null;
	private Toast 				toast;
	private TextView			chooseOrderIdTextView;
	private String				uuidString;
	private String				totalPriceString;//总价
	private String				bendPriceString;//折后价
//	private String				couponPriceString;//优惠后的价格
	private EditText			totalPriceEditText;//总价textview
//	private TextView			bendPriceTextView;//优惠价
	private TextView			couponPriceTextView;//优惠的钱数
	private EditText			salesManEditText;
	private EditText			customerNameEditText;
	private EditText			shopperEditText;
	private EditText			phoneEditText;
	private EditText			notesEditText;
	private EditText			giftNoteEditText;//赠品备注
	
	private ChooseOrderDao		chooseOrderDao;//订单数据表
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chooseorder);
		//标题
		TextView topTitle = (TextView)findViewById(R.id.topTitleView);
		topTitle.setTextColor(Color.WHITE);
		topTitle.setText("提交订单");
		//返回按钮
		ImageButton leftNaviImageButton = (ImageButton)findViewById(R.id.leftNaviButton);
		leftNaviImageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		totalPriceEditText = (EditText)findViewById(R.id.totalPriceEditview);//成交价
//		bendPriceTextView = (TextView)findViewById(R.id.bendPriceTextview);
//		couponPriceTextView = (TextView)findViewById(R.id.couponPriceTextview);
		
		salesManEditText = (EditText)findViewById(R.id.salesManEditText);
		customerNameEditText = (EditText)findViewById(R.id.customerNameEditText);
		giftNoteEditText = (EditText)findViewById(R.id.giftNotesEditText);
		
		shopperEditText = (EditText)findViewById(R.id.shoppersEditText);
		phoneEditText = (EditText)findViewById(R.id.customerPhoneEditText);
		notesEditText = (EditText)findViewById(R.id.notesEditText);
		
		chooseOrderDao = new ChooseOrderDao(this);
		
		uuidString = GOSHelper.getUUID().substring(0, 8);
		//订单id这个是有uuid生成的
		//chooseOrderIdTextView = (TextView)findViewById(R.id.chooseOrderIdTextview);
		//chooseOrderIdTextView.setText(GOSHelper.getUUID());
		float totalPriceFloat = (float) 0.0;
		Group<ChooseOrder> chooseOrders = chooseOrderDao.readChooseOrderList("yy");
		for (int i = 0; i < chooseOrders.size(); i++) {
			totalPriceFloat += (Float.parseFloat(chooseOrders.get(i).getBusiness_price())) * 
					(Integer.parseInt(chooseOrders.get(i).getBusiness_number()));
		}
		totalPriceString = String.valueOf(totalPriceFloat);
		bendPriceString = totalPriceString;
//		couponPriceString = "0";
		totalPriceEditText.setText(totalPriceString+"    元");
//		bendPriceTextView.setText(bendPriceString);
//		couponPriceTextView.setText(couponPriceString);
		
		Button clickButton = (Button)findViewById(R.id.submitOrderButton);
		clickButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				uploadOrderAction();
			}
		});
	}
	//点击提交按钮事件
	protected void uploadOrderAction() {
		mSaveDialog = ProgressDialog.show(ChooseOrderActivity.this, "上传数据", "订单数据上传中，请稍等...", true);
		new Thread()
		{
			public void run() {
				uploadOrderInfo();
			}
		}.start();
	}
	//订单上传
	protected void uploadOrderInfo() {
		try {
			String dateString = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
			Group<ChooseOrder> chooseOrdergGroup = chooseOrderDao.readChooseOrderList("yy");
			String areaCodeString = GOSHelper.getSharePreStr(ChooseOrderActivity.this, GosHttpApplication.SHOP_CODE_STRING);
			HttpResponse aString = gosHttpOperation.invokerReportOrderInfo(dateString, areaCodeString, uuidString, shopperEditText.getText().toString(), salesManEditText.getText().toString(), 
					customerNameEditText.getText().toString(), phoneEditText.getText().toString(), 
					totalPriceString, notesEditText.getText().toString(), dateString, dateString, "1", uuidString, giftNoteEditText.getText().toString(), chooseOrdergGroup);
			//int statusCode = aString.getStatusLine().getStatusCode();
			HttpEntity entity = aString.getEntity();
			InputStream is = entity.getContent();
			String reString = GOSHelper.convertStreamToString(is);
			
			if (reString == null || reString.equals("")) {
				
			}
			else {
				JSONObject jsonObject = JSON.parseObject(reString);
				//String oneString = jsonObject.getString("data");
				JSONObject dataObject = jsonObject.getJSONObject("data");
				String stateString = dataObject.getString("state");
				if (stateString.equals("1")) {
					mSaveDialog.dismiss();
					AlertDialog.Builder build = new AlertDialog.Builder(this);
				    build.setIcon(R.drawable.ic_launcher);
				    build.setTitle("订单上传");
				    build.setMessage("上传订单成功");
				    build.setPositiveButton("que'di", new DialogInterface.OnClickListener() {
				         
				        @Override
				        public void onClick(DialogInterface dialog, int which) {
				            // TODO Auto-generated method stub
				            setTitle("You click OK");
				        }
				    });
//						new Thread()
//						{
//							public void run() {
//								// 发送消息，通知handler在主线程中更新UI 
//								connectHanlderFive.sendEmptyMessage(0);
//							}
//						}.start();
				 // 发送消息，通知handler在主线程中更新UI 
		            connectHanlderThree.sendEmptyMessage(0);
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (BaseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//Toast.makeText(getApplicationContext(), "上传订单成功", Toast.LENGTH_SHORT).show();
		
//		AlertDialog firmIntroAlertDialog = new AlertDialog.Builder(FirmMienActivity.this).setTitle("公司简介") .setMessage("   飞度科技是专业从事互联网相关业务开发的公司，依托网页设计、网站制作、网站建设、网站策划为主体，为企业提供网络整合营销服务为一体的综合性网络公司。飞度科技致力于网站建设的全方位发展，立足于以WEB为核心，以WEB应用开发及服务为发展战略的经营理念，在网站建设、网上购物商城、直销会员管理系统、电子政务手机客户端开发等方面积累了自己独特的经验并取得了很好的成绩。郑州网站建设-飞度科技拥有一支集市场商务、创意策划和售后技术支持为一体的专业队伍。公司核心成员由原知名的一些网络公司技术人员组成，对于整合相关企业互联网营销业务有独到见解，更能替企业着想，我们以真诚的态度、求实的服务赢得了各级汽车、医药、化工、服装、旅游、上市集团公司等行业内的一大批具有雄厚实力的客户群体。").setPositiveButton("确定", null)
//			 	.show();
		
	}
	//选购订单刷新主线程
    private Handler connectHanlderThree = new Handler() {  
        @Override  
        public void handleMessage(Message msg) {  
            Log.d(TAG, "display view");  
            // 更新UI，显示图片  
            new AlertDialog.Builder(ChooseOrderActivity.this).setTitle("订单提交").setMessage("订单上传成功").setPositiveButton("确定", null).show();
        }  
    };
    
}
