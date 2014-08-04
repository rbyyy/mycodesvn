package com.gov.nzjcy;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gov.nzjcy.entity.ChiefMailBoxEntity;
import com.gov.nzjcy.exception.BaseException;
import com.gov.nzjcy.exception.ParseException;
import com.gov.nzjcy.helper.GOSHelper;
import com.gov.nzjcy.httpoperation.GosHttpApplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
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
public class ChiefEmailActivity extends BaseActivity {
	/**log显示信息标志*/
	private String TAG = "ChiefEmailActivity";
	/** head 头部 的左侧菜单 按钮*/
	private LinearLayout	top_left_Layout;
	/** head 头部 的右侧菜单 按钮*/
	private ImageButton		top_right_headImageButton;
	/**head的标题*/
	private TextView		topTitleTextView;
	/**tabhost的定义*/
	private TabHost 		tabHost;
	/**第一项*/
	private ScrollView		chiefMailScrollView;
	/**第二项*/
	private LinearLayout	chiefMailQueryLineLayout;
	/**发信人姓名*/
	private EditText		chiefMailUserNameEditText;
	/**发信人联系电话*/
	private EditText		chiefMailContactPhoneEditText;
	/**选择收件人*/
	private RadioGroup		chiefMailReceiverRadioGroup;
	/**是否公开*/
	private CheckBox		chiefMailType;
	/**信件标题*/
	private EditText		chiefMailContentTitleEditText;
	/**信件内容*/
	private EditText		chiefMailContentEditText;
	/**检察长类别*/
	private String			chiefTypeString = "0";
	/**是否公开*/
	private String			isOpenString;
	/**提交信件*/
	private Button			chiefMailCommitButton;
	/**来信显示列表*/
	private ListView		chiefMailReciverListView;
	/**来信公示*/
	private ArrayList<HashMap<String, String>>  mailDataArrayList;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chiefemail);
		String nodeString = "检察长信息使用须知"
				+ "\n写信须知事项"
				+ "\n1.咨询、申诉、意见和建议对象明确，内容真实、客观、清楚，不得歪曲或捏造事实；"
				+ "\n2.有关案件线索及案件情况的举报，请点击左侧的“在线举报”栏目进行举报；"
				+ "\n3.请您在签写信件时填写姓名、密码、联系电话，以方便与您的联系沟通；"
				+ "\n4.请妥善保存好您写信时所填写的姓名和密码，以便查询时使用；"
				+ "\n5.您的信件如果需要保密，请在写信时选择“不公开”；"
				+ "\n6.请不要重复发送相同内容的信件。";
		
		new AlertDialog.Builder(this).setTitle("使用须知").
		setMessage(nodeString).
		setPositiveButton("确定", null).show();
		mailDataArrayList = new ArrayList<HashMap<String,String>>();
		initView();
		chiefMailCommitButton = (Button)findViewById(R.id.chiefMailCommitButton);
		chiefMailCommitButton.setOnClickListener(new OnClickListener() {
			
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
		topTitleTextView.setText("检察长信箱");//标题
		top_right_headImageButton = (ImageButton)findViewById(R.id.loginImageButton);//用户登录按钮
		//左边返回按钮的点击事件
		top_left_Layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ChiefEmailActivity.this.finish();
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
		chiefMailScrollView = (ScrollView)findViewById(R.id.chiefMailScrollView);
		chiefMailQueryLineLayout = (LinearLayout)findViewById(R.id.chiefMailQueryLineLayout);
		initTabHost();
		initAllInputBox();
	}
	/**初始化所有输入框*/
	protected void initAllInputBox() {
		chiefMailUserNameEditText = (EditText)findViewById(R.id.chiefMailUserNameEditText);
		chiefMailContactPhoneEditText = (EditText)findViewById(R.id.chiefMailContactPhoneEditText);
		chiefMailReceiverRadioGroup = (RadioGroup)findViewById(R.id.chiefMailReceiverRadioGroup);
		chiefMailReceiverRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() { 
		    @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                 // TODO Auto-generated method stub
                 //获取变更后的选中项的ID
                 int radioButtonId = arg0.getCheckedRadioButtonId();
                 //根据ID获取RadioButton的实例
                 RadioButton rb = (RadioButton)findViewById(radioButtonId);
                 //更新文本内容，以符合选中项
                 Log.v(TAG, rb.getText().toString()+rb.getTag().toString());
                 chiefTypeString = rb.getTag().toString();
             }
        });
		
		chiefMailType = (CheckBox)findViewById(R.id.chiefMailType);
		//给CheckBox设置事件监听 
		chiefMailType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){ 
            @Override 
            public void onCheckedChanged(CompoundButton buttonView, 
                    boolean isChecked) { 
                // TODO Auto-generated method stub 
                if(isChecked){ 
                	isOpenString = "1"; 
                }else{ 
                	isOpenString = "0"; 
                } 
            } 
        }); 
		
		chiefMailContentTitleEditText = (EditText)findViewById(R.id.chiefMailContentTitleEditText);
		chiefMailContentEditText = (EditText)findViewById(R.id.chiefMailContentEditText);
		
		//来信显示
		chiefMailReciverListView = (ListView)findViewById(R.id.chiefMailReciverListView);
	}
	/**判断是否为空*/
	protected void isEmpty() {
		String userIDString = GOSHelper.getSharePreStr(ChiefEmailActivity.this, GosHttpApplication.USER_ID_STRING);
		if (userIDString.equals("")) {
			Toast.makeText(this, "请登录后再进行操作", Toast.LENGTH_SHORT).show();
		}
		else if ((chiefMailUserNameEditText.getText().toString().trim() == null) || (chiefMailUserNameEditText.getText().toString().trim().equals(""))) {
			Toast.makeText(this, "发信人姓名不能为空", Toast.LENGTH_SHORT).show();
		}
		else if ((chiefMailContentEditText.getText().toString().trim() == null) || (chiefMailContentEditText.getText().toString().trim().equals(""))) {
			Toast.makeText(this, "信件内容不能为空", Toast.LENGTH_SHORT).show();
		} 
		else {
			new Thread()
			{
				public void run() {
					setChiefMailBox(generateChiefMailBoxEntity());
				}
			}.start();
		}
	}
	/**生成信件对象*/
	protected ChiefMailBoxEntity generateChiefMailBoxEntity() {
		ChiefMailBoxEntity chiefMailBoxEntity = new ChiefMailBoxEntity();
		String useridString = GOSHelper.getSharePreStr(ChiefEmailActivity.this, GosHttpApplication.USER_ID_STRING);
		if (useridString.equals("")) {
			useridString = "0";
		}
		chiefMailBoxEntity.setUserIdString(useridString);
		chiefMailBoxEntity.setRealName(chiefMailUserNameEditText.getText().toString().trim());
		chiefMailBoxEntity.setMobile(chiefMailContactPhoneEditText.getText().toString().trim());
		chiefMailBoxEntity.setType(chiefTypeString);
		chiefMailBoxEntity.setIsOpen(isOpenString);
		chiefMailBoxEntity.setTitle(chiefMailContentTitleEditText.getText().toString().trim());
		chiefMailBoxEntity.setDescription(chiefMailContentEditText.getText().toString().trim());
		
		return chiefMailBoxEntity;
	}
	/**检察长信箱*/
	protected void setChiefMailBox(ChiefMailBoxEntity chiefMailBoxEntity) {
		try {
			HttpResponse aString = gosHttpOperation.invokerChiefMailBoxResponse(chiefMailBoxEntity);
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
					alertHandler.sendEmptyMessage(1);
				}
				else
				{
					Log.v("测试查询申请公开项", "测试查询申请公开项失败");
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
			setMessage("提交成功!").
			setPositiveButton("确定", null).show();
		}
	}
	/**置空*/
	protected void emptyInput() {		
		chiefMailUserNameEditText.setText("");
		
		chiefMailContactPhoneEditText.setText("");
		chiefMailContentTitleEditText.setText("");
		chiefMailContentEditText.setText("");

	}
	
	
	/**检察长信箱公示*/
	protected void queryChiefMailBoxShow() {
		try {			
			HttpResponse aString = gosHttpOperation.invokerChiefMailShowResponse();
			HttpEntity entity = aString.getEntity();
			InputStream is = entity.getContent();
			String reString = GOSHelper.convertStreamToString(is);
		
			if (reString == null || reString.equals("")) {
				
			}
			else {
				JSONObject jsonObject = JSON.parseObject(reString);
				String stateString = jsonObject.getString("state");
				if (stateString.equals("1")) {
					Log.v("测试查询信箱公示", "测试查询信箱公示成功");
					JSONArray responeArray = jsonObject.getJSONArray("responseList");
					if (responeArray.size() > 0) {
						JSONObject oneObjects = responeArray.getJSONObject(0);
						if (oneObjects == null) {
							showListHandler.sendEmptyMessage(0);
						}
						else {
							for (int i = 0; i < responeArray.size(); i++) {
								JSONObject oneObject = responeArray.getJSONObject(i);
								HashMap<String, String> map = new HashMap<String, String>();
								String shouxinrenString = oneObject.getString("receive").trim();
								map.put("receiver", shouxinrenString);
								String faxinrenString = oneObject.getString("sender").trim();
								map.put("sender", faxinrenString);
								String faxinriqiString = oneObject.getString("date").trim();
								map.put("date", faxinriqiString);
								String biaotiString = oneObject.getString("title").trim();
								map.put("title", biaotiString);
								String neirongString = oneObject.getString("description").trim();
								map.put("description", neirongString);
								mailDataArrayList.add(map);
							}
							showListHandler.sendEmptyMessage(1);
						}
					}
				}
				else
				{
					Log.v("测试信箱公示", "测试查询信箱公示失败");
					showListHandler.sendEmptyMessage(0);
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
	Handler showListHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 0:
				showListMsg("0");
				break;
			case 1:
				showListMsg("1");
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};
	public void showListMsg(String typeString) {
		if (typeString.equals("0")) {
			Toast.makeText(ChiefEmailActivity.this, "没有数据", Toast.LENGTH_SHORT).show();
		}
		else {
			MyMailListAdpater mMyMailListAdapter = new MyMailListAdpater();
			chiefMailReciverListView.setAdapter(mMyMailListAdapter);
		}
	}
	//数据源
	public class MyMailListAdpater extends BaseAdapter
	{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mailDataArrayList == null ? 0 : mailDataArrayList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View convertView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			MailDataListViewHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(getApplication()).inflate(R.layout.list_chiefemailcontent, null);
				holder = new MailDataListViewHolder();
				//取到各个控件的对象
				 holder.receiverTextView = (TextView)convertView.findViewById(R.id.receiverTextView);//收信人
				 holder.senderTextView = (TextView)convertView.findViewById(R.id.senderTextView);//发信人
				 holder.sendDateTextView = (TextView)convertView.findViewById(R.id.senderDateTextView);//发信日期
				 holder.mailTitleTextView = (TextView)convertView.findViewById(R.id.mailTitleTextView);//邮件标题
				 holder.mailContentTextView = (TextView)convertView.findViewById(R.id.mailContentTextView);//邮件内容
				 
				 convertView.setTag(holder);
			}
			else {
				holder = (MailDataListViewHolder)convertView.getTag();
			}
			holder.receiverTextView.setText(mailDataArrayList.get(arg0).get("receiver"));
			holder.senderTextView.setText(mailDataArrayList.get(arg0).get("sender"));
			holder.sendDateTextView.setText(mailDataArrayList.get(arg0).get("date"));
			holder.mailTitleTextView.setText(mailDataArrayList.get(arg0).get("title"));
			holder.mailContentTextView.setText(mailDataArrayList.get(arg0).get("description"));
			
			return convertView;
		}
	}
	//配件列表控件类
	/**存放控件*/
   public final class MailDataListViewHolder{
       public TextView 			receiverTextView;
       public TextView			senderTextView;
       public TextView 			sendDateTextView;
       public TextView			mailTitleTextView;
       public TextView 			mailContentTextView;
   }
	
	//初始化tabhost
	protected void initTabHost() {
		tabHost = (TabHost) findViewById(R.id.topChiefMailTabHost);  
        tabHost.setup();  
        TabWidget tabWidget = tabHost.getTabWidget();  
        
        tabHost.addTab(tabHost.newTabSpec("tab1")  
                .setIndicator("签写信件")  
                .setContent(R.id.chiefMailView1));   
          
        tabHost.addTab(tabHost.newTabSpec("tab2")  
                .setIndicator("来信公示")  
                .setContent(R.id.chiefMailView2));  
          
        final int tabs = tabWidget.getChildCount();  
        Log.v(TAG, "***tabWidget.getChildCount() : " + tabs);  
        
        //注意这个就是改变Tabhost默认样式的地方，一定将这部分代码放在上面这段代码的下面，不然样式改变不了
        updateTab(tabHost);
//				        for (int i =0; i < tabWidget.getChildCount(); i++) {  
//				         //修改Tabhost高度和宽度
//				         tabWidget.getChildAt(i).getLayoutParams().height = 60;  
//				         tabWidget.getChildAt(i).getLayoutParams().width = 65;
//				         //修改显示字体大小
//				         TextView tv = (TextView) tabWidget.getChildAt(i).findViewById(android.R.id.title);
//				         tv.setTextSize(15);
//				         //tv.setTextColor(this.getResources().getColorStateList(android.R.color.white));
//				        }
        
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
			   		 	chiefMailScrollView.setVisibility(View.VISIBLE);
			   		 	chiefMailQueryLineLayout.setVisibility(View.INVISIBLE);
			   		 break;
			   	 case 1:
			   		 	chiefMailScrollView.setVisibility(View.INVISIBLE);
			   		 	chiefMailQueryLineLayout.setVisibility(View.VISIBLE);
			   		 	new Thread()
			   		 	{
			   		 		public void run() {
								queryChiefMailBoxShow();
							}
			   		 	}.start();
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
