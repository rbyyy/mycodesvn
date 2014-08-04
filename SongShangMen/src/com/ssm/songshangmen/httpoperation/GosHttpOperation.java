package com.ssm.songshangmen.httpoperation;

import java.io.IOException;
import java.util.List;

import com.ssm.songshangmen.entity.Auth;
import com.ssm.songshangmen.entity.BaseResponse;
import com.ssm.songshangmen.entity.Goods;
import com.ssm.songshangmen.entity.GoodsType;
import com.ssm.songshangmen.entity.OrderGoodsList;
import com.ssm.songshangmen.entity.Shop;
import com.ssm.songshangmen.entity.StateCode;
import com.ssm.songshangmen.exception.BaseException;
import com.ssm.songshangmen.exception.ParseException;


public class GosHttpOperation {
	public static final String API_DOMAIN = "182.92.184.3:80";

    private GosHttpAPIInvoker apiInvoker;
    
    public GosHttpOperation(GosHttpAPIInvoker httpApi) {
        apiInvoker = httpApi;
    }

    public static final GosHttpAPIInvoker createHttpApi(String domain, String clientVersion) {
        return new GosHttpAPIInvoker(domain, clientVersion);
    }

    public static final GosHttpAPIInvoker createHttpApi(String clientVersion) {
        return createHttpApi(API_DOMAIN, clientVersion);
    }
    
    /**
     * 1.0.0 获取手机验证码
     * by 果悦科技 
     * */
    public StateCode invokerPhoneAuthCode(String phoneNumberString) throws ParseException, BaseException, IOException{
    	return apiInvoker.invokerPhoneAuthCode(phoneNumberString);
    }
    
    /**
	 * 1.0.1  检测手机验证码
	 * by 果悦科技 
	 * */
    public StateCode invokerVerifyPhoneAuthCode(String phoneNumberString, String phoneAuthCodeString) throws ParseException, BaseException, IOException{
    	return apiInvoker.invokerVerifyPhoneAuthCode(phoneNumberString, phoneAuthCodeString);
	}
    
    /**
     *1.0.2 调用用户注册接口
     *by 果悦科技 
     * */
    public BaseResponse<Auth> invokerUserRegister(String usernameString, String passwordString, String mobileString) throws ParseException, BaseException, IOException{
    	return apiInvoker.invokerUserRegister(usernameString, passwordString, mobileString);
    }
    
    /**
     * 1.0.3 调用登录接口
     * by 果悦科技
     * */
    public BaseResponse<Auth> invokerUserLogin(String usernameString, String passwordString, String resIdString) throws ParseException, BaseException, IOException{
    	return apiInvoker.invokerUserLogin(usernameString, passwordString, resIdString);
    }
    
    /**
     * 1.0.4 修改密码接口
     * by 果悦科技
     * */
    public StateCode invokerModifyPassword(String roleIdString, String passwordString) throws ParseException, BaseException, IOException{
    	return apiInvoker.invokerModifyPassword(roleIdString, passwordString);
    }
    
    /**
     * 1.0.5 获取用户信息
     * by 果悦科技
     * */
    public StateCode invokerObtainUserInfo(String userIdString) throws ParseException, BaseException, IOException{
    	return apiInvoker.invokerObtainUserInfo(userIdString);
    }
    
    /**
     * 1.0.6 添加用户收货地址
     * by 果悦科技
     * */
    public StateCode invokerAddUserReceiptAddress(String userIdString, String addressString, String nameString,
    		String phoneString) throws ParseException, BaseException, IOException{
    	return apiInvoker.invokerAddUserReceiptAddress(userIdString, addressString, nameString, phoneString);
    }
    
    /**
     * 1.0.7 修改用户收货地址
     * by 果悦科技
     * */
    public StateCode invokerModifyUserReceiptAddress(String idString, String addressString, String nameString, 
    		String phoneString) throws ParseException, BaseException, IOException{
    	return apiInvoker.invokerModifyUserReceiptAddress(idString, addressString, nameString, phoneString);
    }
    
    /**
     * 1.0.8 删除用户收货地址
     * by 果悦科技
     * */
    public StateCode invokerDeleteUserReceiptAddress(String idString) throws ParseException, BaseException, IOException{
    	return apiInvoker.invokerDeleteUserReceiptAddress(idString);
    }
    
    /**
     * 1.0.9 获取用户收货地址
     * by 果悦科技
     * */
    public StateCode invokerObtainUserReceiptAddress(String userIdString) throws ParseException, BaseException, IOException{
    	return apiInvoker.invokerObtainUserReceiptAddress(userIdString);
    }
    
