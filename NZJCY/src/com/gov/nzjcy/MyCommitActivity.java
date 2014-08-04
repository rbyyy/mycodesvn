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
import com.gov.nzjcy.exception.BaseException;
import com.gov.nzjcy.exception.ParseException;
import com.gov.nzjcy.helper.GOSHelper;
import com.gov.nzjcy.httpoperation.GosHttpApplication;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class MyCommitActivity extends BaseActivity {

	/**打印标示*/
	private String	TAG = "AskForOpenActivity";
	/** head 头部 的左侧菜单 按钮*/
	private LinearLayout	top_left_Layout;
	/** head 头部 的右侧菜单 按钮*/
	private ImageButton		top_right_headImageButton;
	/**head的标题*/
	private TextView		topTitleTextView;
	/**我的提交列表*/
	private ListView		mycommitListView;
	/**查询结果列表*/
	private ArrayList<HashMap<String, String>>	queryResultArrayList;
	/**等待*/
	private LinearLayout	openApplyCommitLinearLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mycommit);
		queryResultArrayList = new ArrayList<HashMap<String,String>>();
		initView();
		new Thread()
		{
			public void run() {
				getMyCommitDataByUid();
			}
		}.start();
	}
	//初始化界面
	protected void initView() {
		top_left_Layout = (LinearLayout)findViewById(R.id.leftPopLinearLayout);//左边的返回按钮
		topTitleTextView = (TextView)findViewById(R.id.topTitleTextview);
		String titleString = getIntent().getStringExtra("title");
		topTitleTextView.setText(titleString);//标题
		top_right_headImageButton = (ImageButton)findViewById(R.id.loginImageButton);//用户登录按钮
		//左边返回按钮的点击事件
		top_left_Layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyCommitActivity.this.finish();
			}
		});
		//隐藏右边的按钮
		top_right_headImageButton.setVisibility(View.INVISIBLE);
		openApplyCommitLinearLayout = (LinearLayout)findViewById(R.id.openApplyCommitLinearLayout);
		mycommitListView = (ListView)findViewById(R.id.mycommitListView);
	}
	/**获取数据*/
	protected void getMyCommitDataByUid() {
		try {	
			//GOSHelper.getSharePreStr(MyCommitActivity.this, GosHttpApplication.USER_ID_STRING)
			String userIdString = GOSHelper.getSharePreStr(MyCommitActivity.this, GosHttpApplication.USER_ID_STRING);
			String numberString = getIntent().getStringExtra("number");
			String queryTypeString = "";
			if (numberString.equals("1")) {
				queryTypeString = "getPublicItemByUid";
			}
			else if (numberString.equals("2")) {
				queryTypeString = "getReportByUid";
			}
			else if (numberString.equals("3")) {
				queryTypeString = "getAppealByUid";
			}
			else if (numberString.equals("4")) {
				queryTypeString = "getReservationByUid";
			}
			else if (numberString.equals("5")) {
				queryTypeString = "getMailBoxByUid";
			}
			HttpResponse aString = gosHttpOperation.invokerMySubmitResponse(queryTypeString, userIdString);
			HttpEntity entity = aString.getEntity();
			InputStream is = entity.getContent();
			String reString = GOSHelper.convertStreamToString(is);
		
			if (reString == null || reString.equals("")) {
				
			}
			else {
				JSONObject jsonObject = JSON.parseObject(reString);
				String stateString = jsonObject.getString("state");
				if (stateString.equals("1")) {
					Log.v("查询我的申请公开项", "查询我的申请公开项成功");
					JSONArray responeArray = jsonObject.getJSONArray("responseList");
					if (responeArray.size() > 0) {
						JSONObject oneObjects = responeArray.getJSONObject(0);
						if (oneObjects == null) {
							showViewHandler.sendEmptyMessage(0);
						}
						else {
							
							generateArrayList(numberString, responeArray);
							showViewHandler.sendEmptyMessage(1);
						}
					}
					else {
						showViewHandler.sendEmptyMessage(0);
					}
				}
				else
				{
					Log.v("查询我的申请公开项", "查询我的申请公开项失败");
					showViewHandler.sendEmptyMessage(0);
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
	/**生成数组*/
	protected void generateArrayList(String typeString, JSONArray otherArray) {
		if (typeString.equals("1")) {
			for (int i = 0; i < otherArray.size(); i++) {
				JSONObject oneObject = otherArray.getJSONObject(i);
				HashMap<String, String> map = new HashMap<String, String>();
				
				JSONObject peopleObject = oneObject.getJSONObject("people");
				String peopleNameString = peopleObject.getString("realName");
				map.put("realName", peopleNameString);
				String peopleAddressString = peopleObject.getString("address");
				map.put("address", peopleAddressString);
				String peopleIdString = peopleObject.getString("shenfenzheng");
				map.put("peopleId", peopleIdString);
				int peopleSexInt = peopleObject.getIntValue("sex");
				String peopleSexString = "";
				if (peopleSexInt == 1) {
					peopleSexString = "男";
				}
				else {
					peopleSexString = "女";
				}
				map.put("peoplesex", peopleSexString);
				String peopleEmailString = peopleObject.getString("email");
				map.put("peopleemail", peopleEmailString);
				String peopleMobileString = peopleObject.getString("mobile");
				map.put("peoplemobile", peopleMobileString);
				String peopleTelString = peopleObject.getString("tel");
				map.put("peopletel", peopleTelString);
				String mailingAddressString = peopleObject.getString("mailingAddress");
				map.put("mailingAddress", mailingAddressString);
				
				
				String queryNumberString = oneObject.getString("number");
				map.put("querynumber", queryNumberString);
				String neirongmiaoshuString = oneObject.getString("conDescription");
				map.put("gongkaiType", neirongmiaoshuString);
				String tijiaodateandtimeString = oneObject.getString("dateandtime");
				map.put("tijiaodateandtime", tijiaodateandtimeString);
				String descriptionString = oneObject.getString("description");
				map.put("shenqingneirong", descriptionString);
				String guanxiString = oneObject.getString("guanXi");
				map.put("guanxi", guanxiString);
				String huoquWayString = oneObject.getString("huoquWay");
				map.put("huoquWay", huoquWayString);
				String jiezhiString = oneObject.getString("jieZhi");
				map.put("jiezhi", jiezhiString);
				String yongtuString = oneObject.getString("usefulDescription");
				map.put("yongtu", yongtuString);
				String chuliriqiString = oneObject.getString("workDateandtime");
				map.put("chuliriqi", chuliriqiString);
				String chengbanbumenString = oneObject.getString("chengbanbumen");
				map.put("chengbanbumen", chengbanbumenString);
				String chengbanrenString = oneObject.getString("chengbaner");
				map.put("chengbanren", chengbanrenString);
				String shoulibumenString = oneObject.getString("workBumen");
				map.put("workBumen", shoulibumenString);
				String shoulirenString = oneObject.getString("worker");
				map.put("worker", shoulirenString);
				String shoulirencontactString = oneObject.getString("workContact");
				map.put("workContact", shoulirencontactString);
				String chuliresultString = oneObject.getString("workDescription");
				map.put("workDescription", chuliresultString);
				
				queryResultArrayList.add(map);
			}
		}
		else if (typeString.equals("2")) {
			for (int i = 0; i < otherArray.size(); i++) {
				JSONObject oneObject = otherArray.getJSONObject(i);
				HashMap<String, String> map = new HashMap<String, String>();
				
				JSONObject peopleObject = oneObject.getJSONObject("people");
				String peopleNameString = peopleObject.getString("realName");
				map.put("realName", peopleNameString);
				String peopleAddressString = peopleObject.getString("address");
				map.put("address", peopleAddressString);
				String peopleEmailString = peopleObject.getString("email");
				map.put("peopleemail", peopleEmailString);
				String peopleMobileString = peopleObject.getString("mobile");
				map.put("peoplemobile", peopleMobileString);
				String peopleTelString = peopleObject.getString("tel");
				map.put("peopletel", peopleTelString);
				
				
				
				String queryNumberString = oneObject.getString("number");
				map.put("querynumber", queryNumberString);
				String bjbPersonTypeString = oneObject.getString("bjb_Person_Type");
				String bjbPersonString = "";
				if (bjbPersonTypeString.equals("0")) {
					bjbPersonString = "职务犯罪线索";
				}
				else if (bjbPersonTypeString.equals("1")) {
					bjbPersonString = "违法违纪干警";
				} 
				else{
					bjbPersonString = "";
				}
				
				map.put("bjbpersonStr", bjbPersonString);
				String bjbRealNameString = oneObject.getString("bjb_Person_Realname");
				map.put("bjbRealName", bjbRealNameString);
				String bjbPersonSexString = oneObject.getString("bjb_Person_Sex");
				String bjbPersonSex = "";
				if (bjbPersonSexString.equals("1")) {
					bjbPersonSex = "男";
				}
				else {
					bjbPersonSex = "女";
				}
				map.put("bjbpersonsex", bjbPersonSex);
				
				String bjbCompanyString = oneObject.getString("bjb_Person_CompanyNameOrBumen");
				map.put("bjbCompany", bjbCompanyString);
				String bjbPersonzhijiString = oneObject.getString("bjb_Person_Zhiji");
				map.put("bjbPersonzhiji", bjbPersonzhijiString);
				String bjbPersonzhiwuString = oneObject.getString("bjb_Person_Zhiwu");
				map.put("bjbPersonzhiwu", bjbPersonzhiwuString);
				String bjbAddressString = oneObject.getString("bjb_Person_Address");
				map.put("bjbPersonAddress", bjbAddressString);
				String bjbPersonAnyouString = oneObject.getString("bjb_Person_Anyou");
				map.put("bjbpersonanyou", bjbPersonAnyouString);
				String bjbjubaocontentString = oneObject.getString("description");
				map.put("bjbjubaocontent", bjbjubaocontentString);
				
				String tijiaoriqiString = oneObject.getString("dateandtime");
				map.put("dateandtime", tijiaoriqiString);
				
				String shoulibumenString = oneObject.getString("workBumen");
				map.put("workBumen", shoulibumenString);
				String shoulirenString = oneObject.getString("worker");
				map.put("worker", shoulirenString);
				String shoulirencontactString = oneObject.getString("workContact");
				map.put("workContact", shoulirencontactString);
				String chuliresultString = oneObject.getString("workDescription");
				map.put("workDescription", chuliresultString);

				queryResultArrayList.add(map);
			}
		}
		else if (typeString.equals("3")) {
			for (int i = 0; i < otherArray.size(); i++) {
				
				JSONObject oneObject = otherArray.getJSONObject(i);
				HashMap<String, String> map = new HashMap<String, String>();
				
				JSONObject peopleObject = oneObject.getJSONObject("people");
				String peopleNameString = peopleObject.getString("realName");
				map.put("realName", peopleNameString);
				String peopleBirthdayString = peopleObject.getString("birthday");
				map.put("birthday", peopleBirthdayString);
				String jiguanString = peopleObject.getString("jiguan");
				map.put("jiguan", jiguanString);
				String minzuString = peopleObject.getString("minzu");
				map.put("minzu", minzuString);
				String companyString = peopleObject.getString("company");
				map.put("company", companyString);
				String peopleAddressString = peopleObject.getString("address");
				map.put("address", peopleAddressString);
				String peopleIdString = peopleObject.getString("shenfenzheng");
				map.put("peopleId", peopleIdString);
				int peopleSexInt = peopleObject.getIntValue("sex");
				String peopleSexString = "";
				if (peopleSexInt == 1) {
					peopleSexString = "男";
				}
				else {
					peopleSexString = "女";
				}
				map.put("peoplesex", peopleSexString);
				String peopleEmailString = peopleObject.getString("email");
				map.put("peopleemail", peopleEmailString);
				String peopleMobileString = peopleObject.getString("mobile");
				map.put("peoplemobile", peopleMobileString);
				
				
				String queryNumberString = oneObject.getString("number");
				map.put("querynumber", queryNumberString);
				
				String anjianTypeString = oneObject.getString("anjianType");
				map.put("anjiantype", anjianTypeString);
				
				String konggaocntentString = oneObject.getString("description");
				map.put("konggaocontent", konggaocntentString);
				
				String tijiaoriqiString = oneObject.getString("dateandtime");
				map.put("dateandtime", tijiaoriqiString);
				
				
				queryResultArrayList.add(map);
				
			}
		}
		else if (typeString.equals("4")) {
			for (int i = 0; i < otherArray.size(); i++) {
				JSONObject oneObject = otherArray.getJSONObject(i);
				HashMap<String, String> map = new HashMap<String, String>();
				
				String sqrshenfenString = oneObject.getString("sqr_Shenfen");
				map.put("sqrshenfen", sqrshenfenString);
				String lvshishiwumingziString = oneObject.getString("sqr_ShiwusuoName");
				map.put("lvshishiwusuomz", lvshishiwumingziString);
				int sqrlvshizhuyeInt = oneObject.getIntValue("sqr_IsLower");
				String sqrlvshizhuyeString = "";
				if (sqrlvshizhuyeInt == 1) {
					sqrlvshizhuyeString = "是";
				}
				else {
					sqrlvshizhuyeString = "否";
				}
				map.put("zhiyelvshi", sqrlvshizhuyeString);
				int falvyuanzhuInt = oneObject.getIntValue("sqr_IsFromHelpCenter");
				String falvyuanzhuString = "";
				if (falvyuanzhuInt == 1) {
					falvyuanzhuString = "是";
				}
				else {
					falvyuanzhuString = "否";
				}
				map.put("falvyuanzhuzhipai", falvyuanzhuString);
				int sqrisneedhideInt = oneObject.getIntValue("sqr_IsNeedHide");
				String sqrisneedhideString = "";
				if (sqrisneedhideInt == 1) {
					sqrisneedhideString = "是";
				}
				else {
					sqrisneedhideString = "否";
				}
				map.put("shifouhuibi", sqrisneedhideString);
				JSONObject peopleObject = oneObject.getJSONObject("people");
				String peopleNameString = peopleObject.getString("realName");
				map.put("realName", peopleNameString);
				String peopleMobileString = peopleObject.getString("mobile");
				map.put("peoplemobile", peopleMobileString);
				
				String bgrealnameString = oneObject.getString("bg_Realname");
				map.put("bgrealname", bgrealnameString);
				int bgSexType = oneObject.getIntValue("bg_Sex");
				String bgSexString = "";
				if (bgSexType == 1) {
					bgSexString = "男";
				}
				else {
					bgSexString = "女";
				}
				map.put("bgsex", bgSexString);
				String bgshenfenzhengString = oneObject.getString("bg_Shenfenzheng");
				map.put("bgpeopleId", bgshenfenzhengString);
				String bgbirthdayString = oneObject.getString("bg_Birthday");
				map.put("bgbirthday", bgbirthdayString);
				String bgjianchayuanString = oneObject.getString("bg_Jianchayuan");
				map.put("bgbananjianchayuan", bgjianchayuanString);
				String bgjieduanString = oneObject.getString("bg_Jieduan");
				map.put("bgjieduan", bgjieduanString);
				String bgcuoshiString = oneObject.getString("bg_Cuoshi");
				map.put("bgcuoshi", bgcuoshiString);
				String bganyouString = oneObject.getString("bg_Anyou");
				map.put("bganyou", bganyouString);
				String wtrrealnameString = oneObject.getString("wtr_Realname");
				map.put("wtrrealname", wtrrealnameString);
				String wtrshenfenzhengString = oneObject.getString("wtr_Shenfenzheng");
				map.put("wtrpeopleid", wtrshenfenzhengString);
				String yuyuetypeString = oneObject.getString("type");
				map.put("yuyuetype", yuyuetypeString);
				String beizhuString = oneObject.getString("description");
				map.put("notes", beizhuString);
				
				
				String queryNumberString = oneObject.getString("number");
				map.put("querynumber", queryNumberString);
				String tijiaoriqiString = oneObject.getString("dateandtime");
				map.put("dateandtime", tijiaoriqiString);
				
				String shoulibumenString = oneObject.getString("workBumen");
				map.put("workBumen", shoulibumenString);
				String shoulirenString = oneObject.getString("worker");
				map.put("worker", shoulirenString);
				String shoulirencontactString = oneObject.getString("workContact");
				map.put("workContact", shoulirencontactString);
				String chuliresultString = oneObject.getString("workDescription");
				map.put("workDescription", chuliresultString);
				
				queryResultArrayList.add(map);
				
			}
		}
		else if (typeString.equals("5")) {
			for (int i = 0; i < otherArray.size(); i++) {
				JSONObject oneObject = otherArray.getJSONObject(i);
				
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
				queryResultArrayList.add(map);
			}
		}
	}
	/***/
	Handler showViewHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 0:
				openApplyCommitLinearLayout.setVisibility(View.INVISIBLE);
				Toast.makeText(MyCommitActivity.this, "没有数据", Toast.LENGTH_SHORT).show();
				break;
			case 1:
				openApplyCommitLinearLayout.setVisibility(View.INVISIBLE);
				MyCommitDataAdapter myCommitDataAdapter = new MyCommitDataAdapter();
				mycommitListView.setAdapter(myCommitDataAdapter);
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};
	/**数据源*/
	private class MyCommitDataAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return queryResultArrayList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			String numberString = getIntent().getStringExtra("number");
			if (!(numberString.equals("5"))) {
				CommitDataListViewHolder 	holder;
				if (convertView == null) {
					convertView = LayoutInflater.from(getApplication()).inflate(R.layout.list_mycommititem, null);
					holder = new CommitDataListViewHolder();
					//取到各个控件的对象
					 holder.oneTextView = (TextView)convertView.findViewById(R.id.myCommitOneTextView);
					 holder.twoTextView = (TextView)convertView.findViewById(R.id.myCommitTwoTextView);
					 holder.threeTextView = (TextView)convertView.findViewById(R.id.myCommitThreeTextView);
					 holder.fourTextView = (TextView)convertView.findViewById(R.id.myCommitFourTextView);
					 holder.fiveTextView = (TextView)convertView.findViewById(R.id.myCommitFiveTextView);
					 holder.sixTextView = (TextView)convertView.findViewById(R.id.myCommitSixTextView);
					 holder.sevenTextView = (TextView)convertView.findViewById(R.id.myCommitSevenTextView);
					 holder.eightTextView = (TextView)convertView.findViewById(R.id.myCommitEightTextView);
					 holder.nineTextView = (TextView)convertView.findViewById(R.id.myCommitNineTextView);
					 holder.tenTextView = (TextView)convertView.findViewById(R.id.myCommitTenTextView);
					 holder.elvenTextView = (TextView)convertView.findViewById(R.id.myCommitElvenTextView);
					 holder.twelveTextView = (TextView)convertView.findViewById(R.id.myCommitTwelveTextView);
					 holder.thirteenTextView = (TextView)convertView.findViewById(R.id.myCommitThirteenTextView);
					 holder.fourteenTextView = (TextView)convertView.findViewById(R.id.myCommitFourteenTextView);
					 holder.fifteenTextView = (TextView)convertView.findViewById(R.id.myCommitFifteenTextView);
					 
					 holder.sixTeenTextView = (TextView)convertView.findViewById(R.id.myCommitSixteenTextView);
					 holder.sevenTeenTextView = (TextView)convertView.findViewById(R.id.myCommitSevneteenTextView);
					 holder.eightTeenTextView = (TextView)convertView.findViewById(R.id.myCommitEightteenTextView);
					 holder.nineTeenTextView = (TextView)convertView.findViewById(R.id.myCommitNineteenTextView);
					 holder.twentyTextView = (TextView)convertView.findViewById(R.id.myCommitTwentyTextView);
					 holder.twentyOneTextView = (TextView)convertView.findViewById(R.id.myCommitTwentyOneTextView);
					 holder.twentyTwoTextView = (TextView)convertView.findViewById(R.id.myCommitTwentyTwoTextView);
					 holder.twentyThreeTextView = (TextView)convertView.findViewById(R.id.myCommitTwentyThreeTextView);
					 holder.twentyFourTextView = (TextView)convertView.findViewById(R.id.myCommitTwentyFourTextView);
					 holder.twentyFiveTextView = (TextView)convertView.findViewById(R.id.myCommitTwentyFiveTextView);
					 
					 holder.twentySixTextView = (TextView)convertView.findViewById(R.id.myCommitTwentySixTextView);
					 holder.twentySevenTextView = (TextView)convertView.findViewById(R.id.myCommitTwentySevenTextView);
					 holder.twentyEightTextView = (TextView)convertView.findViewById(R.id.myCommitTwentyEightTextView);
					 holder.twentyNineTextView = (TextView)convertView.findViewById(R.id.myCommitTwentyNineTextView);
					 holder.thirtyTextView = (TextView)convertView.findViewById(R.id.myCommitThirtyTextView);
					 holder.thirtyOneTextView = (TextView)convertView.findViewById(R.id.myCommitThirtyOneTextView);
					 holder.thirtyTwoTextView = (TextView)convertView.findViewById(R.id.myCommitThirtyTwoTextView);
					 holder.thirtyThreeTextView = (TextView)convertView.findViewById(R.id.myCommitThirtyThreeTextView);
					 holder.thirtyFourTextView = (TextView)convertView.findViewById(R.id.myCommitThirtyFourTextView);
					 holder.thirtyFiveTextView = (TextView)convertView.findViewById(R.id.myCommitThirtyFiveTextView);
					 holder.thirtySixTextView = (TextView)convertView.findViewById(R.id.myCommitThirtySixTextView);
					 holder.thirtySevenTextView = (TextView)convertView.findViewById(R.id.myCommitThirtySevenTextView);
					 holder.thirtyEightTextView = (TextView)convertView.findViewById(R.id.myCommitThirtyEightTextView);
					 holder.thirtyNineTextView = (TextView)convertView.findViewById(R.id.myCommitThirtyNineTextView);
					 holder.foutyTextView = (TextView)convertView.findViewById(R.id.myCommitFoutyTextView);
					 holder.foutyOneTextView = (TextView)convertView.findViewById(R.id.myCommitFoutyOneTextView);
					 holder.foutyTwoTextView = (TextView)convertView.findViewById(R.id.myCommitFoutyTwoTextView);
					 
					 holder.foutyThreeTextView = (TextView)convertView.findViewById(R.id.myCommitFoutyThreeTextView);
					 holder.foutyFourTextView = (TextView)convertView.findViewById(R.id.myCommitFoutyFourTextView);
					 holder.foutyFiveTextView = (TextView)convertView.findViewById(R.id.myCommitFoutyFiveTextView);
					 holder.foutySixTextView = (TextView)convertView.findViewById(R.id.myCommitFoutySixTextView);
					 holder.foutySevenTextView = (TextView)convertView.findViewById(R.id.myCommitFoutySevenTextView);
					 holder.foutyEightTextView = (TextView)convertView.findViewById(R.id.myCommitFoutyEightTextView);
					 holder.foutyNineTextView = (TextView)convertView.findViewById(R.id.myCommitFoutyNineTextView);
					 holder.fiftyTextView = (TextView)convertView.findViewById(R.id.myCommitFiftyTextView);
					 
					 convertView.setTag(holder);
				}
				else {
					holder = (CommitDataListViewHolder)convertView.getTag();
				}
				
				
				
				if (numberString.equals("1")) {
					holder.oneTextView.setText("查询编号");
					holder.oneTextView.setTextColor(Color.BLACK);
					holder.twoTextView.setText(queryResultArrayList.get(position).get("querynumber"));
					holder.twoTextView.setTextColor(Color.rgb(86, 79, 79));
					holder.threeTextView.setText("提交日期");
					holder.threeTextView.setTextColor(Color.BLACK);
					holder.fourTextView.setText(queryResultArrayList.get(position).get("tijiaodateandtime"));
					holder.fourTextView.setTextColor(Color.rgb(86, 79, 79));
				
					holder.fiveTextView.setText("姓名");
					holder.fiveTextView.setTextColor(Color.BLACK);
					holder.sixTextView.setText(queryResultArrayList.get(position).get("realName"));
					holder.sixTextView.setTextColor(Color.rgb(86, 79, 79));
					holder.sevenTextView.setText("身份证号码或组织机构代码证");
					holder.sevenTextView.setTextColor(Color.BLACK);
					holder.eightTextView.setText(queryResultArrayList.get(position).get("peopleId"));
					holder.eightTextView.setTextColor(Color.rgb(86, 79, 79));
					holder.nineTextView.setText("地址");
					holder.nineTextView.setTextColor(Color.BLACK);
					holder.tenTextView.setText(queryResultArrayList.get(position).get("address"));
					holder.tenTextView.setTextColor(Color.rgb(86, 79, 79));
					holder.elvenTextView.setText("性别");
					holder.elvenTextView.setTextColor(Color.BLACK);
					holder.twelveTextView.setText(queryResultArrayList.get(position).get("peoplesex"));
					holder.twelveTextView.setTextColor(Color.rgb(86, 79, 79));
					holder.thirteenTextView.setText("电子邮箱");
					holder.thirteenTextView.setTextColor(Color.BLACK);
					holder.fourteenTextView.setText(queryResultArrayList.get(position).get("peopleemail"));
					holder.fourteenTextView.setTextColor(Color.rgb(86, 79, 79));
					holder.fifteenTextView.setText("手机号码");
					holder.fifteenTextView.setTextColor(Color.BLACK);
					holder.sixTeenTextView.setText(queryResultArrayList.get(position).get("peoplemobile"));
					holder.sixTeenTextView.setTextColor(Color.rgb(86, 79, 79));
					holder.sevenTeenTextView.setText("电话");
					holder.sevenTeenTextView.setTextColor(Color.BLACK);
					holder.eightTeenTextView.setText(queryResultArrayList.get(position).get("peopletel"));
					holder.eightTeenTextView.setTextColor(Color.rgb(86, 79, 79));
					
					holder.nineTeenTextView.setText("邮寄地址");
					holder.nineTeenTextView.setTextColor(Color.BLACK);
					holder.twentyTextView.setText(queryResultArrayList.get(position).get("mailingAddress"));
					holder.twentyTextView.setTextColor(Color.rgb(86, 79, 79));
					
					holder.twentyOneTextView.setText("申请内容");
					holder.twentyOneTextView.setTextColor(Color.BLACK);
					holder.twentyTwoTextView.setText(queryResultArrayList.get(position).get("gongkaiType") + "\n" 
					+ queryResultArrayList.get(position).get("shenqingneirong"));
					holder.twentyTwoTextView.setTextColor(Color.rgb(86, 79, 79));
					holder.twentyThreeTextView.setText("用途");
					holder.twentyThreeTextView.setTextColor(Color.BLACK);
					holder.twentyFourTextView.setText(queryResultArrayList.get(position).get("yongtu"));
					holder.twentyFourTextView.setTextColor(Color.rgb(86, 79, 79));
					
					
					
					holder.twentyFiveTextView.setText("介质");
					holder.twentyFiveTextView.setTextColor(Color.BLACK);
					holder.twentySixTextView.setText(queryResultArrayList.get(position).get("jiezhi"));
					holder.twentySixTextView.setTextColor(Color.rgb(86, 79, 79));
					holder.twentySevenTextView.setText("获取方式");
					holder.twentySevenTextView.setTextColor(Color.BLACK);
					
					holder.twentyEightTextView.setText(queryResultArrayList.get(position).get("huoquWay"));
					holder.twentyEightTextView.setTextColor(Color.rgb(86, 79, 79));
					
					holder.twentyNineTextView.setText("处理日期");
					holder.twentyNineTextView.setTextColor(Color.BLACK);
					holder.thirtyTextView.setText(queryResultArrayList.get(position).get("chuliriqi"));
					holder.thirtyTextView.setTextColor(Color.rgb(86, 79, 79));
					
					holder.thirtyOneTextView.setText("承办部门");
					holder.thirtyOneTextView.setTextColor(Color.BLACK);
					holder.thirtyTwoTextView.setText(queryResultArrayList.get(position).get("chengbanbumen"));
					holder.thirtyTwoTextView.setTextColor(Color.rgb(86, 79, 79));
					
					holder.thirtyThreeTextView.setText("承办人");
					holder.thirtyThreeTextView.setTextColor(Color.BLACK);
					holder.thirtyFourTextView.setText(queryResultArrayList.get(position).get("chengbanren"));
					holder.thirtyFourTextView.setTextColor(Color.rgb(86, 79, 79));
					
					holder.thirtyFiveTextView.setText("受理人");
					holder.thirtyFiveTextView.setTextColor(Color.BLACK);
					holder.thirtySixTextView.setText(queryResultArrayList.get(position).get("worker"));
					holder.thirtySixTextView.setTextColor(Color.rgb(86, 79, 79));
					
					holder.thirtySevenTextView.setText("受理部门");
					holder.thirtySevenTextView.setTextColor(Color.BLACK);
					holder.thirtyEightTextView.setText(queryResultArrayList.get(position).get("workBumen"));
					holder.thirtyEightTextView.setTextColor(Color.rgb(86, 79, 79));
					
					holder.thirtyNineTextView.setText("受理部门联系电话");
					holder.thirtyNineTextView.setTextColor(Color.BLACK);
					holder.foutyTextView.setText(queryResultArrayList.get(position).get("workContact"));
					holder.foutyTextView.setTextColor(Color.rgb(86, 79, 79));
					
					holder.foutyOneTextView.setText("处理结果");
					holder.foutyOneTextView.setTextColor(Color.BLACK);
					holder.foutyTwoTextView.setText(GOSHelper.stripTags(queryResultArrayList.get(position).get("workDescription")));
					holder.foutyTwoTextView.setTextColor(Color.rgb(86, 79, 79));
					
					
				}
				else if (numberString.equals("2")) {
					holder.oneTextView.setText("查询编号");
					holder.oneTextView.setTextColor(Color.BLACK);
					holder.twoTextView.setText(queryResultArrayList.get(position).get("querynumber"));
					holder.twoTextView.setTextColor(Color.rgb(86, 79, 79));
					holder.threeTextView.setText("提交日期");
					holder.threeTextView.setTextColor(Color.BLACK);
					holder.fourTextView.setText(queryResultArrayList.get(position).get("dateandtime"));
					holder.fourTextView.setTextColor(Color.rgb(86, 79, 79));
				
					holder.fiveTextView.setText("举报人姓名");
					holder.fiveTextView.setTextColor(Color.BLACK);
					holder.sixTextView.setText(queryResultArrayList.get(position).get("realName"));
					holder.sixTextView.setTextColor(Color.rgb(86, 79, 79));
					
					holder.sevenTextView.setText("举报人家庭地址");
					holder.sevenTextView.setTextColor(Color.BLACK);
					holder.eightTextView.setText(queryResultArrayList.get(position).get("address"));
					holder.eightTextView.setTextColor(Color.rgb(86, 79, 79));
					holder.nineTextView.setText("举报人电子邮箱");
					holder.nineTextView.setTextColor(Color.BLACK);
					holder.tenTextView.setText(queryResultArrayList.get(position).get("peopleemail"));
					holder.tenTextView.setTextColor(Color.rgb(86, 79, 79));
					holder.elvenTextView.setText("举报人手机号码");
					holder.elvenTextView.setTextColor(Color.BLACK);
					holder.twelveTextView.setText(queryResultArrayList.get(position).get("peoplemobile"));
					holder.twelveTextView.setTextColor(Color.rgb(86, 79, 79));
					holder.thirteenTextView.setText("举报人固定电话");
					holder.thirteenTextView.setTextColor(Color.BLACK);
					holder.fourteenTextView.setText(queryResultArrayList.get(position).get("peopletel"));
					holder.fourteenTextView.setTextColor(Color.rgb(86, 79, 79));
					
					
					holder.fifteenTextView.setText("被举报人信息:");
					holder.fifteenTextView.setTextColor(Color.BLACK);
					holder.sixTeenTextView.setText(queryResultArrayList.get(position).get("bjbpersonStr") + "\n" + 
					"姓名 \n" + queryResultArrayList.get(position).get("bjbRealName"));
					holder.sixTeenTextView.setTextColor(Color.rgb(86, 79, 79));
					holder.sevenTeenTextView.setText("性别");
					holder.sevenTeenTextView.setTextColor(Color.BLACK);
					holder.eightTeenTextView.setText(queryResultArrayList.get(position).get("bjbpersonsex"));
					holder.eightTeenTextView.setTextColor(Color.rgb(86, 79, 79));
					
					
					
					holder.nineTeenTextView.setText("单位或部门");
					holder.nineTeenTextView.setTextColor(Color.BLACK);
					holder.twentyTextView.setText(queryResultArrayList.get(position).get("bjbCompany"));
					holder.twentyTextView.setTextColor(Color.rgb(86, 79, 79));
					holder.twentyOneTextView.setText("职级");
					holder.twentyOneTextView.setTextColor(Color.BLACK);
					
					holder.twentyTwoTextView.setText(queryResultArrayList.get(position).get("bjbPersonzhiji"));
					holder.twentyTwoTextView.setTextColor(Color.rgb(86, 79, 79));
					holder.twentyThreeTextView.setText("职务");
					holder.twentyThreeTextView.setTextColor(Color.BLACK);
					holder.twentyFourTextView.setText(queryResultArrayList.get(position).get("bjbPersonzhiwu"));
					holder.twentyFourTextView.setTextColor(Color.rgb(86, 79, 79));
					holder.twentyFiveTextView.setText("住址");
					holder.twentyFiveTextView.setTextColor(Color.BLACK);
					holder.twentySixTextView.setText(queryResultArrayList.get(position).get("bjbPersonAddress"));
					holder.twentySixTextView.setTextColor(Color.rgb(86, 79, 79));
					holder.twentySevenTextView.setText("案由");
					holder.twentySevenTextView.setTextColor(Color.BLACK);
					holder.twentyEightTextView.setText(queryResultArrayList.get(position).get("bjbpersonanyou"));
					holder.twentyEightTextView.setTextColor(Color.rgb(86, 79, 79));
					holder.twentyNineTextView.setText("举报内容");
					holder.twentyNineTextView.setTextColor(Color.BLACK);
					holder.thirtyTextView.setText(queryResultArrayList.get(position).get("bjbjubaocontent"));
					holder.thirtyTextView.setTextColor(Color.rgb(86, 79, 79));
					
					holder.thirtyOneTextView.setText("受理人");
					holder.thirtyOneTextView.setTextColor(Color.BLACK);
					holder.thirtyTwoTextView.setText(queryResultArrayList.get(position).get("worker"));
					holder.thirtyTwoTextView.setTextColor(Color.rgb(86, 79, 79));
					
					holder.thirtyThreeTextView.setText("受理部门");
					holder.thirtyThreeTextView.setTextColor(Color.BLACK);
					holder.thirtyFourTextView.setText(queryResultArrayList.get(position).get("workBumen"));
					holder.thirtyFourTextView.setTextColor(Color.rgb(86, 79, 79));
					
					holder.thirtyFiveTextView.setText("受理部门联系电话");
					holder.thirtyFiveTextView.setTextColor(Color.BLACK);
					holder.thirtySixTextView.setText(queryResultArrayList.get(position).get("workContact"));
					holder.thirtySixTextView.setTextColor(Color.rgb(86, 79, 79));
					
					holder.thirtySevenTextView.setText("处理结果");
					holder.thirtySevenTextView.setTextColor(Color.BLACK);
					holder.thirtyEightTextView.setText(GOSHelper.stripTags(queryResultArrayList.get(position).get("workDescription")));
					holder.thirtyEightTextView.setTextColor(Color.rgb(86, 79, 79));
					
				}
				else if (numberString.equals("3")) {
					
					holder.oneTextView.setText("查询编号");
					holder.oneTextView.setTextColor(Color.BLACK);
					holder.twoTextView.setText(queryResultArrayList.get(position).get("querynumber"));
					holder.twoTextView.setTextColor(Color.rgb(86, 79, 79));
					holder.threeTextView.setText("提交日期");
					holder.threeTextView.setTextColor(Color.BLACK);
					holder.fourTextView.setText(queryResultArrayList.get(position).get("dateandtime"));
					holder.fourTextView.setTextColor(Color.rgb(86, 79, 79));
				
					holder.fiveTextView.setText("姓名");
					holder.fiveTextView.setTextColor(Color.BLACK);
					holder.sixTextView.setText(queryResultArrayList.get(position).get("realName"));
					holder.sixTextView.setTextColor(Color.rgb(86, 79, 79));
					
					holder.sevenTextView.setText("出生年月");
					holder.sevenTextView.setTextColor(Color.BLACK);
					holder.eightTextView.setText(queryResultArrayList.get(position).get("birthday"));
					holder.eightTextView.setTextColor(Color.rgb(86, 79, 79));
					holder.nineTextView.setText("性别");
					holder.nineTextView.setTextColor(Color.BLACK);
					holder.tenTextView.setText(queryResultArrayList.get(position).get("peoplesex"));
					holder.tenTextView.setTextColor(Color.rgb(86, 79, 79));
					holder.elvenTextView.setText("籍贯");
					holder.elvenTextView.setTextColor(Color.BLACK);
					holder.twelveTextView.setText(queryResultArrayList.get(position).get("jiguan"));
					holder.twelveTextView.setTextColor(Color.rgb(86, 79, 79));
					holder.thirteenTextView.setText("工作单位");
					holder.thirteenTextView.setTextColor(Color.BLACK);
					holder.fourteenTextView.setText(queryResultArrayList.get(position).get("company"));
					holder.fourteenTextView.setTextColor(Color.rgb(86, 79, 79));
					
					
					holder.fifteenTextView.setText("民族");
					holder.fifteenTextView.setTextColor(Color.BLACK);
					holder.sixTeenTextView.setText(queryResultArrayList.get(position).get("minzu"));
					holder.sixTeenTextView.setTextColor(Color.rgb(86, 79, 79));
					holder.sevenTeenTextView.setText("家庭住址");
					holder.sevenTeenTextView.setTextColor(Color.BLACK);
					holder.eightTeenTextView.setText(queryResultArrayList.get(position).get("address"));
					holder.eightTeenTextView.setTextColor(Color.rgb(86, 79, 79));
					
					
					
					holder.nineTeenTextView.setText("身份证号码");
					holder.nineTeenTextView.setTextColor(Color.BLACK);
					holder.twentyTextView.setText(queryResultArrayList.get(position).get("peopleId"));
					holder.twentyTextView.setTextColor(Color.rgb(86, 79, 79));
					holder.twentyOneTextView.setText("电子邮件");
					holder.twentyOneTextView.setTextColor(Color.BLACK);
					
					holder.twentyTwoTextView.setText(queryResultArrayList.get(position).get("email"));
					holder.twentyTwoTextView.setTextColor(Color.rgb(86, 79, 79));
					
					
					holder.twentyThreeTextView.setText("案件类型");
					holder.twentyThreeTextView.setTextColor(Color.BLACK);
					holder.twentyFourTextView.setText(queryResultArrayList.get(position).get("anjiantype"));
					holder.twentyFourTextView.setTextColor(Color.rgb(86, 79, 79));
					holder.twentyFiveTextView.setText("申诉内容");
					holder.twentyFiveTextView.setTextColor(Color.BLACK);
					holder.twentySixTextView.setText(queryResultArrayList.get(position).get("konggaocontent"));
					holder.twentySixTextView.setTextColor(Color.rgb(86, 79, 79));
					
					
				}
				else if (numberString.equals("4")) {
					
					holder.oneTextView.setText("查询编号");
					holder.oneTextView.setTextColor(Color.BLACK);
					holder.twoTextView.setText(queryResultArrayList.get(position).get("querynumber"));
					holder.twoTextView.setTextColor(Color.rgb(86, 79, 79));
					holder.threeTextView.setText("提交日期");
					holder.threeTextView.setTextColor(Color.BLACK);
					holder.fourTextView.setText(queryResultArrayList.get(position).get("dateandtime"));
					holder.fourTextView.setTextColor(Color.rgb(86, 79, 79));
				
					holder.fiveTextView.setText("申请人身份");
					holder.fiveTextView.setTextColor(Color.BLACK);
					holder.sixTextView.setText(queryResultArrayList.get(position).get("sqrshenfen"));
					holder.sixTextView.setTextColor(Color.rgb(86, 79, 79));
					holder.sevenTextView.setText("申请人律师事务所名称");
					holder.sevenTextView.setTextColor(Color.BLACK);
					holder.eightTextView.setText(queryResultArrayList.get(position).get("lvshishiwusuomz"));
					holder.eightTextView.setTextColor(Color.rgb(86, 79, 79));
					holder.nineTextView.setText("申请人职业律师");
					holder.nineTextView.setTextColor(Color.BLACK);
					holder.tenTextView.setText(queryResultArrayList.get(position).get("zhiyelvshi"));
					holder.tenTextView.setTextColor(Color.rgb(86, 79, 79));
					holder.elvenTextView.setText("申请人法律援助中心指派");
					holder.elvenTextView.setTextColor(Color.BLACK);
					holder.twelveTextView.setText(queryResultArrayList.get(position).get("falvyuanzhuzhipai"));
					holder.twelveTextView.setTextColor(Color.rgb(86, 79, 79));
					holder.thirteenTextView.setText("申请人需要回避");
					holder.thirteenTextView.setTextColor(Color.BLACK);
					holder.fourteenTextView.setText(queryResultArrayList.get(position).get("shifouhuibi"));
					holder.fourteenTextView.setTextColor(Color.rgb(86, 79, 79));
					holder.fifteenTextView.setText("申请人姓名");
					holder.fifteenTextView.setTextColor(Color.BLACK);
					holder.sixTeenTextView.setText(queryResultArrayList.get(position).get("realName"));
					holder.sixTeenTextView.setTextColor(Color.rgb(86, 79, 79));
					holder.sevenTeenTextView.setText("申请人手机");
					holder.sevenTeenTextView.setTextColor(Color.BLACK);
					holder.eightTeenTextView.setText(queryResultArrayList.get(position).get("peoplemobile"));
					holder.eightTeenTextView.setTextColor(Color.rgb(86, 79, 79));
					holder.nineTeenTextView.setText("被告人姓名");
					holder.nineTeenTextView.setTextColor(Color.BLACK);
					holder.twentyTextView.setText(queryResultArrayList.get(position).get("bgrealname"));
					holder.twentyTextView.setTextColor(Color.rgb(86, 79, 79));
					holder.twentyOneTextView.setText("被告人性别");
					holder.twentyOneTextView.setTextColor(Color.BLACK);
					holder.twentyTwoTextView.setText(queryResultArrayList.get(position).get("bgsex"));
					holder.twentyTwoTextView.setTextColor(Color.rgb(86, 79, 79));
					
					
					
					holder.twentyThreeTextView.setText("被告人身份证号码");
					holder.twentyThreeTextView.setTextColor(Color.BLACK);
					holder.twentyFourTextView.setText(queryResultArrayList.get(position).get("bgpeopleId"));
					holder.twentyFourTextView.setTextColor(Color.rgb(86, 79, 79));
					holder.twentyFiveTextView.setText("被告人出生年月");
					holder.twentyFiveTextView.setTextColor(Color.BLACK);
					
					holder.twentySixTextView.setText(queryResultArrayList.get(position).get("bgbirthday"));
					holder.twentySixTextView.setTextColor(Color.rgb(86, 79, 79));
					holder.twentySevenTextView.setText("被告人办案检察院");
					holder.twentySevenTextView.setTextColor(Color.BLACK);
					holder.twentyEightTextView.setText(queryResultArrayList.get(position).get("bgbananjianchayuan"));
					holder.twentyEightTextView.setTextColor(Color.rgb(86, 79, 79));
					holder.twentyNineTextView.setText("被告人诉讼阶段");
					holder.twentyNineTextView.setTextColor(Color.BLACK);
					holder.thirtyTextView.setText(queryResultArrayList.get(position).get("bgjieduan"));
					holder.thirtyTextView.setTextColor(Color.rgb(86, 79, 79));
					holder.thirtyOneTextView.setText("被告人强制措施");
					holder.thirtyOneTextView.setTextColor(Color.BLACK);
					holder.thirtyTwoTextView.setText(queryResultArrayList.get(position).get("bgcuoshi"));
					holder.thirtyTwoTextView.setTextColor(Color.rgb(86, 79, 79));
					holder.thirtyThreeTextView.setText("被告人案由");
					holder.thirtyThreeTextView.setTextColor(Color.BLACK);
					holder.thirtyFourTextView.setText(queryResultArrayList.get(position).get("bganyou"));
					holder.thirtyFourTextView.setTextColor(Color.rgb(86, 79, 79));
					holder.thirtyFiveTextView.setText("委托人姓名");
					holder.thirtyFiveTextView.setTextColor(Color.BLACK);
					holder.thirtySixTextView.setText(queryResultArrayList.get(position).get("wtrrealname"));
					holder.thirtySixTextView.setTextColor(Color.rgb(86, 79, 79));
					holder.thirtySevenTextView.setText("委托人身份证号码");
					holder.thirtySevenTextView.setTextColor(Color.BLACK);
					holder.thirtyEightTextView.setText(queryResultArrayList.get(position).get("wtrpeopleid"));
					holder.thirtyEightTextView.setTextColor(Color.rgb(86, 79, 79));
					holder.thirtyNineTextView.setText("委托人预约内容");
					holder.thirtyNineTextView.setTextColor(Color.BLACK);
					holder.foutyTextView.setText(queryResultArrayList.get(position).get("yuyuetype"));
					holder.foutyTextView.setTextColor(Color.rgb(86, 79, 79));
					holder.foutyOneTextView.setText("委托人备注");
					holder.foutyOneTextView.setTextColor(Color.BLACK);
					holder.foutyTwoTextView.setText(queryResultArrayList.get(position).get("notes"));
					holder.foutyTwoTextView.setTextColor(Color.rgb(86, 79, 79));
					
					holder.foutyThreeTextView.setText("受理人");
					holder.foutyThreeTextView.setTextColor(Color.BLACK);
					holder.foutyFourTextView.setText(queryResultArrayList.get(position).get("worker"));
					holder.foutyFourTextView.setTextColor(Color.rgb(86, 79, 79));
					
					holder.foutyFiveTextView.setText("受理部门");
					holder.foutyFiveTextView.setTextColor(Color.BLACK);
					holder.foutySixTextView.setText(queryResultArrayList.get(position).get("workBumen"));
					holder.foutySixTextView.setTextColor(Color.rgb(86, 79, 79));
					
					holder.foutySevenTextView.setText("受理部门联系电话");
					holder.foutySevenTextView.setTextColor(Color.BLACK);
					holder.foutyEightTextView.setText(queryResultArrayList.get(position).get("workContact"));
					holder.foutyEightTextView.setTextColor(Color.rgb(86, 79, 79));
					
					holder.foutyNineTextView.setText("处理结果");
					holder.foutyNineTextView.setTextColor(Color.BLACK);
					holder.fiftyTextView.setText(GOSHelper.stripTags(queryResultArrayList.get(position).get("workDescription")));
					holder.fiftyTextView.setTextColor(Color.rgb(86, 79, 79));
				}
			}
			else if (numberString.equals("5")){
				CommitDataOneListViewHolder 	holder;
				if (convertView == null) {
					convertView = LayoutInflater.from(getApplication()).inflate(R.layout.list_mycommitmailitem, null);
					holder = new CommitDataOneListViewHolder();
					//取到各个控件的对象
					 holder.oneTextView = (TextView)convertView.findViewById(R.id.myCommitOneTextView);
					 holder.twoTextView = (TextView)convertView.findViewById(R.id.myCommitTwoTextView);
					 holder.threeTextView = (TextView)convertView.findViewById(R.id.myCommitThreeTextView);
					 holder.fourTextView = (TextView)convertView.findViewById(R.id.myCommitFourTextView);
					 holder.fiveTextView = (TextView)convertView.findViewById(R.id.myCommitFiveTextView);
					 holder.sixTextView = (TextView)convertView.findViewById(R.id.myCommitSixTextView);
					 holder.sevenTextView = (TextView)convertView.findViewById(R.id.myCommitSevenTextView);
					 holder.eightTextView = (TextView)convertView.findViewById(R.id.myCommitEightTextView);
					 holder.nineTextView = (TextView)convertView.findViewById(R.id.myCommitNineTextView);
					 holder.tenTextView = (TextView)convertView.findViewById(R.id.myCommitTenTextView);
					 
					 
					 convertView.setTag(holder);
				}
				else {
					holder = (CommitDataOneListViewHolder)convertView.getTag();
				}
				holder.oneTextView.setText("收信人");
				holder.oneTextView.setTextColor(Color.BLACK);
				holder.twoTextView.setText(queryResultArrayList.get(position).get("receiver"));
				holder.twoTextView.setTextColor(Color.rgb(86, 79, 79));
				holder.threeTextView.setText("发信人");
				holder.threeTextView.setTextColor(Color.BLACK);
				holder.fourTextView.setText(queryResultArrayList.get(position).get("sender"));
				holder.fourTextView.setTextColor(Color.rgb(86, 79, 79));
				holder.fiveTextView.setText("发信日期");
				holder.fiveTextView.setTextColor(Color.BLACK);
				holder.sixTextView.setText(queryResultArrayList.get(position).get("date"));
				holder.sixTextView.setTextColor(Color.rgb(86, 79, 79));
				holder.sevenTextView.setText("标题");
				holder.sevenTextView.setTextColor(Color.BLACK);
				holder.eightTextView.setText(queryResultArrayList.get(position).get("title"));
				holder.eightTextView.setTextColor(Color.rgb(86, 79, 79));
				holder.nineTextView.setText("内容");
				holder.nineTextView.setTextColor(Color.BLACK);
				holder.tenTextView.setText(queryResultArrayList.get(position).get("description"));
				holder.tenTextView.setTextColor(Color.rgb(86, 79, 79));
			}
			
			return convertView;
		}
		
	}
	
	/**控件*/
   public final class CommitDataListViewHolder{
       public TextView 			oneTextView;
       public TextView			twoTextView;
       public TextView 			threeTextView;
       public TextView			fourTextView;
       public TextView 			fiveTextView;
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
       public TextView			sevenTeenTextView;
       public TextView			eightTeenTextView;
       public TextView			nineTeenTextView;
       public TextView			twentyTextView;
       public TextView			twentyOneTextView;
       public TextView			twentyTwoTextView;
       public TextView			twentyThreeTextView;
       public TextView			twentyFourTextView;
       public TextView			twentyFiveTextView;
       public TextView			twentySixTextView;
       public TextView			twentySevenTextView;
       public TextView			twentyEightTextView;
       public TextView			twentyNineTextView;
       public TextView			thirtyTextView;
       public TextView			thirtyOneTextView;
       public TextView			thirtyTwoTextView;
       public TextView			thirtyThreeTextView;
       public TextView			thirtyFourTextView;
       public TextView			thirtyFiveTextView;
       public TextView			thirtySixTextView;
       public TextView			thirtySevenTextView;
       public TextView			thirtyEightTextView;
       public TextView			thirtyNineTextView;
       public TextView			foutyTextView;
       public TextView			foutyOneTextView;
       public TextView			foutyTwoTextView;
       
       public TextView			foutyThreeTextView;
       public TextView			foutyFourTextView;
       public TextView			foutyFiveTextView;
       public TextView			foutySixTextView;
       public TextView			foutySevenTextView;
       public TextView			foutyEightTextView;
       public TextView			foutyNineTextView;
       public TextView			fiftyTextView;
       
   }
   
   /**控件*/
   public final class CommitDataOneListViewHolder{
       public TextView 			oneTextView;
       public TextView			twoTextView;
       public TextView 			threeTextView;
       public TextView			fourTextView;
       public TextView 			fiveTextView;
       public TextView			sixTextView;
       public TextView			sevenTextView;
       public TextView			eightTextView;
       public TextView			nineTextView;
       public TextView			tenTextView;
   }

	
}
