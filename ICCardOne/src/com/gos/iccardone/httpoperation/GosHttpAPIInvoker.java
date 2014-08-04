package com.gos.iccardone.httpoperation;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.gos.iccardone.exception.BaseException;
import com.gos.iccardone.helper.GOSHelper;
import com.gos.iccardone.http.HttpApi;
import com.gos.iccardone.http.AbstractHttpApi;
import com.gos.iccardone.http.HttpApiWithBasicAuth;

public class GosHttpAPIInvoker {
	private final DefaultHttpClient mHttpClient                    	  = AbstractHttpApi.createHttpClient();
    private HttpApi                 mHttpApi;
	private final String            mApiBaseUrl;
	private static final String     URL_API_ONE                 	  = "syt1.html";                  // 
    private static final String     URL_API_TWO                    	  = "yinlian1.html";              //
    private static final String     URL_API_THREE					  = "version.html";
	
	public GosHttpAPIInvoker(String domain, String clientVersion)
	{
		mApiBaseUrl = "http://" + domain; 
		mHttpApi = new HttpApiWithBasicAuth(mHttpClient, clientVersion);
	}
	private String fullUrl(String url){
        return mApiBaseUrl + url;
    }
    /**
     * 1.0.1 调用登录接口
     * by 果悦科技
     * */
    protected HttpResponse invokerUserLogin(String usernameString, String passwordString) throws ParseException, BaseException, IOException{
    	
    	JSONObject paramJsonObject2 = new JSONObject();
    	paramJsonObject2.put("username", usernameString);//用户名
    	paramJsonObject2.put("password", passwordString);//密码
    	JSONObject paramJsonObject3 = new JSONObject();
    	paramJsonObject3.put("action", "1001");
    	paramJsonObject3.put("data", paramJsonObject2);
    	String paramString = paramJsonObject3.toString();
    	
    	
    	HttpPost mHttpPost = mHttpApi.createOneHttpPost(fullUrl(URL_API_ONE), paramString);

    	return mHttpApi.doHttpResponse(mHttpPost);
    }
    /**
     * 1.0.2 修改密码
     * by 果悦科技
     * */
    protected HttpResponse invokerModifyPassword(String cardIdString, 
    		String newPasswordString, String oldPasswordString, String tokenString) throws ParseException, BaseException, IOException{
    	JSONObject paramJsonObject2 = new JSONObject();
    	paramJsonObject2.put("cardId", cardIdString);//cardid
    	paramJsonObject2.put("token", tokenString);//密码
    	paramJsonObject2.put("newPwd", newPasswordString);//用户名
    	paramJsonObject2.put("oldPwd", oldPasswordString);//密码
    	JSONObject paramJsonObject3 = new JSONObject();
    	paramJsonObject3.put("action", "1003");
    	paramJsonObject3.put("data", paramJsonObject2);
    	String paramString = paramJsonObject3.toString();
    	

    	HttpPost mHttpPost = mHttpApi.createOneHttpPost(fullUrl(URL_API_ONE), paramString);
    	return mHttpApi.doHttpResponse(mHttpPost);
    }
    
    /**
     * 1.0.3 查询余额
     * by 果悦科技
     * */
    protected HttpResponse invokerQueryBalance(String cardIdString, 
    		 String tokenString) throws ParseException, BaseException, IOException{
    	JSONObject paramJsonObject2 = new JSONObject();
    	paramJsonObject2.put("cardId", cardIdString);//cardid
    	paramJsonObject2.put("token", tokenString);//密码
    	JSONObject paramJsonObject3 = new JSONObject();
    	paramJsonObject3.put("action", "1002");
    	paramJsonObject3.put("data", paramJsonObject2);
    	String paramString = paramJsonObject3.toString();
    	

    	HttpPost mHttpPost = mHttpApi.createOneHttpPost(fullUrl(URL_API_ONE), paramString);
    	return mHttpApi.doHttpResponse(mHttpPost);
    }
    /**
     * 1.0.4 代收
     * by 果悦科技
     * */
    protected HttpResponse invokerInToAccount(String userNameString, String userPasswordString, String bankCodeString, 
      		 String accountNoString, String accountNameString, String amountString) throws ParseException, BaseException, IOException{

    	JSONObject paramJsonObject2 = new JSONObject();
    	paramJsonObject2.put("username", userNameString);
    	paramJsonObject2.put("userpass", userPasswordString);//密码
	   	paramJsonObject2.put("bankCode", bankCodeString);
	   	paramJsonObject2.put("accountNo", accountNoString);
	   	paramJsonObject2.put("accountName", accountNameString);
	   	paramJsonObject2.put("amount", amountString);
	   	JSONObject paramJsonObject3 = new JSONObject();
	   	paramJsonObject3.put("action", "2001");
	   	paramJsonObject3.put("data", paramJsonObject2);
	   	String paramString = paramJsonObject3.toString();
	   	
	   	HttpPost mHttpPost = mHttpApi.createOneHttpPost(fullUrl(URL_API_TWO), paramString);
	   	return mHttpApi.doHttpResponse(mHttpPost);
   }
    
