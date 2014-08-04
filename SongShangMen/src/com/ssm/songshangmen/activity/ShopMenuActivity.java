package com.ssm.songshangmen.activity;

import java.io.IOException;
import java.util.ArrayList;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ssm.songshangmen.R;
import com.ssm.songshangmen.adapter.DishesListAdapter;
import com.ssm.songshangmen.adapter.MenusListAdapter;
import com.ssm.songshangmen.database.OrderDao;
import com.ssm.songshangmen.entity.BaseResponse;
import com.ssm.songshangmen.entity.Goods;
import com.ssm.songshangmen.entity.GoodsType;
import com.ssm.songshangmen.entity.OrderGoods;
import com.ssm.songshangmen.entity.Shop;
import com.ssm.songshangmen.exception.BaseException;
import com.ssm.songshangmen.exception.ParseException;
import com.ssm.songshangmen.helper.SSMHelper;




import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;


@SuppressLint("HandlerLeak")
public class ShopMenuActivity extends BaseActivity implements ActivityCallBridge.OnMethodCallback {
	/**打印*/
	private		String					TAG = "ShopMenuActivity";
	/**图片加载*/
	protected 	ImageLoader 			imageLoader = ImageLoader.getInstance();
	/**菜单*/
	private		ArrayList<GoodsType> 	menuArrayList = new ArrayList<GoodsType>();
	/**菜单数据容器*/
	private		MenusListAdapter 		shopListAdapter;
	/**订单菜品*/
	private 	ArrayList<OrderGoods> 	dishesArrayList = new ArrayList<OrderGoods>();
	/**商品列表*/
	private		ArrayList<Goods> 	  	goodsArrayList = new ArrayList<Goods>();	
	/**菜品数据容器*/
	private 	DishesListAdapter 		dishesListAdapter;
	/**函数回调*/
	private 	ActivityCallBridge 		mBridge;
	/**记录上一次的位置*/
	private     int  					dishBeforeSelectPosi = -1;
	/**选取单个商品的总数*/
	private     int  					dishSelectTotal = 0;
	/**选取单类全部商品的总数*/
	private     int  					allDishSelectTotal = 0;
	/**被选取所有类的全部商品的总数*/
	private 	int	 					totalAllDishSelectTotal = 0;
	/**订单总数和总价*/
	private		TextView				dishNumberAndPriceTextView;
	/**总价*/
	private 	double					allDishPrice = 0;
	/**确认购买*/
	private		Button					commitBuyButton;
	/**订单数据表*/
	private 	OrderDao				orderDao;
	/**商店名称*/
	private		String					shopNameString;
	/**商店id*/
	private		String					shopIdString;
	/**等待动画*/
	private		ProgressDialog			progressDialog;
	/**当前选中的类型id位置*/
	private		int						typeIdPosition = 0;
	/**第一次进入*/
	private		Boolean					accessFlag = false;
	/**tabOneButton*/
	private		Button					tabOneButton;
	/***/
	private		Button					tabTwoButton;
	/**第一个视图*/
	private		RelativeLayout			topOneRelativeLayout;
	/**第二个视图*/
	private		RelativeLayout			topTwoRelativeLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shopmenu);
		orderDao = new OrderDao(this);
		shopNameString = getIntent().getStringExtra("shopName");
		shopIdString = getIntent().getStringExtra("shopId");
		findview();
	}
	
	protected void findview() {
		LinearLayout leftBackLinearLayout = (LinearLayout)findViewById(R.id.leftBackLinearLayout);
		leftBackLinearLayout.setVisibility(View.VISIBLE);
		leftBackLinearLayout.setOnClickListener(new buttonClickAction());
		((TextView)findViewById(R.id.title)).setText(shopNameString);
		((RelativeLayout)findViewById(R.id.bottomRelativeLayout)).setOnClickListener(new buttonClickAction());
		
		tabOneButton = (Button)findViewById(R.id.tabOneButton);
		tabOneButton.setOnClickListener(new buttonClickAction());
		tabTwoButton = (Button)findViewById(R.id.tabTwoButton);
		tabTwoButton.setOnClickListener(new buttonClickAction());
		int color = getResources().getColor(R.color.dish_tab_bgcolor);
		tabOneButton.setBackgroundResource(R.drawable.tabhost_bg);
		tabTwoButton.setBackgroundColor(color);
		
		topOneRelativeLayout = (RelativeLayout)findViewById(R.id.topOneRelativeLayout);
		topTwoRelativeLayout = (RelativeLayout)findViewById(R.id.topTwoRelativeLayout);
		
		dishNumberAndPriceTextView = (TextView)findViewById(R.id.dishNumberAndPriceTextView);
		commitBuyButton = (Button)findViewById(R.id.menuOrderButton);
		commitBuyButton.setOnClickListener(new buttonClickAction());
	}
	/**按钮点击事件*/
	class buttonClickAction implements OnClickListener
	{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.bottomRelativeLayout:
				if (totalAllDishSelectTotal>0) {
					accessToMenuCommit();
				}
				break;
			case R.id.menuOrderButton:
				if (totalAllDishSelectTotal>0) {
					accessToMenuCommit();
				}
				break;
			case R.id.leftBackLinearLayout:
				ShopMenuActivity.this.finish();
				break;
			case R.id.tabOneButton:
				int color = getResources().getColor(R.color.dish_tab_bgcolor);
				tabOneButton.setBackgroundResource(R.drawable.tabhost_bg);
				tabTwoButton.setBackgroundColor(color);
				topOneRelativeLayout.setVisibility(View.VISIBLE);
				topTwoRelativeLayout.setVisibility(View.INVISIBLE);
				break;
			case R.id.tabTwoButton:
				int colorOne = getResources().getColor(R.color.dish_tab_bgcolor);
				tabOneButton.setBackgroundColor(colorOne);
				tabTwoButton.setBackgroundResource(R.drawable.tabhost_bg);
				topOneRelativeLayout.setVisibility(View.INVISIBLE);
				topTwoRelativeLayout.setVisibility(View.VISIBLE);
				SSMHelper.showProgressDialog(progressDialog);
				new Thread(){
					public void run() {
						requestShopDetailInfo();
					}
				}.start();
				break;
			default:
				break;
			}
		}
		
	}
	/**点击进入*/
	protected void accessToMenuCommit() {
		Intent oneIntent = new Intent();
		oneIntent.setClass(ShopMenuActivity.this, OrderCommitActivity.class);
		oneIntent.putExtra("shopid", shopIdString);
		oneIntent.putExtra("totalprice", String.valueOf(allDishPrice));
		ShopMenuActivity.this.startActivity(oneIntent);
	}
	
	/**
	 * 点选东西的数量
	 * */
    public void doMethod(int menuPosition, int dishNumber, int dishPosition) {
		Log.v("123", "1234");
		if (dishPosition != dishBeforeSelectPosi) {
			dishBeforeSelectPosi = dishPosition;
			dishSelectTotal = dishesArrayList.get(dishPosition).getGoodsNumber();
		}
		dishSelectTotal = dishSelectTotal + dishNumber;
		if (dishSelectTotal > 0) {
			OrderGoods orderGoods = new OrderGoods();
			orderGoods.setGoodsId(dishesArrayList.get(dishPosition).getGoodsId());
			orderGoods.setShopId(dishesArrayList.get(dishPosition).getShopId());
			orderGoods.setName(dishesArrayList.get(dishPosition).getName());
			orderGoods.setPrice(dishesArrayList.get(dishPosition).getPrice());
			orderGoods.setType(dishesArrayList.get(dishPosition).getType());
			orderGoods.setSalePrice(dishesArrayList.get(dishPosition).getSalePrice());
			orderGoods.setGoodsNumber(dishSelectTotal);
			
			ArrayList<OrderGoods> orderGoodsArrayList = orderDao.readOrderGoods("ssm",
					dishesArrayList.get(dishPosition).getGoodsId(), dishesArrayList.get(dishPosition).getShopId(),dishesArrayList.get(dishPosition).getType());
			
			if (orderGoodsArrayList.size()>0) {
				orderDao.updateNumberById("ssm", dishesArrayList.get(dishPosition).getGoodsId(), 
						dishesArrayList.get(dishPosition).getShopId(), dishesArrayList.get(dishPosition).getType(), dishSelectTotal);
				
			}
			else {
				orderDao.insert("ssm", orderGoods);
			}
		}
		
		totalAllDishSelectTotal += dishNumber;
		allDishSelectTotal = menuArrayList.get(menuPosition).getNumber() + dishNumber;
		allDishPrice = allDishPrice + dishNumber*dishesArrayList.get(dishPosition).getPrice();
		if (dishSelectTotal >= 0) {
			menuArrayList.get(menuPosition).setNumber(allDishSelectTotal);
			shopListAdapter.notifyDataSetChanged();
		}
		dishNumberAndPriceTextView.setText("已点" + totalAllDishSelectTotal + "道菜" +",总价" + allDishPrice + "元");
    }
    
    /**获取商品的种类*/
	protected void requestGoodsType() {
		try {
			BaseResponse<GoodsType> goodsType = gosHttpOperation.invokerObtainGoodsType(shopIdString);
			int codeInt = goodsType.getCode();
			if (codeInt == 1) {
				menuArrayList = (ArrayList<GoodsType>)goodsType.getData();
				handler.sendEmptyMessage(1);
			}
			else {
				handler.sendEmptyMessage(0);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**获取商品的列表*/
	protected void requestGoodsList(String typeString, String pageString) {
		try {
			BaseResponse<Goods> goods = gosHttpOperation.invokerObtainGoodsList(shopIdString, typeString, pageString);
			int codeInt = goods.getCode();
			if (codeInt == 1) {
				goodsArrayList = (ArrayList<Goods>)goods.getData();
				handler.sendEmptyMessage(2);
			}
			else {
				handler.sendEmptyMessage(0);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**对结果进行处理*/
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				requestDataFailed();
				break;
			case 1:
				requestDataSuccessed(1);
				break;
			case 2:
				requestDataSuccessed(2);
				break;
			case 3:
				requestDidFailedOne();
				break;
			case 4:
				requestDidSuccessedOne();
				break;
			default:
				break;
			}
		}
	};
	
	/**获取数据失败*/
	protected void requestDataFailed() {
		SSMHelper.showMsg(ShopMenuActivity.this, "获取数据失败");
		accessFlag = true;
		progressDialog.dismiss();
	}
	
	/**获取数据成功*/
	protected void requestDataSuccessed(int successCode) {
		switch (successCode) {
		case 1:
			setShopMenuList();
			accessFlag = true;
			break;
		case 2:
			setMenuDishesShow();
			break;
		default:
			break;
		}
		if (accessFlag) {
			progressDialog.dismiss();
		}
	}
	
	/**
	 * 菜单展示
	 * */
	protected void setShopMenuList() {

		for (int i = 0; i < menuArrayList.size(); i++) {
			
			ArrayList<OrderGoods> orderGoodsArrayList = orderDao.readOrderGoodsByType("ssm", shopIdString,menuArrayList.get(i).getId());
			if (orderGoodsArrayList.size()>0) {
				int menuTotalNum = 0;
				for (int j = 0; j < orderGoodsArrayList.size(); j++) {
					menuTotalNum = menuTotalNum + orderGoodsArrayList.get(j).getGoodsNumber();
				}
				menuArrayList.get(i).setNumber(menuTotalNum);
			}
			else {
				menuArrayList.get(i).setNumber(0);
			}

		}
		
		shopListAdapter = new MenusListAdapter(ShopMenuActivity.this, menuArrayList);
		ListView menuListView = (ListView)findViewById(R.id.menusListView);
		menuListView.setAdapter(shopListAdapter);
		
		menuListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// TODO Auto-generated method stub
				
				int color = getResources().getColor(R.color.dish_menu_selected);
				int colorOne = getResources().getColor(R.color.dish_menu_unselected);
				
				 for(int i=0;i<parent.getCount();i++){
		             View v=parent.getChildAt(i);
		             
		             if (position == i) {
		            	 v.setBackgroundColor(color);
		            	 TextView numberTextView = (TextView)v.findViewById(R.id.menusNumberTextView);
		            	 numberTextView.setTextColor(Color.WHITE);
		            	 TextView textView = (TextView) v.findViewById(R.id.menusTextView);
		            	 textView.setTextColor(Color.WHITE);
		             } else {
		            	 v.setBackgroundColor(Color.WHITE);
		            	 TextView numberTextView = (TextView)v.findViewById(R.id.menusNumberTextView);
		            	 numberTextView.setTextColor(colorOne);
		            	 TextView textView = (TextView) v.findViewById(R.id.menusTextView);
		            	 textView.setTextColor(colorOne);
		             }
		         }
				SSMHelper.showProgressDialog(progressDialog);
				allDishSelectTotal = menuArrayList.get(position).getNumber();
				dishBeforeSelectPosi = -1;
				typeIdPosition = position;
				//setMenuDishesList(position, menuArrayList.get(position).getShopId(), position);
				new Thread(){
					public void run() {
						requestGoodsList(String.valueOf(menuArrayList.get(position).getId()), "1");
					}
				}.start();
			}
		});
		
	}
	
	/**菜品展示*/
	protected void setMenuDishesShow() {
		dishesArrayList = new ArrayList<OrderGoods>();
		for (int i = 0; i < goodsArrayList.size(); i++) {
			OrderGoods orderGoods = new OrderGoods();
			orderGoods.setGoodsId(goodsArrayList.get(i).getId());
			orderGoods.setType(goodsArrayList.get(i).getType());
			orderGoods.setName(goodsArrayList.get(i).getName());
			orderGoods.setPrice(goodsArrayList.get(i).getPrice());
			orderGoods.setSalePrice(goodsArrayList.get(i).getSalePrice());
			ArrayList<OrderGoods> orderGoodsArrayList = orderDao.readOrderGoods("ssm", goodsArrayList.get(i).getId(), shopIdString,goodsArrayList.get(i).getType());
			if (orderGoodsArrayList.size()>0) {
				orderGoods.setGoodsNumber(orderGoodsArrayList.get(0).getGoodsNumber());
			}
			else {
				orderGoods.setGoodsNumber(0);
			}
			orderGoods.setShopId(shopIdString);
			dishesArrayList.add(orderGoods);
		}
		
		dishesListAdapter = new DishesListAdapter(ShopMenuActivity.this, imageLoader, dishesArrayList, typeIdPosition);
		ListView dishesListView = (ListView)findViewById(R.id.dishesListView);
		dishesListView.setAdapter(dishesListAdapter);
	}
	/**数据获取*/
	protected void shopMenuAndOrder() {
		accessFlag = false;
		progressDialog = new ProgressDialog(ShopMenuActivity.this);
		SSMHelper.showProgressDialog(progressDialog);
		//setShopList();
		new Thread(){
			public void run() {
				requestGoodsType();
				requestGoodsList("1", "1");
			}
		}.start();
	}
	/**点餐总数和总价*/
	protected void totalDishAndPrice() {
		orderDao = new OrderDao(this);
		ArrayList<OrderGoods> orderGoodsArrayList = orderDao.readOrderGoodsList("ssm");
		if (orderGoodsArrayList.size()>0) {
			int total = 0;
			double totalPrice = 0;
			for (int i = 0; i < orderGoodsArrayList.size(); i++) {
				total += orderGoodsArrayList.get(i).getGoodsNumber();
				totalPrice += orderGoodsArrayList.get(i).getGoodsNumber()*orderGoodsArrayList.get(i).getPrice();
			}
			totalAllDishSelectTotal = total;
			allDishPrice = totalPrice;
			dishNumberAndPriceTextView.setText("已点" + total + "道菜" +",总价" + totalPrice + "元");
		}
		else {
			totalAllDishSelectTotal = 0;
			allDishPrice = 0;
			dishNumberAndPriceTextView.setText("已点" + 0 + "道菜" +",总价" + 0 + "元");
		}
	}
	/**商店详细信息*/
	protected void requestShopDetailInfo() {
		try {
			BaseResponse<Shop> shopList = gosHttpOperation.invokerObtainShopDetailByShopId(shopIdString);
			int codeInt = shopList.getCode();
			if (codeInt == 1) {
				handler.sendEmptyMessage(4);
			}
			else {
				handler.sendEmptyMessage(3);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**网络请求成功*/
	protected void requestDidFailedOne() {
		SSMHelper.showMsg(ShopMenuActivity.this, "获取数据失败");
		progressDialog.dismiss();
	}
	/**网络请求失败*/
	protected void requestDidSuccessedOne() {
		SSMHelper.showMsg(ShopMenuActivity.this, "获取数据成功");
		progressDialog.dismiss();
	}
	
	
	protected void onResume() {
		super.onResume();
		
		shopMenuAndOrder();
		totalDishAndPrice();
		mBridge = ActivityCallBridge.getInstance();
        mBridge.setOnMethodCallback(this);
	}
	
	protected void onPause() {
		super.onPause();
	}
    

}



