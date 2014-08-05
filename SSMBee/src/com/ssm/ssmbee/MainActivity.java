package com.ssm.ssmbee;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ssm.ssmbee.adapter.MenuExpandableListAdapter;
import com.ssm.ssmbee.entity.BaseResponse;
import com.ssm.ssmbee.entity.OrderMenu;
import com.ssm.ssmbee.entity.StateCode;
import com.ssm.ssmbee.exception.BaseException;
import com.ssm.ssmbee.httpoperation.GosHttpApplication;
import com.ssm.ssmbee.httpoperation.GosHttpOperation;
import com.ssm.ssmbee.helper.SSMHelper;

import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

@SuppressLint({ "InlinedApi", "NewApi" })
public class MainActivity extends BaseActivity {
	
	/**等待动画*/
	private		ProgressDialog			progressDialog;
	/**退出时间*/
	private 	long 					exitTime	=	0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setActionBar();
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}
	/**设置actionbar*/
	protected void setActionBar() {
		ActionBar actionBar = getSupportActionBar();
		//actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayUseLogoEnabled(true);
		actionBar.setLogo(R.drawable.ico_backbee);
		actionBar.setTitle("我的抢单");
		actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.title_bar));		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		MenuItem grabItem = menu.add("抢单");
		grabItem.setIcon(R.drawable.bee_qiang_bg);
		grabItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		grabItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				Intent oneIntent = new Intent();
				oneIntent.setClass(MainActivity.this, BeeQiangActivity.class);
				MainActivity.this.startActivity(oneIntent);
				return true;
			}
		});
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	@SuppressLint("HandlerLeak")
	public static class PlaceholderFragment extends Fragment {
		
		/**完成按钮*/
		private		Button						finishButton;
		/**未完成按钮*/
		private		Button						unfinishButton;
		/**数据列表*/
		private ExpandableListView 				expandableListView;
		/**数据源*/
		private MenuExpandableListAdapter 		adapter;
		private ArrayList<String> 				groupList = new ArrayList<String>();
		private ArrayList<List<OrderMenu>> 		childList = new ArrayList<List<OrderMenu>>();
		protected GosHttpApplication 			gosHttpApplication;
		protected GosHttpOperation 				gosHttpOperation;
		/**等待动画*/
		private		ProgressDialog				progressDialog;

		
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			gosHttpApplication = (GosHttpApplication)getActivity().getApplication();
			gosHttpOperation = gosHttpApplication.getGosHttpOperation();
			findView(rootView);
			progressDialog = new ProgressDialog(getActivity());
			SSMHelper.showProgressDialog(progressDialog);
			new Thread(){
				public void run() {
					requestBeeStatusOrder("1");
				}
			}.start();
			return rootView;
		}
		
		protected void findView(View view) {
			unfinishButton = (Button)view.findViewById(R.id.unfininshButton);
			unfinishButton.setOnClickListener(new OnClickAction());
			finishButton = (Button)view.findViewById(R.id.fininshedButton);
			finishButton.setOnClickListener(new OnClickAction());
			expandableListView = (ExpandableListView) view.findViewById(R.id.menuExpandableList);
		}
		
	    /**请求小蜜蜂状态订单信息*/
		protected void requestBeeStatusOrder(String orderStatusString) {
			try {
				String userIdString = SSMHelper.getSharePreStr(getActivity(), GosHttpApplication.USER_ID_STRING);
				BaseResponse<OrderMenu> baseResponse = gosHttpOperation.invokerObtainBeeStatusOrder(userIdString, orderStatusString);
				int codeInt = baseResponse.getCode();
				if (codeInt == 1) {
					groupList.clear();
					childList.clear();
					List<OrderMenu> childListOne = baseResponse.getData();
					for (int i = 0; i < childListOne.size(); i++) {
						groupList.add(String.valueOf(i+1));
						List<OrderMenu> childTemp = new ArrayList<OrderMenu>();
						childTemp.add(childListOne.get(i));
						childList.add(childTemp);
					}
					loginHandler.sendEmptyMessage(1);
				}
				else {
					loginHandler.sendEmptyMessage(0);
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				loginHandler.sendEmptyMessage(0);
			} catch (BaseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				loginHandler.sendEmptyMessage(0);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				loginHandler.sendEmptyMessage(0);
			}
		}
		
		private Handler loginHandler = new Handler(){
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
					loginRequestFailed();
					break;
				case 1:
					loginRequestSuccessed();
					break;
				default:
					break;
				}
			}
		};
		/**请求失败*/
		protected void loginRequestFailed() {
			progressDialog.dismiss();
			Toast.makeText(getActivity(), "获取小蜜蜂订单信息失败", Toast.LENGTH_SHORT).show();
		}
		/**请求成功*/
		protected void loginRequestSuccessed() {
			adapter = new MenuExpandableListAdapter(getActivity(), groupList, childList);
			expandableListView.setAdapter(adapter);
			progressDialog.dismiss();
		}
		
		protected class OnClickAction implements OnClickListener{

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.unfininshButton:
					unfinishButton.setBackgroundResource(R.drawable.button_selected_bg);
					unfinishButton.setCompoundDrawablesWithIntrinsicBounds(null, 
							getResources().getDrawable(R.drawable.unfinish_bg), null, null);
					finishButton.setBackgroundResource(R.drawable.button_unselect_bg);
					finishButton.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.finish_bg), null, null);
					SSMHelper.showProgressDialog(progressDialog);
					new Thread(){
						public void run() {
							requestBeeStatusOrder("1");
						}
					}.start();
					break;
				case R.id.fininshedButton:
					unfinishButton.setBackgroundResource(R.drawable.button_unselect_bg);
					unfinishButton.setCompoundDrawablesWithIntrinsicBounds(null, 
							getResources().getDrawable(R.drawable.unfinished_bg), null, null);
					finishButton.setBackgroundResource(R.drawable.button_selected_bg);
					finishButton.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.finished_bg), null, null);
					SSMHelper.showProgressDialog(progressDialog);
					new Thread(){
						public void run() {
							requestBeeStatusOrder("2");
						}
					}.start();
					break;
				default:
					break;
				}
			}
			
		}
		
	}
	
	/**请求小蜜蜂订单数据*/
	protected void requestBeeOrder(String userIdString, String orderStatus) {
		try {
			StateCode baseResponse = gosHttpOperation.invokerObtainBeeOrder(userIdString, orderStatus);
			int codeInt = baseResponse.getCode();
			if (codeInt == 1) {
				beeOrderHandler.sendEmptyMessage(1);
			}
			else {
				beeOrderHandler.sendEmptyMessage(0);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			beeOrderHandler.sendEmptyMessage(0);
		} catch (BaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			beeOrderHandler.sendEmptyMessage(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			beeOrderHandler.sendEmptyMessage(0);
		}
	}
	
	@SuppressLint("HandlerLeak")
	private Handler beeOrderHandler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				loginRequestFailed();
				break;
			case 1:
				loginRequestSuccessed();
				break;
			default:
				break;
			}
		}
	};
	/**登录请求失败*/
	protected void loginRequestFailed() {
		progressDialog.dismiss();
		Toast.makeText(MainActivity.this, "请求订单数据失败", Toast.LENGTH_SHORT).show();
	}
	/**登录请求成功*/
	protected void loginRequestSuccessed() {
		progressDialog.dismiss();
		Toast.makeText(MainActivity.this, "请求订单数据成功", Toast.LENGTH_SHORT).show();
	}
	
	//按两次返回键退出
	 @Override
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
	     if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
	     	if ((System.currentTimeMillis() - exitTime) > 2000) {
	             Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
	             exitTime = System.currentTimeMillis();
	         } else {
	             MainActivity.this.finish();
	             System.exit(0);
	         }
	         return true;
	     }
	     return super.onKeyDown(keyCode, event);
	 }

}
