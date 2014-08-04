package com.ssm.ssmbee.httpoperation;

import java.io.IOException;

import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import com.alibaba.fastjson.JSONObject;
import com.ssm.ssmbee.entity.Auth;
import com.ssm.ssmbee.entity.BaseResponse;
import com.ssm.ssmbee.entity.OrderMenu;
import com.ssm.ssmbee.entity.StateCode;
import com.ssm.ssmbee.exception.BaseException;
import com.ssm.ssmbee.http.AbstractHttpApi;
import com.ssm.ssmbee.http.HttpApi;
import com.ssm.ssmbee.http.HttpApiWithBasicAuth;
import com.ssm.ssmbee.parser.LoginUpAndInParser;
import com.ssm.ssmbee.parser.OrderMenuParser;
import com.ssm.ssmbee.parser.StateCodeParser;

@SuppressWarnings("unchecked")
public class GosHttpAPIInvoker {
	private final DefaultHttpClient mHttpClient                    	  	= AbstractHttpApi.createHttpClient();
    private HttpApi                 mHttpApi;
	private final String            mApiBaseUrl;
	
	private static final String     URL_API_AUTH                  		= "/ssm/auth.html";//注册登录验证
	private static final String     URL_API_USER						= "/ssm/user.html";//用户信息
	private static final String     URL_API_ORDER						= "/ssm/order.html";//添加订单
	
	public GosHttpAPIInvoker(String domain, String clientVersion)
	{
		mApiBaseUrl = "http://" + domain; 
		mHttpApi = new HttpApiWithBasicAuth(mHttpClient, clientVersion);
	}
	private String fullUrl(String url){
        return mApiBaseUrl + url;
    }
	
    /**
     *1.0.2 调用用户注册接口
     *by 果悦科技 
     * */
    protected BaseResponse<Auth> invokerUserRegister(String usernameString, String passwordString, String mobileString) throws ParseException, BaseException, IOException{
    	JSONObject paramJsonObject = new JSONObject();
    	paramJsonObject.put("action", "101001");
    	
    	JSONObject paramJsonObject2 = new JSONObject();
    	paramJsonObject2.put("username", usernameString);//用户名
    	paramJsonObject2.put("password", passwordString);//登录密码
    	paramJsonObject2.put("mobile", mobileString);//手机号码
    	paramJsonObject2.put("roleType", "4");//角色类型
    	
    	paramJsonObject.put("data", paramJsonObject2);
    	
    	HttpPost mHttpPost = mHttpApi.createHttpPost(fullUrl(URL_API_AUTH),  paramJsonObject.toString());
    	return (BaseResponse<Auth>)mHttpApi.doHttpRequest(mHttpPost, new LoginUpAndInParser());
    }
    
    /**
     * 1.0.3 调用登录接口
     * by 果悦科技
     * */
    protected BaseResponse<Auth> invokerUserLogin(String usernameString, String passwordString, String resIdString) throws ParseException, BaseException, IOException{
    	JSONObject paramJsonObject = new JSONObject();
    	paramJsonObject.put("action", "101002");
    	
    	JSONObject paramJsonObject2 = new JSONObject();
    	paramJsonObject2.put("account", usernameString);//用户名
    	paramJsonObject2.put("password", passwordString);//密码
    	paramJsonObject2.put("registrationId", resIdString);//注册id
    	paramJsonObject2.put("roleType", "3");//角色类型
    	
    	paramJsonObject.put("data", paramJsonObject2);
    	
    	HttpPost mHttpPost = mHttpApi.createHttpPost(fullUrl(URL_API_AUTH),  paramJsonObject.toString());
    	return (BaseResponse<Auth>)mHttpApi.doHttpRequest(mHttpPost, new LoginUpAndInParser());
    }
    
