package com.gov.nzjcy;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Types;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gov.nzjcy.entity.AskForOpenEntity;
import com.gov.nzjcy.exception.BaseException;
import com.gov.nzjcy.exception.ParseException;
import com.gov.nzjcy.helper.GOSHelper;
import com.gov.nzjcy.httpoperation.GosHttpApplication;
import com.gov.nzjcy.testutil.TestNetPort;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class AskForOpenActivity extends BaseActivity {
	/**打印标示*/
	private String	TAG = "AskForOpenActivity";
	/** head 头部 的左侧菜单 按钮*/
	private LinearLayout	top_left_Layout;
	/** head 头部 的右侧菜单 按钮*/
	private ImageButton		top_right_headImageButton;
	/**head的标题*/
	private TextView		topTitleTextView;
	/**tabhost的定义*/
	private TabHost 		tabHost;
	/**提交申请linelayout*/
	private ScrollView		topAskForOpenScrollView;
	/**申请结果查询*/
	private	LinearLayout	askForOpenQueryLineLayout;
	/**当前选中的项*/
	private int				currentSelectItem = 0;//当前选中的项
	/**申请公开事项的类别数组*/
	private String[] openActionTypes = new String[]{"案件诉讼文书、技术性鉴定材料","对犯罪嫌疑人延长羁押期限或者变更强制措施的办理情况、理由和依据", 
			"控告申诉案件的办理情况、理由和依据" };
	/**信息的通知方式*/
	private String[] infoNotifications = new String[]{"纸质版", "电子版", "口头"};
	/**信息的获取方式*/
	private String[] infoGetTypeStrings = new String[]{"自行领取", "邮寄", "电子邮件"};
	private RadioOnClick OnClick = new RadioOnClick(0);
	
	/**姓名*/
	private EditText			openManNameEditText;
	/**经常居住地或工作单位*/
	private EditText			openWorkAddressEditText;
	/**身份证号或组织机构代码证*/
	private EditText			openIDEditText;
	/**性别*/
	private RadioGroup			openSexRadioGroup;
	/**电子邮箱*/
	private EditText			openEmailEditText;
	/**手机号码*/
	private EditText			openPhoneNumberEditText;
	/**固定电话*/
	private EditText			openTelphoneNumberEditText;
	/**邮寄地址*/
	private EditText			openMailAddressEditText;
	/**案件诉讼文书、技术鉴定材料*/
	private TextView			openApplyTypeTextView;
	/**申请内容*/
	private EditText			openApplyContextEditText;
	/**用途*/
	private EditText			openPurposeEditText;
	/**信息通知方式*/
	private TextView			openInfoNotificatonTextView;
	/**信息的获取方式*/
	private TextView			openInfoGetTypeTextView;
	/**提交按钮*/
	private Button				openOneCommitButton;
	
	/**性别选择结果*/
	private String				sexString = "1";
	
	private String				numberString = "";
	
	/**申请编号*/
	private EditText			queryCommitedApplyEditText;
	/**查询按钮*/
	private Button				openQueryButton;
	/**查询结果*/
	private String				queryResultString;
	

	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_askforopen);
		
		String nodeString = "依申请公开栏目须知："
				+ "\n1、按要求填写检察信息公开申请表，申请人身份、联系方式等要真实有效，所申请的检察信息内容描述清楚，每个申请表只申请一项检察信息。"
				+ "\n2、申请同一检察信息内容，成功递交一次均可，请不要多次重复递交。"
				+ "\n3、网上申请一定要保存好系统反馈的查询码，并及时查询办理状态及反馈信息";
		
		new AlertDialog.Builder(this).setTitle("使用须知").
		setMessage(nodeString).
		setPositiveButton("确定", null).show();
		
		initView();
		openOneCommitButton = (Button)findViewById(R.id.openOneCommitButton);
		openOneCommitButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isEmpty();
			}
		});
	}
	//初始化界面
	protected void initView() {
		top_left_Layout = (LinearLayout)findViewById(R.id.leftPopLinearLayout);//左边的返回按钮
		topTitleTextView = (TextView)findViewById(R.id.topTitleTextview);
		topTitleTextView.setText("依申请公开项");//标题
		top_right_headImageButton = (ImageButton)findViewById(R.id.loginImageButton);//用户登录按钮
		//左边返回按钮的点击事件
		top_left_Layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AskForOpenActivity.this.finish();
			}
		});
		//隐藏右边的按钮
		top_right_headImageButton.setVisibility(View.INVISIBLE);
		//右边登录按钮的点击事件
