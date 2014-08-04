package com.law.fangyuan;

import java.util.ArrayList;
import java.util.HashMap;

import cn.sharesdk.douban.Douban.ShareParams;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.weibo.TencentWeibo;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class Share {
	private Context context;
	private String msg;
	
	public Share(Context context,String msg){
		this.context = context;
		this.msg = msg;
		ShareSDK.initSDK(context);
	}
	
	public void InitPopWin(View v){
		PopupWindow popupWindow = new PopupWindow (InitView(),LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
		popupWindow.setAnimationStyle(R.style.popupwindow_anim);
		popupWindow.update();
	}
	
	public View InitView(){
		View view = (View)LayoutInflater.from(context).inflate(R.layout.share,null);
		GridView gridview = (GridView) view.findViewById(R.id.share_item);
	    SimpleAdapter saMenuItem = new SimpleAdapter(context,   
	    		InitData(), //����Դ   
	    		R.layout.share_item, //xmlʵ��   
	    		new String[]{"ItemImage","ItemText","ItemId"}, //��Ӧmap��Key   
	    		new int[]{R.id.ItemImage,R.id.ItemText,R.id.ItemId});  //��ӦR��Id 
	    //���Item��������
	    gridview.setAdapter(saMenuItem); 
	    gridview.setOnItemClickListener(new OnItemClickListener() { 

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				switch(Integer.parseInt(((TextView)view.findViewById(R.id.ItemId)).getText().toString())){
				case 0:
					ShareMessage();
				break;
				case 1:
					ShareEmail();
				break;
				case 2:
					ShareSinaWeiBo();
				break;
				case 3:
					ShareQQWeiBo();
				break;
				case 4:
					ShareWeiXinTimeLine();
				break;
				case 5:
					ShareWeixin();
				break;
				}
			}
	    }); 
	    return view;
	}
	
	private ArrayList<HashMap<String, Object>> InitData(){
		ArrayList<HashMap<String, Object>> meumList = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("ItemImage", R.drawable.sns_message_icon);
		map.put("ItemText", "����");
		map.put("ItemId", "0"); 
		meumList.add(map);
		map = new HashMap<String, Object>();
		map.put("ItemImage", R.drawable.sns_email_icon);
		map.put("ItemText", "����");
		map.put("ItemId", "1");
		meumList.add(map);
		map = new HashMap<String, Object>();
		map.put("ItemImage", R.drawable.sns_sina_icon);
		map.put("ItemText", "����΢��");
		map.put("ItemId", "2");
		meumList.add(map);
		map = new HashMap<String, Object>();
		map.put("ItemImage", R.drawable.sns_qq_icon);
		map.put("ItemText", "��Ѷ΢��");
		map.put("ItemId", "3");
		meumList.add(map);
		map = new HashMap<String, Object>();
		map.put("ItemImage", R.drawable.sns_weixin_timeline_icon);
		map.put("ItemText", "΢������Ȧ");
		map.put("ItemId", "4");
		meumList.add(map);
		map = new HashMap<String, Object>();
		map.put("ItemImage", R.drawable.sns_weixin_icon);
		map.put("ItemText", "΢��");
		map.put("ItemId", "5");
		meumList.add(map);
		return meumList;
	}
	
	private void ShareEmail(){
		Intent email = new Intent(android.content.Intent.ACTION_SEND);
		email.setType("plain/text");
		email.putExtra(android.content.Intent.EXTRA_SUBJECT, "��Բ�ʼ�����");
		email.putExtra(android.content.Intent.EXTRA_TEXT, msg);
		((Activity) context).startActivityForResult(Intent.createChooser(email, "��ѡ���ʼ��������"),1001);
	}
	
	private void ShareMessage(){
		Uri uri = Uri.parse("smsto:");
		Intent sms = new Intent(Intent.ACTION_VIEW,uri);
		sms.putExtra("sms_body", msg);
		sms.setType("vnd.android-dir/mms-sms");
		((Activity) context).startActivityForResult(sms, 1002);
	}
	
	private void ShareSinaWeiBo(){ 
		Platform sinaWeibo = ShareSDK.getPlatform(context, SinaWeibo.NAME);
		sinaWeibo.setPlatformActionListener(new PaListener());
		ShareParams sp = new ShareParams();
		sp.setText(msg);
		sinaWeibo.share(sp);
	}
	
	private void ShareQQWeiBo(){
		ShareParams sp = new ShareParams();
		sp.setText(msg);
		Platform qqWeiBo = ShareSDK.getPlatform(context, TencentWeibo.NAME);
		qqWeiBo.SSOSetting(true);
		qqWeiBo.setPlatformActionListener(new PaListener());
		qqWeiBo.share(sp);
	}
	
	private void ShareWeiXinTimeLine(){
//		Platform wechatMomentsPlatform = ShareSDK.getPlatform(context, WechatMoments.NAME);
//		wechatMomentsPlatform.setPlatformActionListener(new PaListener());
//		wechatMomentsPlatform.authorize();
//		ShareParams sp = new ShareParams();
//		sp.setText(msg);
//		sp.setShareType(Platform.SHARE_TEXT);
//		wechatMomentsPlatform.share(sp);
		showShare(false, WechatMoments.NAME);
	}
	
	private void ShareWeixin(){
//		Platform wechatPlatform = ShareSDK.getPlatform(context, Wechat.NAME);
//		wechatPlatform.setPlatformActionListener(new PaListener());
//		wechatPlatform.authorize();
//		ShareParams sp = new ShareParams();
//		sp.setText(msg);
//		sp.setShareType(Platform.SHARE_TEXT);
//		wechatPlatform.share(sp);
		showShare(false, Wechat.NAME);
	}
	
	private class PaListener implements PlatformActionListener{

		@Override
		public void onCancel(Platform arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onComplete(Platform arg0, int arg1,
				HashMap<String, Object> arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onError(Platform arg0, int arg1, Throwable arg2) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	
	private void showShare(boolean silent, String platform) {
		final OnekeyShare oks = new OnekeyShare();
		oks.setText(msg);
		oks.setSilent(silent);
		if (platform != null) {
			oks.setPlatform(platform);
		}

		// ȡ��ע�ͣ�����ʵ�ֶԾ����View���н�������
//		oks.setViewToShare(getPage());

		// ȥ��ע�ͣ�����༭ҳ����ʾΪDialogģʽ
//		oks.setDialogMode();

		// ȥ��ע�ͣ����Զ���Ȩʱ���Խ���SSO��ʽ
//		oks.disableSSOWhenAuthorize();

		// ȥ��ע�ͣ����ݷ���Ĳ��������ͨ��OneKeyShareCallback�ص�
//		oks.setCallback(new OneKeyShareCallback());
		//oks.setShareContentCustomizeCallback(new ShareContentCustomizeDemo());

		// ȥ��ע�ͣ���ʾ�ھŹ��������Զ����ͼ��
//		Bitmap logo = BitmapFactory.decodeResource(menu.getResources(), R.drawable.ic_launcher);
//		String label = menu.getResources().getString(R.string.app_name);
//		OnClickListener listener = new OnClickListener() {
//			public void onClick(View v) {
//				String text = "Customer Logo -- ShareSDK " + ShareSDK.getSDKVersionName();
//				Toast.makeText(menu.getContext(), text, Toast.LENGTH_SHORT).show();
//				oks.finish();
//			}
//		};
//		oks.setCustomerLogo(logo, label, listener);

		// ȥ��ע�ͣ����ݷ���Ź����н���������΢������Ѷ΢��
//		oks.addHiddenPlatform(SinaWeibo.NAME);
//		oks.addHiddenPlatform(TencentWeibo.NAME);

		oks.show(context);
	}
	
	
}
