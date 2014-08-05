package com.ssm.ssmbee.httpoperation;

import java.io.IOException;

import com.ssm.ssmbee.entity.Auth;
import com.ssm.ssmbee.entity.BaseResponse;
import com.ssm.ssmbee.entity.OrderMenu;
import com.ssm.ssmbee.entity.StateCode;
import com.ssm.ssmbee.exception.BaseException;
import com.ssm.ssmbee.exception.ParseException;
import com.ssm.ssmbee.parser.OrderMenuParser;


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
     * 1.0.6 获取小蜜蜂的订单
     * by 果悦科技
     * */
    public StateCode invokerObtainBeeOrder(String userIdString, String orderStatus) throws ParseException, BaseException, IOException{
    	return apiInvoker.invokerObtainBeeOrder(userIdString, orderStatus);
    }
    /**
     * 1.0.7 获取区域的订单
     * by 果悦科技
     * */
    public BaseResponse<OrderMenu> invokerObtainAreaOrder(String areaIdString) throws ParseException, BaseException, IOException{
    	return apiInvoker.invokerObtainAreaOrder(areaIdString);
    }
    /**
     * 1.0.8 小蜜蜂抢单
     * by 果悦科技
     * */
    public StateCode invokerBeeGrabOrder(String beeIdString, String orderIdString) throws ParseException, BaseException, IOException{
    	return apiInvoker.invokerBeeGrabOrder(beeIdString, orderIdString);
    }
    /**
     * 1.0.9 小蜜蜂订单列表
     * by 果悦科技
     * */
    public BaseResponse<OrderMenu> invokerObtainBeeStatusOrder(String beeIdString, String orderStatusString) throws ParseException, BaseException, IOException{
    	return apiInvoker.invokerObtainBeeStatusOrder(beeIdString, orderStatusString);
    }
        
}
