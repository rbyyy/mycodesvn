package com.gos.yypad;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.gos.yypad.database.ChooseOrderDao;
import com.gos.yypad.entity.ChooseOrder;
import com.gos.yypad.helper.GOSHelper;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gos.yypad.database.ChooseOrderDao;

public class ProductMainSceneActivity extends BaseActivity {
	private String					TAG = "ProductMainSceneActivity";
	private String					productCodeString;//产品编码
	private String					productTypeString;//产品编码
	private String					productNameString;//产品编码
	private String					productPriceString;//产品价格
	private String 					pictureUrlString;//产品图片url
	private String 					productRemarkString;//产品详情
	private String					productOperatorNmeString;//产品操作系统
	private String					productIsOnString;//产品在售状态
	private ChooseOrderDao			chooseOrderDao;//订单数据库
	
	private ImageButton				beforeArrowImageBtn;
	private ImageButton				afterArrowImageBtn;
	
	private String[]				pictureStrings;
	
	private	int						currentPictureItem;//当前第几张图
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_productmainscene);
		//标题
		TextView topTitle = (TextView)findViewById(R.id.topTitleView);
		topTitle.setTextColor(Color.WHITE);
		String titleString = getIntent().getStringExtra("accessProductMainType");
		switch (Integer.parseInt(titleString)) {
		case 1:
			topTitle.setText("DIY专区(产品主图)");
			break;
		case 2:
			topTitle.setText("整机专区");
			break;
		case 3:
			topTitle.setText("精品展示");
			break;
		default:
			break;
		}
		//返回按钮
		ImageButton leftNaviImageButton = (ImageButton)findViewById(R.id.leftNaviButton);
		leftNaviImageButton.setOnClickListener(new OnClickListener() {
					
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ProductMainSceneActivity.this.finish();
			}
		});
		//前一张的按钮
		beforeArrowImageBtn = (ImageButton)findViewById(R.id.beforeArrowImageButton);
		//后一张的按钮
		afterArrowImageBtn = (ImageButton)findViewById(R.id.afterArrowImageButton);
		
		chooseOrderDao = new ChooseOrderDao(this);//订单
		
		productCodeString = getIntent().getStringExtra("productCode");
		productTypeString = getIntent().getStringExtra("productType");
		productNameString = getIntent().getStringExtra("productName");
		productPriceString = getIntent().getStringExtra("productPrice");
		pictureUrlString = getIntent().getStringExtra("productImageUrl");
		productRemarkString = getIntent().getStringExtra("productPremark");
		productOperatorNmeString = getIntent().getStringExtra("productOperatorNme");
		productIsOnString = getIntent().getStringExtra("productIsOn");
		currentPictureItem = 0;
		
		pictureStrings = pictureUrlString.split(",");
		productMainPicture(pictureStrings[0]);//
		
		if (currentPictureItem == 0) {
			beforeArrowImageBtn.setVisibility(View.INVISIBLE);
		}
		
		
		if (pictureStrings.length > 1) {
			beforeArrowImageBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if (currentPictureItem > 0) {
						currentPictureItem--;
						productMainPicture(pictureStrings[currentPictureItem]);
					}
					if (currentPictureItem == 0) {
						beforeArrowImageBtn.setVisibility(View.INVISIBLE);
					}
					if (currentPictureItem < pictureStrings.length-1) {
						afterArrowImageBtn.setVisibility(View.VISIBLE);
					}
				}
			});
			afterArrowImageBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (currentPictureItem < pictureStrings.length) {
						currentPictureItem++;
						productMainPicture(pictureStrings[currentPictureItem]);
					}
					if (currentPictureItem > 0) {
						beforeArrowImageBtn.setVisibility(View.VISIBLE);
					}
					if (currentPictureItem == pictureStrings.length-1) {
						afterArrowImageBtn.setVisibility(View.INVISIBLE);
					}
				}
			});
		}
		else if (pictureStrings.length == 1) {
			beforeArrowImageBtn.setVisibility(View.INVISIBLE);
			afterArrowImageBtn.setVisibility(View.INVISIBLE);
		}
