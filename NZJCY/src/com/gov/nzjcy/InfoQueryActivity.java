package com.gov.nzjcy;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gov.nzjcy.AskForOpenActivity.RadioClickListener;
import com.gov.nzjcy.AskForOpenActivity.RadioOnClick;
import com.gov.nzjcy.MyCommitActivity.CommitDataListViewHolder;
import com.gov.nzjcy.exception.BaseException;
import com.gov.nzjcy.exception.ParseException;
import com.gov.nzjcy.helper.GOSHelper;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;

public class InfoQueryActivity extends BaseActivity {
	/**打印标示*/
	private String	TAG = "InfoQueryActivity";
	/** head 头部 的左侧菜单 按钮*/
	private LinearLayout	top_left_Layout;
	/** head 头部 的右侧菜单 按钮*/
	private ImageButton		top_right_headImageButton;
	/**head的标题*/
	private TextView		topTitleTextView;
	/**tabhost的定义*/
	private TabHost 		tabHost;
	/**登陆项*/
	private LinearLayout	topInfoQueryOneLinearLayout;
	/**注册项*/
	private RelativeLayout	topInfoQueryTwoRelativeLayout;
	/**当前选中的项*/
	private int				currentSelectItem = 0;//当前选中的项
	private RadioOnClick OnClick = new RadioOnClick(0);
	/**承办单位*/
	private TextView		infoQueryUnitTextView;
	/**承办单位数据数组*/
	private String[] 		unitStrings = new String[]{"全市","市院", "宛城区院","卧龙区院","邓州市院","桐柏县院",
			"镇平县院","方城县院", "新野县院","内乡县院","浙川县院","西峡县院","杜旗县院","唐河县院","南召县院"};
	/**案件类型*/
	private TextView		infoQueryClassifyTextView;
	/**案件类型数据数组*/
	private String[] 		anjianTypeStrings = new String[]{"审查起诉案件","抗诉案件","审查逮捕案件","提请延长侦查羁押期限案件","职务犯罪不立案案件","职务犯罪撤销案件", 
	"民行抗诉案件"};
	
	/**查询结果显示*/
	private	ListView		infoQueryResultListView;
	/**等待*/
	private LinearLayout	infoQueryResultLinearLayout;
	/**数据数组*/
	private ArrayList<HashMap<String, String>>	infoQueryArrayList;
	/**选择的类型*/
	private int				chooseTypeInt = 0;
	
	
	//当前选用的项
	private int currentSelectDatePicker = 0;
	//用来保存年月日：  
    private int mYear;  
    private int mMonth;  
    private int mDay;  
    //声明一个独一无二的标识，来作为要显示DatePicker的Dialog的ID：  
    static final int DATE_DIALOG_ID = 0; 
	
