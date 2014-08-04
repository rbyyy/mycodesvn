package com.gov.nzjcy;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gov.nzjcy.entity.OnlineBookEntity;
import com.gov.nzjcy.exception.BaseException;
import com.gov.nzjcy.exception.ParseException;
import com.gov.nzjcy.helper.GOSHelper;
import com.gov.nzjcy.httpoperation.GosHttpApplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
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

import org.w3c.dom.Document;  
import org.w3c.dom.Element;  
import org.w3c.dom.Node;  
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class OnlineBookActivity extends BaseActivity {
	/**log显示信息标志*/
	private String TAG = "OnlineBookActivity";
	/** head 头部 的左侧菜单 按钮*/
	private LinearLayout	top_left_Layout;
	/** head 头部 的右侧菜单 按钮*/
	private ImageButton		top_right_headImageButton;
	/**head的标题*/
	private TextView		topTitleTextView;
	/**tabhost的定义*/
	private TabHost 		tabHost;
	/**第一个界面*/
	private ScrollView		onlineBookScrollView;
	/**第二个界面*/
	private LinearLayout	onlineBookQueryLineLayout;
	
	//用来保存年月日：  
    private int mYear;  
    private int mMonth;  
    private int mDay;  
    //声明一个独一无二的标识，来作为要显示DatePicker的Dialog的ID：  
    static final int DATE_DIALOG_ID = 0; 
	
    /**申请人身份*/
    private RadioGroup			newOnlineBookRadioGroup;
    /**律师事务所名称*/
    private	EditText			lawFirmNameEditText;
    /**是否选择职业律师*/
    private CheckBox			regularLawyerCheckBox;
    /**是否选择法律援助中心指派*/
    private	CheckBox			assignLawyerCheckBox;
    /**是否需要回避*/
    private	CheckBox			needToAvoidCheckBox;
    /**姓名*/
    private EditText			onlineBookManNameEditText;
    /**身份证号*/
    private EditText			onlineBookManIDEditText;
    /**手机号码*/
    private EditText			onlineBookManPhoneEditText;
    /**被告人姓名*/
    private EditText			onlineBookDefendantNameEditText;
    /**性别*/
    private	RadioGroup			onlineBookSexRadioGroup;
    /**被告人身份证号码*/
    private EditText			onlineBookDefendantIDEditText;
    /**出生年月*/
    private TextView			onlineBookBirthdayTextView;
    /**检察院选择*/
    private TextView			courtSelectTextView;
    /**诉讼阶段*/
    private RadioGroup			onlineBookLawsuitSectionRadioGroup;
    /**强制措施*/
    private RadioGroup			onlineBookForceStepRadioGroup;
    /**案由*/
    private EditText			onlineBookBriefEditText;
    /**委托人姓名*/
    private EditText			onlineBookConsignorNameEditText;
    /**委托人身份证号*/
    private EditText			onlineBookConsignorIdEditText;
    /**申请阅卷*/
    private CheckBox			onlineBookTypeOne;
    /**申请会见承办人*/
    private CheckBox			onlineBookTypeTwo;
    /**其它*/
    private	CheckBox			onlineBookTypeThree;
    /**其它预约内容*/
    private EditText			onlineBookOtherContentEditText;
    /**备注信息*/
    private EditText			onlineBookNoteEditText;
    /**提交申请按钮*/
    private Button				onlineBookCommitButton;
    
    /**身份选择结果*/
    private String				sqrShenFenResultString = "辩护人";
    /**是否为职业律师*/
    private String				isLowerString = "0";
    /**是否为法律援助中心指派*/
    private String				isAssginString = "0";
    /**是否需要回避*/
    private String				isNeedHideString = "0";
    /**性别选择结果*/
    private String				sexSelectResultString = "1";
    /**诉讼阶段*/
    private String				lawsuitSectionString = "侦查(自侦)";
    /**强制措施*/
    private String				forceStepString = "无";
    /**预约类型内容*/
    private String				onlineBookTypeResultString = "";
    /**提交*/
    private String				numberString = "";
    /**须知*/
    private String				dataString = "";
    /**申请编号*/
    private EditText			onlineBookCommitedApplyEditText;
    /**查询按钮*/
    private Button				onlineBookQueryButton;
	/**查询结果*/
	private String				queryResultString;
    
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_onlinebook);
		new Thread()
		{
			public void run() {
				queryGetNotes("Yuyue_xuzhi");
			}
		}.start();
		initView();
		onlineBookCommitButton = (Button)findViewById(R.id.onlineBookCommitButton);
		onlineBookCommitButton.setOnClickListener(new OnClickListener() {
			
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
                String nodeString = property.getNodeValue().replace("<p>", "").replace("</p>", "");
                
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
		topTitleTextView.setText("在线预约");//标题
		top_right_headImageButton = (ImageButton)findViewById(R.id.loginImageButton);//用户登录按钮
		//左边返回按钮的点击事件
		top_left_Layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				OnlineBookActivity.this.finish();
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
		onlineBookScrollView = (ScrollView)findViewById(R.id.onlineBookScrollView);
		onlineBookQueryLineLayout = (LinearLayout)findViewById(R.id.onlineBookQueryLineLayout);
		initAllInputBox();
	}
	//初始化界面上的输入控件
	protected void initAllInputBox() {
		newOnlineBookRadioGroup = (RadioGroup)findViewById(R.id.newOnlineBookRadioGroup);

		//绑定一个匿名监听器
		newOnlineBookRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() { 
		    @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                 // TODO Auto-generated method stub
                 //获取变更后的选中项的ID
                 int radioButtonId = arg0.getCheckedRadioButtonId();
                 //根据ID获取RadioButton的实例
                 RadioButton rb = (RadioButton)findViewById(radioButtonId);
                 //更新文本内容，以符合选中项
                 Log.v(TAG, rb.getText().toString()+rb.getTag().toString());
                 sqrShenFenResultString = rb.getText().toString();
             }
        });
		
		lawFirmNameEditText = (EditText)findViewById(R.id.lawFirmNameEditText);
		
		regularLawyerCheckBox = (CheckBox)findViewById(R.id.regularLawyerCheckBox);
		//给CheckBox设置事件监听 
		regularLawyerCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){ 
            @Override 
            public void onCheckedChanged(CompoundButton buttonView, 
                    boolean isChecked) { 
                // TODO Auto-generated method stub 
                if(isChecked){ 
                	isLowerString = "1"; 
                }else{ 
                	isLowerString = "0"; 
                } 
            } 
        }); 
		
		
		assignLawyerCheckBox = (CheckBox)findViewById(R.id.assignLawyerCheckBox);
		//给CheckBox设置事件监听 
		assignLawyerCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){ 
            @Override 
            public void onCheckedChanged(CompoundButton buttonView, 
                    boolean isChecked) { 
                // TODO Auto-generated method stub 
                if(isChecked){ 
                	isAssginString = "1"; 
                }else{ 
                	isAssginString = "0"; 
                } 
            } 
        }); 
		

		needToAvoidCheckBox = (CheckBox)findViewById(R.id.needToAvoidCheckBox);
		//给CheckBox设置事件监听 
		needToAvoidCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){ 
            @Override 
            public void onCheckedChanged(CompoundButton buttonView, 
                    boolean isChecked) { 
                // TODO Auto-generated method stub 
                if(isChecked){ 
                	isNeedHideString = "1"; 
                }else{ 
                	isNeedHideString = "0"; 
                } 
            } 
        }); 
		
		onlineBookManNameEditText = (EditText)findViewById(R.id.onlineBookManNameEditText);
		onlineBookManIDEditText = (EditText)findViewById(R.id.onlineBookManIDEditText);
		onlineBookManPhoneEditText = (EditText)findViewById(R.id.onlineBookManPhoneEditText);
		onlineBookDefendantNameEditText = (EditText)findViewById(R.id.onlineBookDefendantNameEditText);
		
		onlineBookSexRadioGroup = (RadioGroup)findViewById(R.id.onlineBookSexRadioGroup);
		//绑定一个匿名监听器
		onlineBookSexRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() { 
		    @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                 // TODO Auto-generated method stub
                 //获取变更后的选中项的ID
                 int radioButtonId = arg0.getCheckedRadioButtonId();
                 //根据ID获取RadioButton的实例
                 RadioButton rb = (RadioButton)findViewById(radioButtonId);
                 //更新文本内容，以符合选中项
                 Log.v(TAG, rb.getText().toString()+rb.getTag().toString());
                 sexSelectResultString = rb.getTag().toString();
             }
        });
		
		onlineBookDefendantIDEditText = (EditText)findViewById(R.id.onlineBookDefendantIDEditText);
		
		onlineBookBirthdayTextView = (TextView)findViewById(R.id.onlineBookBirthdayTextView);
		onlineBookBirthdayTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
                //调用Activity类的方法来显示Dialog:调用这个方法会允许Activity管理该Dialog的生命周期，  
                //并会调用 onCreateDialog(int)回调函数来请求一个Dialog  
                showDialog(DATE_DIALOG_ID);  
			}
		});
		//获得当前的日期：  
        final Calendar currentDate = Calendar.getInstance();  
        mYear = currentDate.get(Calendar.YEAR);  
        mMonth = currentDate.get(Calendar.MONTH);  
        mDay = currentDate.get(Calendar.DAY_OF_MONTH);  
        //设置文本的内容：  
        onlineBookBirthdayTextView.setText(new StringBuilder()  
                    .append(mYear).append("年")  
                    .append(mMonth + 1).append("月")//得到的月份+1，因为从0开始  
                    .append(mDay).append("日"));  
        
        courtSelectTextView = (TextView)findViewById(R.id.courtSelectTextView);
		
		onlineBookLawsuitSectionRadioGroup = (RadioGroup)findViewById(R.id.onlineBookLawsuitSectionRadioGroup);
		//绑定一个匿名监听器
		onlineBookLawsuitSectionRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() { 
		    @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                 // TODO Auto-generated method stub
                 //获取变更后的选中项的ID
                 int radioButtonId = arg0.getCheckedRadioButtonId();
                 //根据ID获取RadioButton的实例
                 RadioButton rb = (RadioButton)findViewById(radioButtonId);
                 //更新文本内容，以符合选中项
                 Log.v(TAG, rb.getText().toString()+rb.getTag().toString());
                 lawsuitSectionString = rb.getText().toString();
             }
        });
		
		onlineBookForceStepRadioGroup = (RadioGroup)findViewById(R.id.onlineBookForceStepRadioGroup);
		//绑定一个匿名监听器
		onlineBookForceStepRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() { 
		    @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                 // TODO Auto-generated method stub
                 //获取变更后的选中项的ID
                 int radioButtonId = arg0.getCheckedRadioButtonId();
                 //根据ID获取RadioButton的实例
                 RadioButton rb = (RadioButton)findViewById(radioButtonId);
                 //更新文本内容，以符合选中项
                 Log.v(TAG, rb.getText().toString()+rb.getTag().toString());
                 forceStepString = rb.getText().toString();
             }
        });
		
		onlineBookBriefEditText = (EditText)findViewById(R.id.onlineBookBriefEditText);
		onlineBookConsignorNameEditText = (EditText)findViewById(R.id.onlineBookConsignorNameEditText);
		onlineBookConsignorIdEditText = (EditText)findViewById(R.id.onlineBookConsignorIdEditText);
		
		onlineBookTypeOne = (CheckBox)findViewById(R.id.onlineBookTypeOne);
		//给CheckBox设置事件监听 
		onlineBookTypeOne.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){ 
            @Override 
            public void onCheckedChanged(CompoundButton buttonView, 
                    boolean isChecked) { 
                // TODO Auto-generated method stub 
                if(isChecked){ 
                	onlineBookTypeResultString = onlineBookTypeResultString + onlineBookTypeOne.getText().toString().trim();	 
                }else{ 
                	 if (onlineBookTypeResultString.contains(onlineBookTypeOne.getText().toString().trim())) {
						onlineBookTypeResultString.replace(onlineBookTypeOne.getText().toString().trim(), "");
					}
                } 
            } 
        }); 
		
		onlineBookTypeTwo = (CheckBox)findViewById(R.id.onlineBookTypeTwo);
		//给CheckBox设置事件监听 
		onlineBookTypeTwo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){ 
            @Override 
            public void onCheckedChanged(CompoundButton buttonView, 
                    boolean isChecked) { 
                // TODO Auto-generated method stub 
                if(isChecked){ 
                	onlineBookTypeResultString = onlineBookTypeResultString + onlineBookTypeTwo.getText().toString().trim();
                }else{ 
                	if (onlineBookTypeResultString.contains(onlineBookTypeTwo.getText().toString().trim())) {
						onlineBookTypeResultString.replace(onlineBookTypeTwo.getText().toString().trim(), "");
					}
                } 
            } 
        });
		
		onlineBookTypeThree = (CheckBox)findViewById(R.id.onlineBookTypeThree);
		//给CheckBox设置事件监听 
		onlineBookTypeThree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){ 
            @Override 
            public void onCheckedChanged(CompoundButton buttonView, 
                    boolean isChecked) { 
                // TODO Auto-generated method stub 
                if(isChecked){ 
                	onlineBookTypeResultString = onlineBookTypeResultString + onlineBookTypeThree.getText().toString().trim();
                }else{ 
                	if (onlineBookTypeResultString.contains(onlineBookTypeThree.getText().toString().trim())) {
						onlineBookTypeResultString.replace(onlineBookTypeThree.getText().toString().trim(), "");
					}
                } 
            } 
        });
		
		onlineBookOtherContentEditText = (EditText)findViewById(R.id.onlineBookOtherContentEditText);
		onlineBookTypeResultString = onlineBookTypeResultString + onlineBookOtherContentEditText.getText().toString().trim();
		
		onlineBookNoteEditText = (EditText)findViewById(R.id.onlineBookNoteEditText);
		
		//查询
		onlineBookCommitedApplyEditText = (EditText)findViewById(R.id.onlineBookCommitedApplyEditText);
		onlineBookQueryButton = (Button)findViewById(R.id.onlineBookQueryButton);
		onlineBookQueryButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final String queryNumberString = onlineBookCommitedApplyEditText.getText().toString().trim();
				if (queryNumberString.equals("") || queryNumberString == null) {
					Toast.makeText(OnlineBookActivity.this, "申请编号不能为空", Toast.LENGTH_SHORT).show();
				}
				else {
					new Thread()
					{
						public void run() {
							queryOnlineBook(queryNumberString);
						}
					}.start();
				}
			}
		});
	}
	/**判断是否为空*/
	protected void isEmpty() {
		String userIDString = GOSHelper.getSharePreStr(OnlineBookActivity.this, GosHttpApplication.USER_ID_STRING);
		if (userIDString.equals("")) {
			Toast.makeText(this, "请登录后再进行操作", Toast.LENGTH_SHORT).show();
		}
		else if ((lawFirmNameEditText.getText().toString().trim() == null) || (lawFirmNameEditText.getText().toString().trim().equals(""))) {
			Toast.makeText(this, "律师事务所名称不能为空", Toast.LENGTH_SHORT).show();
		}
		else if ((onlineBookManNameEditText.getText().toString().trim() == null) || (onlineBookManNameEditText.getText().toString().trim().equals(""))) {
			Toast.makeText(this, "申请人姓名不能为空", Toast.LENGTH_SHORT).show();
		} 
		else if ((onlineBookManIDEditText.getText().toString().trim() == null) || (onlineBookManIDEditText.getText().toString().trim().equals(""))) {
			Toast.makeText(this, "申请人身份证号不能为空", Toast.LENGTH_SHORT).show();
		}
		else if ((onlineBookManPhoneEditText.getText().toString().trim() == null) || (onlineBookManPhoneEditText.getText().toString().trim().equals(""))) {
			Toast.makeText(this, "申请人手机号不能为空", Toast.LENGTH_SHORT).show();
		}
		else if ((onlineBookDefendantNameEditText.getText().toString().trim() == null) || (onlineBookDefendantNameEditText.getText().toString().trim().equals(""))) {
			Toast.makeText(this, "举报案由不能为空", Toast.LENGTH_SHORT).show();
		}
		else if ((onlineBookDefendantIDEditText.getText().toString().trim() == null) || (onlineBookDefendantIDEditText.getText().toString().trim().equals(""))) {
			Toast.makeText(this, "举报内容不能为空", Toast.LENGTH_SHORT).show();
		}
		else if ((onlineBookConsignorNameEditText.getText().toString().trim() == null) || (onlineBookConsignorNameEditText.getText().toString().trim().equals(""))) {
			Toast.makeText(this, "委托人姓名不能为空", Toast.LENGTH_SHORT).show();
		}
		else if ((onlineBookNoteEditText.getText().toString().trim() == null) || (onlineBookNoteEditText.getText().toString().trim().equals(""))) {
			Toast.makeText(this, "委托人信息备注不能为空", Toast.LENGTH_SHORT).show();
		}
		else {
			new Thread()
			{
				public void run() {
					commitNewOnlineBook(generateOnlineBookEntity());
				}
			}.start();
		}
	}
	/**生成网上预约对象*/
	@SuppressLint("SimpleDateFormat")
	protected OnlineBookEntity generateOnlineBookEntity() {
		OnlineBookEntity onlineBookEntity = new OnlineBookEntity();
		String userIDString = GOSHelper.getSharePreStr(OnlineBookActivity.this, GosHttpApplication.USER_ID_STRING);
		if (userIDString.equals("")) {
			userIDString = "0";
		}
		onlineBookEntity.setUserIdString(userIDString);
		onlineBookEntity.setSQR_shenfen(sqrShenFenResultString);
		onlineBookEntity.setSQR_shiwusuoName(lawFirmNameEditText.getText().toString().trim());
		onlineBookEntity.setSQR_isLower(isLowerString);
		onlineBookEntity.setSQR_IsFromHelpCenter(isAssginString);
		onlineBookEntity.setSQR_IsNeedHide(isNeedHideString);
		onlineBookEntity.setSQR_realName(onlineBookManNameEditText.getText().toString().trim());
		onlineBookEntity.setSQR_IDCardNumber(onlineBookManIDEditText.getText().toString().trim());
		onlineBookEntity.setSQR_mobile(onlineBookManPhoneEditText.getText().toString().trim());
		onlineBookEntity.setBG_Realname(onlineBookDefendantNameEditText.getText().toString().trim());
		onlineBookEntity.setBG_Sex(sexSelectResultString);
		onlineBookEntity.setBG_Shenfenzheng(onlineBookDefendantIDEditText.getText().toString().trim());
		String dateTimeString = onlineBookBirthdayTextView.getText().toString().trim();
		String dateString = dateTimeString.replace("年", "-").replace("月", "-").replace("日", "");
		
		 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		 try {
			Date fDate = format.parse(dateString);
			String dateString2 = format.format(fDate);
			onlineBookEntity.setBG_Birthday(dateString2);
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		
		onlineBookEntity.setBG_Jianchayuan(courtSelectTextView.getText().toString().trim());
		onlineBookEntity.setBG_Jieduan(lawsuitSectionString);
		onlineBookEntity.setBG_Cuoshi(forceStepString);
		onlineBookEntity.setBG_Anyou(onlineBookBriefEditText.getText().toString().trim());
		onlineBookEntity.setWTR_Realname(onlineBookConsignorNameEditText.getText().toString().trim());
		onlineBookEntity.setWTR_Shenfenzheng(onlineBookConsignorIdEditText.getText().toString().trim());
		onlineBookEntity.setType(onlineBookTypeResultString);
		onlineBookEntity.setDescription(onlineBookNoteEditText.getText().toString().trim());
		
		return onlineBookEntity;
	}
	//提交新的预约
	protected void commitNewOnlineBook(OnlineBookEntity onlineBookEntity) {
		try {
			HttpResponse aString = gosHttpOperation.invokerSetReservationResponse(onlineBookEntity);
			HttpEntity entity = aString.getEntity();
			InputStream is = entity.getContent();
			String reString = GOSHelper.convertStreamToString(is);
		
			if (reString == null || reString.equals("")) {
				
			}
			else {
				JSONObject jsonObject = JSON.parseObject(reString);
				JSONObject dataObject = jsonObject.getJSONObject("data");
				String stateString = dataObject.getString("state");
				if (stateString.equals("1")) {
					Log.v("测试依申请公开项", "测试依申请公开项成功");
					JSONArray responeArray = dataObject.getJSONArray("responseList");
					
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
	@SuppressLint("HandlerLeak")
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
		lawFirmNameEditText.setText("");
		onlineBookManNameEditText.setText("");
		onlineBookManIDEditText.setText("");
		onlineBookManPhoneEditText.setText("");
		onlineBookDefendantNameEditText.setText("");
		onlineBookDefendantIDEditText.setText("");
		
		onlineBookBriefEditText.setText("");
		onlineBookConsignorNameEditText.setText("");
		onlineBookConsignorIdEditText.setText("");
		onlineBookOtherContentEditText.setText("");
		onlineBookNoteEditText.setText("");
		
		regularLawyerCheckBox.setChecked(false);
		assignLawyerCheckBox.setChecked(false);
		needToAvoidCheckBox.setChecked(false);
		onlineBookTypeOne.setChecked(false);
		onlineBookTypeTwo.setChecked(false);
		onlineBookTypeThree.setChecked(false);
		
		RadioButton rbOne = (RadioButton)findViewById(R.id.onlineBookPleader);
		rbOne.setChecked(true);
		RadioButton rbTwo = (RadioButton)findViewById(R.id.onlineBookOpenMale);
		rbTwo.setChecked(true);
		RadioButton rbThree = (RadioButton)findViewById(R.id.lawsuitSectionOneRadioButton);
		rbThree.setChecked(true);
		RadioButton rbFour = (RadioButton)findViewById(R.id.onlineBookForceStepOneRadioButton);
		rbFour.setChecked(true);
	}
	
	/** 查询预约*/
	protected void queryOnlineBook(String numberString) {
		try {			
			HttpResponse aString = gosHttpOperation.invokerQueryOnlineBookResponse(numberString);
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
								String dateString = oneObject.getString("dateandtime") == null?"":oneObject.getString("dateandtime").trim();
								String shoulibumenString = oneObject.getString("workBumen") == null?"":oneObject.getString("workBumen").trim();
								String shoulirenString = oneObject.getString("worker") == null?"":oneObject.getString("worker").trim();
								String shoulicontactString = oneObject.getString("workContact") == null?"":oneObject.getString("workContact").trim();
								String chuliresultString = oneObject.getString("workDescription") == null?"":oneObject.getString("workDescription").trim();
								queryResultString = "<br>受理部门</br>"+shoulibumenString+ "<br>受理人</br>" + shoulirenString + "<br>受理人联系方式</br>" +
								shoulicontactString + "<br>处理结果</br>" + chuliresultString;//"提交日期\n" + dateString+ "\n" +
							}
							queryResultHandler.sendEmptyMessage(1);
						}
					}
				}
				else
				{
					Log.v("查询申请公开项", "查询申请公开项失败");
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
			oneIntent.setClass(OnlineBookActivity.this, QueryHandleResult.class);
			oneIntent.putExtra("queryResult", queryResultString);
			OnlineBookActivity.this.startActivity(oneIntent);
		}
	}
	
	
    //需要定义弹出的DatePicker对话框的事件监听器：  
    private DatePickerDialog.OnDateSetListener mDateSetListener =new OnDateSetListener() {  
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {  
            mYear = year;  
            mMonth = monthOfYear;  
            mDay = dayOfMonth;  
            //设置文本的内容：  
            onlineBookBirthdayTextView.setText(new StringBuilder()  
                        .append(mYear).append("年")  
                        .append(mMonth + 1).append("月")//得到的月份+1，因为从0开始  
                        .append(mDay).append("日"));  
        }  
    }; 
    /** 
     * 当Activity调用showDialog函数时会触发该函数的调用： 
     */  
    @Override  
    protected Dialog onCreateDialog(int id) {  
       switch (id) {  
        case DATE_DIALOG_ID:  
            return new DatePickerDialog(OnlineBookActivity.this, mDateSetListener, mYear, mMonth, mDay);  
        }  
        return null;  
    }
	
	//初始化tabhost
	protected void initTabHost() {
		tabHost = (TabHost) findViewById(R.id.topOnlineBookTabHost);  
        tabHost.setup();  
        TabWidget tabWidget = tabHost.getTabWidget();  
        
        tabHost.addTab(tabHost.newTabSpec("tab1")  
                .setIndicator("提交新的预约", getResources().getDrawable(R.drawable.ic_launcher))  
                .setContent(R.id.onLineBookView1));   
          
        tabHost.addTab(tabHost.newTabSpec("tab2")  
                .setIndicator("预约受理情况查询")  
                .setContent(R.id.onLineBookView2));  
          
        final int tabs = tabWidget.getChildCount();  
        Log.v(TAG, "***tabWidget.getChildCount() : " + tabs);  
        
        //注意这个就是改变Tabhost默认样式的地方，一定将这部分代码放在上面这段代码的下面，不然样式改变不了
        updateTab(tabHost);
//	        for (int i =0; i < tabWidget.getChildCount(); i++) {  
//	         //修改Tabhost高度和宽度
//	         tabWidget.getChildAt(i).getLayoutParams().height = 60;  
//	         tabWidget.getChildAt(i).getLayoutParams().width = 65;
//	         //修改显示字体大小
//	         TextView tv = (TextView) tabWidget.getChildAt(i).findViewById(android.R.id.title);
//	         tv.setTextSize(15);
//	         //tv.setTextColor(this.getResources().getColorStateList(android.R.color.white));
//	        }
        
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
			   		 	onlineBookScrollView.setVisibility(View.VISIBLE);
			   		 	onlineBookQueryLineLayout.setVisibility(View.INVISIBLE);
			   		 break;
			   	 case 1:
			   		 	onlineBookScrollView.setVisibility(View.INVISIBLE);
			   		 	onlineBookQueryLineLayout.setVisibility(View.VISIBLE);
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
