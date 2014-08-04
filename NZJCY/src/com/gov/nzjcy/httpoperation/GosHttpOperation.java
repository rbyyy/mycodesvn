package com.gov.nzjcy.httpoperation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse;

import com.gov.nzjcy.entity.AskForOpenEntity;
import com.gov.nzjcy.entity.ChiefMailBoxEntity;
import com.gov.nzjcy.entity.OnlineBookEntity;
import com.gov.nzjcy.entity.OnlineReportEntity;
import com.gov.nzjcy.entity.SetAppealEntity;
import com.gov.nzjcy.exception.BaseException;
import com.gov.nzjcy.exception.ParseException;


public class GosHttpOperation {
	public static final String API_DOMAIN = "218.28.109.18:8080/NanZhao/index.action";//192.168.1.106:8080 218.28.109.18:8080

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

//    public HttpResponse invokerGoodsList(String uid, String page) throws ParseException, BaseException, IOException {
//        return apiInvoker.invokerGoodsList(uid, page);
//    }
    
    /**
     *1.0.1 用户注册
     *by 果悦科技 
     * */
    public HttpResponse invokerUserRegisteredResponse(String usernameString, String passwordString) throws ParseException, BaseException, IOException{
    	return apiInvoker.invokerRegisteredUsers(usernameString, passwordString);
    }
    
    /**
     * 1.0.2 用户登录
     * by 果悦科技
     * */
    public HttpResponse invokerUserLoginUpResponse(String usernameString, String passwordString) throws ParseException, BaseException, IOException{
    	return apiInvoker.invokerUserLogin(usernameString, passwordString);
    }
    
    /**
     * 1.0.3 设置用户信息
     * by 果悦科技
     * */
    public HttpResponse invokerSetUserInfoResponse(String userIDString, String passWordString, String userNameString, String telephoneString, String mobileString, 
    		String emailString) throws ParseException, BaseException, IOException{
    	return apiInvoker.invokerSetUserInfo(userIDString, passWordString, userNameString, telephoneString, mobileString, emailString);
    }

    /**
     * 1.0.4 获取不同分类的文章
     * by 果悦科技
     * */
    public HttpResponse invokerNewsResponseByCategoryId( String categoryIDString,  String pageString) throws ParseException, BaseException, IOException{
    	return apiInvoker.invokerNewsByCategory(categoryIDString, pageString);
    }
    
    /**
     * 1.0.4.1 获取不同分类的文章详情信息
     * by 果悦科技
     * */
    public HttpResponse invokerNewsContentResponseByNewsId( String categoryIDString, String newsIdString) throws ParseException, BaseException, IOException{
    	return apiInvoker.invokerNewsContentByNewsId(categoryIDString, newsIdString);
    }
    
    /**
     * 1.0.5 获取文章的相关评论
     * by 果悦科技
     * */
    public HttpResponse invokerGetCommentResponseByNewsId(String newsIdString, String pageString) throws ParseException, BaseException, IOException{
    	return apiInvoker.invokerGetCommetByNewsId(newsIdString, pageString);
    }
    
    /**
     * 1.0.5.1 获取文章的相关评论
     * by 果悦科技
     * */
    public HttpResponse invokerGetCommentCountResponseByNewsId(String newsIdString) throws ParseException, BaseException, IOException{
    	return apiInvoker.invokerGetCommentCountByNewsId(newsIdString);
    }
    
    /**
     * 1.0.5.2 发表评论
     * by 果悦科技
     * */
    public HttpResponse invokerSendCommentResponseByNewsId(String newsIdString, String userIdString, String contentString) throws ParseException, BaseException, IOException{
    	return apiInvoker.invokerSendCommentByNewsId(newsIdString, userIdString, contentString);
    }
    
    /**
     * 1.0.6 提交依申请公开项
     * by 果悦科技
     * */
    public HttpResponse invokerAskForOpenResponse(AskForOpenEntity askForOpenEntity) throws ParseException, BaseException, IOException{
    	return apiInvoker.invokerAskForOpenHttpResponse(askForOpenEntity);
    }
    
    /**
     * 1.0.6.1 查询依申请公开项
     * by 果悦科技
     * */
    public HttpResponse invokerQueryAskForOpenResponse(String numberString) throws ParseException, BaseException, IOException{
    	return apiInvoker.invokerQueryAskForOpenHttpResponse(numberString);
    }
    
    /**
     * 1.0.7 设置预约
     * by 果悦科技
     * */
    public HttpResponse invokerSetReservationResponse(OnlineBookEntity onlineBookEntity) throws ParseException, BaseException, IOException{
    	return apiInvoker.invokerSetReservationHttpResponse(onlineBookEntity);
    }
    