	/**案件查询起始日期*/
	private TextView		infoQueryDateStartTextView;
	/**案件查询结束日期*/
	private	TextView		infoQueryDateEndTextView;
	/**案件相关人姓名*/
	private EditText		infoQueryUserNameEditText;
	/**查询按钮*/
	private Button			infoQueryResultButton;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_infoquery);
		infoQueryArrayList = new ArrayList<HashMap<String,String>>();
		initView();
		initInputData();
	}
	//初始化界面
	protected void initView() {
		top_left_Layout = (LinearLayout)findViewById(R.id.leftPopLinearLayout);//左边的返回按钮
		topTitleTextView = (TextView)findViewById(R.id.topTitleTextview);
		topTitleTextView.setText("案件信息查询");//标题
		top_right_headImageButton = (ImageButton)findViewById(R.id.loginImageButton);//用户登录按钮
		//左边返回按钮的点击事件
		top_left_Layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				InfoQueryActivity.this.finish();
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
		//initTabHost();
		topInfoQueryOneLinearLayout = (LinearLayout)findViewById(R.id.topInfoQueryOneLinearLayout);
		//topInfoQueryTwoRelativeLayout = (RelativeLayout)findViewById(R.id.topInfoQueryTwoRelativeLayout);
		infoQueryResultButton = (Button)findViewById(R.id.infoQueryResultButton);
		infoQueryResultButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				new Thread()
//				{
//					public void run() {
//						requestInfoQuery();
//					}
//				}.start();
				String unitString = infoQueryUnitTextView.getText().toString();
		    	String userNameString = infoQueryUserNameEditText.getText().toString();
		    	String tableString = infoQueryClassifyTextView.getText().toString();
		    	String startDateString = infoQueryDateStartTextView.getText().toString();
		    	String endDateString = infoQueryDateEndTextView.getText().toString();
		    	
				Intent oneIntent = new Intent(InfoQueryActivity.this, AnJianQueryResultActivity.class);
				oneIntent.putExtra("unit", unitString);
				oneIntent.putExtra("name", userNameString);
				oneIntent.putExtra("table", tableString);
				oneIntent.putExtra("startDate", startDateString);
				oneIntent.putExtra("endDate", endDateString);
				
				InfoQueryActivity.this.startActivity(oneIntent);
			}
		});
	}
	/**初始化输入数据框*/
	protected void initInputData() {
		infoQueryUnitTextView = (TextView)findViewById(R.id.infoQueryUnitTextView);
		infoQueryUnitTextView.setOnClickListener(new RadioClickListener());
		infoQueryClassifyTextView = (TextView)findViewById(R.id.infoQueryClassifyTextView);
		infoQueryClassifyTextView.setOnClickListener(new RadioClickListener());
		
		infoQueryDateStartTextView = (TextView)findViewById(R.id.infoQueryDateStartTextView);
		infoQueryDateStartTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
                //调用Activity类的方法来显示Dialog:调用这个方法会允许Activity管理该Dialog的生命周期，  
                //并会调用 onCreateDialog(int)回调函数来请求一个Dialog  
				currentSelectDatePicker = 0;
                showDialog(DATE_DIALOG_ID);  
			}
		});
		//获得当前的日期：  
        final Calendar currentDate = Calendar.getInstance();  
        mYear = currentDate.get(Calendar.YEAR);  
        mMonth = currentDate.get(Calendar.MONTH);  
        mDay = currentDate.get(Calendar.DAY_OF_MONTH);  
//        //设置文本的内容：  
//        infoQueryDateStartTextView.setText(new StringBuilder()  
//                    .append(mYear).append("年")  
//                    .append(mMonth + 1).append("月")//得到的月份+1，因为从0开始  
//                    .append(mDay).append("日"));
		
		infoQueryDateEndTextView = (TextView)findViewById(R.id.infoQueryDateEndTextView);
		infoQueryDateEndTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
                //调用Activity类的方法来显示Dialog:调用这个方法会允许Activity管理该Dialog的生命周期，  
                //并会调用 onCreateDialog(int)回调函数来请求一个Dialog  
				currentSelectDatePicker = 1;
                showDialog(DATE_DIALOG_ID);  
			}
		});
		//获得当前的日期：  
        final Calendar currentDateOne = Calendar.getInstance();  
        mYear = currentDateOne.get(Calendar.YEAR);  
        mMonth = currentDateOne.get(Calendar.MONTH);  
        mDay = currentDateOne.get(Calendar.DAY_OF_MONTH);  
