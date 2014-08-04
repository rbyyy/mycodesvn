package com.gov.nzjcy;

import java.util.ArrayList;
import java.util.HashMap;

import com.gov.nzjcy.helper.GOSHelper;
import com.gov.nzjcy.httpoperation.GosHttpApplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class UserLoginResultActivity extends BaseActivity {
	
	/**打印标志*/
	private String	TAG = "UserLoginResultActivity";
	/** head 头部 的左侧菜单 按钮*/
	private LinearLayout	top_left_Layout;
	/** head 头部 的右侧菜单 按钮*/
	private ImageButton		top_right_headImageButton;
	/**head的标题*/
	private TextView		topTitleTextView;
	/**用户名*/
	private TextView		usernameTextView;
	/**列表*/
	private ListView		userloginResultSettingListView;
	/**用户名*/
	private String 			usernameString;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_userloginresult);
		usernameString = GOSHelper.getSharePreStr(UserLoginResultActivity.this, GosHttpApplication.USER_NAME_STRING);
		initView();
	}
	//初始化界面
	protected void initView() {
		top_left_Layout = (LinearLayout)findViewById(R.id.leftPopLinearLayout);//左边的返回按钮
		topTitleTextView = (TextView)findViewById(R.id.topTitleTextview);
		topTitleTextView.setText("我");//标题
		top_right_headImageButton = (ImageButton)findViewById(R.id.loginImageButton);//用户登录按钮
		//左边返回按钮的点击事件
		top_left_Layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String userIdString = GOSHelper.getSharePreStr(UserLoginResultActivity.this, GosHttpApplication.USER_ID_STRING);
				if (userIdString.equals("")) {
					UserLoginResultActivity.this.finish();
				}
				else {
					Intent oneIntent = new Intent(UserLoginResultActivity.this,MainActivity.class);
					UserLoginResultActivity.this.startActivity(oneIntent);
				}
			}
		});
		//隐藏右边的按钮
		top_right_headImageButton.setVisibility(View.INVISIBLE);
		//右边登录按钮的点击事件
//				top_right_headImageButton.setOnClickListener(new OnClickListener() {
//					
//					@Override
//					public void onClick(View v) {
//						// TODO Auto-generated method stub
//
//					}
//				});
		usernameTextView = (TextView)findViewById(R.id.userloginresultUserName);
		usernameTextView.setText(usernameString);
		userloginResultSettingListView = (ListView)findViewById(R.id.userloginresultSettingListView);
		
	    SimpleAdapter saMenuItem = new SimpleAdapter(this,   
	    		InitData(), //数据源   
	    		R.layout.list_loginresultitem, //xml实现   
	    		new String[]{"ItemImage","ItemText","ItemId"}, //对应map的Key   
	    		new int[]{R.id.loginResultItemImage, R.id.loginResultTitleTextView,R.id.loginResultItemId});  //对应R的Id 
	    //添加Item到网格中
	    userloginResultSettingListView.setAdapter(saMenuItem); 
	    userloginResultSettingListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent oneIntent = new Intent();
				switch(Integer.parseInt(((TextView)view.findViewById(R.id.loginResultItemId)).getText().toString())){
				case 1:
					oneIntent.setClass(UserLoginResultActivity.this, MyCommitActivity.class);
					oneIntent.putExtra("title", "我提交的公开申请");
					oneIntent.putExtra("number", "1");
					UserLoginResultActivity.this.startActivity(oneIntent);
				break;
				case 2:
					oneIntent.setClass(UserLoginResultActivity.this,MyCommitActivity.class);
					oneIntent.putExtra("title", "我提交的网上举报");
					oneIntent.putExtra("number", "2");
					UserLoginResultActivity.this.startActivity(oneIntent);
				break;
				case 3:
					oneIntent.setClass(UserLoginResultActivity.this,MyCommitActivity.class);
					oneIntent.putExtra("title", "我提交的控告申诉");
					oneIntent.putExtra("number", "3");
					UserLoginResultActivity.this.startActivity(oneIntent);
				break;
				case 4:
					oneIntent.setClass(UserLoginResultActivity.this,MyCommitActivity.class);
					oneIntent.putExtra("title", "我提交的网上预约");
					oneIntent.putExtra("number", "4");
					UserLoginResultActivity.this.startActivity(oneIntent);
				break;
				case 5:
					oneIntent.setClass(UserLoginResultActivity.this,MyCommitActivity.class);
					oneIntent.putExtra("title", "检察长信箱");
					oneIntent.putExtra("number", "5");
					UserLoginResultActivity.this.startActivity(oneIntent);
				break;
				case 6:
					oneIntent.setClass(UserLoginResultActivity.this, ModifyUserInfoActivity.class);
					UserLoginResultActivity.this.startActivity(oneIntent);
				break;
				case 7:
					GOSHelper.putSharePre(UserLoginResultActivity.this, GosHttpApplication.USER_ID_STRING, "");
					oneIntent.setClass(UserLoginResultActivity.this,MainActivity.class);
					UserLoginResultActivity.this.startActivity(oneIntent);
				break;
				default:
					break;
				}
			}
		});
		
	}
	
	private ArrayList<HashMap<String, Object>> InitData(){
		ArrayList<HashMap<String, Object>> meumList = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("ItemImage", R.drawable.menu_file_open_one);
		map.put("ItemText", "我提交的公开申请");
		map.put("ItemId", "1"); 
		meumList.add(map);
		map = new HashMap<String, Object>();
		map.put("ItemImage", R.drawable.menu_online_report_one);
		map.put("ItemText", "我提交的网上举报");
		map.put("ItemId", "2");
		meumList.add(map);
		map = new HashMap<String, Object>();
		map.put("ItemImage", R.drawable.menu_sue_appeal_one);
		map.put("ItemText", "我提交的控告申诉");
		map.put("ItemId", "3");
		meumList.add(map);
		map = new HashMap<String, Object>();
		map.put("ItemImage", R.drawable.menu_online_book_one);
		map.put("ItemText", "我提交的网上预约");
		map.put("ItemId", "4");
		meumList.add(map);
		map = new HashMap<String, Object>();
		map.put("ItemImage", R.drawable.menu_mail_jiancha_one);
		map.put("ItemText", "检察长信箱");
		map.put("ItemId", "5");
		meumList.add(map);
		map = new HashMap<String, Object>();
		map.put("ItemImage", "");
		map.put("ItemText", "修改资料");
		map.put("ItemId", "6");
		meumList.add(map);
		map = new HashMap<String, Object>();
		map.put("ItemImage", "");
		map.put("ItemText", "退出登陆");
		map.put("ItemId", "7");
		meumList.add(map);
		return meumList;
	}
	//返回键事件
	 @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
        	String userIdString = GOSHelper.getSharePreStr(UserLoginResultActivity.this, GosHttpApplication.USER_ID_STRING);
			if (userIdString.equals("")) {
				UserLoginResultActivity.this.finish();
			}
			else {
				Intent oneIntent = new Intent(UserLoginResultActivity.this,MainActivity.class);
				UserLoginResultActivity.this.startActivity(oneIntent);
			}
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
	
}
