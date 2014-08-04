package com.ssm.songshangmen.httpoperation;

import java.io.IOException;
import java.util.List;

import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ssm.songshangmen.entity.Auth;
import com.ssm.songshangmen.entity.BaseResponse;
import com.ssm.songshangmen.entity.Goods;
import com.ssm.songshangmen.entity.GoodsType;
import com.ssm.songshangmen.entity.OrderGoodsList;
import com.ssm.songshangmen.entity.Shop;
import com.ssm.songshangmen.entity.StateCode;
import com.ssm.songshangmen.exception.BaseException;
import com.ssm.songshangmen.http.HttpApi;
import com.ssm.songshangmen.http.AbstractHttpApi;
import com.ssm.songshangmen.http.HttpApiWithBasicAuth;
import com.ssm.songshangmen.parser.GoodsParser;
import com.ssm.songshangmen.parser.GoodsTypeParser;
import com.ssm.songshangmen.parser.LoginUpAndInParser;
import com.ssm.songshangmen.parser.PhoneAuthCodeParser;
import com.ssm.songshangmen.parser.ShopParser;
import com.ssm.songshangmen.parser.StateCodeParser;

@SuppressWarnings("unchecked")
public class GosHttpAPIInvoker {
	private final DefaultHttpClient mHttpClient                    	  	= AbstractHttpApi.createHttpClient();
    private HttpApi                 mHttpApi;
	private final String            mApiBaseUrl;
	
	private static final String     URL_API_AUTH                  		= "/ssm/auth.html";//注册登录验证
	private static final String     URL_API_USER						= "/ssm/user.html";//用户信息
	private static final String     URL_API_ADDRESS						= "/ssm/address.html";//收货地址
	private static final String     URL_API_SHOP						= "/ssm/shop.html";//商店列表
	private static final String     URL_API_GOODS						= "/ssm/goods.html";//商点详细信息
	private static final String     URL_API_GOODSTYPE					= "/ssm/goodsType.html";//商品信息
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
	 * 1.0.0  获取手机验证码
	 * by 果悦科技
	 * */
	protected StateCode invokerPhoneAuthCode(String phoneNumberString) throws ParseException, BaseException, IOException{
    	JSONObject paramJsonObject = new JSONObject();
    	paramJsonObject.put("action", "101006");
    	JSONObject paramJsonObject2 = new JSONObject();
    	paramJsonObject2.put("phone", phoneNumberString);//手机号
    	paramJsonObject.put("data", paramJsonObject2);

    	HttpPost mHttpPost = mHttpApi.createHttpPost(fullUrl(URL_API_AUTH),  paramJsonObject.toString());
    	return (StateCode)mHttpApi.doHttpRequest(mHttpPost, new PhoneAuthCodeParser());
    }
	
