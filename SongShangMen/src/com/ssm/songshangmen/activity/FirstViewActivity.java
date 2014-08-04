package com.ssm.songshangmen.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.ssm.songshangmen.R;
import com.ssm.songshangmen.R.dimen;
import com.ssm.songshangmen.adapter.GoodsListAdapter;
import com.ssm.songshangmen.adapter.ShopListAdapter;
import com.ssm.songshangmen.entity.BaseResponse;
import com.ssm.songshangmen.entity.Goods;
import com.ssm.songshangmen.entity.Shop;
import com.ssm.songshangmen.exception.BaseException;
import com.ssm.songshangmen.exception.ParseException;
import com.ssm.songshangmen.helper.SSMHelper;
import com.ssm.songshangmen.httpoperation.GosHttpApplication;
import com.ssm.songshangmen.httpoperation.GosHttpOperation;

public class FirstViewActivity extends Fragment {
	private 	String 					TAG = "FirstViewActivity";
	protected 	ImageLoader 			imageLoader = ImageLoader.getInstance();
	protected 	GosHttpApplication 		gosHttpApplication;
	protected 	GosHttpOperation 		gosHttpOperation;
	/**商店列表*/
	private		ArrayList<Shop>			shopsList = new ArrayList<Shop>();
	/**等待动画*/
	private		ProgressDialog			progressDialog;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		gosHttpApplication = (GosHttpApplication)getActivity().getApplication();
		gosHttpOperation = gosHttpApplication.getGosHttpOperation();
		
		View v = inflater.inflate(R.layout.activity_firstview, container, false); 
		initTopBar(v);
        return v;       
    }
	/**顶部栏*/
	protected void initTopBar(View v) {
		((ImageButton)v.findViewById(R.id.image_btn_right_one)).setVisibility(View.VISIBLE);
		((ImageButton)v.findViewById(R.id.image_btn_right_two)).setVisibility(View.VISIBLE);
		((Button)v.findViewById(R.id.takeoutButton)).setOnClickListener(new TopButtonClickAction());
		((Button)v.findViewById(R.id.dimestoreButton)).setOnClickListener(new TopButtonClickAction());
		((Button)v.findViewById(R.id.serviceButton)).setOnClickListener(new TopButtonClickAction());
//		name(v);
		//setShopList(v);
		progressDialog = new ProgressDialog(getActivity());
		SSMHelper.showProgressDialog(progressDialog);
		new Thread(){
			public void run() {
				requestGuessYouLikeData();
			}
		}.start();
	}
	/**顶部按钮点击事件*/
	class TopButtonClickAction implements OnClickListener
	{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent oneIntent = new Intent();
			switch (v.getId()) {
			case R.id.takeoutButton:
				oneIntent.setClass(getActivity(), TakeOutActivity.class);
				FirstViewActivity.this.startActivity(oneIntent);
				break;
			case R.id.dimestoreButton:
				oneIntent.setClass(getActivity(), DimeStoreActivity.class);
				FirstViewActivity.this.startActivity(oneIntent);
				break;
			case R.id.serviceButton:
				oneIntent.setClass(getActivity(), ServicesActivity.class);
				FirstViewActivity.this.startActivity(oneIntent);
				break;
			default:
				break;
			}
		}
		
	}
	/**商店列表*/
	protected void name(View v) {
		
		ArrayList<Goods> list = new ArrayList<Goods>();
		for (int i = 0; i < 10; i++) {
			Goods goods = new Goods();
			goods.setId(1);
			goods.setName("百年老妈火锅");
			goods.setDescription("【2店通用】100元代金券1张，可叠加使用，大厅和包间免费使用");
			goods.setPicture("http://img30.360buyimg.com/myjd/jfs/t163/231/1433806799/3947081/f67899b0/53ad0dd5Nf77ff7d6.jpg");
			goods.setSalePrice(68.0);
			goods.setPrice(100.0);
//			goods.setGoods_month_sale("已售3772");
			goods.setShopId("12");
			goods.setType(1);
			list.add(goods);
		}
		
		GoodsListAdapter goodsListAdapter = new GoodsListAdapter(getActivity(), imageLoader, list);
		ListView goodsListView = (ListView)v.findViewById(R.id.goodsListView);
		goodsListView.setAdapter(goodsListAdapter);
	}
	
	/**商店列表*/
	protected void setShopListOne(View v) {
		
		final ArrayList<Shop> list = new ArrayList<Shop>();
		for (int i = 0; i < 10; i++) {
			Shop shops = new Shop();
			shops.setId("1");
			shops.setName("百年老妈火锅");
			shops.setLevel(3);
			//goods.setFreeSendLimit(20);
			shops.setSendTime("40");
			list.add(shops);
		}
		
		ShopListAdapter shopListAdapter = new ShopListAdapter(getActivity(), imageLoader, list);
		ListView shopListView = (ListView)v.findViewById(R.id.goodsListView);
		shopListView.setAdapter(shopListAdapter);
		shopListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent oneIntent = new Intent();
				oneIntent.setClass(getActivity(), ShopMenuActivity.class);
				oneIntent.putExtra("shopName", list.get(position).getName());
				getActivity().startActivity(oneIntent);
			}
		});
	}
	/**获取猜你喜欢的数据*/
	protected void requestGuessYouLikeData() {
		try {
			BaseResponse<Shop> shopList = gosHttpOperation.invokerGuessYourLike("116.417384", "39.920178");
			int codeInt = shopList.getCode();
			if (codeInt == 1) {
				shopsList = (ArrayList<Shop>)shopList.getData();
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
	
	/**对结果进行处理*/
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				requestDataFailed();
				break;
			case 1:
				requestDataSuccessed();
				break;
			default:
				break;
			}
		}
	};
	
	/**获取数据失败*/
	protected void requestDataFailed() {
		SSMHelper.showMsg(getActivity(), "获取数据失败");
		progressDialog.dismiss();
	}
	
	/**获取数据成功*/
	protected void requestDataSuccessed() {
		setShopList();
		progressDialog.dismiss();
	}
	
	/**商店列表*/
	protected void setShopList() {
		ShopListAdapter shopListAdapter = new ShopListAdapter(getActivity(), imageLoader, shopsList);
		ListView shopListView = (ListView)getActivity().findViewById(R.id.goodsListView);
		shopListView.setAdapter(shopListAdapter);
	}
	
}
