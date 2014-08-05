package com.gos.yypad;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.gos.yypad.helper.GOSHelper;
import com.gos.yypad.helper.ImageHelper;
import com.gos.yypad.httpoperation.GosHttpApplication;
import com.gos.yypad.database.ChooseOrderDao;
import com.gos.yypad.database.PictureShowDao;
import com.gos.yypad.database.ServerAddressDao;
import com.gos.yypad.database.ShopListDao;
import com.gos.yypad.entity.ServerAddress;

public class LauncherActivity extends BaseActivity {
	private GOSHelper			helper;
	private ChooseOrderDao		chooseOrderDao;//选择订单
	private PictureShowDao		pictureShowDao;//图片展示
	private ShopListDao			shopListDao;//店面列表
	private ServerAddressDao	serverAddressDao;//服务器地址
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launcher);
		String dateString = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
		String s2 = "2014-07-12 00:00:00";
		try {
			DateCompare(dateString, s2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		chooseOrderDao = new ChooseOrderDao(this);
		pictureShowDao = new PictureShowDao(this);
		shopListDao = new ShopListDao(this);
		serverAddressDao = new ServerAddressDao(this);
		//readAssertFile();
		InitLauncher();
		
	}
	
	public void DateCompare(String s1,String s2) throws Exception {
		//设定时间的模板
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//得到指定模范的时间
		Date d1 = sdf.parse(s1);
		Date d2 = sdf.parse(s2);
		//比较
		if(Math.abs(((d1.getTime() - d2.getTime())/(24*3600*1000))) >=30) {
			LauncherActivity.this.finish();
			System.exit(0);
		}else{
			System.out.println("小于三天");
		}
	}
	
	private void InitLauncher(){
        new Handler().postDelayed(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Boolean firString = GOSHelper.getSharePreBoolean(LauncherActivity.this, GosHttpApplication.FIRST_ACCESS_STRING);
				if (!firString) {
					Intent main = new Intent();
			   		main.setClass(LauncherActivity.this,SettingActivity.class);//splash
			   		main.putExtra("accessType", "0");//进入类型
					LauncherActivity.this.startActivity(main);
				}
				else {
					Intent main = new Intent();
			   		main.setClass(LauncherActivity.this,Splash.class);//splash
					LauncherActivity.this.startActivity(main);
				}
				LauncherActivity.this.finish();
			}
        	
        }, 3000);
	}
	
//	protected Boolean readAssertFile(String fileNameString) {
//		try {     
//            //以下是取得图片的两种方法  
//            //////////////// 方法1：取得的是byte数组, 从byte数组生成bitmap 
//            byte[] data =  getFromAssetsFolder("mainpagefive.png");
//            if(data!=null){  
//                //Bitmap newBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);// bitmap  
//            	String fileFolder = GOSHelper.getExternDir();
//            	String filepathimg = fileFolder + "/" + "one.jpg";
//                //File f = new File(filepathimg);
//                writeFileSdcard(filepathimg, data);
//            }else{  
//                Toast.makeText(LauncherActivity.this, "Image error!", Toast.LENGTH_SHORT).show();  
//            }   
//        } catch (Exception e) {  
//            Toast.makeText(LauncherActivity.this,"无法链接网络！", Toast.LENGTH_SHORT).show();  
//            e.printStackTrace();  
//        }
//		return true;
//	}
//	
//	//写在/mnt/sdcard/目录下面的文件
//
//	   public void writeFileSdcard(String fileName,byte[] messagep){ 
//
//	       try{ 
//
//	        //FileOutputStream fout = openFileOutput(fileName, MODE_PRIVATE);
//
//	       FileOutputStream fout = new FileOutputStream(fileName);
//
//	        byte [] bytes = messagep; 
//
//	        fout.write(bytes); 
//
//	         fout.close(); 
//
//	        } 
//
//	       catch(Exception e){ 
//
//	        e.printStackTrace(); 
//
//	       } 
//
//	   }
//	   
//	 //从assets 文件夹中获取文件并读取数据流  
//	    public byte[] getFromAssetsFolder(String fileName){  
//	        byte[] result = {};  
//	            try {  
//	                InputStream in = getResources().getAssets().open(fileName);  
//	                //获取文件的字节数  
//	                int lenght = in.available();  
//	                //创建byte数组  
//	                byte[]  buffer = new byte[lenght];  
//	                //将文件中的数据读到byte数组中  
//	                in.read(buffer);  
////	                result = EncodingUtils.getString(buffer, ENCODING);  
//	                return buffer;
//	            } catch (Exception e) {  
//	                e.printStackTrace();  
//	            }  
//	            return result;  
//	    }
	   
}