//        //设置文本的内容：  
//        infoQueryDateEndTextView.setText(new StringBuilder()  
//                    .append(mYear).append("年")  
//                    .append(mMonth + 1).append("月")//得到的月份+1，因为从0开始  
//                    .append(mDay).append("日"));
		
		
		infoQueryUserNameEditText = (EditText)findViewById(R.id.infoQueryUserNameEditText);
		
		//查询结果
		infoQueryResultListView = (ListView)findViewById(R.id.infoQueryResultListView);
		infoQueryResultLinearLayout = (LinearLayout)findViewById(R.id.infoQueryResultLinearLayout);
	}
	//
	class RadioClickListener implements OnClickListener {
		   @Override
		   public void onClick(View v) {
			   switch (v.getId()) {
					case R.id.infoQueryUnitTextView:
						currentSelectItem = 0;
						AlertDialog ad =new AlertDialog.Builder(InfoQueryActivity.this).setTitle("")
					    .setSingleChoiceItems(unitStrings, OnClick.getIndex(),new RadioOnClick(0)).create();
					    ad.show();
						break;
					case R.id.infoQueryClassifyTextView:
						currentSelectItem = 1;
						AlertDialog adOne =new AlertDialog.Builder(InfoQueryActivity.this).setTitle("")
					    .setSingleChoiceItems(anjianTypeStrings, OnClick.getIndex(),new RadioOnClick(0)).create();
						adOne.show();
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
			   		infoQueryUnitTextView.setText(unitStrings[index]);
			   		break;
			   	case 1:
			   		//Toast.makeText(AskForOpenActivity.this, "您已经选择了 " +  ":" + infoNotifications[index], Toast.LENGTH_LONG).show();
			   		infoQueryClassifyTextView.setText(anjianTypeStrings[index]);
			   		break;
			    default:
				    break;
			}
		   dialog.dismiss();
		   
	   }
	 }
    
  //需要定义弹出的DatePicker对话框的事件监听器：  
    private DatePickerDialog.OnDateSetListener mDateSetListener =new OnDateSetListener() {  
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {  
        	
            mYear = year;  
            mMonth = monthOfYear;  
            mDay = dayOfMonth;
            //设置文本的内容：  
            if (currentSelectDatePicker == 0) {
            	infoQueryDateStartTextView.setText(new StringBuilder()  
                .append(mYear).append("年")  
                .append(mMonth + 1).append("月")//得到的月份+1，因为从0开始  
                .append(mDay).append("日"));  
			}
            else if (currentSelectDatePicker == 1) {
            	infoQueryDateEndTextView.setText(new StringBuilder()  
                .append(mYear).append("年")  
                .append(mMonth + 1).append("月")//得到的月份+1，因为从0开始  
                .append(mDay).append("日")); 
			}
            
        }  
    }; 
    /** 
     * 当Activity调用showDialog函数时会触发该函数的调用： 
     */  
    @Override  
    protected Dialog onCreateDialog(int id) {  
       switch (id) {  
        case DATE_DIALOG_ID:  
            return new DatePickerDialog(InfoQueryActivity.this, mDateSetListener, mYear, mMonth, mDay);  
        }  
        return null;  
    }
    
	//初始化tabhost
