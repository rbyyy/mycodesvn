package com.ssm.ssmshop.httpoperation;

import java.io.IOException;
import java.util.List;

import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ssm.ssmshop.entity.Auth;
import com.ssm.ssmshop.entity.BaseResponse;
import com.ssm.ssmshop.entity.Goods;
import com.ssm.ssmshop.entity.GoodsBaseResponse;
import com.ssm.ssmshop.entity.OrderPreview;
import com.ssm.ssmshop.entity.StateCode;
import com.ssm.ssmshop.exception.BaseException;
import com.ssm.ssmshop.http.AbstractHttpApi;
import com.ssm.ssmshop.http.HttpApi;
import com.ssm.ssmshop.http.HttpApiWithBasicAuth;
import com.ssm.ssmshop.parser.LoginUpAndInParser;
import com.ssm.ssmshop.parser.OrderGoodsParser;
import com.ssm.ssmshop.parser.OrderPreviewParser;
import com.ssm.ssmshop.parser.StateCodeParser;

@SuppressWarnings("unchecked")
public class GosHttpAPIInvoker {
	private final DefaultHttpClient mHttpClient                    	  	= AbstractHttpApi.createHttpClient();
    private HttpApi                 mHttpApi;
	private final String            mApiBaseUrl;
	
	private static final String     URL_API_AUTH                  		= "/ssm/auth.html";//注册登录验证
	private static final String     URL_API_USER						= "/ssm/user.html";//用户信息
	private static final String     URL_API_ORDER						= "/ssm/order.html";//添加订单
	private static final String		URL_API_SHOP						= "/ssm/shop.html";//商店信息
	
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
    	paramJsonObject2.put("roleType", "2");//角色类型
    	
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
     * 1.0.6 获取商家订单列表
     * by 果悦科技
     * */
    protected BaseResponse<OrderPreview> invokerObtainShopOrderList(String shopIdString, String pageString) throws ParseException, BaseException, IOException{
    	JSONObject paramJsonObject = new JSONObject();
    	paramJsonObject.put("action", "107005");
    	
    	JSONObject paramJsonObject2 = new JSONObject();
    	paramJsonObject2.put("shopId", shopIdString);//商家id
    	paramJsonObject2.put("page", pageString);//页码
    	
    	paramJsonObject.put("data", paramJsonObject2);
    	
    	HttpPost mHttpPost = mHttpApi.createHttpPost(fullUrl(URL_API_ORDER),  paramJsonObject.toString());
    	return (BaseResponse<OrderPreview>)mHttpApi.doHttpRequest(mHttpPost, new OrderPreviewParser());
    }
    /**
     * 1.0.7 获取商家订单详情
     * by 果悦科技
     * */
    protected GoodsBaseResponse<Goods> invokerObtainShopOrderDetail(String orderIdString) throws ParseException, BaseException, IOException{
    	JSONObject paramJsonObject = new JSONObject();
    	paramJsonObject.put("action", "107006");
    	
    	JSONObject paramJsonObject2 = new JSONObject();
    	paramJsonObject2.put("orderId", orderIdString);//订单id
    	
    	paramJsonObject.put("data", paramJsonObject2);
    	
    	HttpPost mHttpPost = mHttpApi.createHttpPost(fullUrl(URL_API_ORDER),  paramJsonObject.toString());
    	return (GoodsBaseResponse<Goods>)mHttpApi.doHttpRequest(mHttpPost, new OrderGoodsParser());
    }
    /**
     * 1.0.8 获取商家详细信息
     * by 果悦科技
     * */
    protected StateCode invokerObtainShopDetail(String shopIdString) throws ParseException, BaseException, IOException{
    	JSONObject paramJsonObject = new JSONObject();
    	paramJsonObject.put("action", "103011");
    	
    	JSONObject paramJsonObject2 = new JSONObject();
    	paramJsonObject2.put("id", shopIdString);//商家id
    	
    	paramJsonObject.put("data", paramJsonObject2);
    	
    	HttpPost mHttpPost = mHttpApi.createHttpPost(fullUrl(URL_API_SHOP),  paramJsonObject.toString());
    	return (StateCode)mHttpApi.doHttpRequest(mHttpPost, new StateCodeParser());
    }
    /**
     * 1.0.9 商家审核订单
     * by 果悦科技
     * */
    protected StateCode invokerShopAuditOrder(String orderIdString) throws ParseException, BaseException, IOException{
    	JSONObject paramJsonObject = new JSONObject();
    	paramJsonObject.put("action", "107012");
    	
    	JSONObject paramJsonObject2 = new JSONObject();
    	paramJsonObject2.put("orderId", orderIdString);//订单id
    	
    	paramJsonObject.put("data", paramJsonObject2);
    	
    	HttpPost mHttpPost = mHttpApi.createHttpPost(fullUrl(URL_API_ORDER),  paramJsonObject.toString());
    	return (StateCode)mHttpApi.doHttpRequest(mHttpPost, new StateCodeParser());
    }
    
}
