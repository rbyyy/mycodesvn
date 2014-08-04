package com.law.fangyuan.modify;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import org.apache.http.util.EncodingUtils; 
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.Toast;

public class GOSHelper {
public static final String ENCODING = "UTF-8"; 
	
	/** ��ȡ��Ļ�Ŀ�� */
	public final static int getWindowsWidth(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}
	//��������ת��Ϊ�ַ���
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
	//�õ�����ϵ��ļ���
	public static String getExternDir(){
		try {
			String strDir= Environment.getExternalStorageDirectory()+"/YYPad";
		
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
	//����·��ɾ���ļ�
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
	//�ж��ļ��Ƿ����
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
    
	public  static final String FILE="yypad";
	/**
	 * 
	 * @param mContext �����ģ���������һ��activity���õ�
	 * @param whichSp ʹ�õ�SharedPreferences������
	 * @param field SharedPreferences����һ���ֶ�
	 * @return
	 */
	//ȡ��whichSp��field�ֶζ�Ӧ��string���͵�ֵ
	public static String getSharePreStr(Context mContext,String field){
		SharedPreferences sp=(SharedPreferences) mContext.getSharedPreferences(FILE, 0);
		String s=sp.getString(field,"");//������ֶ�û��Ӧֵ����ȡ���ַ���0
		return s;
	}
	//ȡ��whichSp��field�ֶζ�Ӧ��int���͵�ֵ
	public static int getSharePreInt(Context mContext,String field){
		SharedPreferences sp=(SharedPreferences) mContext.getSharedPreferences(FILE, 0);
		int i=sp.getInt(field,0);//������ֶ�û��Ӧֵ����ȡ��0
		return i;
	}
	
	//ȡ��whichSp��field�ֶζ�Ӧ��boolean���͵�ֵ
	public static boolean getSharePreBoolean(Context mContext,String field){
		SharedPreferences sp=(SharedPreferences) mContext.getSharedPreferences(FILE, 0);
		boolean i=sp.getBoolean(field, false);//������ֶ�û��Ӧֵ����ȡ��0
		return i;
	}
	//����string���͵�value��whichSp�е�field�ֶ�
	public static void putSharePre(Context mContext,String field,String value){
		SharedPreferences sp=(SharedPreferences) mContext.getSharedPreferences(FILE, 0);
		sp.edit().putString(field, value).commit();
	}
	//����int���͵�value��whichSp�е�field�ֶ�
	public static void putSharePre(Context mContext,String field,int value){
		SharedPreferences sp=(SharedPreferences) mContext.getSharedPreferences(FILE, 0);
		sp.edit().putInt(field, value).commit();
	}
	
	//����boolean���͵�value��whichSp�е�field�ֶ�
	public static void putSharePre(Context mContext,String field,Boolean value){
		SharedPreferences sp=(SharedPreferences) mContext.getSharedPreferences(FILE, 0);
		sp.edit().putBoolean(field, value).commit();
	}
	
	/**
	 * Toast�ķ�װ
	 * @param mContext �����ģ���������һ��activity���õ�
	 * @param msg ��ϣ����ʾ��ֵ��
	 */
	public static void showMsg(Context mContext,String msg) {
		Toast toast=new Toast(mContext);
		toast=Toast.makeText(mContext,msg, 300);
		toast.setGravity(Gravity.CENTER_HORIZONTAL,0,0);//���þ���
		toast.show();//��ʾ,(ȱ����䲻��ʾ)
	}
	
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
	//��ȡuuid
	public static String getUUID(){
        String s = UUID.randomUUID().toString();
        //ȥ����-������
        return s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24);
    }
	//д���ݵ�SD�е��ļ�  
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
	  
	    
	//��SD�е��ļ�  
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
//                else if (fileFormatString.equals("gif")) {
//					
//				}
            
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
//			File file = new File(fileName);
//			if (file.exists()) {
//				bitmap = BitmapFactory.decodeFile(fileName);
//			}
        
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
	 * MD5����
	 * */
	public static String getMD5(String val) throws NoSuchAlgorithmException{    
        MessageDigest md5 = MessageDigest.getInstance("MD5");    
        md5.update(val.getBytes());    
        byte[] m = md5.digest();//����     
        return getString(m);    
	}
	/**
	 * �õ��ַ���
	 * */
    private static String getString(byte[] b){    
        StringBuffer sb = new StringBuffer();    
         for(int i = 0; i < b.length; i ++){    
          sb.append(b[i]);    
         }    
         return sb.toString();    
    }   	
		
}