//	protected void initTabHost() {
//		tabHost = (TabHost) findViewById(R.id.topInfoQueryTabHost);  
//        tabHost.setup();  
//        TabWidget tabWidget = tabHost.getTabWidget();  
//        
//        tabHost.addTab(tabHost.newTabSpec("tab1")  
//                .setIndicator("查询", getResources().getDrawable(R.drawable.ic_launcher))  
//                .setContent(R.id.infoQueryView1));   
//          
//        tabHost.addTab(tabHost.newTabSpec("tab2")  
//                .setIndicator("结果")  
//                .setContent(R.id.infoQueryView2));  
//          
//        final int tabs = tabWidget.getChildCount();  
//        Log.v(TAG, "***tabWidget.getChildCount() : " + tabs);  
//        
//        //注意这个就是改变Tabhost默认样式的地方，一定将这部分代码放在上面这段代码的下面，不然样式改变不了
//        updateTab(tabHost);
////		        for (int i =0; i < tabWidget.getChildCount(); i++) {  
////		         //修改Tabhost高度和宽度
////		         tabWidget.getChildAt(i).getLayoutParams().height = 60;  
////		         tabWidget.getChildAt(i).getLayoutParams().width = 65;
////		         //修改显示字体大小
////		         TextView tv = (TextView) tabWidget.getChildAt(i).findViewById(android.R.id.title);
////		         tv.setTextSize(15);
////		         //tv.setTextColor(this.getResources().getColorStateList(android.R.color.white));
////		        }
//        
//        tabHost.setOnTabChangedListener(new OnTabChangedListener()); // 选择监听器
//	}
//	/**tab变化时的监听*/
//	class OnTabChangedListener implements OnTabChangeListener {
//
//		  @Override
//		  public void onTabChanged(String tabId) {
//			   tabHost.setCurrentTabByTag(tabId);
//			   System.out.println("tabid " + tabId);
//			   System.out.println("curreny after: " + tabHost.getCurrentTabTag());
//			   Log.v(TAG, "tabid " + tabId);
//			   Log.v(TAG, "curreny after: " + tabHost.getCurrentTabTag());
//			   updateTab(tabHost);
//			   switch (tabHost.getCurrentTab()) {
//			   	 case 0:
//			   		 	topInfoQueryOneLinearLayout.setVisibility(View.VISIBLE);
//			   		 	topInfoQueryTwoRelativeLayout.setVisibility(View.INVISIBLE);
//			   		 break;
//			   	 case 1:
//			   		 	topInfoQueryOneLinearLayout.setVisibility(View.INVISIBLE);
//			   		 	topInfoQueryTwoRelativeLayout.setVisibility(View.VISIBLE);
//			   		 break;
//			   	 default:
//			   		 break;
//			   }
//		  }
//	}
//	/**
//     * 更新Tab标签的颜色，和字体的颜色
//     * @param tabHost
//     */
//    private void updateTab(final TabHost tabHost) {
//        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
//            View view = tabHost.getTabWidget().getChildAt(i);
//            //修改Tabhost高度和宽度
//            view.getLayoutParams().height = 60;  
//            view.getLayoutParams().width = 65;
//            
//            TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
//            tv.setTextSize(15);
//            tv.setTypeface(Typeface.SERIF, 2); // 设置字体和风格
//            if (tabHost.getCurrentTab() == i) {//选中
//                //view.setBackgroundDrawable(getResources().getDrawable(R.drawable.category_current));//选中后的背景 
//            	int defColor = Color.rgb(66, 139, 202);
//            	tv.setTextColor(defColor);
//            } else {//不选中
//                //view.setBackgroundDrawable(getResources().getDrawable(R.drawable.category_bg));//非选择的背景
//                tv.setTextColor(this.getResources().getColorStateList(android.R.color.black));
//            }
//        }
//    }
    /**数据请求*/