//			top_right_headImageButton.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//
//				}
//			});
		initTabHost();
		topAskForOpenScrollView = (ScrollView)findViewById(R.id.topAskForOpenScrollView);
		askForOpenQueryLineLayout = (LinearLayout)findViewById(R.id.askForOpenQueryLineLayout);
		initAllInputBox();
	}
	//初始所有的输入框
	protected void initAllInputBox() {
		openManNameEditText = (EditText)findViewById(R.id.openManNameEditText);
		openWorkAddressEditText = (EditText)findViewById(R.id.openWorkAddressEditText);
		openIDEditText = (EditText)findViewById(R.id.openIDEditText);
		openSexRadioGroup = (RadioGroup)findViewById(R.id.openSexRadioGroup);
		//绑定一个匿名监听器
		openSexRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() { 
		    @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                 // TODO Auto-generated method stub
                 //获取变更后的选中项的ID
                 int radioButtonId = arg0.getCheckedRadioButtonId();
                 //根据ID获取RadioButton的实例
                 RadioButton rb = (RadioButton)findViewById(radioButtonId);
                 //更新文本内容，以符合选中项
                 Log.v(TAG, rb.getText().toString()+rb.getTag().toString());
                 sexString = rb.getTag().toString();
             }
        });
		
		
		openEmailEditText = (EditText)findViewById(R.id.openEmailEditText);
		openPhoneNumberEditText = (EditText)findViewById(R.id.openPhoneNumberEditText);
		openTelphoneNumberEditText = (EditText)findViewById(R.id.openTelphoneNumberEditText);
		openMailAddressEditText = (EditText)findViewById(R.id.openMailAddressEditText);

		openApplyContextEditText = (EditText)findViewById(R.id.openApplyContextEditText);
		openPurposeEditText = (EditText)findViewById(R.id.openPurposeEditText);
		
		openApplyTypeTextView = (TextView)findViewById(R.id.openApplyTypeTextView);
		openApplyTypeTextView.setOnClickListener(new RadioClickListener());
		
		openInfoNotificatonTextView = (TextView)findViewById(R.id.openInfoNotificatonTextView);
		openInfoNotificatonTextView.setOnClickListener(new RadioClickListener());
		
		openInfoGetTypeTextView = (TextView)findViewById(R.id.openInfoGetTypeTextView);
		openInfoGetTypeTextView.setOnClickListener(new RadioClickListener());
		
		//查询的控件
		queryCommitedApplyEditText = (EditText)findViewById(R.id.queryCommitedApplyEditText);
		openQueryButton = (Button)findViewById(R.id.openQueryButton);
		openQueryButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final String queryNumberString = queryCommitedApplyEditText.getText().toString().trim();
				if (queryNumberString.equals("") || queryNumberString == null) {
					Toast.makeText(AskForOpenActivity.this, "申请编号不能为空", Toast.LENGTH_SHORT).show();
				}
				else {
					new Thread()
					{
						public void run() {
							queryAskForOpen(queryNumberString);
						}
					}.start();
				}
			}
		});
	}
	//判断是否为空
	protected void isEmpty() {
		String userIDString = GOSHelper.getSharePreStr(AskForOpenActivity.this, GosHttpApplication.USER_ID_STRING);
		if (userIDString.equals("")) {
			Toast.makeText(this, "请登录后再进行操作", Toast.LENGTH_SHORT).show();
		}
		else if ((openManNameEditText.getText().toString().trim() == null) || (openManNameEditText.getText().toString().trim().equals(""))) {
			Toast.makeText(this, "姓名或法人名称不能为空", Toast.LENGTH_SHORT).show();
		}
		else if ((openWorkAddressEditText.getText().toString().trim() == null) || (openManNameEditText.getText().toString().trim().equals(""))) {
			Toast.makeText(this, "经常居住地或工作单位不能为空", Toast.LENGTH_SHORT).show();
		} 
		else if ((openIDEditText.getText().toString().trim() == null) || (openIDEditText.getText().toString().trim().equals(""))) {
			Toast.makeText(this, "身份证号码或组织机构代码证不能为空", Toast.LENGTH_SHORT).show();
		}
		else if ((openPhoneNumberEditText.getText().toString().trim() == null) || (openPhoneNumberEditText.getText().toString().trim().equals(""))) {
			Toast.makeText(this, "手机号码不能为空", Toast.LENGTH_SHORT).show();
		}
		else if ((openMailAddressEditText.getText().toString().trim() == null) || (openMailAddressEditText.getText().toString().trim().equals(""))) {
			Toast.makeText(this, "邮寄地址不能为空", Toast.LENGTH_SHORT).show();
		}
		else if ((openPurposeEditText.getText().toString().trim() == null) || (openPurposeEditText.getText().toString().trim().equals(""))) {
			Toast.makeText(this, "用途不能为空", Toast.LENGTH_SHORT).show();
		}
		else {
			new Thread()
			{
				public void run() {
					commitAskForOpen(generateAskForOpenEntity());
				}
			}.start();
		}
	}
	//
	//生成askforopenEntity对象
	protected AskForOpenEntity generateAskForOpenEntity() {
		AskForOpenEntity askForOpenEntity = new AskForOpenEntity();
		String userIDString = GOSHelper.getSharePreStr(AskForOpenActivity.this, GosHttpApplication.USER_ID_STRING);
		if (userIDString.equals("")) {
			userIDString = "0";
		}
		askForOpenEntity.setUserID(userIDString);
		askForOpenEntity.setRealName(openManNameEditText.getText().toString().trim());
		askForOpenEntity.setAddress(openWorkAddressEditText.getText().toString().trim());
		askForOpenEntity.setIDcardNumber(openIDEditText.getText().toString().trim());
		askForOpenEntity.setSex(sexString);
		askForOpenEntity.setEmail(openEmailEditText.getText().toString().trim());
		askForOpenEntity.setMobile(openPhoneNumberEditText.getText().toString().trim());
		askForOpenEntity.setTel(openTelphoneNumberEditText.getText().toString().trim());
		askForOpenEntity.setMailingAddress(openMailAddressEditText.getText().toString().trim());
		askForOpenEntity.setConDescription(openApplyTypeTextView.getText().toString().trim());
		askForOpenEntity.setDescription(openApplyContextEditText.getText().toString().trim());
		askForOpenEntity.setUsefulDescription(openPurposeEditText.getText().toString().trim());
		askForOpenEntity.setMedium(openInfoNotificatonTextView.getText().toString().trim());
		askForOpenEntity.setGetMode(openInfoGetTypeTextView.getText().toString().trim());
		return askForOpenEntity;
	}
	//提交事件
	protected void commitAskForOpen(AskForOpenEntity askForOpenEntity) {
		try {
			HttpResponse aString = gosHttpOperation.invokerAskForOpenResponse(askForOpenEntity);
			HttpEntity entity = aString.getEntity();
			InputStream is = entity.getContent();
			String reString = GOSHelper.convertStreamToString(is);
		
			if (reString == null || reString.equals("")) {
				
			}
			else {
				JSONObject jsonObject = JSON.parseObject(reString);
				String stateString = jsonObject.getString("state");
				if (stateString.equals("1")) {
					Log.v("测试依申请公开项", "测试依申请公开项成功");
					JSONArray responeArray = jsonObject.getJSONArray("responseList");
					
					if (responeArray.size() > 0) {
						for (int i = 0; i < responeArray.size(); i++) {
							JSONObject oneObject = responeArray.getJSONObject(i);
							numberString = oneObject.getString("number").trim();
							alertHandler.sendEmptyMessage(1);
						}
					}
				}
				else
				{
					Log.v("测试依申请公开项", "测试依申请公开项失败");
					alertHandler.sendEmptyMessage(0);
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (BaseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//
	Handler alertHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 0:
				alertMsg("0");
				break;
			case 1:
				alertMsg("1");
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};
	public void alertMsg(String typeString) {
		if (typeString.equals("0")) {
			new AlertDialog.Builder(this).setTitle("温馨提示").
			setMessage("提交失败!请重新提交。").
			setPositiveButton("确定", null).show();
		}
		else {
			emptyInput();
			new AlertDialog.Builder(this).setTitle("温馨提示").
			setMessage("提交成功!申请编号"+numberString+",可使用此编号查询处理状态").
			setPositiveButton("确定", null).show();
		}
	}
	/**置空*/
	protected void emptyInput() {
		openManNameEditText.setText("");
		openWorkAddressEditText.setText("");
		openIDEditText.setText("");
		openEmailEditText.setText("");
		openPhoneNumberEditText.setText("");
		openTelphoneNumberEditText.setText("");
		openMailAddressEditText.setText("");
		openApplyContextEditText.setText("");
		openPurposeEditText.setText("");
	}
	
	
	/**查询依申请公开项*/
	protected void queryAskForOpen(String numberString) {
		try {			
			HttpResponse aString = gosHttpOperation.invokerQueryAskForOpenResponse(numberString);
			HttpEntity entity = aString.getEntity();
			InputStream is = entity.getContent();
			String reString = GOSHelper.convertStreamToString(is);
			
			
			if (reString == null || reString.equals("")) {
				
			}
			else {
				JSONObject jsonObject = JSON.parseObject(reString);
				String stateString = jsonObject.getString("state");
				if (stateString.equals("1")) {
					Log.v("测试查询申请公开项", "测试查询申请公开项成功");
					JSONArray responeArray = jsonObject.getJSONArray("responseList");
					JSONObject oneObjects = responeArray.getJSONObject(0);
					
					if (oneObjects == null) {
						queryResultHandler.sendEmptyMessage(0);
					}
					else {
						if (responeArray.size() > 0) {
							for (int i = 0; i < responeArray.size(); i++) {
								JSONObject oneObject = responeArray.getJSONObject(i);
								
								String shoulibumenString = oneObject.getString("workBumen") == null?"":oneObject.getString("workBumen").trim();
								String shoulirenString = oneObject.getString("worker") == null?"":oneObject.getString("worker").trim();
								String shoulicontactString = oneObject.getString("workContact") == null?"":oneObject.getString("workContact").trim();
								String chengbanbumenString = oneObject.getString("chengbanbumen") == null?"":oneObject.getString("chengbanbumen").trim();
								String chengbanrenString = oneObject.getString("chengbaner") == null?"":oneObject.getString("chengbaner").trim();
								String chengbancontactString = oneObject.getString("number") == null?"":oneObject.getString("number").trim();
								String chuliresultString = oneObject.getString("workDescription") == null?"":oneObject.getString("workDescription").trim();
								
								if (shoulibumenString.equals("") && shoulirenString.equals("") && shoulicontactString.equals("") && chengbanbumenString.equals("") && chengbanrenString.equals("") && chengbancontactString.equals("") && chuliresultString.equals("")) {
									queryResultString = "该申请还没有处理，请耐心等待!";
								}
								else {
									queryResultString = "<br>受理部门</br>"+shoulibumenString + "<br>受理人</br>" + shoulirenString  + "<br>受理人联系方式</br>" +
											shoulicontactString + "<br>承办部门</br>" + chengbanbumenString + "<br>承办人</br>" + chengbanrenString +
											  "<br>承办人联系方式</br>" +chengbancontactString + "<br>处理结果</br>" + chuliresultString;
								}
							}
							queryResultHandler.sendEmptyMessage(1);
						}
						
					}
				}
				else
				{
					Log.v("查询申请公开项", "查询申请公开项失败");
					queryResultHandler.sendEmptyMessage(0);
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (BaseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//
	Handler queryResultHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 0:
				queryAlertMsg("0");
				break;
			case 1:
				queryAlertMsg("1");
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};
	
	public void queryAlertMsg(String typeString) {
		if (typeString.equals("0")) {
			new AlertDialog.Builder(this).setTitle("处理结果").
			setMessage("没有结果!").
			setPositiveButton("确定", null).show();
		}
		else {
//			new AlertDialog.Builder(this).setTitle("处理结果").
//			setMessage(queryResultString).
//			setPositiveButton("确定", null).show();
			Intent oneIntent = new Intent();
			oneIntent.setClass(AskForOpenActivity.this, QueryHandleResult.class);
			oneIntent.putExtra("queryResult", queryResultString);
			AskForOpenActivity.this.startActivity(oneIntent);
		}
	}
	
	
	//
	class RadioClickListener implements OnClickListener {
		   @Override
		   public void onClick(View v) {
			   switch (v.getId()) {
					case R.id.openApplyTypeTextView:
						currentSelectItem = 0;
						AlertDialog ad =new AlertDialog.Builder(AskForOpenActivity.this).setTitle("")
					    .setSingleChoiceItems(openActionTypes, OnClick.getIndex(),new RadioOnClick(0)).create();
					    ad.show();
						break;
					case R.id.openInfoNotificatonTextView:
						currentSelectItem = 1;
						AlertDialog adOne =new AlertDialog.Builder(AskForOpenActivity.this).setTitle("")
					    .setSingleChoiceItems(infoNotifications, OnClick.getIndex(),new RadioOnClick(0)).create();
						adOne.show();
						break;
					case R.id.openInfoGetTypeTextView:
						currentSelectItem = 2;
						AlertDialog adTwo =new AlertDialog.Builder(AskForOpenActivity.this).setTitle("")
					    .setSingleChoiceItems(infoGetTypeStrings, OnClick.getIndex(),new RadioOnClick(0)).create();
						adTwo.show();
						break;
					default:
						break;
				}
		  }
	}
	//
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
			   		openApplyTypeTextView.setText(openActionTypes[index]);
			   		break;
			   	case 1:
			   		//Toast.makeText(AskForOpenActivity.this, "您已经选择了 " +  ":" + infoNotifications[index], Toast.LENGTH_LONG).show();
			   		openInfoNotificatonTextView.setText(infoNotifications[index]);
			   		break;
			   	case 2:
			   		//Toast.makeText(AskForOpenActivity.this, "您已经选择了 " +  ":" + infoGetTypeStrings[index], Toast.LENGTH_LONG).show();
			   		openInfoGetTypeTextView.setText(infoGetTypeStrings[index]);
			   		break;
			    default:
				    break;
			}
		   dialog.dismiss();
		   
	   }
	 }
	
	//初始化tabhost
	protected void initTabHost() {
		tabHost = (TabHost) findViewById(R.id.topAskForOpenTabHost);  
        tabHost.setup();  
        TabWidget tabWidget = tabHost.getTabWidget();  
        
        tabHost.addTab(tabHost.newTabSpec("tab1")  
                .setIndicator("提交新的申请", getResources().getDrawable(R.drawable.ic_launcher))  
                .setContent(R.id.askForOpenView1));   
          
        tabHost.addTab(tabHost.newTabSpec("tab2")  
                .setIndicator("申请结果查询")  
                .setContent(R.id.askForOpenView2));  
          
        final int tabs = tabWidget.getChildCount();  
        Log.v(TAG, "***tabWidget.getChildCount() : " + tabs);  
        
        //注意这个就是改变Tabhost默认样式的地方，一定将这部分代码放在上面这段代码的下面，不然样式改变不了
        updateTab(tabHost);
//        for (int i =0; i < tabWidget.getChildCount(); i++) {  
//         //修改Tabhost高度和宽度
//         tabWidget.getChildAt(i).getLayoutParams().height = 60;  
//         tabWidget.getChildAt(i).getLayoutParams().width = 65;
//         //修改显示字体大小
//         TextView tv = (TextView) tabWidget.getChildAt(i).findViewById(android.R.id.title);
//         tv.setTextSize(15);
//         //tv.setTextColor(this.getResources().getColorStateList(android.R.color.white));
//        }
        
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
			   		 	topAskForOpenScrollView.setVisibility(View.VISIBLE);
			   		 	askForOpenQueryLineLayout.setVisibility(View.INVISIBLE);
			   		 break;
			   	 case 1:
			   		 	topAskForOpenScrollView.setVisibility(View.INVISIBLE);
			   		 	askForOpenQueryLineLayout.setVisibility(View.VISIBLE);
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
	
}
