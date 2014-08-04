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
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;

public class ContactPlatformOneActivity extends BaseActivity {
	/**打印标示*/
	private String	TAG = "ContactPlatformOneActivity";
	/** head 头部 的左侧菜单 按钮*/
	private LinearLayout	top_left_Layout;
	/** head 头部 的右侧菜单 按钮*/
	private ImageButton		top_right_headImageButton;
	/**head的标题*/
	private TextView		topTitleTextView;
	/**tabhost的定义*/
	private TabHost 		tabHost;
	/**提交建议*/
	private LinearLayout	contactPlatformOneLineLayout;
	/**公共信息展示*/
	private LinearLayout	publicInfoLinearLayout;
	/**我的建议内容*/
	private LinearLayout	mySuggestLinearLayout;
	/**建议内容*/
	private EditText		contactPlatformOneSuggestEditText;
	/**提交建议*/
	private Button			contactPlatformOneCommitButton;
	/**公共信息*/
	private ListView		publicInfoListView;
	/**我的建议列表*/
	private ListView		mySuggestListView;
	/**公共信息数据源*/
	private ArrayList<HashMap<String, String>> publicInfoArrayList;
	/**我的建议数据源*/
	private ArrayList<HashMap<String, Object>> mySuggestArrayList;
	/***/
	private MyPublicInfoListAdapter				myPublicInfoListAdapter;
	/**我的建议*/
	private	MySuggestListAdapter				mySuggestListAdapter;
	