	/**
	 * 1.0.1 检测手机验证码
	 * by 果悦科技 
	 * */
	protected StateCode invokerVerifyPhoneAuthCode(String phoneNumberString, String phoneAuthCodeString) throws ParseException, BaseException, IOException{
    	JSONObject paramJsonObject = new JSONObject();
    	paramJsonObject.put("action", "101007");
    	JSONObject paramJsonObject2 = new JSONObject();
    	paramJsonObject2.put("phone", phoneNumberString);//手机号
    	paramJsonObject2.put("verifyCode", phoneAuthCodeString);//验证码
    	paramJsonObject.put("data", paramJsonObject2);

    	HttpPost mHttpPost = mHttpApi.createHttpPost(fullUrl(URL_API_AUTH),  paramJsonObject.toString());
    	return (StateCode)mHttpApi.doHttpRequest(mHttpPost, new PhoneAuthCodeParser());
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
    	paramJsonObject2.put("roleType", "4");//角色类型
    	
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
     * 1.0.6 添加用户收货地址
     * by 果悦科技
     * */
    protected StateCode invokerAddUserReceiptAddress(String userIdString, String addressString, String nameString,
    		String phoneString) throws ParseException, BaseException, IOException{
    	JSONObject paramJsonObject = new JSONObject();
    	paramJsonObject.put("action", "120001");
    	
    	JSONObject paramJsonObject2 = new JSONObject();
    	paramJsonObject2.put("userId", userIdString);//用户id
    	paramJsonObject2.put("address", addressString);//地址
    	paramJsonObject2.put("name", nameString);//用户姓名
    	paramJsonObject2.put("phone", phoneString);//手机
    	
    	paramJsonObject.put("data", paramJsonObject2);
    	
    	HttpPost mHttpPost = mHttpApi.createHttpPost(fullUrl(URL_API_ADDRESS),  paramJsonObject.toString());
    	return (StateCode)mHttpApi.doHttpRequest(mHttpPost, new StateCodeParser());
    }
    
    /**
     * 1.0.7 修改用户收货地址
     * by 果悦科技
     * */
    protected StateCode invokerModifyUserReceiptAddress(String idString, String addressString, String nameString, 
    		String phoneString) throws ParseException, BaseException, IOException{
    	JSONObject paramJsonObject = new JSONObject();
    	paramJsonObject.put("action", "120002");
    	
    	JSONObject paramJsonObject2 = new JSONObject();
    	paramJsonObject2.put("id", idString);//地址id
    	paramJsonObject2.put("address", addressString);//地址
    	paramJsonObject2.put("name", nameString);//姓名
    	paramJsonObject2.put("phone", phoneString);//电话
    	
    	paramJsonObject.put("data", paramJsonObject2);
    	
    	HttpPost mHttpPost = mHttpApi.createHttpPost(fullUrl(URL_API_ADDRESS),  paramJsonObject.toString());
    	return (StateCode)mHttpApi.doHttpRequest(mHttpPost, new StateCodeParser());
    }
    
    /**
     * 1.0.8 删除用户收货地址
     * by 果悦科技
     * */
    protected StateCode invokerDeleteUserReceiptAddress(String idString) throws ParseException, BaseException, IOException{
    	JSONObject paramJsonObject = new JSONObject();
    	paramJsonObject.put("action", "120003");
    	
    	JSONObject paramJsonObject2 = new JSONObject();
    	paramJsonObject2.put("id", idString);//地址id
    	
    	paramJsonObject.put("data", paramJsonObject2);
    	
    	HttpPost mHttpPost = mHttpApi.createHttpPost(fullUrl(URL_API_ADDRESS),  paramJsonObject.toString());
    	return (StateCode)mHttpApi.doHttpRequest(mHttpPost, new StateCodeParser());
    }

    /**
     * 1.0.9 获取用户收货地址
     * by 果悦科技
     * */
    protected StateCode invokerObtainUserReceiptAddress(String userIdString) throws ParseException, BaseException, IOException{
    	JSONObject paramJsonObject = new JSONObject();
    	paramJsonObject.put("action", "120004");
    	
    	JSONObject paramJsonObject2 = new JSONObject();
    	paramJsonObject2.put("userId", userIdString);//用户id
    	
    	paramJsonObject.put("data", paramJsonObject2);
    	
    	HttpPost mHttpPost = mHttpApi.createHttpPost(fullUrl(URL_API_ADDRESS),  paramJsonObject.toString());
    	return (StateCode)mHttpApi.doHttpRequest(mHttpPost, new StateCodeParser());
    }
    
//    /**
//     * 1.0.3 设置用户信息
//     * by 果悦科技
//     * */
//    protected HttpResponse invokerSetUserInfo(String userIDString, String passWordString, String userNameString,
//    		String telephoneString, String mobileString, String emailString) throws ParseException, BaseException, IOException{
//    	JSONObject paramJsonObject = new JSONObject();
//    	paramJsonObject.put("comStr", "setUserInfo");
//    	
//    	JSONObject paramJsonObject2 = new JSONObject();
//    	paramJsonObject2.put("userID", userIDString);//userID
//    	paramJsonObject2.put("passWord", passWordString);//用户密码
//    	paramJsonObject2.put("userName", userNameString);//用户名
//    	paramJsonObject2.put("tel", telephoneString);//用户电话
//    	paramJsonObject2.put("mobile", mobileString);//用户手机
//    	paramJsonObject2.put("email", emailString);//用户电子邮件
//
//    	paramJsonObject.put("info", paramJsonObject2);
//    	
//    	JSONObject paramOne = new JSONObject();
//    	paramOne.put("data", paramJsonObject);
//    	
//    	HttpPost mHttpPost = mHttpApi.createHttpPost(fullUrl(""), new BasicNameValuePair("data", paramOne.toString()));
//    	return mHttpApi.doHttpResponse(mHttpPost);
//    }
//    /**
//     * 1.0.4 应用升级
//     * by 果悦科技
//     * */
//    protected HttpResponse invokerAppUpdate() throws ParseException, BaseException, IOException{
// 
//    	HttpPost mHttpPost = mHttpApi.createOneHttpPost(fullUrl(""), "");
//    	return mHttpApi.doHttpResponse(mHttpPost);
//    	
//    }
//    
    
    /**
	 * 1.0.10 获取用户周围3000米内的商店
	 * by 果悦科技 
	 * */
	protected BaseResponse<Shop> invokerObtainShopListByDistance(String shopTypeString, String longitudeString, String latitudeString) throws ParseException, BaseException, IOException{
    	JSONObject paramJsonObject = new JSONObject();
    	paramJsonObject.put("action", "103005");
    	JSONObject paramJsonObject2 = new JSONObject();
    	paramJsonObject2.put("shopType", shopTypeString);//商店类型
    	paramJsonObject2.put("longitude", longitudeString);//经度
    	paramJsonObject2.put("latitude", latitudeString);//纬度
    	paramJsonObject.put("data", paramJsonObject2);

    	HttpPost mHttpPost = mHttpApi.createHttpPost(fullUrl(URL_API_SHOP),  paramJsonObject.toString());
    	return (BaseResponse<Shop>)mHttpApi.doHttpRequest(mHttpPost, new ShopParser());
    }
    
    /**
	 * 1.0.2 根据类型获取商店列表 
	 * 弃用接口
	 * by 果悦科技 
	 * */
	protected BaseResponse<Shop> invokerObtainShopListByType(String typeString, String pageString) throws ParseException, BaseException, IOException{
    	JSONObject paramJsonObject = new JSONObject();
    	paramJsonObject.put("action", "103004");
    	JSONObject paramJsonObject2 = new JSONObject();
    	paramJsonObject2.put("type", typeString);//类型
    	paramJsonObject2.put("page", Integer.valueOf(pageString));//页码
    	paramJsonObject.put("data", paramJsonObject2);

    	HttpPost mHttpPost = mHttpApi.createHttpPost(fullUrl(URL_API_SHOP),  paramJsonObject.toString());
    	return (BaseResponse<Shop>)mHttpApi.doHttpRequest(mHttpPost, new ShopParser());
    }
	
    /**
	 * 1.0.11  商店详细信息
	 * by 果悦科技 
	 * */
	protected BaseResponse<Shop> invokerObtainShopDetailByShopId(String shopIdString) throws ParseException, BaseException, IOException{
    	JSONObject paramJsonObject = new JSONObject();
    	paramJsonObject.put("action", "103009");
    	JSONObject paramJsonObject2 = new JSONObject();
    	paramJsonObject2.put("id", shopIdString);//商店id

    	paramJsonObject.put("data", paramJsonObject2);

    	HttpPost mHttpPost = mHttpApi.createHttpPost(fullUrl(URL_API_GOODS),  paramJsonObject.toString());
    	return (BaseResponse<Shop>)mHttpApi.doHttpRequest(mHttpPost, new ShopParser());
    }
	
    /**
	 * 1.0.12  猜你喜欢
	 * by 果悦科技 
	 * */
	protected BaseResponse<Shop> invokerGuessYourLike(String longitudeString, String latitudeString) throws ParseException, BaseException, IOException{
    	JSONObject paramJsonObject = new JSONObject();
    	paramJsonObject.put("action", "103010");
    	JSONObject paramJsonObject2 = new JSONObject();
    	paramJsonObject2.put("longitude", longitudeString);//经度
    	paramJsonObject2.put("latitude", latitudeString);//纬度
    	paramJsonObject.put("data", paramJsonObject2);

    	HttpPost mHttpPost = mHttpApi.createHttpPost(fullUrl(URL_API_SHOP),  paramJsonObject.toString());
    	return (BaseResponse<Shop>)mHttpApi.doHttpRequest(mHttpPost, new ShopParser());
    }
	
    /**
	 * 1.0.13 获取商店的商品种类
	 * by 果悦科技 
	 * */
	protected BaseResponse<GoodsType> invokerObtainGoodsType(String shopIdString) throws ParseException, BaseException, IOException{
    	JSONObject paramJsonObject = new JSONObject();
    	paramJsonObject.put("action", "106005");
    	JSONObject paramJsonObject2 = new JSONObject();
    	paramJsonObject2.put("shopId", shopIdString);
    	paramJsonObject.put("data", paramJsonObject2);

    	HttpPost mHttpPost = mHttpApi.createHttpPost(fullUrl(URL_API_GOODSTYPE),  paramJsonObject.toString());
    	return (BaseResponse<GoodsType>)mHttpApi.doHttpRequest(mHttpPost, new GoodsTypeParser());
    }
	
    /**
	 * 1.0.14 获取商品列表
	 * by 果悦科技 
	 * */
	protected BaseResponse<Goods> invokerObtainGoodsList(String shopIdString, String typeString, String pageString) throws ParseException, BaseException, IOException{
    	JSONObject paramJsonObject = new JSONObject();
    	paramJsonObject.put("action", "105006");
    	JSONObject paramJsonObject2 = new JSONObject();
    	paramJsonObject2.put("shopId", shopIdString);
    	paramJsonObject2.put("type", typeString);
    	paramJsonObject2.put("page", pageString);
    	
    	paramJsonObject.put("data", paramJsonObject2);

    	HttpPost mHttpPost = mHttpApi.createHttpPost(fullUrl(URL_API_GOODS),  paramJsonObject.toString());
    	return (BaseResponse<Goods>)mHttpApi.doHttpRequest(mHttpPost, new GoodsParser());
    }
	
    /**
	 * 1.0.15添加订单
	 * by 果悦科技 
	 * */
	protected StateCode invokerAddOrder(String shopIdString, String userIdString, String addressIdString, List<OrderGoodsList> orderGoodsDataList,
			String isPaidString, String orderStatusString, String totalPriceString, String dateString) throws ParseException, BaseException, IOException{
    	JSONObject paramJsonObject = new JSONObject();
    	paramJsonObject.put("action", "107001");
    	JSONObject paramJsonObject2 = new JSONObject();
    	paramJsonObject2.put("shopId", shopIdString);
    	paramJsonObject2.put("userId", userIdString);
    	paramJsonObject2.put("addressId", addressIdString);
    	
    	JSONArray jsonArray = new JSONArray();
    	for (int i = 0; i < orderGoodsDataList.size(); i++) {
			JSONObject jsonObject = new JSONObject();
			OrderGoodsList orderGoodsList = (OrderGoodsList)orderGoodsDataList.get(i);
			jsonObject.put("name", orderGoodsList.getGoodsName());
			jsonObject.put("price", orderGoodsList.getGoodsPrice());
			jsonObject.put("num", orderGoodsList.getGoodsNumber());
			jsonArray.add(jsonObject);
		}
    	
    	paramJsonObject2.put("goodsList", jsonArray);
    	paramJsonObject2.put("isPaid", isPaidString);
    	paramJsonObject2.put("orderStatus", orderStatusString);
    	paramJsonObject2.put("totalPrice", totalPriceString);
    	
    	paramJsonObject.put("data", paramJsonObject2);

    	HttpPost mHttpPost = mHttpApi.createHttpPost(fullUrl(URL_API_ORDER),  paramJsonObject.toString());
    	return (StateCode)mHttpApi.doHttpRequest(mHttpPost, new StateCodeParser());
    }
	
	/**
	 * 1.0.16 获取用户订单
	 * by 果悦科技 
	 * */
	protected BaseResponse<Shop> invokerObtainUserOrder(String userIdString) throws ParseException, BaseException, IOException{
    	JSONObject paramJsonObject = new JSONObject();
    	paramJsonObject.put("action", "107004");
    	JSONObject paramJsonObject2 = new JSONObject();
    	paramJsonObject2.put("userId", userIdString);
    	
    	paramJsonObject.put("data", paramJsonObject2);

    	HttpPost mHttpPost = mHttpApi.createHttpPost(fullUrl(URL_API_ORDER),  paramJsonObject.toString());
    	return (BaseResponse<Shop>)mHttpApi.doHttpRequest(mHttpPost, new ShopParser());
    }
	
	/**
	 * 1.0.17小蜜蜂所获取的订单列表
	 * by 果悦科技 
	 * */
	protected BaseResponse<Shop> invokerBeeOrderList(String userIdString, String orderStatusString) throws ParseException, BaseException, IOException{
    	JSONObject paramJsonObject = new JSONObject();
    	paramJsonObject.put("action", "107009");
    	JSONObject paramJsonObject2 = new JSONObject();
    	paramJsonObject2.put("beeId", userIdString);
    	paramJsonObject2.put("orderStatus", orderStatusString);
    	
    	paramJsonObject.put("data", paramJsonObject2);

    	HttpPost mHttpPost = mHttpApi.createHttpPost(fullUrl(URL_API_ORDER),  paramJsonObject.toString());
    	return (BaseResponse<Shop>)mHttpApi.doHttpRequest(mHttpPost, new ShopParser());
    }
	
	/**
	 * 1.0.18 获取订单详情
	 * by 果悦科技 
	 * */
	protected BaseResponse<Shop> invokerOrderDetail(String orderIdString) throws ParseException, BaseException, IOException{
    	JSONObject paramJsonObject = new JSONObject();
    	paramJsonObject.put("action", "107006");
    	JSONObject paramJsonObject2 = new JSONObject();
    	paramJsonObject2.put("orderId", orderIdString);
    	
    	paramJsonObject.put("data", paramJsonObject2);

    	HttpPost mHttpPost = mHttpApi.createHttpPost(fullUrl(URL_API_ORDER),  paramJsonObject.toString());
    	return (BaseResponse<Shop>)mHttpApi.doHttpRequest(mHttpPost, new ShopParser());
    }
	
}