//		
//		//
//		int a = getIntent().getIntExtra("productID", 10);
//		String str = String.valueOf(a);
//		Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
		
		navigationBarButton();
	}
	//产品主图
	protected void productMainPicture(String picUrlString) {
		//final String pictureUrlString = getIntent().getStringExtra("productImageUrl");
		//final String productRemarkString = getIntent().getStringExtra("productPremark");
		//产品图片
		ImageView productImageView = (ImageView)findViewById(R.id.productImageview);
		productImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent toPopIntent = new Intent(ProductMainSceneActivity.this,  PopImagePreviewActivity.class);
				toPopIntent.putExtra("productImageUrl", pictureUrlString);
				startActivity(toPopIntent);
			}
		});
		//imageLoader.displayImage(pictureUrlString, productImageView, options);
		
//		String[] pictureStringArray = pictureUrlString.split(",");
//		String picUrlString = pictureStringArray[0];
//		String[] urlStrings = picUrlString.split("\\/");
//		String filePathString = urlStrings[urlStrings.length-1];
//		String extPath = GOSHelper.getExternDir()+"/"+filePathString;
		
		String[] urlStrings = picUrlString.split("\\//");
		String[] oneUrlStrings = urlStrings[urlStrings.length-1].split("\\/");
		String twoUrlString = urlStrings[urlStrings.length-1].replace(oneUrlStrings[0], "");

		String extPath = GOSHelper.getExternDir()+twoUrlString;
		
		try {
			Bitmap bitmap = GOSHelper.getBitmapByFileName(extPath);
			productImageView.setBackgroundDrawable(new BitmapDrawable(bitmap));
		} catch (Exception e) {
			// TODO: handle exception
			 e.printStackTrace();
		}
		
		//查看详情
		ImageButton checkDetailImageButton = (ImageButton)findViewById(R.id.checkDetailsImageButton);
		checkDetailImageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent detailIntent = new Intent(ProductMainSceneActivity.this, ProductDetailActivity.class);
				detailIntent.putExtra("productImageUrl", pictureUrlString);
				detailIntent.putExtra("productPremark", productRemarkString);
				ProductMainSceneActivity.this.startActivity(detailIntent);
				//ProductMainSceneActivity.this.finish();
			}
		});
		//加入选购单
		ImageButton joinChooseImageButton = (ImageButton)findViewById(R.id.joinChooseImageButton);
		joinChooseImageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				joinChooseImageButtonAction();
			}
		});
	}
	 //选用button事件响应
   @SuppressLint("SimpleDateFormat")
   private void joinChooseImageButtonAction (){
	   int numberInt = Integer.parseInt(chooseOrderDao.queryOrderIdIsExist("yy", productCodeString));
	   if (numberInt > 0) {
		   Log.v(TAG, "订单数据已经存在");
		   numberInt++;
		   if (chooseOrderDao.updateNumberById("yy", productCodeString, String.valueOf(numberInt))) {
				//Toast.makeText(DiyDivsionActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
				Log.v(TAG, "更新成功");
				Intent chooseIntent = new Intent(ProductMainSceneActivity.this, DiyDivsionActivity.class);
				ProductMainSceneActivity.this.startActivity(chooseIntent);
				ProductMainSceneActivity.this.finish();
		   } 
		   else {
			   //Toast.makeText(DiyDivsionActivity.this, "更新失败", Toast.LENGTH_SHORT).show();
			   Log.v(TAG, "更新失败");  
		   }
	   }
	   else {
		   String dateString = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:sss").format(new Date());
		   ChooseOrder chooseOrder = new ChooseOrder();
		   chooseOrder.setOrder_id(productCodeString);
		   chooseOrder.setOrder_type(productTypeString);
		   chooseOrder.setBusiness_name(productNameString);
		   chooseOrder.setBusiness_price(productPriceString);
		   chooseOrder.setBusiness_number("1");
		   chooseOrder.setBusiness_date(dateString);
		   if (chooseOrderDao.insert("yy", chooseOrder)) {
			    Log.i(TAG, "插入chooseorderdao数据库成功");
				Intent chooseIntent = new Intent(ProductMainSceneActivity.this, DiyDivsionActivity.class);
				ProductMainSceneActivity.this.startActivity(chooseIntent);
				ProductMainSceneActivity.this.finish();
		   }
	   }

   }
	
	//导航条按钮事件
	protected void navigationBarButton() {
		ImageButton completeButton = (ImageButton)findViewById(R.id.completeSectionImageButton);
		completeButton.setOnClickListener(new MyOnClickListener());
		
		ImageButton diyButton = (ImageButton)findViewById(R.id.diySectionImageButton);
		diyButton.setOnClickListener(new MyOnClickListener());
		
		ImageButton promotionButton = (ImageButton)findViewById(R.id.promotionSectionImageButton);
		promotionButton.setOnClickListener(new MyOnClickListener());
		
		ImageButton boutiqueButton = (ImageButton)findViewById(R.id.boutiqueSectionImageButton);
		boutiqueButton.setOnClickListener(new MyOnClickListener());
		
		ImageButton firmButton = (ImageButton)findViewById(R.id.firmSectionImageButton);
		firmButton.setOnClickListener(new MyOnClickListener());
		
		ImageButton chooseButton = (ImageButton)findViewById(R.id.chooseSectionImageButton);
		chooseButton.setOnClickListener(new MyOnClickListener());
		
		ImageButton setButton = (ImageButton)findViewById(R.id.setSectionImageButton);
		setButton.setOnClickListener(new MyOnClickListener());
		
	}
	//点击事件
	class MyOnClickListener implements OnClickListener{
		public void onClick(View v)
		{
			switch(v.getId()) {
            
            case R.id.firmSectionImageButton: 
            	Intent firmIntent = new Intent(ProductMainSceneActivity.this, FirmMienActivity.class);
            	ProductMainSceneActivity.this.startActivity(firmIntent); 
                break;
            case R.id.promotionSectionImageButton:  
            	Intent salesIntent = new Intent(ProductMainSceneActivity.this, SalesPromotionActivity.class);
            	ProductMainSceneActivity.this.startActivity(salesIntent);
                break;
            case R.id.diySectionImageButton:   
            	Intent diyIntent = new Intent(ProductMainSceneActivity.this, DiyDivsionActivity.class);
            	ProductMainSceneActivity.this.startActivity(diyIntent);
                break;
            case R.id.completeSectionImageButton:  
            	Intent completeIntent = new Intent(ProductMainSceneActivity.this, CompleteMachineActivity.class);
            	ProductMainSceneActivity.this.startActivity(completeIntent);
                break;
            case R.id.boutiqueSectionImageButton:   
            	Intent boutiqueIntent = new Intent(ProductMainSceneActivity.this, BoutiQueshowActivity.class);
            	ProductMainSceneActivity.this.startActivity(boutiqueIntent);
                break;
            case R.id.chooseSectionImageButton:  
				Intent chooseIntent = new Intent(ProductMainSceneActivity.this, DiyDivsionActivity.class);
				ProductMainSceneActivity.this.startActivity(chooseIntent);
				ProductMainSceneActivity.this.finish();
                break;
            case R.id.setSectionImageButton:   
            	Intent setIntent = new Intent(ProductMainSceneActivity.this, SettingActivity.class);
            	setIntent.putExtra("accessType", "1");
            	ProductMainSceneActivity.this.startActivity(setIntent);
                break;
            default: break;
            }    
		}
	}
}