    /**
     * 1.0.5 代付
     * by 果悦科技
     * */
    protected HttpResponse invokerOutAccount(String userNameString, String userPasswordString, String bankCodeString, 
   		 String accountNoString, String accountNameString, String amountString) throws ParseException, BaseException, IOException{
    	JSONObject paramJsonObject2 = new JSONObject();
    	paramJsonObject2.put("username", userNameString);
    	paramJsonObject2.put("userpass", userPasswordString);//密码
	   	paramJsonObject2.put("bankCode", bankCodeString);
	   	paramJsonObject2.put("accountNo", accountNoString);
	   	paramJsonObject2.put("accountName", accountNameString);
	   	paramJsonObject2.put("amount", amountString);
	   	JSONObject paramJsonObject3 = new JSONObject();
	   	paramJsonObject3.put("action", "2002");
	   	paramJsonObject3.put("data", paramJsonObject2);
	   	String paramString = paramJsonObject3.toString();
	   	
	   	HttpPost mHttpPost = mHttpApi.createOneHttpPost(fullUrl(URL_API_TWO), paramString);
	   	return mHttpApi.doHttpResponse(mHttpPost);
   }
   /**
    * 1.0.6 通过ic卡卡号登录
    * by 果悦科技
    * */
    protected HttpResponse invokerICCardLogin(String cardIdString, String passwordString, String tokenString) throws ParseException, BaseException, IOException{
       	JSONObject paramJsonObject2 = new JSONObject();
       	paramJsonObject2.put("cardId", cardIdString);
       	paramJsonObject2.put("password", passwordString);//密码
   	   	paramJsonObject2.put("token", tokenString);
   	   	JSONObject paramJsonObject3 = new JSONObject();
   	   	paramJsonObject3.put("action", "1005");
   	   	paramJsonObject3.put("data", paramJsonObject2);
   	   	String paramString = paramJsonObject3.toString();
   	   	
   	   	HttpPost mHttpPost = mHttpApi.createOneHttpPost(fullUrl(URL_API_ONE), paramString);
   	   	return mHttpApi.doHttpResponse(mHttpPost);
      }
    /**
     * 1.0.7 充值
     * by 果悦科技
     * */
     protected HttpResponse invokerPay( String userNameString, String userPasswordString, String bankCodeString, 
       		 String accountNoString, String accountNameString, String amountString, String cardIdString, String tokenString) throws ParseException, BaseException, IOException{
        	JSONObject paramJsonObject2 = new JSONObject();
        	paramJsonObject2.put("username", userNameString);
        	paramJsonObject2.put("userpass", userPasswordString);//密码
        	paramJsonObject2.put("bankCode", bankCodeString);
        	paramJsonObject2.put("accountNo", accountNoString);
        	paramJsonObject2.put("accountName", accountNameString);
        	paramJsonObject2.put("amount", amountString);
        	paramJsonObject2.put("cardId", cardIdString);
    	   	paramJsonObject2.put("token", tokenString);

    	   	JSONObject paramJsonObject3 = new JSONObject();
    	   	paramJsonObject3.put("action", "1006");
    	   	paramJsonObject3.put("data", paramJsonObject2);
    	   	String paramString = paramJsonObject3.toString();
    	   	
    	   	HttpPost mHttpPost = mHttpApi.createOneHttpPost(fullUrl(URL_API_ONE), paramString);
    	   	return mHttpApi.doHttpResponse(mHttpPost);
       }
     /**
      * 1.0.8 消费
      * by 果悦科技
      * */
      protected HttpResponse invokerConsume( String userNameString, String userPasswordString, String bankCodeString, 
        		 String accountNoString, String accountNameString, String amountString, String cardIdString, String passwordString, String tokenString) throws ParseException, BaseException, IOException{
    	  	JSONObject paramJsonObject2 = new JSONObject();
	      	paramJsonObject2.put("username", userNameString);
	      	paramJsonObject2.put("userpass", userPasswordString);//密码
	      	paramJsonObject2.put("bankCode", bankCodeString);
	      	paramJsonObject2.put("accountNo", accountNoString);
	      	paramJsonObject2.put("accountName", accountNameString);
	      	paramJsonObject2.put("amount", amountString);
	      	paramJsonObject2.put("cardId", cardIdString);
	      	paramJsonObject2.put("password", passwordString);
	  	   	paramJsonObject2.put("token", tokenString);
	  	   	
     	   	JSONObject paramJsonObject3 = new JSONObject();
     	   	paramJsonObject3.put("action", "1007");
     	   	paramJsonObject3.put("data", paramJsonObject2);
     	   	String paramString = paramJsonObject3.toString();
     	   	
     	   	HttpPost mHttpPost = mHttpApi.createOneHttpPost(fullUrl(URL_API_ONE), paramString);
     	   	return mHttpApi.doHttpResponse(mHttpPost);
        }
      /**
       * 1.0.9 交易流水
       * by 果悦科技
       * */
       protected HttpResponse invokerOperatin( String cardIdString, String startDateString, String endDateString, String extraString,
    		   String tokenString) throws ParseException, BaseException, IOException{
     	  	JSONObject paramJsonObject2 = new JSONObject();
 	      	paramJsonObject2.put("cardId", cardIdString);//卡号
 	      	paramJsonObject2.put("startDate", startDateString);
 	  	   	paramJsonObject2.put("endDate", endDateString);
 	  	   	paramJsonObject2.put("extra", extraString);
 	  	   	paramJsonObject2.put("token", tokenString);
 	  	   	
      	   	JSONObject paramJsonObject3 = new JSONObject();
      	   	paramJsonObject3.put("action", "1004");
      	   	paramJsonObject3.put("data", paramJsonObject2);
      	   	String paramString = paramJsonObject3.toString();
      	   	
      	   	HttpPost mHttpPost = mHttpApi.createOneHttpPost(fullUrl(URL_API_ONE), paramString);
      	   	return mHttpApi.doHttpResponse(mHttpPost);
         }
   /**
    * 1.0.10 卡卡转账
    * by 果悦科技
    * */
    protected HttpResponse invokerCardCardTransfer( String outCardIdString, String outPwdString, String inCardIdString, 
 		   String amountString, String tokenString) throws ParseException, BaseException, IOException{
  	  	JSONObject paramJsonObject2 = new JSONObject();
      	paramJsonObject2.put("outCardId", outCardIdString);//卡号
      	paramJsonObject2.put("outPwd", outPwdString);
  	   	paramJsonObject2.put("inCardId", inCardIdString);
  	   	paramJsonObject2.put("amount", amountString);
  	   	paramJsonObject2.put("token", tokenString);
  	   	
   	   	JSONObject paramJsonObject3 = new JSONObject();
   	   	paramJsonObject3.put("action", "1008");
   	   	paramJsonObject3.put("data", paramJsonObject2);
   	   	String paramString = paramJsonObject3.toString();
   	   	
   	   	HttpPost mHttpPost = mHttpApi.createOneHttpPost(fullUrl(URL_API_ONE), paramString);
   	   	return mHttpApi.doHttpResponse(mHttpPost);
      }
    /**
     * 1.0.11 卡关联信息
     * by 果悦科技
     * */
     protected HttpResponse invokerCardAssociation( String cardIdString, String tokenString) throws ParseException, BaseException, IOException{
       	  	JSONObject paramJsonObject2 = new JSONObject();
   	      	paramJsonObject2.put("cardId", cardIdString);//卡号
   	  	   	paramJsonObject2.put("token", tokenString);
  	   	
    	   	JSONObject paramJsonObject3 = new JSONObject();
    	   	paramJsonObject3.put("action", "1009");
    	   	paramJsonObject3.put("data", paramJsonObject2);
    	   	String paramString = paramJsonObject3.toString();
    	   	
    	   	HttpPost mHttpPost = mHttpApi.createOneHttpPost(fullUrl(URL_API_ONE), paramString);
    	   	return mHttpApi.doHttpResponse(mHttpPost);
       }
     /**
      * 1.0.12 软件更新
      * by 果悦科技
      * */
      protected HttpResponse invokerSoftwareUpdate() throws ParseException, BaseException, IOException{
    	  	JSONObject paramJsonObject2 = new JSONObject();
   	   	
     	   	JSONObject paramJsonObject3 = new JSONObject();
     	   	paramJsonObject3.put("action", "3001");
     	   	paramJsonObject3.put("data", paramJsonObject2);
     	   	String paramString = paramJsonObject3.toString();
     	   	
     	   	HttpPost mHttpPost = mHttpApi.createOneHttpPost(fullUrl(URL_API_THREE), paramString);
     	   	return mHttpApi.doHttpResponse(mHttpPost);
        }
        
       
}
