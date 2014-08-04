package com.ssm.ssmshop.httpoperation;

import java.io.IOException;

import com.ssm.ssmshop.entity.Auth;
import com.ssm.ssmshop.entity.BaseResponse;
import com.ssm.ssmshop.entity.Goods;
import com.ssm.ssmshop.entity.GoodsBaseResponse;
import com.ssm.ssmshop.entity.OrderPreview;
import com.ssm.ssmshop.entity.StateCode;
import com.ssm.ssmshop.exception.BaseException;
import com.ssm.ssmshop.exception.ParseException;


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
     * 1.0.6 获取商家订单列表
     * by 果悦科技
     * */
    public BaseResponse<OrderPreview> invokerObtainShopOrderList(String shopIdString, String pageString) throws ParseException, BaseException, IOException{
    	return apiInvoker.invokerObtainShopOrderList(shopIdString, pageString);
    }
    /**
     * 1.0.7 获取商家订单详情
     * by 果悦科技
     * */
    public GoodsBaseResponse<Goods> invokerObtainShopOrderDetail(String orderIdString) throws ParseException, BaseException, IOException{
    	return apiInvoker.invokerObtainShopOrderDetail(orderIdString);
    }
    /**
     * 1.0.8 获取商家详细信息
     * by 果悦科技
     * */
    public StateCode invokerObtainShopDetail(String shopIdString) throws ParseException, BaseException, IOException{
    	return apiInvoker.invokerObtainShopDetail(shopIdString);
    }
    /**
     * 1.0.9 商家审核订单
     * by 果悦科技
     * */
    public StateCode invokerShopAuditOrder(String orderIdString) throws ParseException, BaseException, IOException{
    	return apiInvoker.invokerShopAuditOrder(orderIdString);
    }
    
       
}
