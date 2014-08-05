package com.gos.iccardone.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import org.apache.http.util.EncodingUtils;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.view.Gravity;
import android.widget.Toast;

public class GOSHelper {
	public static final String ENCODING = "UTF-8"; 
	//把数据流转化为字符串
	public static String convertStreamToString(InputStream is) {
	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    StringBuilder sb = new StringBuilder();

	    String line = null;
	    try {
	        while ((line = reader.readLine()) != null) {
	            sb.append((line + "\n"));
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            is.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    return sb.toString();
	}
	//得到外存上的文件夹
	public static String getExternDir(){
		try {
			String strDir= Environment.getExternalStorageDirectory()+"/songshangmen";
		
			File file = new File(strDir);
			if(!file.exists()){
				file.mkdir();
			}
			return strDir;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return "";
		}
	}
	//根据路径删除文件
	public static boolean deleteFiles(String path){
		if (path == null || path.length() == 0) {
			return false;
		}
		File file = new File(path);
		try {
			if(file.isFile() && file.exists()){
				if(file.delete()){
				      return true;
				}
			}
		} catch (Exception e) {
		
			return false;
		}
			
	    return false;
	}
	//判断文件是否存在
	public static boolean fileIsExists(String path){
        try{
            File f=new File(path);
            if(!f.exists()){
                 return false;
            }
            
        }catch (Exception e) {
                // TODO: handle exception
            return false;
        }
        return true;
	}
	
    
	public  static final String FILE="songshangmen";
	/**
	 * 
	 * @param mContext 上下文，来区别哪一个activity调用的
	 * @param whichSp 使用的SharedPreferences的名字
	 * @param field SharedPreferences的哪一个字段
	 * @return
	 */
	//取出whichSp中field字段对应的string类型的值
	public static String getSharePreStr(Context mContext,String field){
		SharedPreferences sp=(SharedPreferences) mContext.getSharedPreferences(FILE, 0);
		String s=sp.getString(field,"");//如果该字段没对应值，则取出字符串0
		return s;
	}
	//取出whichSp中field字段对应的int类型的值
	public static int getSharePreInt(Context mContext,String field){
		SharedPreferences sp=(SharedPreferences) mContext.getSharedPreferences(FILE, 0);
		int i=sp.getInt(field,0);//如果该字段没对应值，则取出0
		return i;
	}
	
	//取出whichSp中field字段对应的boolean类型的值
	public static boolean getSharePreBoolean(Context mContext,String field){
		SharedPreferences sp=(SharedPreferences) mContext.getSharedPreferences(FILE, 0);
		boolean i=sp.getBoolean(field, false);//如果该字段没对应值，则取出0
		return i;
	}
	//保存string类型的value到whichSp中的field字段
	public static void putSharePre(Context mContext,String field,String value){
		SharedPreferences sp=(SharedPreferences) mContext.getSharedPreferences(FILE, 0);
		sp.edit().putString(field, value).commit();
	}
	//保存int类型的value到whichSp中的field字段
	public static void putSharePre(Context mContext,String field,int value){
		SharedPreferences sp=(SharedPreferences) mContext.getSharedPreferences(FILE, 0);
		sp.edit().putInt(field, value).commit();
	}
	
	//保存boolean类型的value到whichSp中的field字段
	public static void putSharePre(Context mContext,String field,Boolean value){
		SharedPreferences sp=(SharedPreferences) mContext.getSharedPreferences(FILE, 0);
		sp.edit().putBoolean(field, value).commit();
	}
	
	/**
	 * Toast的封装
	 * @param mContext 上下文，来区别哪一个activity调用的
	 * @param msg 你希望显示的值。
	 */
	public static void showMsg(Context mContext,String msg) {
		Toast toast=new Toast(mContext);
		toast=Toast.makeText(mContext,msg, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER_HORIZONTAL,0,0);//设置居中
		toast.show();
	}
	/**对null的转化*/
	public static String isNull(JSONObject dataObject, String property) {
		String data = "";
		try {
			if (!dataObject.isNull(property)) {
				data = dataObject.getString(property);
			}
		} catch (JSONException e) {
		}
		return data;
	}
	//获取uuid
	public static String getUUID(){
        String s = UUID.randomUUID().toString();
        //去掉“-”符号
        return s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24);
    }
	//写数据到SD中的文件  
	public static void writeFileSdcardFile(String fileName,String write_str) throws IOException{   
	 try{   
		   
	       FileOutputStream fout = new FileOutputStream(fileName);   
	       byte [] bytes = write_str.getBytes();   
	  
	       fout.write(bytes);   
	       fout.close();   
	     }  
	  
	      catch(Exception e){   
	        e.printStackTrace();   
	       }   
	  }   
	  
	    
	//读SD中的文件  
	public static String readFileSdcardFile(String fileName) throws IOException{   
	  String res="";   
	  try{   
	         FileInputStream fin = new FileInputStream(fileName);   
	  
	         int length = fin.available();   
	  
	         byte [] buffer = new byte[length];   
	         fin.read(buffer);       
	  
	         res = EncodingUtils.getString(buffer, "UTF-8");   
	  
	         fin.close();       
	        }   
	  
	        catch(Exception e){   
	         e.printStackTrace();   
	        }   
	        return res;   
	} 
	
	public static void storeImageInSD(String fileName, Bitmap bitmap) throws IOException {

        if (fileIsExists(fileName)) {
        	deleteFiles(fileName);
        }
        String[] fileStrings = fileName.split("\\.");
        String fileFormatString = fileStrings[fileStrings.length - 1];
        try {

            FileOutputStream fos = new FileOutputStream(fileName);
            if (fileFormatString.equals("jpg")) {
            	bitmap.compress(CompressFormat.JPEG, 100, fos);
			}
            else if (fileFormatString.equals("png")) {
            	bitmap.compress(CompressFormat.PNG, 100, fos);
			}
//            else if (fileFormatString.equals("gif")) {
//				
//			}
            
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

	public static Bitmap getBitmapByFileName(String fileName) throws IOException {
		Bitmap bitmap = null;
//		File file = new File(fileName);
//		if (file.exists()) {
//			bitmap = BitmapFactory.decodeFile(fileName);
//		}
        
        if (fileIsExists(fileName)) {
            try {
                bitmap = BitmapFactory.decodeStream(new FileInputStream(fileName));
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return bitmap;
    }
	
	/**
     * 字符串转换到时间格式
     * @param dateStr 需要转换的字符串
     * @return dateFormatStr 需要转换的字符串的时间格式
     * @param formatStr 需要格式的目标字符串  举例 yyyyMMdd
     * @return String 返回转换后的时间字符串
     * @throws ParseException 转换异常
     */
    @SuppressLint("SimpleDateFormat")
	public static String StringToDate(String dateStr,String dateFormatStr,String formatStr){
        DateFormat sdf=new SimpleDateFormat(dateFormatStr);
        Date date=null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat s=new SimpleDateFormat(formatStr);
         
        return s.format(date);
    }
    
	public static SharedPreferences getSharedPreferences(Context context){
		return context.getSharedPreferences("iccardone_config", 0);
	}
	

}