    /**
   	 * 1.0.10 获取用户周围3000米内的商店
   	 * by 果悦科技 
   	 * */
    public BaseResponse<Shop> invokerObtainShopListByDistance(String shopTypeString, String longitudeString, String latitudeString) throws ParseException, BaseException, IOException{
   		return apiInvoker.invokerObtainShopListByDistance(shopTypeString, longitudeString, latitudeString);
   	}
    
    /**
   	 * 1.0.11  商店详细信息
   	 * by 果悦科技 
   	 * */
    public BaseResponse<Shop> invokerObtainShopDetailByShopId(String shopIdString) throws ParseException, BaseException, IOException{
   		return apiInvoker.invokerObtainShopDetailByShopId(shopIdString);
   	}
   	
   	/**
	 * 1.0.12  猜你喜欢
	 * by 果悦科技 
	 * */
    public BaseResponse<Shop> invokerGuessYourLike(String longitudeString, String latitudeString) throws ParseException, BaseException, IOException{
		return apiInvoker.invokerGuessYourLike(longitudeString, latitudeString);
	}
	
	/**
	 * 1.0.13 获取商店的商品种类
	 * by 果悦科技 
	 * */
    public BaseResponse<GoodsType> invokerObtainGoodsType(String shopIdString) throws ParseException, BaseException, IOException{
		return apiInvoker.invokerObtainGoodsType(shopIdString);
	}
	
    /**
	 * 1.0.14 获取商品列表
	 * by 果悦科技 
	 * */
	public BaseResponse<Goods> invokerObtainGoodsList(String shopIdString, String typeString, String pageString) throws ParseException, BaseException, IOException{
		return apiInvoker.invokerObtainGoodsList(shopIdString, typeString, pageString);
	}
	
    /**
	 * 1.0.15添加订单
	 * by 果悦科技 
	 * */
	public StateCode invokerAddOrder(String shopIdString, String userIdString, String addressIdString, List<OrderGoodsList> orderGoodsDataList,
			String isPaidString, String orderStatusString, String totalPriceString, String dateString) throws ParseException, BaseException, IOException{
		return apiInvoker.invokerAddOrder(shopIdString, userIdString, addressIdString, orderGoodsDataList, isPaidString, orderStatusString, totalPriceString, dateString);
	}
	
	/**
	 * 1.0.16 获取用户订单
	 * by 果悦科技 
	 * */
	public BaseResponse<Shop> invokerObtainUserOrder(String userIdString) throws ParseException, BaseException, IOException{
		return apiInvoker.invokerObtainUserOrder(userIdString);
	}
	
	/**
	 * 1.0.17小蜜蜂所获取的订单列表
	 * by 果悦科技 
	 * */
	public BaseResponse<Shop> invokerBeeOrderList(String userIdString, String orderStatusString) throws ParseException, BaseException, IOException{
		return apiInvoker.invokerBeeOrderList(userIdString, orderStatusString);
	}
	
	/**
	 * 1.0.18 获取订单详情
	 * by 果悦科技 
	 * */
	public BaseResponse<Shop> invokerOrderDetail(String orderIdString) throws ParseException, BaseException, IOException{
		return invokerOrderDetail(orderIdString);
	}
	
    /**
	 * 1.0.2 根据类型获取商店列表
	 * by 果悦科技 
	 * */
	public BaseResponse<Shop> invokerObtainShopListByType(String typeString, String pageString) throws ParseException, BaseException, IOException{
		return apiInvoker.invokerObtainShopListByType(typeString, pageString);
	}
    
//    /**
//     *1.0.1 用户注册
//     *by 果悦科技 
//     * */
//    public HttpResponse invokerUserRegisteredResponse(String usernameString, String passwordString) throws ParseException, BaseException, IOException{
//    	return apiInvoker.invokerRegisteredUsers(usernameString, passwordString);
//    }
//    
//    /**
//     * 1.0.2 用户登录
//     * by 果悦科技
//     * */
//    public HttpResponse invokerUserLoginUpResponse(String usernameString, String passwordString) throws ParseException, BaseException, IOException{
//    	return apiInvoker.invokerUserLogin(usernameString, passwordString);
//    }
//    
//    /**
//     * 1.0.3 设置用户信息
//     * by 果悦科技
//     * */
//    public HttpResponse invokerSetUserInfoResponse(String userIDString, String passWordString, String userNameString, String telephoneString, String mobileString, 
//    		String emailString) throws ParseException, BaseException, IOException{
//    	return apiInvoker.invokerSetUserInfo(userIDString, passWordString, userNameString, telephoneString, mobileString, emailString);
//    }
//    
//    /**
//     * 1.0.4 应用升级
//     * by 果悦科技
//     * */
//    public HttpResponse invokerAppUpdateResponse() throws ParseException, BaseException, IOException{
//    	return apiInvoker.invokerAppUpdate();
//    }
    
}