    /**
     * 1.0.4 修改密码接口
     * by 果悦科技
     * */
    protected StateCode invokerModifyPassword(String roleIdString, String passwordString) throws ParseException, BaseException, IOException{
    	JSONObject paramJsonObject = new JSONObject();
    	paramJsonObject.put("action", "101003");
    	
    	JSONObject paramJsonObject2 = new JSONObject();
    	paramJsonObject2.put("roleId", roleIdString);//用户id
    	paramJsonObject2.put("password", passwordString);//密码
    	
    	paramJsonObject.put("data", paramJsonObject2);
    	
    	HttpPost mHttpPost = mHttpApi.createHttpPost(fullUrl(URL_API_USER),  paramJsonObject.toString());
    	return (StateCode)mHttpApi.doHttpRequest(mHttpPost, new StateCodeParser());
    }
    
    /**
     * 1.0.5 获取用户信息
     * by 果悦科技
     * */
    protected StateCode invokerObtainUserInfo(String userIdString) throws ParseException, BaseException, IOException{
    	JSONObject paramJsonObject = new JSONObject();
    	paramJsonObject.put("action", "102004");
    	
    	JSONObject paramJsonObject2 = new JSONObject();
    	paramJsonObject2.put("userId", userIdString);//用户id
    	
    	paramJsonObject.put("data", paramJsonObject2);
    	
    	HttpPost mHttpPost = mHttpApi.createHttpPost(fullUrl(URL_API_USER),  paramJsonObject.toString());
    	return (StateCode)mHttpApi.doHttpRequest(mHttpPost, new StateCodeParser());
    }
    /**
     * 1.0.6 获取小蜜蜂的订单
     * by 果悦科技
     * */
    protected StateCode invokerObtainBeeOrder(String userIdString, String orderStatus) throws ParseException, BaseException, IOException{
    	JSONObject paramJsonObject = new JSONObject();
    	paramJsonObject.put("action", "107009");
    	
    	JSONObject paramJsonObject2 = new JSONObject();
    	paramJsonObject2.put("beeId", userIdString);//用户id
    	paramJsonObject2.put("orderStatus", orderStatus);//订单状态
    	
    	paramJsonObject.put("data", paramJsonObject2);
    	
    	HttpPost mHttpPost = mHttpApi.createHttpPost(fullUrl(URL_API_ORDER),  paramJsonObject.toString());
    	return (StateCode)mHttpApi.doHttpRequest(mHttpPost, new StateCodeParser());
    }
    /**
     * 1.0.7 获取区域的订单
     * by 果悦科技
     * */
    protected BaseResponse<OrderMenu> invokerObtainAreaOrder(String areaIdString) throws ParseException, BaseException, IOException{
    	JSONObject paramJsonObject = new JSONObject();
    	paramJsonObject.put("action", "107008");
    	
    	JSONObject paramJsonObject2 = new JSONObject();
    	paramJsonObject2.put("areaId", areaIdString);//区域id
    	
    	paramJsonObject.put("data", paramJsonObject2);
    	
    	HttpPost mHttpPost = mHttpApi.createHttpPost(fullUrl(URL_API_ORDER),  paramJsonObject.toString());
    	return (BaseResponse<OrderMenu>)mHttpApi.doHttpRequest(mHttpPost, new OrderMenuParser());
    }
    /**
     * 1.0.8 小蜜蜂抢单
     * by 果悦科技
     * */
    protected StateCode invokerBeeGrabOrder(String beeIdString, String orderIdString) throws ParseException, BaseException, IOException{
    	JSONObject paramJsonObject = new JSONObject();
    	paramJsonObject.put("action", "107010");
    	
    	JSONObject paramJsonObject2 = new JSONObject();
    	paramJsonObject2.put("beeId", beeIdString);//小蜜蜂id
    	paramJsonObject2.put("orderId", orderIdString);//订单id
    	
    	paramJsonObject.put("data", paramJsonObject2);
    	
    	HttpPost mHttpPost = mHttpApi.createHttpPost(fullUrl(URL_API_ORDER),  paramJsonObject.toString());
    	return (StateCode)mHttpApi.doHttpRequest(mHttpPost, new StateCodeParser());
    }
    
	
}
