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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gov.nzjcy.entity.SetAppealEntity;
import com.gov.nzjcy.exception.BaseException;
import com.gov.nzjcy.exception.ParseException;
import com.gov.nzjcy.helper.GOSHelper;
import com.gov.nzjcy.httpoperation.GosHttpApplication;
import com.gov.nzjcy.testutil.TestNetPort;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
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

public class SueComplaintActivity extends BaseActivity {
	
	/**log显示信息标志*/
	private String TAG = "SueComplaintActivity";
	/** head 头部 的左侧菜单 按钮*/
	private LinearLayout	top_left_Layout;
	/** head 头部 的右侧菜单 按钮*/
	private ImageButton		top_right_headImageButton;
	/**head的标题*/
	private TextView		topTitleTextView;
	/**第一个选项*/
	private ScrollView		sueComplainScrollView;
	/**第二个选项*/
	private LinearLayout	sueComplaintQueryLineLayout;
	/**tabhost的定义*/
	private TabHost 		tabHost;
	/**控告编号*/
	private EditText		sueComplaintNumberEditText;
	/**控告查询按钮*/
	private Button			sueComplaintQueryButton;
	/**查询结果*/
	private String				queryResultString;
	
	
	//用来保存年月日：  
    private int mYear;  
    private int mMonth;  
    private int mDay;  
    //声明一个独一无二的标识，来作为要显示DatePicker的Dialog的ID：  
    static final int DATE_DIALOG_ID = 0; 
	
