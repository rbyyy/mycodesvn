package com.gov.nzjcy;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gov.nzjcy.entity.OnlineReportEntity;
import com.gov.nzjcy.exception.BaseException;
import com.gov.nzjcy.exception.ParseException;
import com.gov.nzjcy.helper.GOSHelper;
import com.gov.nzjcy.httpoperation.GosHttpApplication;
import com.gov.nzjcy.testutil.TestNetPort;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost.OnTabChangeListener;

@SuppressLint("HandlerLeak")
public class OnlineReportActivity extends BaseActivity {
	
	/**log显示信息标志*/
	private String TAG = "OnlineReportActivity";
	/** head 头部 的左侧菜单 按钮*/
	private LinearLayout	top_left_Layout;
	/** head 头部 的右侧菜单 按钮*/
	private ImageButton		top_right_headImageButton;
	/**head的标题*/
	private TextView		topTitleTextView;
	/**第一个选项*/
	private ScrollView		onlineReportScrollView;
	/**第二个选项*/
	private LinearLayout	onlineReportQueryLineLayout;
	/**tabhost的定义*/
	private TabHost 		tabHost;
	/**举报人姓名*/
	private EditText        onlineReportUserNameEditText;
	/**举报人身份证号*/
	private EditText		onlineReportIDEditText;
	/**举报人手机号码*/
	private EditText		onlineReportPhoneNumberEditText;
	/**举报人固定电话*/
	private EditText		onlineReportTelphoneNumberEditText;
	/**举报人家庭地址*/
	private EditText		onlineReportHomeAddressEditText;
	/**举报人电子邮件*/
	private EditText		onlineReportEmailEditText;
	/**被举报人犯罪类型*/
	private RadioGroup		onlineReportRadioOneGroup;
	/**被举报人犯罪类型结果*/
	private String			onlineReportCrimeTypeResultString = "0";
	/**被举报人姓名*/
	private EditText		onlineReportIllegalManNameEditText;
	/**被举报人性别*/
	private RadioGroup		onlineReportSexRadioGroup;
	/**被举报人性别结果*/
	private String			onlineReportSexResultString = "1";
	/**被举报人单位*/
	private EditText		onlineReportDepartmentEditText;
	/**被举报人职级*/
	private	EditText		onlineReportDutyLevelEditText;
	/**被举报人职务*/
	private	EditText		onlineReportDutyEditText;
	/**被举报人住址*/
	private	EditText		onlineReportOneHomeAddressEditText;
	/**举报案由*/
	private	EditText		onlineReportSummaryEditText;
	/**举报内容*/
	private EditText		onlineReportContentEditText;
	/**提交按钮*/
	private Button			onlineReportCommitButton;
	/**编号*/
	private String				numberString = "";
    /**须知*/
    private String				dataString = "";
    /**举报编号*/
    private EditText		 onlineReportNumberEditText;
    /**查询按钮*/
    private Button			 onlineReportQueryButton;
    /**查询结果*/
	private String				queryResultString;
    

	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_onlinereport);
		new Thread()
		{
			public void run() {
				queryGetNotes("Jubao_xuzhi");
			}
		}.start();
		initView();
		onlineReportCommitButton = (Button)findViewById(R.id.onlineReportCommitButton);
		onlineReportCommitButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isEmpty();
			}
		});
	}
	/** 查询使用须知*/
	protected void queryGetNotes(String modelNameString) {
		try {			
			HttpResponse aString = gosHttpOperation.invokerQueryUseRulesResponse(modelNameString);
			HttpEntity entity = aString.getEntity();
			InputStream is = entity.getContent();
			String reString = GOSHelper.convertStreamToString(is);
		
			if (reString == null || reString.equals("")) {
				
			}
			else {
				JSONObject jsonObject = JSON.parseObject(reString);
				String stateString = jsonObject.getString("state");
				if (stateString.equals("1")) {
					JSONArray responeArray = jsonObject.getJSONArray("responseList");
					if (responeArray.size() > 0) {
						for (int i = 0; i < responeArray.size(); i++) {
							JSONObject oneObject = responeArray.getJSONObject(i);
							dataString = oneObject.getString("data").trim();
							notificationHandler.sendEmptyMessage(1);
						}
					}
				}
				else
				{
					notificationHandler.sendEmptyMessage(0);
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
	/**须知提醒*/
	@SuppressLint("HandlerLeak")
	Handler notificationHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 0:
				alertNotificationMsg("0");
				break;
			case 1:
				alertNotificationMsg("1");
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};
	/**须知显示*/
	public void alertNotificationMsg(String typeString) {
		if (typeString.equals("0")) {
			new AlertDialog.Builder(this).setTitle("使用须知").
			setMessage("获取使用须知失败").
			setPositiveButton("确定", null).show();
		}
		else {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  //取得DocumentBuilderFactory实例  
	        DocumentBuilder builder;
			try {
				//从factory获取DocumentBuilder实例  
				builder = factory.newDocumentBuilder();
				ByteArrayInputStream in_nocode = new ByteArrayInputStream(dataString.getBytes("utf-16"));
		        Document doc;
				doc = builder.parse(in_nocode);
				 //解析输入流 得到Document实例  
		        Element rootElement = doc.getDocumentElement();  
		        NodeList items = rootElement.getElementsByTagName("Description"); 
		        Node item = items.item(0);
		        NodeList properties = item.getChildNodes();  
                Node property = properties.item(0);  
                String nodeString = property.getNodeValue().replace("<p>", "").
                		replace("</p>", "").replace("&nbsp;", "");
                
                //String nodeName = property.getNodeName();
				new AlertDialog.Builder(this).setTitle("使用须知").
				setMessage(nodeString).
				setPositiveButton("确定", null).show();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
		}
	}
	//初始化界面
	protected void initView() {
		top_left_Layout = (LinearLayout)findViewById(R.id.leftPopLinearLayout);//左边的返回按钮
		topTitleTextView = (TextView)findViewById(R.id.topTitleTextview);
		topTitleTextView.setText("网上举报");//标题
		top_right_headImageButton = (ImageButton)findViewById(R.id.loginImageButton);//用户登录按钮
		//左边返回按钮的点击事件
		top_left_Layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				OnlineReportActivity.this.finish();
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
		onlineReportScrollView = (ScrollView)findViewById(R.id.onlineReportScrollView);
		onlineReportQueryLineLayout = (LinearLayout)findViewById(R.id.onlineReportQueryLineLayout);
		initTabHost();
		initAllInputBox();
	}
	/**设置所有的输入框*/
	protected void initAllInputBox()
	{
		onlineReportUserNameEditText = (EditText)findViewById(R.id.onlineReportUserNameEditText);
		onlineReportIDEditText = (EditText)findViewById(R.id.onlineReportIDEditText);
		onlineReportPhoneNumberEditText = (EditText)findViewById(R.id.onlineReportPhoneNumberEditText);
		onlineReportTelphoneNumberEditText = (EditText)findViewById(R.id.onlineReportTelphoneNumberEditText);
		onlineReportHomeAddressEditText = (EditText)findViewById(R.id.onlineReportHomeAddressEditText);
		onlineReportEmailEditText = (EditText)findViewById(R.id.onlineReportEmailEditText);
		
		onlineReportRadioOneGroup = (RadioGroup)findViewById(R.id.onlineReportRadioOneGroup);
		//绑定一个匿名监听器
		onlineReportRadioOneGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() { 
		    @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                 // TODO Auto-generated method stub
                 //获取变更后的选中项的ID
                 int radioButtonId = arg0.getCheckedRadioButtonId();
                 //根据ID获取RadioButton的实例
                 RadioButton rb = (RadioButton)findViewById(radioButtonId);
                 //更新文本内容，以符合选中项
                 Log.v(TAG, rb.getText().toString()+rb.getTag().toString());
                 onlineReportCrimeTypeResultString = rb.getTag().toString();
             }
        });
		
		
		onlineReportIllegalManNameEditText = (EditText)findViewById(R.id.onlineReportIllegalManNameEditText);
		onlineReportSexRadioGroup = (RadioGroup)findViewById(R.id.onlineReportSexRadioGroup);
		//绑定一个匿名监听器
		onlineReportSexRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() { 
		    @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                 // TODO Auto-generated method stub
                 //获取变更后的选中项的ID
                 int radioButtonId = arg0.getCheckedRadioButtonId();
                 //根据ID获取RadioButton的实例
                 RadioButton rb = (RadioButton)findViewById(radioButtonId);
                 //更新文本内容，以符合选中项
                 Log.v(TAG, rb.getText().toString()+rb.getTag().toString());
                 onlineReportSexResultString = rb.getTag().toString();
             }
        });
		
		
		onlineReportDepartmentEditText = (EditText)findViewById(R.id.onlineReportDepartmentEditText);
		onlineReportDutyLevelEditText = (EditText)findViewById(R.id.onlineReportDutyLevelEditText);
		onlineReportDutyEditText = (EditText)findViewById(R.id.onlineReportDutyEditText);

		onlineReportOneHomeAddressEditText = (EditText)findViewById(R.id.onlineReportOneHomeAddressEditText);
		onlineReportSummaryEditText = (EditText)findViewById(R.id.onlineReportSummaryEditText);
		
		onlineReportContentEditText = (EditText)findViewById(R.id.onlineReportContentEditText);
		
		//查询
		onlineReportNumberEditText = (EditText)findViewById(R.id.onlineReportNumberEditText);
		onlineReportQueryButton = (Button)findViewById(R.id.onlineReportQueryButton);
		onlineReportQueryButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final String queryNumberString = onlineReportNumberEditText.getText().toString().trim();
				if (queryNumberString.equals("") || queryNumberString == null) {
					Toast.makeText(OnlineReportActivity.this, "申请编号不能为空", Toast.LENGTH_SHORT).show();
				}
				else {
					new Thread()
					{
						public void run() {
							queryOnlineReport(queryNumberString);
						}
					}.start();
				}
			}
		});
		
	}
	//判断是否为空
	protected void isEmpty() {
		String userIDString = GOSHelper.getSharePreStr(OnlineReportActivity.this, GosHttpApplication.USER_ID_STRING);
		if (userIDString.equals("")) {
			Toast.makeText(this, "请登录后再进行操作", Toast.LENGTH_SHORT).show();
		}
		else if ((onlineReportUserNameEditText.getText().toString().trim() == null) || (onlineReportUserNameEditText.getText().toString().trim().equals(""))) {
			Toast.makeText(this, "举报人姓名不能为空", Toast.LENGTH_SHORT).show();
		}
		else if ((onlineReportIDEditText.getText().toString().trim() == null) || (onlineReportIDEditText.getText().toString().trim().equals(""))) {
			Toast.makeText(this, "举报人身份证号码不能为空", Toast.LENGTH_SHORT).show();
		} 
		else if ((onlineReportIllegalManNameEditText.getText().toString().trim() == null) || (onlineReportIllegalManNameEditText.getText().toString().trim().equals(""))) {
			Toast.makeText(this, "被举报人姓名不能为空", Toast.LENGTH_SHORT).show();
		}
		else if ((onlineReportDepartmentEditText.getText().toString().trim() == null) || (onlineReportDepartmentEditText.getText().toString().trim().equals(""))) {
			Toast.makeText(this, "被举报人部门不能为空", Toast.LENGTH_SHORT).show();
		}
		else if ((onlineReportSummaryEditText.getText().toString().trim() == null) || (onlineReportSummaryEditText.getText().toString().trim().equals(""))) {
			Toast.makeText(this, "举报案由不能为空", Toast.LENGTH_SHORT).show();
		}
		else if ((onlineReportContentEditText.getText().toString().trim() == null) || (onlineReportContentEditText.getText().toString().trim().equals(""))) {
			Toast.makeText(this, "举报内容不能为空", Toast.LENGTH_SHORT).show();
		}
		else {
			new Thread()
			{
				public void run() {
					setOnlineReport(generateOnlineReportEntity());
				}
			}.start();
		}
	}
	/**生成网上举报对象*/
	protected OnlineReportEntity generateOnlineReportEntity() {
		OnlineReportEntity onlineReportEntity = new OnlineReportEntity();
		String userIDString = GOSHelper.getSharePreStr(OnlineReportActivity.this, GosHttpApplication.USER_ID_STRING);
		if (userIDString.equals("")) {
			userIDString = "0";
		}
		onlineReportEntity.setUserIdString(userIDString);
		onlineReportEntity.setOnlineReportUserName(onlineReportUserNameEditText.getText().toString().trim());
		onlineReportEntity.setOnlineReportIDCardNumber(onlineReportIDEditText.getText().toString().trim());
		onlineReportEntity.setOnlineReportMobile(onlineReportPhoneNumberEditText.getText().toString().trim());
		onlineReportEntity.setOnlineReportTel(onlineReportTelphoneNumberEditText.getText().toString().trim());
		onlineReportEntity.setOnlineReportAddress(onlineReportHomeAddressEditText.getText().toString().trim());
		onlineReportEntity.setOnlineReportEmail(onlineReportEmailEditText.getText().toString().trim());
		onlineReportEntity.setBJB_Person_Type(onlineReportCrimeTypeResultString);
		onlineReportEntity.setBJB_Person_Realname(onlineReportIllegalManNameEditText.getText().toString().trim());
		onlineReportEntity.setBJB_Person_Sex(onlineReportSexResultString);
		onlineReportEntity.setBJB_Person_CompanyNameOrBumen(onlineReportDepartmentEditText.getText().toString().trim());
		onlineReportEntity.setBJB_Person_Zhiji(onlineReportDutyLevelEditText.getText().toString().trim());
		onlineReportEntity.setBJB_Person_Zhiwu(onlineReportDutyEditText.getText().toString().trim());
		onlineReportEntity.setBJB_Person_Address(onlineReportOneHomeAddressEditText.getText().toString().trim());
		onlineReportEntity.setBJB_Person_Anyou(onlineReportSummaryEditText.getText().toString().trim());
		onlineReportEntity.setBJB_Description(onlineReportContentEditText.getText().toString().trim());
		
		return onlineReportEntity;
	}
	/**设置网上举报*/
	protected void setOnlineReport(OnlineReportEntity onlineReportEntity) {
		try {	
			HttpResponse aString = gosHttpOperation.invokerSetReportResponse(onlineReportEntity);
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
		onlineReportUserNameEditText.setText("");
		onlineReportIDEditText.setText("");
		onlineReportPhoneNumberEditText.setText("");
		onlineReportTelphoneNumberEditText.setText("");
		onlineReportHomeAddressEditText.setText("");
		onlineReportEmailEditText.setText("");
		
		
		
		onlineReportIllegalManNameEditText.setText("");
		
		onlineReportDepartmentEditText.setText("");
		onlineReportDutyLevelEditText.setText("");
		onlineReportDutyEditText.setText("");
		onlineReportOneHomeAddressEditText.setText("");
		onlineReportSummaryEditText.setText("");
		onlineReportContentEditText.setText("");
	}
	
	/**查询网上举报*/
	protected void queryOnlineReport(String numberString) {
		try {			
			HttpResponse aString = gosHttpOperation.invokerQueryOnlineReportResponse(numberString);
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
					
					if (responeArray.size() > 0) {
						JSONObject oneObjects = responeArray.getJSONObject(0);
						if (oneObjects == null) {
							queryResultHandler.sendEmptyMessage(0);
						}
						else {
							for (int i = 0; i < responeArray.size(); i++) {
								JSONObject oneObject = responeArray.getJSONObject(i);
								String dateString = oneObject.getString("dateandtime") == null?"":oneObject.getString("dateandtime").trim();
								String shoulibumenString = oneObject.getString("workBumen") == null?"":oneObject.getString("workBumen").trim();
								String shoulirenString = oneObject.getString("worker") == null?"":oneObject.getString("worker").trim();
								String shoulicontactString = oneObject.getString("workContact") == null?"":oneObject.getString("workContact").trim();
								String chuliresultString = oneObject.getString("workDescription") == null?"":oneObject.getString("workDescription").trim();
//								queryResultString = "<br>受理部门</br>"+ shoulibumenString + "<br>受理人</br>" + shoulirenString + "<br>受理人联系方式</br>" +
//								shoulicontactString + "<br>处理结果</br>" + chuliresultString;//"提交日期\n" + dateString+ "\n" + 
								if (shoulibumenString.equals("") && shoulirenString.equals("") && shoulicontactString.equals("") && chuliresultString.equals("")) {
									queryResultString = "";
								}
								else {
									queryResultString = "<br>受理部门</br>"+ shoulibumenString + "<br>受理人</br>" + shoulirenString + "<br>受理人联系方式</br>" +
											shoulicontactString + "<br>处理结果</br>" + chuliresultString;
								}
							}
							queryResultHandler.sendEmptyMessage(1);
						}
					}
					else {
						queryResultHandler.sendEmptyMessage(0);
					}
				}
				else
				{
					Log.v("测试查询申请公开项", "测试查询申请公开项失败");
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
			if (queryResultString.equals("")) {
				new AlertDialog.Builder(this).setTitle("处理结果").
				setMessage("该信息还没有处理，请继续保持关注，谢谢！").
				setPositiveButton("确定", null).show();
			}
			else {
				Intent oneIntent = new Intent();
				oneIntent.setClass(OnlineReportActivity.this, QueryHandleResult.class);
				oneIntent.putExtra("queryResult", queryResultString);
				OnlineReportActivity.this.startActivity(oneIntent);
			}
		}
	}
	
	//初始化tabhost
	protected void initTabHost() {
		tabHost = (TabHost) findViewById(R.id.topOnlineReportTabHost);  
        tabHost.setup();  
        TabWidget tabWidget = tabHost.getTabWidget();  
        
        tabHost.addTab(tabHost.newTabSpec("tab1")  
                .setIndicator("提交新的举报", getResources().getDrawable(R.drawable.ic_launcher))  
                .setContent(R.id.onlineReportView1));   
          
        tabHost.addTab(tabHost.newTabSpec("tab2")  
                .setIndicator("举报结果查询")  
                .setContent(R.id.onlineReportView2));  
          
        final int tabs = tabWidget.getChildCount();  
        Log.v(TAG, "***tabWidget.getChildCount() : " + tabs);  
        
        //注意这个就是改变Tabhost默认样式的地方，一定将这部分代码放在上面这段代码的下面，不然样式改变不了
        updateTab(tabHost);
//		        for (int i =0; i < tabWidget.getChildCount(); i++) {  
//		         //修改Tabhost高度和宽度
//		         tabWidget.getChildAt(i).getLayoutParams().height = 60;  
//		         tabWidget.getChildAt(i).getLayoutParams().width = 65;
//		         //修改显示字体大小
//		         TextView tv = (TextView) tabWidget.getChildAt(i).findViewById(android.R.id.title);
//		         tv.setTextSize(15);
//		         //tv.setTextColor(this.getResources().getColorStateList(android.R.color.white));
//		        }
        
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
			   		 	onlineReportScrollView.setVisibility(View.VISIBLE);
			   		 	onlineReportQueryLineLayout.setVisibility(View.INVISIBLE);
			   		 break;
			   	 case 1:
			   		 	onlineReportScrollView.setVisibility(View.INVISIBLE);
			   		 	onlineReportQueryLineLayout.setVisibility(View.VISIBLE);
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
