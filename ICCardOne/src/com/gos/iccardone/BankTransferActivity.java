package com.gos.iccardone;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gos.iccardone.exception.BaseException;
import com.gos.iccardone.exception.ParseException;
import com.gos.iccardone.helper.GOSHelper;
import com.gos.iccardone.httpoperation.GosHttpApplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;

public class BankTransferActivity extends BaseActivity {
	/**打印标志*/
	private String 			TAG = "BankTransferActivity";
	/**tabhost的定义*/
	private TabHost 		tabHost;
	/**转账标志*/
	private int				bankTransfer = 0;
	/**开户行*/
	private EditText		bankNameEditText;
	/**卡号*/
	private	EditText		cardNumberEditText;
	/**姓名*/
	private EditText		accountNameEditText;
	/**金额*/
	private	EditText		moneyEditText;
	/**按钮*/
	private Button			commitButton;
	/**卡卡卡号*/
	private EditText		twoCardNumberEditText;
	/**卡卡姓名*/
	private EditText		twoAccountNameEditText;
	/**卡卡金额*/
	private EditText		twoMoneyEditText;
	/**确认按钮*/
	private Button			okTwoButton;
	/**开户行名称集合*/
	private String[] 		openAccountStrings = new String[]{"中国工商银行","中国农业银行", "中国银行", "中国建设银行", "交通银行", 
			"中信银行", "中国光大银行", "华夏银行", "中国民生银行", "广东发展银行", "深圳发展银行/平安银行", "招商银行", "兴业银行", "上海浦东发展银行", "中国邮政储蓄银行"};
	/**开户行id集合*/
	private String[]		openAccountIdStrings = new String[]{"102", "103", "104", "105", "301", "302", 
			"303", "304", "305", "306", "307", "308", "309", "310", "403"};
	/**当前选择的类型*/
	private int 				currentSelectItem = 0;
	/**当前选择的银行id*/
	private String				currentSelectBankIdString = "";
	/**点击*/
	private RadioOnClick 		OnClick = new RadioOnClick(0);
	/**oneLinearLayout*/
	private LinearLayout		oneLinearLayout;
	/**twoLinearLayout*/
	private LinearLayout    	twoLinearLayout;
	/**错误信息*/
	private String				errorString = "";
	/**卡关联姓名*/
	private String				cardAsNameString = "";
	