//    protected void requestInfoQuery() {
//    	
//    	String unitString = infoQueryUnitTextView.getText().toString();
//    	String userNameString = infoQueryUserNameEditText.getText().toString();
//    	String tableString = infoQueryClassifyTextView.getText().toString();
//    	String tableNameString = "";
//    	if (tableString.equals("审查起诉案件")) {
//			tableNameString = "prosecution";
//			chooseTypeInt = 0;
//		}
//    	else if (tableString.equals("抗诉案件")) {
//    		tableNameString = "counterappeal";
//    		chooseTypeInt = 1;
//		}
//    	else if (tableString.equals("审查逮捕案件")) {
//    		tableNameString = "daibucase";
//    		chooseTypeInt = 2;
//		}
//    	else if (tableString.equals("提请延长侦查羁押期限案件")) {
//    		tableNameString = "yanyacase";
//    		chooseTypeInt = 3;
//		}
//    	else if (tableString.equals("职务犯罪不立案案件")) {
//    		tableNameString = "buliancase";
//    		chooseTypeInt = 4;
//		}
//    	else if (tableString.equals("职务犯罪撤销案件")) {
//    		tableNameString = "chexiaocase";
//    		chooseTypeInt = 5;
//		}
//    	else if (tableString.equals("民行抗诉案件")) {
//    		tableNameString = "minxingcase";
//    		chooseTypeInt = 6;
//		}
//    	try {
//			HttpResponse aString = gosHttpOperation.invokerInfoQueryResponse(unitString, tableNameString, userNameString);
//			HttpEntity entity = aString.getEntity();
//			InputStream is = entity.getContent();
//			String reString = GOSHelper.convertStreamToString(is);
//		
//			if (reString == null || reString.equals("")) {
//				showQueryViewHandler.sendEmptyMessage(0);
//			}
//			else {
//				JSONObject jsonObject = JSON.parseObject(reString);
//				String stateString = jsonObject.getString("state");
//				if (stateString.equals("1")) {
//					Log.v("测试案件信息查询", "测试案件信息查询成功");
//					JSONArray responeArray = jsonObject.getJSONArray("responseList");
//					
//					if (responeArray.size() > 0) {
//						for (int i = 0; i < responeArray.size(); i++) {
//							JSONObject oneObject = responeArray.getJSONObject(i);
//							anjianInfoQueryData(chooseTypeInt, oneObject);
//						}
//						showQueryViewHandler.sendEmptyMessage(1);
//					}
//					else {
//						showQueryViewHandler.sendEmptyMessage(0);
//					}
//				}
//				else
//				{
//					Log.v("测试案件信息查询", "测试案件信息查询失败");
//					showQueryViewHandler.sendEmptyMessage(0);
//				}
//			}
//		} catch (ParseException e) {
//			e.printStackTrace();
//		} catch (BaseException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//    /**数据分析*/
//    protected void anjianInfoQueryData(int chooseTyInt, JSONObject chooseJsonObject) {
//		if (chooseTyInt == 0) {
//			HashMap<String, String> map = new HashMap<String, String>();
//			map.put("name_undersuspicion", chooseJsonObject.getString("name_undersuspicion"));
//			String dateString = chooseJsonObject.getString("time");
//			
//	        String formatStr="yyyyMMdd";//然后再格式化为想要的格式
//	        String dateFromatStr="yyyy-MM-dd";//先把字符串按这个格式格式化为日期类型
//	        String dateOneString=GOSHelper.StringToDate(dateString, dateFromatStr,formatStr);
//			map.put("time", dateOneString);
//			map.put("date_decision", chooseJsonObject.getString("date_decision"));
//			map.put("condition_examined", chooseJsonObject.getString("condition_examined"));
//			map.put("accusation_examined", chooseJsonObject.getString("accusation_examined"));
//			map.put("date_court", chooseJsonObject.getString("date_court"));
//			map.put("sentence", chooseJsonObject.getString("sentence"));
//			map.put("ifcounterappeal", chooseJsonObject.getString("ifcounterappeal"));
//			infoQueryArrayList.add(map);
//		}
//		else if (chooseTyInt == 1) {
//			HashMap<String, String> map = new HashMap<String, String>();
//			map.put("name_undersuspicion", chooseJsonObject.getString("name_undersuspicion"));
//			map.put("nature_case", chooseJsonObject.getString("nature_case"));
//			map.put("date_counterappeal", chooseJsonObject.getString("date_counterappeal"));
//			map.put("sentence", chooseJsonObject.getString("sentence"));
//			map.put("sentence_final", chooseJsonObject.getString("sentence_final"));
//			infoQueryArrayList.add(map);
//		}
//		else if (chooseTyInt == 2) {
//			HashMap<String, String> map = new HashMap<String, String>();
//			map.put("ryxm", chooseJsonObject.getString("ryxm"));
//			map.put("sasj", chooseJsonObject.getString("sasj"));
//			map.put("sjjg", chooseJsonObject.getString("sjjg"));
//			map.put("sjsj", chooseJsonObject.getString("sjsj"));
//			map.put("ajcbr", chooseJsonObject.getString("ajcbr"));
//
//			infoQueryArrayList.add(map);
//		}
//		else if (chooseTyInt == 3) {
//			HashMap<String, String> map = new HashMap<String, String>();
//			map.put("ryxm", chooseJsonObject.getString("ryxm"));
//			map.put("pzhjddbsj", chooseJsonObject.getString("pzhjddbsj"));
//			map.put("tqyysj", chooseJsonObject.getString("tqyysj"));
//			map.put("yysj", chooseJsonObject.getString("yysj"));
//			map.put("ajcbr", chooseJsonObject.getString("ajcbr"));
//			
//			infoQueryArrayList.add(map);
//		}
//		else if (chooseTyInt == 4) {
//			HashMap<String, String> map = new HashMap<String, String>();
//			map.put("ryxm", chooseJsonObject.getString("ryxm"));
//			map.put("sasj", chooseJsonObject.getString("sasj"));
//			map.put("sjsj", chooseJsonObject.getString("sjsj"));
//			map.put("sjjg", chooseJsonObject.getString("sjjg"));
//			map.put("ajcbr", chooseJsonObject.getString("ajcbr"));
//
//			infoQueryArrayList.add(map);
//		}
//		else if (chooseTyInt == 5) {
//			HashMap<String, String> map = new HashMap<String, String>();
//			map.put("ryxm", chooseJsonObject.getString("ryxm"));
//			map.put("time", chooseJsonObject.getString("time"));
//			map.put("lasj", chooseJsonObject.getString("lasj"));
//			map.put("pzhjddbsj", chooseJsonObject.getString("pzhjddbsj"));
//			map.put("ajjd", chooseJsonObject.getString("accusation_examined"));
//			map.put("ajcbr", chooseJsonObject.getString("ajcbr"));
//			infoQueryArrayList.add(map);
//		}
//		else if (chooseTyInt == 6) {
//			HashMap<String, String> map = new HashMap<String, String>();
//			map.put("xingming", chooseJsonObject.getString("xingming"));
//			map.put("sasj", chooseJsonObject.getString("sasj"));
//			map.put("ajxz", chooseJsonObject.getString("ajxz"));
//			map.put("yspj", chooseJsonObject.getString("yspj"));
//			map.put("ksrq", chooseJsonObject.getString("ksrq"));
//			map.put("ajcbr", chooseJsonObject.getString("ajcbr"));
//
//			infoQueryArrayList.add(map);
//		}
//	}
//    
//    /***/
//	Handler showQueryViewHandler = new Handler()
//	{
//		@Override
//		public void handleMessage(Message msg) {
//			// TODO Auto-generated method stub
//			switch (msg.what) {
//			case 0:
//				infoQueryResultLinearLayout.setVisibility(View.INVISIBLE);
//				Toast.makeText(InfoQueryActivity.this, "没有数据", Toast.LENGTH_SHORT).show();
//				break;
//			case 1:
//				infoQueryResultLinearLayout.setVisibility(View.INVISIBLE);
//				MyInfoQueryAdapter myCommitDataAdapter = new MyInfoQueryAdapter();
//				infoQueryResultListView.setAdapter(myCommitDataAdapter);
//				break;
//			default:
//				break;
//			}
//			super.handleMessage(msg);
//		}
//	};
//    
//    /**数据源*/
//    protected class MyInfoQueryAdapter extends BaseAdapter{
//
//		@Override
//		public int getCount() {
//			// TODO Auto-generated method stub
//			return infoQueryArrayList.size();
//		}
//
//		@Override
//		public Object getItem(int position) {
//			// TODO Auto-generated method stub
//			return position;
//		}
//
//		@Override
//		public long getItemId(int position) {
//			// TODO Auto-generated method stub
//			return position;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			// TODO Auto-generated method stub
//			InfoQueryDataListViewHolder 	holder;
//			if (convertView == null) {
//				convertView = LayoutInflater.from(getApplication()).inflate(R.layout.list_mycommititem, null);
//				holder = new InfoQueryDataListViewHolder();
//				//取到各个控件的对象
//				 holder.oneInfoTextView = (TextView)convertView.findViewById(R.id.listInfoQueryOneTextView);
//				 holder.twoTextView = (TextView)convertView.findViewById(R.id.listInfoQueryTwoTextView);
//				 holder.threeTextView = (TextView)convertView.findViewById(R.id.listInfoQueryThreeTextView);
//				 holder.fourTextView = (TextView)convertView.findViewById(R.id.listInfoQueryFourTextView);
//				 holder.fiveTextView = (TextView)convertView.findViewById(R.id.listInfoQueryFiveTextView);
//				 holder.sixTextView = (TextView)convertView.findViewById(R.id.listInfoQuerySixTextView);
//				 holder.sevenTextView = (TextView)convertView.findViewById(R.id.listInfoQuerySevenTextView);
//				 holder.eightTextView = (TextView)convertView.findViewById(R.id.listInfoQueryEightTextView);
//				 holder.nineTextView = (TextView)convertView.findViewById(R.id.listInfoQueryNineTextView);
//				 holder.tenTextView = (TextView)convertView.findViewById(R.id.listInfoQueryTenTextView);
//				 holder.elvenTextView = (TextView)convertView.findViewById(R.id.listInfoQueryElvenTextView);
//				 holder.twelveTextView = (TextView)convertView.findViewById(R.id.listInfoQueryTwelveTextView);
//				 holder.thirteenTextView = (TextView)convertView.findViewById(R.id.listInfoQueryThirteenTextView);
//				 holder.fourteenTextView = (TextView)convertView.findViewById(R.id.listInfoQueryFourteenTextView);
//				 holder.fifteenTextView = (TextView)convertView.findViewById(R.id.listInfoQueryFifteenTextView);
//				 holder.sixTeenTextView = (TextView)convertView.findViewById(R.id.listInfoQuerySixteenTextView);
//				
//				 convertView.setTag(holder);
//			}
//			else {
//				holder = (InfoQueryDataListViewHolder)convertView.getTag();
//			}
//			if (chooseTypeInt == 0) {
//				holder.oneInfoTextView.setText("嫌疑人姓名");
//				holder.twoTextView.setText(infoQueryArrayList.get(position).get("name_undersuspicion"));
//				holder.threeTextView.setText("受案时间");
//				holder.fourTextView.setText(infoQueryArrayList.get(position).get("time"));
//				holder.fiveTextView.setText("审结时间");
//				holder.sixTextView.setText(infoQueryArrayList.get(position).get("date_decision"));
//				holder.sevenTextView.setText("审结情况");
//				holder.eightTextView.setText(infoQueryArrayList.get(position).get("condition_examined"));
//				holder.nineTextView.setText("审结认定罪名");
//				holder.tenTextView.setText(infoQueryArrayList.get(position).get("accusation_examined"));
//				holder.elvenTextView.setText("出庭时间");
//				holder.twelveTextView.setText(infoQueryArrayList.get(position).get("date_court"));
//				holder.thirteenTextView.setText("判决结果");
//				holder.fourteenTextView.setText(infoQueryArrayList.get(position).get("sentence"));
//				holder.fifteenTextView.setText("是否抗诉");
//				holder.sixTeenTextView.setText(infoQueryArrayList.get(position).get("ifcounterappeal"));
//			}
//			else if (chooseTypeInt == 1) {
//				holder.oneInfoTextView.setText("原审被告人姓名");
//				holder.twoTextView.setText(infoQueryArrayList.get(position).get("name_undersuspicion"));
//				holder.threeTextView.setText("案件性质");
//				holder.fourTextView.setText(infoQueryArrayList.get(position).get("nature_case"));
//				holder.fiveTextView.setText("通知阅卷时间");
//				holder.sixTextView.setText(infoQueryArrayList.get(position).get("date_counterappeal"));
//				holder.sevenTextView.setText("一审或生效判决结果");
//				holder.eightTextView.setText(infoQueryArrayList.get(position).get("sentence"));
//				holder.nineTextView.setText("二审或再审裁判结果");
//				holder.tenTextView.setText(infoQueryArrayList.get(position).get("sentence_final"));
//			}
//			else if (chooseTypeInt == 2) {
//				holder.oneInfoTextView.setText("犯罪嫌疑人姓名");
//				holder.twoTextView.setText(infoQueryArrayList.get(position).get("ryxm"));
//				holder.threeTextView.setText("受案时间");
//				holder.fourTextView.setText(infoQueryArrayList.get(position).get("sasj"));
//				holder.fiveTextView.setText("审结结果");
//				holder.sixTextView.setText(infoQueryArrayList.get(position).get("sjjg"));
//				holder.sevenTextView.setText("审结时间");
//				holder.eightTextView.setText(infoQueryArrayList.get(position).get("sjsj"));
//				holder.nineTextView.setText("案件承办人");
//				holder.tenTextView.setText(infoQueryArrayList.get(position).get("ajcbr"));
//			}
//			else if (chooseTypeInt == 3) {
//				holder.oneInfoTextView.setText("犯罪嫌疑人姓名");
//				holder.twoTextView.setText(infoQueryArrayList.get(position).get("ryxm"));
//				holder.threeTextView.setText("批准/决定逮捕时间");
//				holder.fourTextView.setText(infoQueryArrayList.get(position).get("pzhjddbsj"));
//				holder.fiveTextView.setText("受案时间");
//				holder.sixTextView.setText(infoQueryArrayList.get(position).get("tqyysj"));
//				holder.sevenTextView.setText("延押时间");
//				holder.eightTextView.setText(infoQueryArrayList.get(position).get("yysj"));
//				holder.nineTextView.setText("案件承办人");
//				holder.tenTextView.setText(infoQueryArrayList.get(position).get("ajcbr"));
//
//			}
//			else if (chooseTypeInt == 4) {
//				holder.oneInfoTextView.setText("犯罪嫌疑人姓名");
//				holder.twoTextView.setText(infoQueryArrayList.get(position).get("ryxm"));
//				holder.threeTextView.setText("受案时间");
//				holder.fourTextView.setText(infoQueryArrayList.get(position).get("sasj"));
//				holder.fiveTextView.setText("初查时间");
//				holder.sixTextView.setText(infoQueryArrayList.get(position).get("sjsj"));
//				holder.sevenTextView.setText("初查结果");
//				holder.eightTextView.setText(infoQueryArrayList.get(position).get("sjjg"));
//				holder.nineTextView.setText("案件承办人");
//				holder.tenTextView.setText(infoQueryArrayList.get(position).get("ajcbr"));
//			}
//			else if (chooseTypeInt == 5) {
//				holder.oneInfoTextView.setText("犯罪嫌疑人姓名");
//				holder.twoTextView.setText(infoQueryArrayList.get(position).get("ryxm"));
//				holder.threeTextView.setText("立案时间");
//				holder.fourTextView.setText(infoQueryArrayList.get(position).get("lasj"));
//				holder.fiveTextView.setText("侦结时间");
//				holder.sixTextView.setText(infoQueryArrayList.get(position).get("pzhjddbsj"));
//				holder.sevenTextView.setText("侦结处理结果");
//				holder.eightTextView.setText(infoQueryArrayList.get(position).get("ajjd"));
//				holder.nineTextView.setText("案件承办人");
//				holder.tenTextView.setText(infoQueryArrayList.get(position).get("ajcbr"));
//
//			}
//			else if (chooseTypeInt == 6) {
//				holder.oneInfoTextView.setText("姓名/单位");
//				holder.twoTextView.setText(infoQueryArrayList.get(position).get("xingming"));
//				holder.threeTextView.setText("受理时间");
//				holder.fourTextView.setText(infoQueryArrayList.get(position).get("sasj"));
//				holder.fiveTextView.setText("案件性质");
//				holder.sixTextView.setText(infoQueryArrayList.get(position).get("ajxz"));
//				holder.sevenTextView.setText("原审判决");
//				holder.eightTextView.setText(infoQueryArrayList.get(position).get("yspj"));
//				holder.nineTextView.setText("提出抗诉日期");
//				holder.tenTextView.setText(infoQueryArrayList.get(position).get("ksrq"));
//				holder.elvenTextView.setText("案件承办人");
//				holder.twelveTextView.setText(infoQueryArrayList.get(position).get("ajcbr"));
//
//			}
//			
//			return convertView;
//		}
//    	
//    }
//    /**控件*/
//    public final class InfoQueryDataListViewHolder{
//        public TextView 		oneInfoTextView;
//        public TextView			twoTextView;
//        public TextView 		threeTextView;
//        public TextView			fourTextView;
//        public TextView 		fiveTextView;
//        public TextView			sixTextView;
//        public TextView			sevenTextView;
//        public TextView			eightTextView;
//        public TextView			nineTextView;
//        public TextView			tenTextView;
//        public TextView			elvenTextView;
//        public TextView			twelveTextView;
//        public TextView			thirteenTextView;
//        public TextView			fourteenTextView;
//        public TextView			fifteenTextView;
//        public TextView			sixTeenTextView;
//    }
	
}
