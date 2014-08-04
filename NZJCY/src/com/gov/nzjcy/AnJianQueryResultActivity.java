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
//import com.gov.nzjcy.InfoQueryActivity.InfoQueryDataListViewHolder;
//import com.gov.nzjcy.InfoQueryActivity.MyInfoQueryAdapter;
import com.gov.nzjcy.exception.BaseException;
import com.gov.nzjcy.exception.ParseException;
import com.gov.nzjcy.helper.GOSHelper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AnJianQueryResultActivity extends BaseActivity {
	
	/**打印标示*/
	private String	TAG = "AskForOpenActivity";
	/** head 头部 的左侧菜单 按钮*/
	private LinearLayout	top_left_Layout;
	/** head 头部 的右侧菜单 按钮*/
	private ImageButton		top_right_headImageButton;
	/**head的标题*/
	private TextView		topTitleTextView;
	/**选择的类型*/
	private int				chooseTypeInt = 0;
	/**查询结果显示*/
	private	ListView		infoQueryResultListView;
	/**等待*/
	private LinearLayout	infoQueryResultLinearLayout;
	/**数据数组*/
	private ArrayList<HashMap<String, String>>	infoQueryArrayList;
	/**单位*/
	String unitString;
	/**案件类型*/
	String tableNameString;
	/**姓名*/
	String userNameString;
	/**起始时间*/
	String startTimeString;
	/**结束时间*/
	String endTimeString;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_anjianqueryresult);
		infoQueryArrayList = new ArrayList<HashMap<String,String>>();
		unitString = getIntent().getStringExtra("unit");
		userNameString = getIntent().getStringExtra("name");
		tableNameString = getIntent().getStringExtra("table");
		
		startTimeString = getIntent().getStringExtra("startDate").replace("年", "-").replace("月", "-").replace("日", "");
		endTimeString = getIntent().getStringExtra("endDate").replace("年", "-").replace("月", "-").replace("日", "");

		initView();
		new Thread()
		{
			public void run() {
				requestInfoQuery(unitString, userNameString, startTimeString, endTimeString, tableNameString);
			}
		}.start();
	}
	
	//初始化界面
	protected void initView() {
		top_left_Layout = (LinearLayout)findViewById(R.id.leftPopLinearLayout);//左边的返回按钮
		topTitleTextView = (TextView)findViewById(R.id.topTitleTextview);
		topTitleTextView.setText("案件信息查询结果");//标题
		top_right_headImageButton = (ImageButton)findViewById(R.id.loginImageButton);//用户登录按钮
		//左边返回按钮的点击事件
		top_left_Layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AnJianQueryResultActivity.this.finish();
			}
		});
		//隐藏右边的按钮
		top_right_headImageButton.setVisibility(View.INVISIBLE);
		
		infoQueryResultListView = (ListView)findViewById(R.id.infoQueryResultListView);
		infoQueryResultLinearLayout = (LinearLayout)findViewById(R.id.infoQueryResultLinearLayout);
	}
	
	/**数据请求*/
    protected void requestInfoQuery(String unitString, String userNameString, String oneStartTimeString, String oneEndTimeString, String tableString) {

    	String tableNameString = "";
    	if (tableString.equals("审查起诉案件")) {
			tableNameString = "prosecution";
			chooseTypeInt = 0;
		}
    	else if (tableString.equals("抗诉案件")) {
    		tableNameString = "counterappeal";
    		chooseTypeInt = 1;
		}
    	else if (tableString.equals("审查逮捕案件")) {
    		tableNameString = "daibucase";
    		chooseTypeInt = 2;
		}
    	else if (tableString.equals("提请延长侦查羁押期限案件")) {
    		tableNameString = "yanyacase";
    		chooseTypeInt = 3;
		}
    	else if (tableString.equals("职务犯罪不立案案件")) {
    		tableNameString = "buliancase";
    		chooseTypeInt = 4;
		}
    	else if (tableString.equals("职务犯罪撤销案件")) {
    		tableNameString = "chexiaocase";
    		chooseTypeInt = 5;
		}
    	else if (tableString.equals("民行抗诉案件")) {
    		tableNameString = "minxingcase";
    		chooseTypeInt = 6;
		}
    	try {
			HttpResponse aString = gosHttpOperation.invokerInfoQueryResponse(unitString, tableNameString, oneStartTimeString, oneEndTimeString, userNameString);
			HttpEntity entity = aString.getEntity();
			InputStream is = entity.getContent();
			String reString = GOSHelper.convertStreamToString(is);
		
			if (reString == null || reString.equals("")) {
				showQueryViewHandler.sendEmptyMessage(0);
			}
			else {
				JSONObject jsonObject = JSON.parseObject(reString);
				String stateString = jsonObject.getString("state");
				if (stateString.equals("1")) {
					Log.v("测试案件信息查询", "测试案件信息查询成功");
					JSONArray responeArray = jsonObject.getJSONArray("responseList");
					
					if (responeArray.size() > 0) {
						for (int i = 0; i < responeArray.size(); i++) {
							JSONObject oneObject = responeArray.getJSONObject(i);
							anjianInfoQueryData(chooseTypeInt, oneObject);
						}
						showQueryViewHandler.sendEmptyMessage(1);
					}
					else {
						showQueryViewHandler.sendEmptyMessage(0);
					}
				}
				else
				{
					Log.v("测试案件信息查询", "测试案件信息查询失败");
					showQueryViewHandler.sendEmptyMessage(0);
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
    /**数据分析*/
    protected void anjianInfoQueryData(int chooseTyInt, JSONObject chooseJsonObject) {
		if (chooseTyInt == 0) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("name_undersuspicion", chooseJsonObject.getString("name_undersuspicion"));
			
			String dateString = chooseJsonObject.getString("time");
			if (dateString == null || dateString.equals("")) {
				map.put("time", dateString);
			}
			else {
				String formatStr="yyyy-MM-dd";//然后再格式化为想要的格式
		        String dateFromatStr="yyyy-MM-dd";//先把字符串按这个格式格式化为日期类型
		        String dateOneString=GOSHelper.StringToDate(dateString, dateFromatStr,formatStr);
				map.put("time", dateOneString);
			}
			
			String dateDeciString = chooseJsonObject.getString("date_decision");

			map.put("date_decision", dateDeciString);
			map.put("condition_examined", chooseJsonObject.getString("condition_examined"));
			map.put("accusation_examined", chooseJsonObject.getString("accusation_examined"));
			map.put("date_court", chooseJsonObject.getString("date_court"));
			map.put("sentence", chooseJsonObject.getString("sentence"));
			map.put("ifcounterappeal", chooseJsonObject.getString("ifcounterappeal"));
			infoQueryArrayList.add(map);
		}
		else if (chooseTyInt == 1) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("name_undersuspicion", chooseJsonObject.getString("name_undersuspicion"));
			map.put("nature_case", chooseJsonObject.getString("nature_case"));
			
			map.put("date_counterappeal", chooseJsonObject.getString("date_counterappeal"));
			map.put("sentence", chooseJsonObject.getString("sentence"));
			map.put("sentence_final", chooseJsonObject.getString("sentence_final"));
			infoQueryArrayList.add(map);
		}
		else if (chooseTyInt == 2) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("ryxm", chooseJsonObject.getString("ryxm"));
			
			String dateString = chooseJsonObject.getString("sasj");
			if (dateString == null || dateString.equals("")) {
				map.put("sasj", dateString);
			}
			else {
				String formatStr="yyyy-MM-dd";//然后再格式化为想要的格式
		        String dateFromatStr="yyyy-MM-dd";//先把字符串按这个格式格式化为日期类型
		        String dateOneString=GOSHelper.StringToDate(dateString, dateFromatStr,formatStr);
				map.put("sasj", dateOneString);
			}
			//map.put("sasj", chooseJsonObject.getString("sasj"));
			map.put("sjjg", chooseJsonObject.getString("sjjg"));
			map.put("sjsj", chooseJsonObject.getString("sjsj"));
			map.put("ajcbr", chooseJsonObject.getString("ajcbr"));

			infoQueryArrayList.add(map);
		}
		else if (chooseTyInt == 3) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("ryxm", chooseJsonObject.getString("ryxm"));
			map.put("pzhjddbsj", chooseJsonObject.getString("pzhjddbsj"));
			
			String dateString = chooseJsonObject.getString("tqyysj");
			if (dateString == null || dateString.equals("")) {
				map.put("tqyysj", dateString);
			}
			else {
				String formatStr="yyyy-MM-dd";//然后再格式化为想要的格式
		        String dateFromatStr="yyyy-MM-dd";//先把字符串按这个格式格式化为日期类型
		        String dateOneString=GOSHelper.StringToDate(dateString, dateFromatStr,formatStr);
				map.put("tqyysj", dateOneString);
			}
			
			//map.put("tqyysj", chooseJsonObject.getString("tqyysj"));
			map.put("yysj", chooseJsonObject.getString("yysj"));
			map.put("ajcbr", chooseJsonObject.getString("ajcbr"));
			
			infoQueryArrayList.add(map);
		}
		else if (chooseTyInt == 4) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("ryxm", chooseJsonObject.getString("ryxm"));
			map.put("sasj", chooseJsonObject.getString("sasj"));
			map.put("sjsj", chooseJsonObject.getString("sjsj"));
			map.put("sjjg", chooseJsonObject.getString("sjjg"));
			map.put("ajcbr", chooseJsonObject.getString("ajcbr"));

			infoQueryArrayList.add(map);
		}
		else if (chooseTyInt == 5) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("ryxm", chooseJsonObject.getString("ryxm"));
			map.put("time", chooseJsonObject.getString("time"));
			map.put("lasj", chooseJsonObject.getString("lasj"));
			map.put("pzhjddbsj", chooseJsonObject.getString("pzhjddbsj"));
			map.put("ajjd", chooseJsonObject.getString("accusation_examined"));
			map.put("ajcbr", chooseJsonObject.getString("ajcbr"));
			infoQueryArrayList.add(map);
		}
		else if (chooseTyInt == 6) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("xingming", chooseJsonObject.getString("xingming"));
			
			String dateString = chooseJsonObject.getString("sasj");
			if (dateString == null || dateString.equals("")) {
				map.put("sasj", dateString);
			}
			else {
				String formatStr="yyyy-MM-dd";//然后再格式化为想要的格式
		        String dateFromatStr="yyyy-MM-dd";//先把字符串按这个格式格式化为日期类型
		        String dateOneString=GOSHelper.StringToDate(dateString, dateFromatStr,formatStr);
				map.put("sasj", dateOneString);
			}
			
			//map.put("sasj", chooseJsonObject.getString("sasj"));
			map.put("ajxz", chooseJsonObject.getString("ajxz"));
			map.put("yspj", chooseJsonObject.getString("yspj"));
			map.put("ksrq", chooseJsonObject.getString("ksrq"));
			map.put("ajcbr", chooseJsonObject.getString("ajcbr"));

			infoQueryArrayList.add(map);
		}
	}
    
    /***/
	Handler showQueryViewHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 0:
				infoQueryResultLinearLayout.setVisibility(View.INVISIBLE);
				Toast.makeText(AnJianQueryResultActivity.this, "没有数据", Toast.LENGTH_SHORT).show();
				break;
			case 1:
				infoQueryResultLinearLayout.setVisibility(View.INVISIBLE);
				MyInfoQueryAdapter myCommitDataAdapter = new MyInfoQueryAdapter();
				infoQueryResultListView.setAdapter(myCommitDataAdapter);
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};
    
    /**数据源*/
    protected class MyInfoQueryAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return infoQueryArrayList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			InfoQueryDataListViewHolder 	holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(getApplication()).inflate(R.layout.list_infoqueryitem, null);
				holder = new InfoQueryDataListViewHolder();
				//取到各个控件的对象
				 holder.oneInfoTextView = (TextView)convertView.findViewById(R.id.listInfoQueryOneTextView);
				 holder.twoTextView = (TextView)convertView.findViewById(R.id.listInfoQueryTwoTextView);
				 holder.threeTextView = (TextView)convertView.findViewById(R.id.listInfoQueryThreeTextView);
				 holder.fourTextView = (TextView)convertView.findViewById(R.id.listInfoQueryFourTextView);
				 holder.fiveTextView = (TextView)convertView.findViewById(R.id.listInfoQueryFiveTextView);
				 holder.sixTextView = (TextView)convertView.findViewById(R.id.listInfoQuerySixTextView);
				 holder.sevenTextView = (TextView)convertView.findViewById(R.id.listInfoQuerySevenTextView);
				 holder.eightTextView = (TextView)convertView.findViewById(R.id.listInfoQueryEightTextView);
				 holder.nineTextView = (TextView)convertView.findViewById(R.id.listInfoQueryNineTextView);
				 holder.tenTextView = (TextView)convertView.findViewById(R.id.listInfoQueryTenTextView);
				 holder.elvenTextView = (TextView)convertView.findViewById(R.id.listInfoQueryElvenTextView);
				 holder.twelveTextView = (TextView)convertView.findViewById(R.id.listInfoQueryTwelveTextView);
				 holder.thirteenTextView = (TextView)convertView.findViewById(R.id.listInfoQueryThirteenTextView);
				 holder.fourteenTextView = (TextView)convertView.findViewById(R.id.listInfoQueryFourteenTextView);
				 holder.fifteenTextView = (TextView)convertView.findViewById(R.id.listInfoQueryFifteenTextView);
				 holder.sixTeenTextView = (TextView)convertView.findViewById(R.id.listInfoQuerySixteenTextView);
				
				 convertView.setTag(holder);
			}
			else {
				holder = (InfoQueryDataListViewHolder)convertView.getTag();
			}
			if (chooseTypeInt == 0) {
				holder.oneInfoTextView.setText("嫌疑人姓名");
				holder.twoTextView.setText(infoQueryArrayList.get(position).get("name_undersuspicion"));
				holder.threeTextView.setText("受案时间");
				holder.fourTextView.setText(infoQueryArrayList.get(position).get("time"));
				holder.fiveTextView.setText("审结时间");
				holder.sixTextView.setText(infoQueryArrayList.get(position).get("date_decision"));
				holder.sevenTextView.setText("审结情况");
				holder.eightTextView.setText(infoQueryArrayList.get(position).get("condition_examined"));
				holder.nineTextView.setText("审结认定罪名");
				holder.tenTextView.setText(infoQueryArrayList.get(position).get("accusation_examined"));
				holder.elvenTextView.setText("出庭时间");
				holder.twelveTextView.setText(infoQueryArrayList.get(position).get("date_court"));
				holder.thirteenTextView.setText("判决结果");
				holder.fourteenTextView.setText(infoQueryArrayList.get(position).get("sentence"));
				holder.fifteenTextView.setText("是否抗诉");
				holder.sixTeenTextView.setText(infoQueryArrayList.get(position).get("ifcounterappeal"));
			}
			else if (chooseTypeInt == 1) {
				holder.oneInfoTextView.setText("原审被告人姓名");
				holder.twoTextView.setText(infoQueryArrayList.get(position).get("name_undersuspicion"));
				holder.threeTextView.setText("案件性质");
				holder.fourTextView.setText(infoQueryArrayList.get(position).get("nature_case"));
				holder.fiveTextView.setText("通知阅卷时间");
				holder.sixTextView.setText(infoQueryArrayList.get(position).get("date_counterappeal"));
				holder.sevenTextView.setText("一审或生效判决结果");
				holder.eightTextView.setText(infoQueryArrayList.get(position).get("sentence"));
				holder.nineTextView.setText("二审或再审裁判结果");
				holder.tenTextView.setText(infoQueryArrayList.get(position).get("sentence_final"));
			}
			else if (chooseTypeInt == 2) {
				holder.oneInfoTextView.setText("犯罪嫌疑人姓名");
				holder.twoTextView.setText(infoQueryArrayList.get(position).get("ryxm"));
				holder.threeTextView.setText("受案时间");
				holder.fourTextView.setText(infoQueryArrayList.get(position).get("sasj"));
				holder.fiveTextView.setText("审结结果");
				holder.sixTextView.setText(infoQueryArrayList.get(position).get("sjjg"));
				holder.sevenTextView.setText("审结时间");
				holder.eightTextView.setText(infoQueryArrayList.get(position).get("sjsj"));
				holder.nineTextView.setText("案件承办人");
				holder.tenTextView.setText(infoQueryArrayList.get(position).get("ajcbr"));
			}
			else if (chooseTypeInt == 3) {
				holder.oneInfoTextView.setText("犯罪嫌疑人姓名");
				holder.twoTextView.setText(infoQueryArrayList.get(position).get("ryxm"));
				holder.threeTextView.setText("批准/决定逮捕时间");
				holder.fourTextView.setText(infoQueryArrayList.get(position).get("pzhjddbsj"));
				holder.fiveTextView.setText("受案时间");
				holder.sixTextView.setText(infoQueryArrayList.get(position).get("tqyysj"));
				holder.sevenTextView.setText("延押时间");
				holder.eightTextView.setText(infoQueryArrayList.get(position).get("yysj"));
				holder.nineTextView.setText("案件承办人");
				holder.tenTextView.setText(infoQueryArrayList.get(position).get("ajcbr"));

			}
			else if (chooseTypeInt == 4) {
				holder.oneInfoTextView.setText("犯罪嫌疑人姓名");
				holder.twoTextView.setText(infoQueryArrayList.get(position).get("ryxm"));
				holder.threeTextView.setText("受案时间");
				holder.fourTextView.setText(infoQueryArrayList.get(position).get("sasj"));
				holder.fiveTextView.setText("初查时间");
				holder.sixTextView.setText(infoQueryArrayList.get(position).get("sjsj"));
				holder.sevenTextView.setText("初查结果");
				holder.eightTextView.setText(infoQueryArrayList.get(position).get("sjjg"));
				holder.nineTextView.setText("案件承办人");
				holder.tenTextView.setText(infoQueryArrayList.get(position).get("ajcbr"));
			}
			else if (chooseTypeInt == 5) {
				holder.oneInfoTextView.setText("犯罪嫌疑人姓名");
				holder.twoTextView.setText(infoQueryArrayList.get(position).get("ryxm"));
				holder.threeTextView.setText("立案时间");
				holder.fourTextView.setText(infoQueryArrayList.get(position).get("lasj"));
				holder.fiveTextView.setText("侦结时间");
				holder.sixTextView.setText(infoQueryArrayList.get(position).get("pzhjddbsj"));
				holder.sevenTextView.setText("侦结处理结果");
				holder.eightTextView.setText(infoQueryArrayList.get(position).get("ajjd"));
				holder.nineTextView.setText("案件承办人");
				holder.tenTextView.setText(infoQueryArrayList.get(position).get("ajcbr"));

			}
			else if (chooseTypeInt == 6) {
				holder.oneInfoTextView.setText("姓名/单位");
				holder.twoTextView.setText(infoQueryArrayList.get(position).get("xingming"));
				holder.threeTextView.setText("受理时间");
				holder.fourTextView.setText(infoQueryArrayList.get(position).get("sasj"));
				holder.fiveTextView.setText("案件性质");
				holder.sixTextView.setText(infoQueryArrayList.get(position).get("ajxz"));
				holder.sevenTextView.setText("原审判决");
				holder.eightTextView.setText(infoQueryArrayList.get(position).get("yspj"));
				holder.nineTextView.setText("提出抗诉日期");
				holder.tenTextView.setText(infoQueryArrayList.get(position).get("ksrq"));
				holder.elvenTextView.setText("案件承办人");
				holder.twelveTextView.setText(infoQueryArrayList.get(position).get("ajcbr"));

			}
			
			return convertView;
		}
    	
    }
    /**控件*/
    public final class InfoQueryDataListViewHolder{
        public TextView 		oneInfoTextView;
        public TextView			twoTextView;
        public TextView 		threeTextView;
        public TextView			fourTextView;
        public TextView 		fiveTextView;
        public TextView			sixTextView;
        public TextView			sevenTextView;
        public TextView			eightTextView;
        public TextView			nineTextView;
        public TextView			tenTextView;
        public TextView			elvenTextView;
        public TextView			twelveTextView;
        public TextView			thirteenTextView;
        public TextView			fourteenTextView;
        public TextView			fifteenTextView;
        public TextView			sixTeenTextView;
    }
	
}