	private ProgressDialog 							mSaveDialog = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contactplatformone);
		publicInfoArrayList = new ArrayList<HashMap<String,String>>();
		mySuggestArrayList = new ArrayList<HashMap<String,Object>>();
		initView();
	}
	//初始化界面
	protected void initView() {
		top_left_Layout = (LinearLayout)findViewById(R.id.leftPopLinearLayout);//左边的返回按钮
		topTitleTextView = (TextView)findViewById(R.id.topTitleTextview);
		topTitleTextView.setText("人大代表,政协委员联络平台");//标题
		top_right_headImageButton = (ImageButton)findViewById(R.id.loginImageButton);//用户登录按钮
		//左边返回按钮的点击事件
		top_left_Layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String userIdString = GOSHelper.getSharePreStr(ContactPlatformOneActivity.this, GosHttpApplication.USER_ID_STRING);
				if (userIdString.equals("")) {
					ContactPlatformOneActivity.this.finish();
				}
				else {
					Intent oneIntent = new Intent(ContactPlatformOneActivity.this,MainActivity.class);
					ContactPlatformOneActivity.this.startActivity(oneIntent);
				}
			}
		});
		//隐藏右边的按钮
		top_right_headImageButton.setVisibility(View.INVISIBLE);
		contactPlatformOneLineLayout = (LinearLayout)findViewById(R.id.contactPlatformOneLineLayout);
		publicInfoLinearLayout = (LinearLayout)findViewById(R.id.publicInfoLinearLayout);
		mySuggestLinearLayout = (LinearLayout)findViewById(R.id.mySuggestLinearLayout);
		contactPlatformOneSuggestEditText = (EditText)findViewById(R.id.contactPlatformOneSuggestEditText);
		contactPlatformOneCommitButton = (Button)findViewById(R.id.contactPlatformOneCommitButton);
		contactPlatformOneCommitButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				decideInput();
			}
		});
		publicInfoListView = (ListView)findViewById(R.id.publicInfoListView);
		mySuggestListView = (ListView)findViewById(R.id.mySuggestListView);
		initTabHost();
	}
	/**判断输入*/
	protected void decideInput() {
		final String decideString = contactPlatformOneSuggestEditText.getText().toString().trim();
		if (decideString.equals("") || decideString == null) {
			Toast.makeText(ContactPlatformOneActivity.this, "建议内容不能为空", Toast.LENGTH_SHORT).show();
		}
		else {
			new Thread()
			{
				public void run() {
					representSuggest(decideString);
				}
			}.start();
		}
	}
	
	//初始化tabhost
	protected void initTabHost() {
		tabHost = (TabHost) findViewById(R.id.topContactPlatformOneTabHost);  
        tabHost.setup();  
        TabWidget tabWidget = tabHost.getTabWidget();  
        
        tabHost.addTab(tabHost.newTabSpec("tab1")  
                .setIndicator("提交建议", getResources().getDrawable(R.drawable.ic_launcher))  
                .setContent(R.id.contactPlatformOneView1));   
          
        tabHost.addTab(tabHost.newTabSpec("tab2")  
                .setIndicator("公共信息")  
                .setContent(R.id.contactPlatformOneView2));  
        tabHost.addTab(tabHost.newTabSpec("tab3")  
                .setIndicator("我的建议")  
                .setContent(R.id.contactPlatformOneView3));
          
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
			   		 	contactPlatformOneLineLayout.setVisibility(View.VISIBLE);
			   		 	publicInfoLinearLayout.setVisibility(View.INVISIBLE);
			   		 	mySuggestLinearLayout.setVisibility(View.INVISIBLE);
			   		 break;
			   	 case 1:
			   		 	contactPlatformOneLineLayout.setVisibility(View.INVISIBLE);
			   		 	publicInfoLinearLayout.setVisibility(View.VISIBLE);
			   		 	mySuggestLinearLayout.setVisibility(View.INVISIBLE);
			   		    mSaveDialog = ProgressDialog.show(ContactPlatformOneActivity.this, "数据更新","数据正在更新中，请稍等...", true); 
			   		    mSaveDialog.setCanceledOnTouchOutside(false);
			   		   new Thread()
			   		 	{
			   		 		public void run() {
			   		 			publicInformation("0");
							}
			   		 	}.start();
			   		 break;
			   	 case 2:
			   		 contactPlatformOneLineLayout.setVisibility(View.INVISIBLE);
		   		 	 publicInfoLinearLayout.setVisibility(View.INVISIBLE);
		   		 	 mySuggestLinearLayout.setVisibility(View.VISIBLE);
		   		 	mSaveDialog = ProgressDialog.show(ContactPlatformOneActivity.this, "数据更新","数据正在更新中，请稍等...", true); 
		   		 	mSaveDialog.setCanceledOnTouchOutside(false);
			   		 new Thread()
			   		 {
			   		 	public void run() {
			   		 		mySuggestInfo();
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
    
    /**代表建议*/
	protected void representSuggest(String descriptionString) {
		try {	
			String useridString = GOSHelper.getSharePreStr(ContactPlatformOneActivity.this, GosHttpApplication.USER_RDDB_ID_STRING);
			HttpResponse aString = gosHttpOperation.invokerRepresentSuggestResponse(useridString, descriptionString);
			HttpEntity entity = aString.getEntity();
			InputStream is = entity.getContent();
			String reString = GOSHelper.convertStreamToString(is);
		
			if (reString == null || reString.equals("")) {
				
			}
			else {
				JSONObject jsonObject = JSON.parseObject(reString);
				String stateString = jsonObject.getString("state");
				if (stateString.equals("1")) {
					alertHandler.sendEmptyMessage(1);
				}
				else
				{
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
			setMessage("提交成功!").
			setPositiveButton("确定", null).show();
		}
	}
	/**置空*/
	protected void emptyInput() {		
		contactPlatformOneSuggestEditText.setText("");
	}
	
	
	//公共信息
	protected void publicInformation(String daiBiaoTypeString) {
		try {

			HttpResponse aString = gosHttpOperation.invokerPublicInformationResponse(daiBiaoTypeString);
			HttpEntity entity = aString.getEntity();
			InputStream is = entity.getContent();
			String reString = GOSHelper.convertStreamToString(is);
 			if (publicInfoArrayList.size()>0) {
 				publicInfoArrayList.removeAll(publicInfoArrayList);
 			}
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
						publicInfoShowHandler.sendEmptyMessage(0);
					}
					else {
						if (responeArray.size() > 0) {
							for (int i = 0; i < responeArray.size(); i++) {
								HashMap<String, String> map = new HashMap<String, String>();
								JSONObject oneObject = responeArray.getJSONObject(i);
								String titleString = oneObject.getString("title").trim();
								map.put("title", titleString);
								String dateString = oneObject.getString("dateandtime").trim();
								map.put("dateandtime", dateString);
								String descriptionString = oneObject.getString("description").trim();
								map.put("description", descriptionString);
								publicInfoArrayList.add(map);
							}
							publicInfoShowHandler.sendEmptyMessage(1);
						}
					}
				}
				else
				{
					Log.v("测试查询申请公开项", "测试查询申请公开项失败");
					publicInfoShowHandler.sendEmptyMessage(0);
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
			publicInfoShowHandler.sendEmptyMessage(0);
		} catch (BaseException e) {
			e.printStackTrace();
			publicInfoShowHandler.sendEmptyMessage(0);
		} catch (IOException e) {
			e.printStackTrace();
			publicInfoShowHandler.sendEmptyMessage(0);
		}
	}
	//我的建议
	protected void mySuggestInfo() {
		try {
			String useridString = GOSHelper.getSharePreStr(ContactPlatformOneActivity.this, GosHttpApplication.USER_RDDB_ID_STRING);
			HttpResponse aString = gosHttpOperation.invokerQueryRepresentSuggestResponse(useridString);
			HttpEntity entity = aString.getEntity();
			InputStream is = entity.getContent();
			String reString = GOSHelper.convertStreamToString(is);
 			if (mySuggestArrayList.size()>0) {
 				mySuggestArrayList.removeAll(mySuggestArrayList);
 			}
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
						mySuggestHandler.sendEmptyMessage(0);
					}
					else {
						if (responeArray.size() > 0) {
							for (int i = 0; i < responeArray.size(); i++) {
								HashMap<String, Object> map = new HashMap<String, Object>();
								JSONObject oneObject = responeArray.getJSONObject(i);
								String descriptionString = oneObject.getString("description").trim();
								map.put("description", descriptionString);
								String chulibumen = oneObject.getString("workBumen").trim();
								map.put("workBumen", chulibumen);
								String workerString = oneObject.getString("worker").trim();
								map.put("worker", workerString);
								String workContactString = oneObject.getString("workContact").trim();
								map.put("workContact", workContactString);
								String chengbanerString = oneObject.getString("chengbaner").trim();
								map.put("chengbaner", chengbanerString);
								String chengbanContactString = oneObject.getString("chengbanContact").trim();
								map.put("chengbanContact", chengbanContactString);
								mySuggestArrayList.add(map);
							}
							mySuggestHandler.sendEmptyMessage(1);
						}
					}
				}
				else
				{
					Log.v("我的建议", "查询我的建议失败");
					mySuggestHandler.sendEmptyMessage(0);
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
			mySuggestHandler.sendEmptyMessage(0);
		} catch (BaseException e) {
			e.printStackTrace();
			mySuggestHandler.sendEmptyMessage(0);
		} catch (IOException e) {
			e.printStackTrace();
			mySuggestHandler.sendEmptyMessage(0);
		}
	}
	Handler mySuggestHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			
			switch (msg.what) {
			case 0:
				Toast.makeText(ContactPlatformOneActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
				mSaveDialog.dismiss();
				break;
			case 1:
				mySuggestListAdapter = new MySuggestListAdapter();
				mySuggestListView.setAdapter(mySuggestListAdapter);
				mSaveDialog.dismiss();
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};
	
	/**adapter*/
	private class MySuggestListAdapter extends BaseAdapter
	{
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mySuggestArrayList.size();
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
		public View getView(final int position, View convertView, ViewGroup parent) {
			mySuggestListViewHolder holder;
			// TODO Auto-generated method stub
			if (convertView == null) {
				convertView = LayoutInflater.from(getApplication()).inflate(R.layout.item_mysuggest, null);
				holder = new mySuggestListViewHolder();
				//取到各个控件的对象
				 holder.titleOneTextView = (TextView)convertView.findViewById(R.id.listInfoQueryOneTextView);
				 holder.titleTwoTextView = (TextView)convertView.findViewById(R.id.listInfoQueryTwoTextView);
				 holder.titleThreeTextView = (TextView)convertView.findViewById(R.id.listInfoQueryThreeTextView);
				 holder.titleFourTextView = (TextView)convertView.findViewById(R.id.listInfoQueryFourTextView);
				 holder.titleFiveTextView = (TextView)convertView.findViewById(R.id.listInfoQueryFiveTextView);
				 holder.titleSixTextView = (TextView)convertView.findViewById(R.id.listInfoQuerySixTextView);
				 holder.titleSevenTextView = (TextView)convertView.findViewById(R.id.listInfoQuerySevenTextView);
				 holder.titleEightTextView = (TextView)convertView.findViewById(R.id.listInfoQueryEightTextView);
				 holder.titleNineTextView = (TextView)convertView.findViewById(R.id.listInfoQueryNineTextView);
				 holder.titleTenTextView = (TextView)convertView.findViewById(R.id.listInfoQueryTenTextView);
				 holder.titleElvenTextView = (TextView)convertView.findViewById(R.id.listInfoQueryElvenTextView);
				 holder.titleTwelveTextView = (TextView)convertView.findViewById(R.id.listInfoQueryTwelveTextView);
				 convertView.setTag(holder);
			}
			else {
				holder = (mySuggestListViewHolder)convertView.getTag();
			}

			holder.titleOneTextView.setText("受理部门");
			holder.titleTwoTextView.setText((String)mySuggestArrayList.get(position).get("workBumen"));
			holder.titleThreeTextView.setText("受理人");
			holder.titleFourTextView.setText((String)mySuggestArrayList.get(position).get("worker"));
			holder.titleFiveTextView.setText("受理人电话");
			holder.titleSixTextView.setText((String)mySuggestArrayList.get(position).get("workContact"));
			holder.titleSevenTextView.setText("承办人");
			holder.titleEightTextView.setText((String)mySuggestArrayList.get(position).get("chengbaner"));
			holder.titleNineTextView.setText("承办人电话");
			holder.titleTenTextView.setText((String)mySuggestArrayList.get(position).get("chengbanContact"));
			holder.titleElvenTextView.setText("回复内容");
			holder.titleTwelveTextView.setText(GOSHelper.stripTags((String)mySuggestArrayList.get(position).get("description")));
			
			return convertView;
		}
	}

	/**存放控件*/
   public final class mySuggestListViewHolder{
       public TextView 			titleOneTextView;
       public TextView			titleTwoTextView;
       public TextView 			titleThreeTextView;
       public TextView			titleFourTextView;
       public TextView 			titleFiveTextView;
       public TextView			titleSixTextView;
       public TextView 			titleSevenTextView;
       public TextView			titleEightTextView;
       public TextView 			titleNineTextView;
       public TextView			titleTenTextView;
       public TextView 			titleElvenTextView;
       public TextView			titleTwelveTextView;
   }
	
	
	//
	@SuppressLint("HandlerLeak")
	Handler publicInfoShowHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 0:
				Toast.makeText(ContactPlatformOneActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
				mSaveDialog.dismiss();
				break;
			case 1:
				myPublicInfoListAdapter = new MyPublicInfoListAdapter();
				publicInfoListView.setAdapter(myPublicInfoListAdapter);
				mSaveDialog.dismiss();
				break;
			default:
				break;
			}
			
			super.handleMessage(msg);
		}
	};
	
	/**adapter*/
	private class MyPublicInfoListAdapter extends BaseAdapter
	{
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return publicInfoArrayList.size();
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
		public View getView(final int position, View convertView, ViewGroup parent) {
			multimediaInfoListViewHolder holder;
			// TODO Auto-generated method stub
			if (convertView == null) {
				convertView = LayoutInflater.from(getApplication()).inflate(R.layout.list_multimediainfoitem, null);
				holder = new multimediaInfoListViewHolder();
				//取到各个控件的对象
				 holder.titleTextView = (TextView)convertView.findViewById(R.id.multimediaInfoTextView);//配件名称
				 holder.dateTextView = (TextView)convertView.findViewById(R.id.multimediaInfoDateTextView);//配件状态
				 convertView.setTag(holder);
			}
			else {
				holder = (multimediaInfoListViewHolder)convertView.getTag();
			}
			//标题
			holder.titleTextView.setText((String)publicInfoArrayList.get(position).get("title"));
			//日期
			holder.dateTextView.setText((String)publicInfoArrayList.get(position).get("dateandtime"));
			
			//为每个item添加click事件
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent oneIntent = new Intent(ContactPlatformOneActivity.this, ContactPlatformContentShowActivity.class);
					oneIntent.putExtra("title", (String)publicInfoArrayList.get(position).get("title"));
					oneIntent.putExtra("description", (String)publicInfoArrayList.get(position).get("description"));
					ContactPlatformOneActivity.this.startActivity(oneIntent);
				}
			});
			return convertView;
		}
	}

	/**存放控件*/
   public final class multimediaInfoListViewHolder{
       public TextView 			titleTextView;
       public TextView			dateTextView;
   }
    
  //返回键事件
 @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
      if(keyCode == KeyEvent.KEYCODE_BACK){
    	  String userIdString = GOSHelper.getSharePreStr(ContactPlatformOneActivity.this, GosHttpApplication.USER_ID_STRING);
			if (userIdString.equals("")) {
				ContactPlatformOneActivity.this.finish();
			}
			else {
				Intent oneIntent = new Intent(ContactPlatformOneActivity.this,MainActivity.class);
				ContactPlatformOneActivity.this.startActivity(oneIntent);
			}
          return true;
      }
      return super.onKeyDown(keyCode, event);
  }
	
	
}
