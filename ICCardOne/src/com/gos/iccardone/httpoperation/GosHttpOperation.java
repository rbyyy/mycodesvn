package com.gos.iccardone.httpoperation;

import java.io.IOException;

import org.apache.http.HttpResponse;

import com.gos.iccardone.exception.BaseException;
import com.gos.iccardone.exception.ParseException;



public class GosHttpOperation {
	public static final String API_DOMAIN = "114.215.210.189:8080/ICCard/";//140.206.70.110:8805

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
     * 1.0.1 用户登录
     * by 果悦科技
     * */
    public HttpResponse invokerUserLoginUpResponse(String usernameString, String passwordString) throws ParseException, BaseException, IOException{
    	return apiInvoker.invokerUserLogin(usernameString, passwordString);
    }
    /**
     * 1.0.2 修改密码
     * by 果悦科技
     * */
    public HttpResponse invokerModifyPasswordRespone(String cardIdString, String newPasswordString, String oldPasswordString, String tokenString) throws ParseException, BaseException, IOException{
    	return apiInvoker.invokerModifyPassword(cardIdString, newPasswordString, oldPasswordString, tokenString);
    }
    /**
     * 1.0.3 查询余额
     * by 果悦科技
     * */
    public HttpResponse invokerQueryBalanceResponse(String cardIdString, String tokenString) throws ParseException, BaseException, IOException{
    	return apiInvoker.invokerQueryBalance(cardIdString, tokenString);
    }
    /**
     * 1.0.4 代收
     * by 果悦科技
     * */
    public HttpResponse invokerInToAccountResponse(String userNameString, String userPasswordString, String bankCodeString, 
      		 String accountNoString, String accountNameString, String amountString) throws ParseException, BaseException, IOException{
    	return apiInvoker.invokerInToAccount(userNameString, userPasswordString, bankCodeString, accountNoString, accountNameString, amountString);
    }
    /**
     * 1.0.5 代付
     * by 果悦科技
     * */
    public HttpResponse invokerOutAccountResponse(String userNameString, String userPasswordString, String bankCodeString, 
      		 String accountNoString, String accountNameString, String amountString) throws ParseException, BaseException, IOException{
    	return apiInvoker.invokerOutAccount(userNameString, userPasswordString, bankCodeString, accountNoString, accountNameString, amountString);
    }
    /**
     * 1.0.6 通过ic卡卡号登录
     * by 果悦科技
     * */
    public HttpResponse invokerICCardLoginResponse(String cardIdString, String passwordString, String tokenString) throws ParseException, BaseException, IOException{
    	 return apiInvoker.invokerICCardLogin(cardIdString, passwordString, tokenString);
     }
    /**
     * 1.0.7 充值
     * by 果悦科技
     * */
    public HttpResponse invokerPayResponse(String userNameString, String userPasswordString, String bankCodeString, 
      		 String accountNoString, String accountNameString, String amountString, String cardIdString, String tokenString) throws ParseException, BaseException, IOException{
   	 	return apiInvoker.invokerPay(userNameString, userPasswordString, bankCodeString, accountNoString, accountNameString, amountString, cardIdString, tokenString);
    }
    /**
     * 1.0.8 消费
     * by 果悦科技
     * */
    public HttpResponse invokerConsumeResponse( String userNameString, String userPasswordString, String bankCodeString, 
   		 String accountNoString, String accountNameString, String amountString, String cardIdString, String passwordString,
   		 String tokenString) throws ParseException, BaseException, IOException{
   	 	return apiInvoker.invokerConsume(userNameString, userPasswordString, bankCodeString, accountNoString, 
   	 			accountNameString, amountString, cardIdString, passwordString, tokenString);
    }
    /**
     * 1.0.9 交易流水
     * by 果悦科技
     * */
    public HttpResponse invokerOperatinResponse( String cardIdString, String startDateString, String endDateString, 
    		String extraString,String tokenString ) throws ParseException, BaseException, IOException{
   	 	return apiInvoker.invokerOperatin(cardIdString, startDateString, endDateString, extraString, tokenString);
    }
    /**
     * 1.0.10 卡卡转账
     * by 果悦科技
     * */
    public HttpResponse invokerCardCardTransferResponse( String outCardIdString, String outPwdString, String inCardIdString, 
  		   String amountString, String tokenString) throws ParseException, BaseException, IOException{
   	 	return apiInvoker.invokerCardCardTransfer(outCardIdString, outPwdString, inCardIdString, amountString, tokenString);
    }
    /**
     * 1.0.11 卡关联信息
     * by 果悦科技
     * */
    public HttpResponse invokerCardAssociationResponse( String cardIdString, String tokenString) throws ParseException, BaseException, IOException{
   	 	return apiInvoker.invokerCardAssociation(cardIdString, tokenString);
    }
    /**
     * 1.0.12 软件更新
     * by 果悦科技
     * */
    public HttpResponse invokerSoftwareUpdateResponse() throws ParseException, BaseException, IOException{
   	 	return apiInvoker.invokerSoftwareUpdate();
    }
    

}