	/**控告人姓名*/
	private EditText		sueComplaintUserNameEditText;
	/**控告人出生年月*/
	private TextView		sueComplaintDateTextView;
	/**控告人性别*/
	private RadioGroup		sueComplaintSexRadioGroup;
	/**控告人性别选择结果*/
	private String			sexString = "1";
	/**控告人籍贯*/
	private EditText		sueComplaintBirthplaceEditText;
	/**控告人工作单位*/
	private	EditText		sueComplaintWorkUntiEditText;
	/**控告人民族*/
	private	EditText		sueComplaintMinZuEditText;
	/**控告人家庭地址*/
	private EditText		sueComplaintHomeAddressEditText;
	/**控告人联系电话*/
	private EditText		sueComplaintPhoneNumberEditText;
	/**控告人身份证号码*/
	private EditText 		sueComplaintIDEditText;
	/**控告人电子邮件*/
	private EditText		sueComplaintEmailEditText;
	/**控告类型*/
	private RadioGroup		sueComplaintRadioOneGroup;
	/**控告类型结果*/
	private String			sueComplaintTypeResultString = "民事申诉";
	/**控告内容*/
	private	EditText		sueComplaintContentEditText;
	/**提交按钮*/
	private Button			sueComplaintCommitButton;
	/**编号*/
	private String				numberString = "";
	/**须知*/
    private String				dataString = "";
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_suecomplaint);
//		new Thread()
//		{
//			public void run() {
//				queryGetNotes("Konggao_xuzhi");
//			}
//		}.start();
		
		String nodeString = "刑事案件申诉须知"
				+ "\n1、受理申诉的范围"
				+ "\n人民检察院管辖的申诉包括对人民检察院诉讼终结的刑事处理决定以及对人民法院已经发生法律效力的刑事判决、裁定不服的申诉。"
				+ "\n2、人民检察院管辖申诉案件的具体范围"
				+ "\n（一）县级人民检察院管辖下列申诉："
				+ "\n·不服本院决定的申诉（另有规定的除外）；"
				+ "\n·不服同级人民法院已经发生法律效力的刑事判决、裁定的申诉。"
				+ "\n（二）县级人民检察院以外的人民检察院管辖下列申诉："
				+ "\n·不服本院决定的申诉（另有规定的除外）；"
				+ "\n·被害人不服下一级人民检察院不起诉决定，在七日内提出的申诉；"
				+ "\n·不服下一级人民检察院复查决定的申诉；"
				+ "\n·不服同级和下级人民法院已经发生法律效力的刑事判决、裁定的申诉。"
				+ "\n民行案件申诉须知"
				+ "\n1、哪些人可以向人民检察院提出民事、行政抗诉申请？"
				+ "\n不服人民法院生效的民事、行政判决、裁定的当事人或者与生效判决、裁定有利害关系的第三人或案外人可以向人民检察院申请抗诉。"
				+ "\n2、申请抗诉应向哪级人民检察院提出？"
				+ "\n（一）当事人不服本区、县人民法院生效的民事、行政判决、裁定，应当向本区、县人民检察院申请抗诉。当事人不服北京市管内各基层铁路运输法院生效的民事、行政判决、裁定，应当向北京市管内各基层铁路运输检察院申请抗诉。"
				+ "\n（二）当事人不服北京市第一中级人民法院生效的民事、行政判决、裁定，应当向北京市人民检察院第一分院申请抗诉；当事人不服北京市第二中级人民法院生效的民事、行政判决、裁定，应当向北京市人民检察院第二分院申请抗诉；当事人不服北京铁路运输中级法院生效的民事、行政判决、裁定，应当向北京铁路运输检察分院申请抗诉。"
				+ "\n（三）当事人不服北京市高级人民法院生效的民事、行政判决、裁定，应当向北京市人民检察院申请抗诉。"
				+ "\n3、人民检察院受理案件的条件是什么？"
				+ "\n人民检察院受理民事、行政案件的申请抗诉有两个条件："
				+ "\n（一）人民法院的民事、行政判决、裁定已经发生法律效力。"
				+ "\n（二）有具体的申请抗诉的请求和理由。"
				+ "\n4、人民检察院不受理哪些案件？"
				+ "\n（一）判决、裁定尚未发生法律效力的；"
				+ "\n（二）判决解除婚姻关系或者收养关系的；"
				+ "\n（三）人民法院已经裁定再审的；"
				+ "\n（四）当事人对人民检察院所作的不予立案、终止审查、不予提请抗诉或者不抗诉决定不服，再次提出申诉的；"
				+ "\n（五）《中华人民共和国民事诉讼法》实施前发生法律效力的民事、行政判决、裁定；"
				+ "\n（六）不属于人民检察院主管的其他情形。"
				+ "\n5、向人民检察院提出申诉应当提交哪些材料？"
				+ "\n向人民检察院提出申诉应提交以下材料："
				+ "\n（一）抗诉申请书或申诉书；"
				+ "\n（二）人民法院一审、二审、再审判决、裁定（复印件）；"
				+ "\n（三）相关证据材料。"
				+ "\n（四）如有委托代理人的，须提交授权委托书。"
				+ "\n6、如何写申请抗诉书？"
				+ "\n申请抗诉书应用A4型纸打印或书写（使用蓝色或黑色钢笔、毛笔），格式如下："
				+ "\n申请抗诉书"
				+ "\n申诉人：姓名，性别，民族，出生年月，职业，住址，通信地址，邮编，联系电话。（如申诉人为法人或组织，应写明法定代表人姓名、职务）"
				+ "\n委托代理人：姓名，职业，通信地址，邮编，联系电话。"
				+ "\n对方当事人：××××××（对方当事人自然情况应比照以上各式尽量写明）"
				+ "\n申诉请求：不服××法院××判决（必须写明该裁判书文号），请求人民检察院依法抗诉。"
				+ "\n申诉理由： ××××××（应当针对法院生效判决或裁定，简明扼要地写明该判决或裁定在认定事实、适用法律以及人民法院在审判程序方面存在的问题）"
				+ "\n此致"
				+ "\n北京市××××人民检察院"
				+ "\n申诉人：×××（申诉人为自然人的，应当由申诉人在申诉书上亲笔签名。申诉人为法人或组织的，应当由法定代表人或负责人在申诉书上亲笔签名，并加盖该法人或组织的印章）"
				+ "\n××××年×月×日"
				+ "\n7、申诉人向人民检察院提交的人民法院生效裁判文书应符合哪些形式上的要求？"
				+ "\n人民法院生效判决书或裁定书是当事人申诉时必须提供的材料，申诉人提交的人民法院判决书或裁定书应当是已经发生法律效力的文书，提交的判决书或裁定书的复印件应当字迹清晰，表面整洁，无批注、涂改等情形。"
				+ "\n8、其他告知事项"
				+ "\n（一）因向人民法院申请再审需在判决、裁定生效后两年内提出，人民检察院建议申请抗诉人（以下简称申诉人）在收到人民法院判决、裁定书后，先行向人民法院申请再审。"
				+ "\n（二）为最大限度维护申诉人的合法权益，人民检察院建议申诉人尽量委托律师或者法律专业人士代理在人民检察院的相关诉讼活动。";
		
		new AlertDialog.Builder(this).setTitle("使用须知").
		setMessage(nodeString).
		setPositiveButton("确定", null).show();
		
		initView();
		sueComplaintCommitButton = (Button)findViewById(R.id.sueComplaintCommitButton);
		sueComplaintCommitButton.setOnClickListener(new OnClickListener() {
			
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
		topTitleTextView.setText("控告申诉");//标题
		top_right_headImageButton = (ImageButton)findViewById(R.id.loginImageButton);//用户登录按钮
		//左边返回按钮的点击事件
		top_left_Layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SueComplaintActivity.this.finish();
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
		sueComplainScrollView = (ScrollView)findViewById(R.id.sueComplaintScrollView);
		sueComplaintQueryLineLayout = (LinearLayout)findViewById(R.id.sueComplaintQueryLineLayout);
		initTabHost();
		initInputBox();
	}
	/**初始化输入框*/
	protected void initInputBox() {
		sueComplaintUserNameEditText = (EditText)findViewById(R.id.sueComplaintUserNameEditText);
		sueComplaintDateTextView = (TextView)findViewById(R.id.sueComplaintDateTextView);
		sueComplaintDateTextView.setOnClickListener(new OnClickListener() {
			
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
        sueComplaintDateTextView.setText(new StringBuilder()  
                    .append(mYear).append("年")  
                    .append(mMonth + 1).append("月")//得到的月份+1，因为从0开始  
                    .append(mDay).append("日"));  
		
		
		sueComplaintSexRadioGroup = (RadioGroup)findViewById(R.id.sueComplaintSexRadioGroup);
		//绑定一个匿名监听器
		sueComplaintSexRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() { 
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
		
		sueComplaintBirthplaceEditText = (EditText)findViewById(R.id.sueComplaintBirthplaceEditText);
		sueComplaintWorkUntiEditText = (EditText)findViewById(R.id.sueComplaintWorkUntiEditText);
		sueComplaintMinZuEditText = (EditText)findViewById(R.id.sueComplaintMinZuEditText);
		sueComplaintHomeAddressEditText = (EditText)findViewById(R.id.sueComplaintHomeAddressEditText);
		sueComplaintPhoneNumberEditText = (EditText)findViewById(R.id.sueComplaintPhoneNumberEditText);
		sueComplaintIDEditText = (EditText)findViewById(R.id.sueComplaintIDEditText);
		sueComplaintEmailEditText = (EditText)findViewById(R.id.sueComplaintEmailEditText);
		
		sueComplaintRadioOneGroup = (RadioGroup)findViewById(R.id.sueComplaintRadioOneGroup);
		//绑定一个匿名监听器
		sueComplaintRadioOneGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() { 
		    @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                 // TODO Auto-generated method stub
                 //获取变更后的选中项的ID
                 int radioButtonId = arg0.getCheckedRadioButtonId();
                 //根据ID获取RadioButton的实例
                 RadioButton rb = (RadioButton)findViewById(radioButtonId);
                 //更新文本内容，以符合选中项
                 Log.v(TAG, rb.getText().toString()+rb.getTag().toString());
                 sueComplaintTypeResultString = rb.getText().toString();
             }
        });
		
		sueComplaintContentEditText = (EditText)findViewById(R.id.sueComplaintContentEditText);
		//查询
		sueComplaintNumberEditText = (EditText)findViewById(R.id.sueComplaintNumberEditText);
		sueComplaintQueryButton = (Button)findViewById(R.id.sueComplaintQueryButton);
		sueComplaintQueryButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final String queryNumberString = sueComplaintNumberEditText.getText().toString().trim();
				if (queryNumberString.equals("") || queryNumberString == null) {
					Toast.makeText(SueComplaintActivity.this, "申请编号不能为空", Toast.LENGTH_SHORT).show();
				}
				else {
					new Thread()
					{
						public void run() {
							queryAppeal(queryNumberString);
						}
					}.start();
				}
			}
		});
	}
	/**判断是否为空*/
	protected void isEmpty() {
		String userIDString = GOSHelper.getSharePreStr(SueComplaintActivity.this, GosHttpApplication.USER_ID_STRING);
		if (userIDString.equals("")) {
			Toast.makeText(this, "请登录后再进行操作", Toast.LENGTH_SHORT).show();
		}
		else if ((sueComplaintUserNameEditText.getText().toString().trim() == null) || (sueComplaintUserNameEditText.getText().toString().trim().equals(""))) {
			Toast.makeText(this, "控告人姓名不能为空", Toast.LENGTH_SHORT).show();
		}
		else if ((sueComplaintPhoneNumberEditText.getText().toString().trim() == null) || (sueComplaintPhoneNumberEditText.getText().toString().trim().equals(""))) {
			Toast.makeText(this, "控告人联系电话不能为空", Toast.LENGTH_SHORT).show();
		} 
		else if ((sueComplaintContentEditText.getText().toString().trim() == null) || (sueComplaintContentEditText.getText().toString().trim().equals(""))) {
			Toast.makeText(this, "控告内容不能为空", Toast.LENGTH_SHORT).show();
		}
		else {
			new Thread()
			{
				public void run() {
					setAppeal(generateAppealEntity());
				}
			}.start();
		}
	}
	/**生成控告申诉对象*/
	@SuppressLint("SimpleDateFormat")
	protected SetAppealEntity generateAppealEntity() {
		SetAppealEntity setAppealEntity = new SetAppealEntity();
		String useridString = GOSHelper.getSharePreStr(SueComplaintActivity.this, GosHttpApplication.USER_ID_STRING);
		if (useridString.equals("")) {
			useridString = "0";
		}
		setAppealEntity.setUserIdString(useridString);
		setAppealEntity.setSetAppealUserName(sueComplaintUserNameEditText.getText().toString().trim());
		
		String dateTimeString = sueComplaintDateTextView.getText().toString().trim();
		String dateString = dateTimeString.replace("年", "-").replace("月", "-").replace("日", "");
		
		 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		 try {
			Date fDate = format.parse(dateString);
			String dateString2 = format.format(fDate);
			setAppealEntity.setSetAppealBirthday(dateString2);
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		setAppealEntity.setSetAppealSex(sexString);
		setAppealEntity.setSetAppealJiGuan(sueComplaintBirthplaceEditText.getText().toString().trim());
		setAppealEntity.setCompany(sueComplaintWorkUntiEditText.getText().toString().trim());
		setAppealEntity.setMinzu(sueComplaintMinZuEditText.getText().toString().trim());
		setAppealEntity.setAddress(sueComplaintHomeAddressEditText.getText().toString().trim());
		setAppealEntity.setMobile(sueComplaintPhoneNumberEditText.getText().toString().trim());
		setAppealEntity.setIDCardNumber(sueComplaintIDEditText.getText().toString().trim());
		setAppealEntity.setEmail(sueComplaintEmailEditText.getText().toString().trim());
		setAppealEntity.setAnjianType(sueComplaintTypeResultString);
		setAppealEntity.setDescription(sueComplaintContentEditText.getText().toString().trim());
		
		return setAppealEntity;
	}
	/**设置申诉*/
	protected void setAppeal(SetAppealEntity setAppealEntity) {
		try {	
			HttpResponse aString = gosHttpOperation.invokerSetAppealResponse(setAppealEntity);
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
		sueComplaintUserNameEditText.setText("");
		
		sueComplaintBirthplaceEditText.setText("");
		sueComplaintWorkUntiEditText.setText("");
		sueComplaintMinZuEditText.setText("");
		sueComplaintHomeAddressEditText.setText("");
		sueComplaintPhoneNumberEditText.setText("");
		
				
		sueComplaintIDEditText.setText("");		
		sueComplaintEmailEditText.setText("");
		
		sueComplaintContentEditText.setText("");

	}
	
	
	/**查询申诉*/
	protected void queryAppeal(String numberString) {
		try {			
			HttpResponse aString = gosHttpOperation.invokerQueryAppealResponse(numberString);
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
								String chengbanbumenString = oneObject.getString("chengbanbumen") == null?"":oneObject.getString("chengbanbumen").trim();
								String chengbanrenString = oneObject.getString("chengbaner") == null?"":oneObject.getString("chengbaner").trim();
								String chengbancontactString = oneObject.getString("chengbanContact") == null?"":oneObject.getString("chengbanContact").trim();
								String workDescriptionString = oneObject.getString("workDescription") == null?"":oneObject.getString("workDescription").trim();
								
								queryResultString = "<br>受理部门</br>"+shoulibumenString + "<br>受理人</br>" + shoulirenString + "<br>受理人联系方式</br>" +
										shoulicontactString + "<br>承办部门</br>" + chengbanbumenString + "<br>承办人</br>" + chengbanrenString +
										 "<br>承办人联系方式</br>" +chengbancontactString + "<br>处理结果</br>" + workDescriptionString;//"提交日期\n" + dateString+ "\n"+

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
			oneIntent.setClass(SueComplaintActivity.this, QueryHandleResult.class);
			oneIntent.putExtra("queryResult", queryResultString);
			SueComplaintActivity.this.startActivity(oneIntent);
		}
	}
	
	
	
	//需要定义弹出的DatePicker对话框的事件监听器：  
    private DatePickerDialog.OnDateSetListener mDateSetListener =new OnDateSetListener() {  
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {  
            mYear = year;  
            mMonth = monthOfYear;  
            mDay = dayOfMonth;  
            //设置文本的内容：  
            sueComplaintDateTextView.setText(new StringBuilder()  
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
            return new DatePickerDialog(SueComplaintActivity.this, mDateSetListener, mYear, mMonth, mDay);  
        }  
        return null;  
    }
	
	//初始化tabhost
	protected void initTabHost() {
		tabHost = (TabHost) findViewById(R.id.topSueComplaintTabHost);  
        tabHost.setup();  
        TabWidget tabWidget = tabHost.getTabWidget();  
        
        tabHost.addTab(tabHost.newTabSpec("tab1")  
                .setIndicator("提交新的控告", getResources().getDrawable(R.drawable.ic_launcher))  
                .setContent(R.id.sueComplaintView1));   
          
        tabHost.addTab(tabHost.newTabSpec("tab2")  
                .setIndicator("控告申诉结果查询")  
                .setContent(R.id.sueComplaintView2));  
          
        final int tabs = tabWidget.getChildCount();  
        Log.v(TAG, "***tabWidget.getChildCount() : " + tabs);  
        
        //注意这个就是改变Tabhost默认样式的地方，一定将这部分代码放在上面这段代码的下面，不然样式改变不了
        updateTab(tabHost);
//			        for (int i =0; i < tabWidget.getChildCount(); i++) {  
//			         //修改Tabhost高度和宽度
//			         tabWidget.getChildAt(i).getLayoutParams().height = 60;  
//			         tabWidget.getChildAt(i).getLayoutParams().width = 65;
//			         //修改显示字体大小
//			         TextView tv = (TextView) tabWidget.getChildAt(i).findViewById(android.R.id.title);
//			         tv.setTextSize(15);
//			         //tv.setTextColor(this.getResources().getColorStateList(android.R.color.white));
//			        }
        
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
			   		 	sueComplainScrollView.setVisibility(View.VISIBLE);
			   		 	sueComplaintQueryLineLayout.setVisibility(View.INVISIBLE);
			   		 break;
			   	 case 1:
			   		 	sueComplainScrollView.setVisibility(View.INVISIBLE);
			   		 	sueComplaintQueryLineLayout.setVisibility(View.VISIBLE);
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