	private ProgressDialog 		mSaveDialog = null;
	/**登录密码*/
	private EditText			loginUpPasswordEditText;
	/**登录密码*/
	private EditText			loginUpPasswordOneEditText;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_banktransfer);
		initTabHost();//初始化tabhost
		findView();
	}
	/**界面*/
	protected void findView() {
		
		oneLinearLayout = (LinearLayout)findViewById(R.id.oneLinearLayout);
		twoLinearLayout = (LinearLayout)findViewById(R.id.twoLinearLayout);
		
		bankNameEditText = (EditText)findViewById(R.id.bankNameEditText);
		bankNameEditText.setFocusable(false);   
		bankNameEditText.setFocusableInTouchMode(false);   
		bankNameEditText.requestFocus();  
		bankNameEditText.setOnClickListener(new RadioClickListener());
		
		cardNumberEditText = (EditText)findViewById(R.id.cardNumberEditText);
		//String cardIdString = GOSHelper.getSharePreStr(this, GosHttpApplication.USER_NAME_STRING);
		//cardNumberEditText.setText(cardIdString);
		
		accountNameEditText = (EditText)findViewById(R.id.accountNameEditText);
//		accountNameEditText.setText("张五顺");
		
		moneyEditText = (EditText)findViewById(R.id.moneyEditText);
//		moneyEditText.setText("1");
		
		loginUpPasswordEditText = (EditText)findViewById(R.id.loginUpPasswordEditText);
		
		commitButton = (Button)findViewById(R.id.okButton);//提交
		commitButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isEmpty();
			}
		});
		
		twoCardNumberEditText = (EditText)findViewById(R.id.twoCardIdEditText);
		twoAccountNameEditText = (EditText)findViewById(R.id.twoAccountNameEditText);
		twoMoneyEditText = (EditText)findViewById(R.id.twoMoneyEditText);
		loginUpPasswordOneEditText = (EditText)findViewById(R.id.loginUpPasswordOneEditText);
		okTwoButton = (Button)findViewById(R.id.okTwoButton);
		okTwoButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isEmpty();
			}
		});
		
	}
	/**弹出框*/
	class RadioClickListener implements OnClickListener {
		   @Override
		   public void onClick(View v) {
			   switch (v.getId()) {
					case R.id.bankNameEditText:
						currentSelectItem = 0;
						AlertDialog ad =new AlertDialog.Builder(BankTransferActivity.this).setTitle("")
					    .setSingleChoiceItems(openAccountStrings, OnClick.getIndex(),new RadioOnClick(0)).create();
					    ad.show();
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
		   this.index = index;
	   }
	   public void setIndex(int index){
		   this.index=index;
	   }
	   public int getIndex(){
		   return index;
	   }
	 
	   public void onClick(DialogInterface dialog, int whichButton){
		   setIndex(whichButton);
			switch (currentSelectItem) {
			   	case 0:
			   		//Toast.makeText(AskForOpenActivity.this, "您已经选择了 " +  ":" + openActionTypes[index], Toast.LENGTH_LONG).show();
			   		bankNameEditText.setText(openAccountStrings[index]);
			   		currentSelectBankIdString = openAccountIdStrings[index];
			   		break;
			    default:
				    break;
			}
		   dialog.dismiss();
		   
	   }
	 }
	//初始化tabhost
	protected void initTabHost() {
		tabHost = (TabHost) findViewById(R.id.topBankTransferTabHost);  
        tabHost.setup();  
        TabWidget tabWidget = tabHost.getTabWidget();  
        
        tabHost.addTab(tabHost.newTabSpec("tab1")  
                .setIndicator("现金转入", getResources().getDrawable(R.drawable.ic_launcher))  
                .setContent(R.id.bankTransferView1));   
          
        tabHost.addTab(tabHost.newTabSpec("tab2")  
                .setIndicator("现金转出")  
                .setContent(R.id.bankTransferView2)); 
        
        tabHost.addTab(tabHost.newTabSpec("tab3")  
                .setIndicator("内部转账")  
                .setContent(R.id.bankTransferView3));
          
        final int tabs = tabWidget.getChildCount();  
        Log.v(TAG, "***tabWidget.getChildCount() : " + tabs);  
        
        //注意这个就是改变Tabhost默认样式的地方，一定将这部分代码放在上面这段代码的下面，不然样式改变不了
        updateTab(tabHost);
        tabHost.setOnTabChangedListener(new OnTabChangedListener()); // 选择监听器
	}
	/**tab变化时的监听*/
	class OnTabChangedListener implements OnTabChangeListener {

		  @Override
		  public void onTabChanged(String tabId) {
			   tabHost.setCurrentTabByTag(tabId);
			   System.out.println("tabid " + tabId);
			   System.out.println("curreny after: " + tabHost.getCurrentTabTag());
			   Log.v(TAG, "tabid " + tabId);
			   Log.v(TAG, "curreny after: " + tabHost.getCurrentTabTag());
			   updateTab(tabHost);
			   switch (tabHost.getCurrentTab()) {
			   	 case 0:
			   		 	oneLinearLayout.setVisibility(View.VISIBLE);
			   		 	twoLinearLayout.setVisibility(View.INVISIBLE);
			   		 	bankTransfer = 0;
			   		 break;
			   	 case 1:
			   		 	oneLinearLayout.setVisibility(View.VISIBLE);
			   		 	twoLinearLayout.setVisibility(View.INVISIBLE);
			   		 	bankTransfer = 1;
			   		 break;
			   	 case 2:
			   		 	oneLinearLayout.setVisibility(View.INVISIBLE);
			   		 	twoLinearLayout.setVisibility(View.VISIBLE);
			   	 		bankTransfer = 2;
			   	 	break;
			   	 default:
			   		 break;
			   }
		  }
	}
	/**
     * 更新Tab标签的颜色，和字体的颜色
     * @param tabHost
     */
    private void updateTab(final TabHost tabHost) {
        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            View view = tabHost.getTabWidget().getChildAt(i);
            //修改Tabhost高度和宽度
            view.getLayoutParams().height = 60;  
            view.getLayoutParams().width = 65;
            
            TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextSize(15);
            tv.setTypeface(Typeface.SERIF, 2); // 设置字体和风格
            if (tabHost.getCurrentTab() == i) {//选中
                //view.setBackgroundDrawable(getResources().getDrawable(R.drawable.category_current));//选中后的背景 
            	int defColor = Color.rgb(66, 139, 202);
            	tv.setTextColor(defColor);
            } else {//不选中
                //view.setBackgroundDrawable(getResources().getDrawable(R.drawable.category_bg));//非选择的背景
                tv.setTextColor(this.getResources().getColorStateList(android.R.color.black));
            }
        }
    }
    /**判断是否为空*/
    protected void isEmpty() {
    	String userPasswordString = GOSHelper.getSharePreStr(BankTransferActivity.this, GosHttpApplication.USER_PASSWORD_STRING);
    	if (bankTransfer == 0 || bankTransfer == 1) {
    		final String cardNumber = cardNumberEditText.getText().toString().trim();
        	final String accountName = accountNameEditText.getText().toString().trim();
        	final String amount = moneyEditText.getText().toString().trim();
        	final String passwordString = loginUpPasswordEditText.getText().toString();
    		if (currentSelectBankIdString.equals("")) {
    			Toast.makeText(this, "开户行不能为空", Toast.LENGTH_SHORT).show();
    		}
    		else if ((cardNumber == null) || (cardNumber.equals(""))) {
    			Toast.makeText(this, "卡号不能为空", Toast.LENGTH_SHORT).show();
    		}
    		else if ((accountName == null) || (accountName.equals(""))) {
    			Toast.makeText(this, "姓名不能为空", Toast.LENGTH_SHORT).show();
    		}
    		else if ((amount == null) || (amount.equals(""))) {
    			Toast.makeText(this, "金额不能为空", Toast.LENGTH_SHORT).show();
    		}
    		else if (Float.valueOf(amount) < 1000) {
    			Toast.makeText(this, "转账金额不能小于1000元", Toast.LENGTH_SHORT).show();
			}
    		else if ((passwordString == null) || (passwordString.equals(""))) {
				Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
			}
    		else if (!userPasswordString.equals(passwordString)) {
				Toast.makeText(this, "密码不一致，请重新输入", Toast.LENGTH_SHORT).show();
			}
    		else {
    			if (bankTransfer == 0) {
    				mSaveDialog = ProgressDialog.show(BankTransferActivity.this, "转账充值","转入中，请稍等...", true);
				}
    			else {
    				mSaveDialog = ProgressDialog.show(BankTransferActivity.this, "转账消费","转出中，请稍等...", true);
				}
    			new Thread(){
    				public void run() {
    					switch (bankTransfer) {
    					case 0:
    						//intoAccountRequest(currentSelectBankIdString, cardNumber, accountName, amount);
    						
    						cardAssociation("");
    						if (cardAsNameString.equals(accountName)) {
    							payRequest(currentSelectBankIdString, cardNumber, accountName, amount);
							}
    						else {
    							loginResultHandler.sendEmptyMessage(2);
							}
    						break;
    					case 1:
    						//outAccountRequest(currentSelectBankIdString, cardNumber, accountName, amount);
    						cardAssociation("");
    						if (cardAsNameString.equals(accountName)) {
    							consumeRequest(currentSelectBankIdString, cardNumber, accountName, amount);
							}
    						else {
    							loginResultHandler.sendEmptyMessage(2);
							}
    						break;
    					default:
    						break;
    					}
    				}
    			}.start();
    		}
		}
    	else {
    		
    		final String cardNumber = twoCardNumberEditText.getText().toString().trim();
    		final String accountName = twoAccountNameEditText.getText().toString().trim();
        	final String amount = twoMoneyEditText.getText().toString().trim();
        	final String twoPasswordString = loginUpPasswordOneEditText.getText().toString();
    		if ((cardNumber == null) || (cardNumber.equals(""))) {
    			Toast.makeText(this, "卡号不能为空", Toast.LENGTH_SHORT).show();
    		}
    		else if ((accountName == null) || (accountName.equals(""))) {
    			Toast.makeText(this, "姓名不能为空", Toast.LENGTH_SHORT).show();
			}
    		else if ((amount == null) || (amount.equals(""))) {
    			Toast.makeText(this, "金额不能为空", Toast.LENGTH_SHORT).show();
    		}
    		else if ((twoPasswordString == null) || (twoPasswordString.equals(""))) {
				Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
			}
    		else if (!userPasswordString.equals(twoPasswordString)) {
				Toast.makeText(this, "密码不一致，请重新输入", Toast.LENGTH_SHORT).show();
			}
    		else {
    			mSaveDialog = ProgressDialog.show(BankTransferActivity.this, "卡卡转账","转账中，请稍等...", true);
    			new Thread(){
    				public void run() {
    					switch (bankTransfer) {
    					case 2:
    						//intoAccountRequest(currentSelectBankIdString, cardNumber, accountName, amount);
    						cardAssociation(cardNumber);
    						if (cardAsNameString.equals(accountName)) {
    							cardCardTransfer(cardNumber, amount);
							}
    						else {
    							loginResultHandler.sendEmptyMessage(2);
							}
    						break;
    					default:
    						break;
    					}
    				}
    			}.start();
    		}
		}
    	
	}
    /**
     * 代收网络请求
     * */
    protected void intoAccountRequest(String openBankString, String cardNumberString, String accountNameString, String amountString) {
		try {
//			String userNameString = GOSHelper.getSharePreStr(this, GosHttpApplication.USER_NAME_STRING);
//			String userPasswordString = GOSHelper.getSharePreStr(this, GosHttpApplication.USER_PASSWORD_STRING);
			String userNameString = "00019140020246801";
			String userPasswordString = "1SXNfX"; 
			HttpResponse aHttpResponse = gosHttpOperation.invokerInToAccountResponse(userNameString, userPasswordString, openBankString, cardNumberString, accountNameString, amountString);
			HttpEntity entity = aHttpResponse.getEntity();
			InputStream is = entity.getContent();
			String reString = GOSHelper.convertStreamToString(is);
			if (reString == null || reString.equals("")) {
				loginResultHandler.sendEmptyMessage(0);
			}
			else {
				JSONObject jsonObject = JSON.parseObject(reString);
				JSONArray responeArray = jsonObject.getJSONArray("data");
				if (responeArray.size() > 0) {
					JSONObject oneObject = responeArray.getJSONObject(0);
					String retCodeString = oneObject.getString("retCode");
					//String errMsgString = oneObject.getString("errMsg");
					if (retCodeString.equals("0000")) {
						loginResultHandler.sendEmptyMessage(1);
					}
					else {
						loginResultHandler.sendEmptyMessage(0);
					}
				}
				else {
					loginResultHandler.sendEmptyMessage(0);
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
			loginResultHandler.sendEmptyMessage(0);
		} catch (BaseException e) {
			e.printStackTrace();
			loginResultHandler.sendEmptyMessage(0);
		} catch (IOException e) {
			e.printStackTrace();
			loginResultHandler.sendEmptyMessage(0);
		}
	}
    /**
     * 充值网络请求
     * */
    protected void payRequest(String openBankString, String cardNumberString, String accountNameString, String amountString) {
    	try {
			String cardIdString = GOSHelper.getSharePreStr(this, GosHttpApplication.USER_NAME_STRING);
			//String passwordString = GOSHelper.getSharePreStr(this, GosHttpApplication.USER_PASSWORD_STRING);
			String userNameString = "00019140020246801";
			String userPasswordString = "1SXNfX"; 
			String tokenString = GOSHelper.getSharePreStr(this, GosHttpApplication.USER_TOKEN_STRING);
			HttpResponse aHttpResponse = gosHttpOperation.invokerPayResponse(userNameString, userPasswordString, openBankString, cardNumberString, 
					accountNameString, amountString, cardIdString, tokenString);
			HttpEntity entity = aHttpResponse.getEntity();
			InputStream is = entity.getContent();
			String reString = GOSHelper.convertStreamToString(is);
			if (reString == null || reString.equals("")) {
				loginResultHandler.sendEmptyMessage(0);
			}
			else {
				JSONObject jsonObject = JSON.parseObject(reString);
				String codeString = jsonObject.getInteger("code").toString();
				errorString = jsonObject.getString("data");
				if (codeString.equals("1")) {
					loginResultHandler.sendEmptyMessage(1);
				}
				else {
					loginResultHandler.sendEmptyMessage(0);
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
			loginResultHandler.sendEmptyMessage(0);
		} catch (BaseException e) {
			e.printStackTrace();
			loginResultHandler.sendEmptyMessage(0);
		} catch (IOException e) {
			e.printStackTrace();
			loginResultHandler.sendEmptyMessage(0);
		}
	}
    
    
    /**
     * 代付网络请求
     * */
    protected void outAccountRequest(String openBankString, String cardNumberString, String accountNameString, String amountString) {
		try {
			//String userNameString = GOSHelper.getSharePreStr(this, GosHttpApplication.USER_NAME_STRING);
			//String userPasswordString = GOSHelper.getSharePreStr(this, GosHttpApplication.USER_PASSWORD_STRING);
			String userNameString = "00019140020246701";
			String userPasswordString = "sDFx3B"; 
			HttpResponse aHttpResponse = gosHttpOperation.invokerOutAccountResponse(userNameString, userPasswordString, openBankString, cardNumberString, accountNameString, amountString);
			HttpEntity entity = aHttpResponse.getEntity();
			InputStream is = entity.getContent();
			String reString = GOSHelper.convertStreamToString(is);
			if (reString == null || reString.equals("")) {
				loginResultHandler.sendEmptyMessage(0);
			}
			else {
				JSONObject jsonObject = JSON.parseObject(reString);
				String stateString = jsonObject.getString("code");
				if (stateString.equals("1")) {
					String tokenString = jsonObject.getString("data");
					GOSHelper.putSharePre(this, GosHttpApplication.USER_TOKEN_STRING, tokenString);
					loginResultHandler.sendEmptyMessage(1);
				}
				else {
					loginResultHandler.sendEmptyMessage(0);
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
			loginResultHandler.sendEmptyMessage(0);
		} catch (BaseException e) {
			e.printStackTrace();
			loginResultHandler.sendEmptyMessage(0);
		} catch (IOException e) {
			e.printStackTrace();
			loginResultHandler.sendEmptyMessage(0);
		}
	}
    
    
    /**
     * 消费网络请求
     * */
    protected void consumeRequest(String openBankString, String cardNumberString, String accountNameString, String amountString) {
		try {
			String cardIdString = GOSHelper.getSharePreStr(this, GosHttpApplication.USER_NAME_STRING);
			String passwordString = GOSHelper.getSharePreStr(this, GosHttpApplication.USER_PASSWORD_STRING);
			String tokenString = GOSHelper.getSharePreStr(this, GosHttpApplication.USER_TOKEN_STRING);
			String userNameString = "00019140020246701";
			String userPasswordString = "sDFx3B"; 
			HttpResponse aHttpResponse = gosHttpOperation.invokerConsumeResponse(userNameString, userPasswordString, 
					openBankString, cardNumberString, accountNameString, amountString, cardIdString, passwordString, tokenString);
			HttpEntity entity = aHttpResponse.getEntity();
			InputStream is = entity.getContent();
			String reString = GOSHelper.convertStreamToString(is);
			if (reString == null || reString.equals("")) {
				loginResultHandler.sendEmptyMessage(0);
			}
			else {
				JSONObject jsonObject = JSON.parseObject(reString);
				String codeString = jsonObject.getInteger("code").toString();
				errorString = jsonObject.getString("data");
				if (codeString.equals("1")) {
					loginResultHandler.sendEmptyMessage(1);
				}
				else {
					loginResultHandler.sendEmptyMessage(0);
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
			loginResultHandler.sendEmptyMessage(0);
		} catch (BaseException e) {
			e.printStackTrace();
			loginResultHandler.sendEmptyMessage(0);
		} catch (IOException e) {
			e.printStackTrace();
			loginResultHandler.sendEmptyMessage(0);
		}
	}
    /**
     * 卡卡转账
     * */
    protected void cardCardTransfer(String inCardIdString, String amountString) {
    	try {
			String cardIdString = GOSHelper.getSharePreStr(this, GosHttpApplication.USER_NAME_STRING);
			String passwordString = GOSHelper.getSharePreStr(this, GosHttpApplication.USER_PASSWORD_STRING);
			String tokenString = GOSHelper.getSharePreStr(this, GosHttpApplication.USER_TOKEN_STRING); 
			HttpResponse aHttpResponse = gosHttpOperation.invokerCardCardTransferResponse(cardIdString, passwordString, inCardIdString, amountString, tokenString);
			HttpEntity entity = aHttpResponse.getEntity();
			InputStream is = entity.getContent();
			String reString = GOSHelper.convertStreamToString(is);
			if (reString == null || reString.equals("")) {
				loginResultHandler.sendEmptyMessage(0);
			}
			else {
				JSONObject jsonObject = JSON.parseObject(reString);
				String codeString = jsonObject.getInteger("code").toString();
				errorString = jsonObject.getString("data");
				if (codeString.equals("1")) {
					loginResultHandler.sendEmptyMessage(1);
				}
				else {
					errorString = "转账失败,余额不足";
					loginResultHandler.sendEmptyMessage(0);
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
			errorString = "转账失败";
			loginResultHandler.sendEmptyMessage(0);
		} catch (BaseException e) {
			e.printStackTrace();
			errorString = "转账失败";
			loginResultHandler.sendEmptyMessage(0);
		} catch (IOException e) {
			e.printStackTrace();
			errorString = "转账失败";
			loginResultHandler.sendEmptyMessage(0);
		}
	}
    /**
     * 卡关联
     * */
    protected void cardAssociation(String cardIdString) {
    	try {
    		if (cardIdString.equals("")) {
    			cardIdString = GOSHelper.getSharePreStr(this, GosHttpApplication.USER_NAME_STRING);
			}
			String tokenString = GOSHelper.getSharePreStr(this, GosHttpApplication.USER_TOKEN_STRING); 
			HttpResponse aHttpResponse = gosHttpOperation.invokerCardAssociationResponse(cardIdString, tokenString);
			HttpEntity entity = aHttpResponse.getEntity();
			InputStream is = entity.getContent();
			String reString = GOSHelper.convertStreamToString(is);
			if (reString == null || reString.equals("")) {
				cardAsNameString = "";
			}
			else {
				JSONObject jsonObject = JSON.parseObject(reString);
				String codeString = jsonObject.getInteger("code").toString();
				errorString = jsonObject.getString("data");
				if (codeString.equals("1")) {
					cardAsNameString = errorString;
				}
				else {
					cardAsNameString = "";
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
			cardAsNameString = "";
		} catch (BaseException e) {
			e.printStackTrace();
			cardAsNameString = "";
		} catch (IOException e) {
			e.printStackTrace();
			cardAsNameString = "";
		}
	}
    
    /**登录结果处理handler*/
	Handler loginResultHandler = new Handler()
	{
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				loginFailed();
				break;
			case 1:
				accessToView();
				break;
			case 2:
				accessToViewTwo();
				break;
			default:
				break;
			}
		}
	};
	/**登录失败的显示*/
	protected void loginFailed() {
		mSaveDialog.dismiss();
		Toast.makeText(this, errorString, Toast.LENGTH_LONG).show();
	}
	/**进入登录成功之后的界面*/
	protected void accessToView() {
		mSaveDialog.dismiss();
		if (errorString.equals("")) {
			errorString = "交易成功";
		}
		Toast.makeText(this, errorString, Toast.LENGTH_SHORT).show();
		/**开户行*/
		bankNameEditText.setText("");
		/**卡号*/
		cardNumberEditText.setText("");
		/**姓名*/
		accountNameEditText.setText("");
		/**金额*/
		moneyEditText.setText("");
		/**卡卡卡号*/
		twoCardNumberEditText.setText("");
		/**卡姓名*/
		twoAccountNameEditText.setText("");
		/**卡卡金额*/
		twoMoneyEditText.setText("");
		/**登录密码*/
		loginUpPasswordEditText.setText("");
		/**登录密码*/
		loginUpPasswordOneEditText.setText("");
	}
	/**卡关联验证*/
	protected void accessToViewTwo() {
		mSaveDialog.dismiss();
		Toast.makeText(this, "姓名不一致不能操作", Toast.LENGTH_SHORT).show();
	}
	
}