    /**
     * 1.0.7.1 查询预约
     * by 果悦科技
     * */
    public HttpResponse invokerQueryOnlineBookResponse(String numberString) throws ParseException, BaseException, IOException{
    	return apiInvoker.invokerQueryOnlineBookHttpResponse(numberString);
    }
    
    /**
     * 1.0.8 查询使用须知
     * by 果悦科技
     * */
    public HttpResponse invokerQueryUseRulesResponse(String modelNameString) throws ParseException, BaseException, IOException{
    	return apiInvoker.invokerQueryUseRulesHttpResponse(modelNameString);
    }
    
    /**
     * 1.0.9.0 代表建议
     * by 果悦科技
     * */
    public HttpResponse invokerRepresentSuggestResponse(String userIdString, String descriptionString) throws ParseException, BaseException, IOException{
    	return apiInvoker.invokerRepresentSuggestHttpResponse(userIdString, descriptionString);
    }
    
    /**
     * 1.0.9.0 代表建议查询
     * by 果悦科技
     * */
    public HttpResponse invokerQueryRepresentSuggestResponse(String userIdString) throws ParseException, BaseException, IOException{
    	return apiInvoker.invokerQueryRepresentSuggestHttpResponse(userIdString);
    }
    
    /**
     * 1.0.9.1 公共信息
     * by 果悦科技
     * */
    public HttpResponse invokerPublicInformationResponse(String daibiaoTypeString) throws ParseException, BaseException, IOException{
    	return apiInvoker.invokerPublicInformationHttpResponse(daibiaoTypeString);
    }
    
    /**
     * 1.0.10.0 设置网上举报
     * by 果悦科技
     * */
    public HttpResponse invokerSetReportResponse(OnlineReportEntity onlineReportEntity) throws ParseException, BaseException, IOException{
    	return apiInvoker.invokerSetOnlineReportHttpResponse(onlineReportEntity);
    } 
    
    /**
     * 1.0.10.1 查询网上举报
     * by 果悦科技
     * */
    public HttpResponse invokerQueryOnlineReportResponse(String numberString) throws ParseException, BaseException, IOException{
    	return apiInvoker.invokerQueryOnlineReportHttpResponse(numberString);
    } 
    
    /**
     * 1.0.10.0 设置申诉
     * by 果悦科技
     * */
    public HttpResponse invokerSetAppealResponse(SetAppealEntity setAppealEntity) throws ParseException, BaseException, IOException{
    	return apiInvoker.invokerSetAppealHttpResponse(setAppealEntity);
    }
    
    /**
     * 1.0.10.1 查询申诉
     * by 果悦科技
     * */
    public HttpResponse invokerQueryAppealResponse(String numberString) throws ParseException, BaseException, IOException{
    	return apiInvoker.invokerQueryAppealHttpResponse(numberString);
    }
    
    /**
     * 1.0.11.0 检察长信箱
     * by 果悦科技
     * */
    public HttpResponse invokerChiefMailBoxResponse(ChiefMailBoxEntity chiefMailBoxEntity) throws ParseException, BaseException, IOException{
    	return apiInvoker.invokerChiefMailBoxHttpResponse(chiefMailBoxEntity);
    } 
    
    /**
     * 1.0.11.1 检察长信箱公示
     * by 果悦科技
     * */
    public HttpResponse invokerChiefMailShowResponse() throws ParseException, BaseException, IOException{
    	return apiInvoker.invokerChiefMailShowHttpResponse();
    }
    /**
     * 1.0.12 多媒体信息
     * by 果悦科技
     * */
    public HttpResponse invokerVideoNewsResponse(String pageNumberString) throws ParseException, BaseException, IOException{
    	return apiInvoker.invokerVideoNewsHttpResponse(pageNumberString);
    }
    /**
     * 1.0.13 查询我的提交
     * by 果悦科技
     * */
    public HttpResponse invokerMySubmitResponse(String queryTypeString, String userIdString) throws ParseException, BaseException, IOException{
    	return apiInvoker.invokerMySubmitHttpResponse(queryTypeString, userIdString);
    }
    /**
     * 1.0.14 案件信息查询
     * by 果悦科技
     * */
    public HttpResponse invokerInfoQueryResponse(String unitString, String tableString, String startTimeString, String endTimeString, String nameString) throws ParseException, BaseException, IOException{
    	return apiInvoker.invokerInfoQueryHttpResponse(unitString, tableString, startTimeString, endTimeString, nameString);
    }
    
}
