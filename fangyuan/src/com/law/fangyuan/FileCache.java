package com.law.fangyuan;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;

import org.apache.http.util.EncodingUtils;

import android.content.Context;

public class FileCache {
	public static final String ENCODING = "UTF-8";
	public Context context;
	
	public FileCache(Context context){
		this.context = context;
	}
	
	public void set(String key, String val){
		try {
			File file = new File(getPath(key));
			mkdir(file.getParentFile());
			FileOutputStream out = new FileOutputStream(file);
			byte[] bytes = val.getBytes();
			out.write(bytes);
			out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("resource")
	public String get(String key){
		String result="";
		try {
			File file = new File(getPath(key));
			if(!file.exists()) return result;
			FileInputStream in = new FileInputStream(file);
			int lenght = in.available();
			byte[] buffer = new byte[lenght];
			in.read(buffer);
			return EncodingUtils.getString(buffer, ENCODING);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@SuppressWarnings("resource")
	public String get(String key,int timeout){
		String result="";
		try {
			File file = new File(getPath(key));
			if(!file.exists()) return result;
			timeout = timeout*1000;
			Date date = new Date();
			if((date.getTime()-file.lastModified())<timeout){
				FileInputStream in = new FileInputStream(file);
				int lenght = in.available();
				byte[] buffer = new byte[lenght];
				in.read(buffer);
				return EncodingUtils.getString(buffer, ENCODING);
			}else{
				file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public Boolean delete(String key){
		File file = new File(getPath(key));
		if(file.exists()){
			return file.delete();
		}
		return false;
	}
	
	public void deleteAll(){
		File file = new File(context.getFilesDir().toString());
		deleteDir(file);
	}
	
	public String getPath(String name){
		return context.getFilesDir().toString()+"/"+name+".tmp";
	}
	
	public void mkdir(File file){
		if(file.getParentFile().exists()){
			file.mkdir();
		}else{
			mkdir(file.getParentFile());
			file.mkdir();
		}
	}
	
	public void deleteDir(File file){
		if(file.isFile()||file.listFiles().length==0){
			file.delete();
		}else{
			for(File f : file.listFiles()){
				deleteDir(f);
				f.delete();
			}
		}
	}
}
