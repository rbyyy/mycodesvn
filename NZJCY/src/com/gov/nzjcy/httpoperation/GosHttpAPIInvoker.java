package com.gov.nzjcy.httpoperation;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.net.Uri;

import com.alibaba.fastjson.JSONObject;
import com.gov.nzjcy.entity.AskForOpenEntity;
import com.gov.nzjcy.entity.ChiefMailBoxEntity;
import com.gov.nzjcy.entity.OnlineBookEntity;
import com.gov.nzjcy.entity.OnlineReportEntity;
import com.gov.nzjcy.entity.SetAppealEntity;
import com.gov.nzjcy.exception.BaseException;
import com.gov.nzjcy.http.HttpApi;
import com.gov.nzjcy.http.AbstractHttpApi;
import com.gov.nzjcy.http.HttpApiWithBasicAuth;


public class GosHttpAPIInvoker {
	private final DefaultHttpClient mHttpClient                    	  = AbstractHttpApi.createHttpClient();
    private HttpApi                 mHttpApi;
	private final String            mApiBaseUrl;
	
	public GosHttpAPIInvoker(String domain, String clientVersion)
	{
		mApiBaseUrl = "http://" + domain; 
		mHttpApi = new HttpApiWithBasicAuth(mHttpClient, clientVersion);
	}
	private String fullUrl(String url){
        return mApiBaseUrl + url;
    }
//    protected HttpResponse invokerGoodsList(String uid, String page) throws ParseException, BaseException, IOException{
//    	JSONObject paramJsonObject = new JSONObject();
//    	paramJsonObject.put("comStr", "getSaleArea");
//    	paramJsonObject.put("timeStamp", "2014-03-07 15:14:53.993");
//    	JSONObject paramOne = new JSONObject();
//    	paramOne.put("data", paramJsonObject);
//    	
//    	HttpGet mHttpGet = mHttpApi.createHttpGet(fullUrl(""),
//    			new BasicNameValuePair("data", paramOne.toString()));
//        return mHttpApi.doHttpResponse(mHttpGet);
//    }
    /**
     *1.0.1 调用用户注册接口
     *by 果悦科技 
     * */
    protected HttpResponse invokerRegisteredUsers(String usernameString, String passwordString) throws ParseException, BaseException, IOException{
    	JSONObject paramJsonObject = new JSONObject();
    	paramJsonObject.put("comStr", "registeredUsers");
    	
    	JSONObject paramJsonObject2 = new JSONObject();
    	paramJsonObject2.put("userName", usernameString);//用户名
    	paramJsonObject2.put("passWord", passwordString);//登录密码
    	
    	paramJsonObject.put("info", paramJsonObject2);
    	
    	JSONObject paramOne = new JSONObject();
    	paramOne.put("data", paramJsonObject);
    	
    	HttpPost mHttpPost = mHttpApi.createHttpPost(fullUrl(""), new BasicNameValuePair("data", paramOne.toString()));
    	return mHttpApi.doHttpResponse(mHttpPost);
    	
//    	HttpGet mHttpGet = mHttpApi.createHttpGet(fullUrl(""),
//    			new BasicNameValuePair("data", paramOne.toString()));
//        return mHttpApi.doHttpResponse(mHttpGet);
    }
    /**
     * 1.0.2 调用登录接口
     * by 果悦科技
     * */
    protected HttpResponse invokerUserLogin(String usernameString, String passwordString) throws ParseException, BaseException, IOException{
    	JSONObject paramJsonObject = new JSONObject();
    	paramJsonObject.put("comStr", "loginUser");
    	
    	JSONObject paramJsonObject2 = new JSONObject();
    	paramJsonObject2.put("userName", usernameString);//用户名
    	paramJsonObject2.put("passWord", passwordString);//密码
    	
    	paramJsonObject.put("info", paramJsonObject2);
    	
    	JSONObject paramOne = new JSONObject();
    	paramOne.put("data", paramJsonObject);
    	
    	HttpGet mHttpGet = mHttpApi.createHttpGet(fullUrl(""),
    			new BasicNameValuePair("data", paramOne.toString()));
        return mHttpApi.doHttpResponse(mHttpGet);
    }
    /**
     * 1.0.3 设置用户信息
     * by 果悦科技
     * */
    protected HttpResponse invokerSetUserInfo(String userIDString, String passWordString, String userNameString,
    		String telephoneString, String mobileString, String emailString) throws ParseException, BaseException, IOException{
    	JSONObject paramJsonObject = new JSONObject();
    	paramJsonObject.put("comStr", "setUserInfo");
    	
    	JSONObject paramJsonObject2 = new JSONObject();
    	paramJsonObject2.put("userID", userIDString);//userID
    	paramJsonObject2.put("passWord", passWordString);//用户密码
    	paramJsonObject2.put("userName", userNameString);//用户名
    	paramJsonObject2.put("tel", telephoneString);//用户电话
    	paramJsonObject2.put("mobile", mobileString);//用户手机
    	paramJsonObject2.put("email", emailString);//用户电子邮件

    	paramJsonObject.put("info", paramJsonObject2);
    	
    	JSONObject paramOne = new JSONObject();
    	paramOne.put("data", paramJsonObject);
    	
    	HttpPost mHttpPost = mHttpApi.createHttpPost(fullUrl(""), new BasicNameValuePair("data", paramOne.toString()));
    	return mHttpApi.doHttpResponse(mHttpPost);
    	
//    	HttpGet mHttpGet = mHttpApi.createHttpGet(fullUrl(""),
//    			new BasicNameValuePair("data", paramOne.toString()));
//        return mHttpApi.doHttpResponse(mHttpGet);
    }
    /**
     * 1.0.4 获取不同分类的新闻
     * by 果悦科技
     * */
    protected HttpResponse invokerNewsByCategory(String categoryIDString ,String pageString) throws ParseException, BaseException, IOException{
    	JSONObject paramJsonObject = new JSONObject();
    	paramJsonObject.put("comStr", "getArticle");
  
    	JSONObject paramOne = new JSONObject();
    	paramOne.put("categoryID", categoryIDString);//新闻类别id
    	paramOne.put("page", pageString);//新闻页码
    	
    	paramJsonObject.put("info", paramOne);
    	
    	JSONObject paramFour = new JSONObject();
    	paramFour.put("data", paramJsonObject);
    	
    	HttpGet mHttpGet = mHttpApi.createHttpGet(fullUrl(""),
    			new BasicNameValuePair("data", paramFour.toString()));
        return mHttpApi.doHttpResponse(mHttpGet);
    }
    /**
     * 1.0.4.1 获取不同分类的文章详情信息
     * by 果悦科技
     * */
    protected HttpResponse invokerNewsContentByNewsId(String categoryIDString ,String newsIdString) throws ParseException, BaseException, IOException{
    	JSONObject paramJsonObject = new JSONObject();
    	paramJsonObject.put("comStr", "getArticleDetails");
  
    	JSONObject paramOne = new JSONObject();
    	paramOne.put("categoryID", categoryIDString);//新闻类别id
    	paramOne.put("newID", newsIdString);//新闻页码
    	
    	paramJsonObject.put("info", paramOne);
    	
    	JSONObject paramFour = new JSONObject();
    	paramFour.put("data", paramJsonObject);
    	
    	HttpGet mHttpGet = mHttpApi.createHttpGet(fullUrl(""),
    			new BasicNameValuePair("data", paramFour.toString()));
        return mHttpApi.doHttpResponse(mHttpGet);
    }
    /**
     * 1.0.5 获取文章的相关评论
     * by 果悦科技
     * */
    protected HttpResponse invokerGetCommetByNewsId(String newsIdString, String pageString) throws ParseException, BaseException, IOException{
    	JSONObject paramJsonObject = new JSONObject();
    	paramJsonObject.put("comStr", "getComment");
    	
    	JSONObject paramJsonObject2 = new JSONObject();
    	paramJsonObject2.put("newID", newsIdString);//新闻id
    	paramJsonObject2.put("page", pageString);//评论页码
    	
    	paramJsonObject.put("info", paramJsonObject2);
    	
    	JSONObject paramOne = new JSONObject();
    	paramOne.put("data", paramJsonObject);
    	
    	HttpGet mHttpGet = mHttpApi.createHttpGet(fullUrl(""),
    			new BasicNameValuePair("data", paramOne.toString()));
        return mHttpApi.doHttpResponse(mHttpGet);
    }
    /**
     * 1.0.5.1 获取文章的相关评论数量
     * by 果悦科技
     * */
    protected HttpResponse invokerGetCommentCountByNewsId(String newsIdString) throws ParseException, BaseException, IOException{
    	JSONObject paramJsonObject = new JSONObject();
    	paramJsonObject.put("comStr", "getCommentCount");
    	
    	JSONObject paramJsonObject2 = new JSONObject();
    	paramJsonObject2.put("newID", newsIdString);//新闻id
    	
    	paramJsonObject.put("info", paramJsonObject2);
    	
    	JSONObject paramOne = new JSONObject();
    	paramOne.put("data", paramJsonObject);
    	
    	HttpGet mHttpGet = mHttpApi.createHttpGet(fullUrl(""),
    			new BasicNameValuePair("data", paramOne.toString()));
        return mHttpApi.doHttpResponse(mHttpGet);
    }
    /**
     * 1.0.5.2 发表评论
     * by 果悦科技
     * */
    protected HttpResponse invokerSendCommentByNewsId(String newsIdString, String userIdString, String contentString) throws ParseException, BaseException, IOException{
    	JSONObject paramJsonObject = new JSONObject();
    	paramJsonObject.put("comStr", "setComment");
    	
    	JSONObject paramJsonObject2 = new JSONObject();
    	paramJsonObject2.put("newID", newsIdString);//新闻id
    	paramJsonObject2.put("userID", userIdString);//用户id
    	paramJsonObject2.put("content", contentString);//评论内容
    	
    	paramJsonObject.put("info", paramJsonObject2);
    	
    	JSONObject paramOne = new JSONObject();
    	paramOne.put("data", paramJsonObject);
    	

    	HttpPost mHttpPost = mHttpApi.createHttpPost(fullUrl(""), new BasicNameValuePair("data", paramOne.toString()));
    	return mHttpApi.doHttpResponse(mHttpPost);
//    	HttpGet mHttpGet = mHttpApi.createHttpGet(fullUrl(""),
//    			new BasicNameValuePair("data", paramOne.toString()));
//        return mHttpApi.doHttpResponse(mHttpGet);
    }
    /**
     * 1.0.6 提交依申请公开项
     * by 果悦科技
     * */
    protected HttpResponse invokerAskForOpenHttpResponse(AskForOpenEntity askForOpenEntity) throws ParseException, BaseException, IOException{
    	JSONObject paramJsonObject = new JSONObject();
    	paramJsonObject.put("comStr", "setPublicItem");
    	
    	JSONObject paramJsonObject2 = new JSONObject();
    	paramJsonObject2.put("userID", askForOpenEntity.getUserID());//用户id
    	paramJsonObject2.put("realName", askForOpenEntity.getRealName());//用户真实名称
    	paramJsonObject2.put("address", askForOpenEntity.getAddress());//常住地址
    	paramJsonObject2.put("IDCardNumber", askForOpenEntity.getIDcardNumber());//身份证号
    	paramJsonObject2.put("sex", askForOpenEntity.getSex());//性别，0女，1男
    	paramJsonObject2.put("email", askForOpenEntity.getEmail());//邮箱地址
    	paramJsonObject2.put("mobile", askForOpenEntity.getMobile());//移动电话
    	paramJsonObject2.put("tel", askForOpenEntity.getTel());//座机
    	paramJsonObject2.put("mailingAddress", askForOpenEntity.getMailingAddress());//邮寄地址
    	paramJsonObject2.put("conDescription", askForOpenEntity.getConDescription());//公开项类型
    	paramJsonObject2.put("description", askForOpenEntity.getDescription());//内容描述
    	paramJsonObject2.put("usefulDescription", askForOpenEntity.getUsefulDescription());//用途描述
    	paramJsonObject2.put("jieZhi", askForOpenEntity.getMedium());//介质
    	paramJsonObject2.put("huoquWay", askForOpenEntity.getGetMode());//获取方式
    	
    	paramJsonObject.put("info", paramJsonObject2);
    	
    	JSONObject paramOne = new JSONObject();
    	paramOne.put("data", paramJsonObject);
    	String paramOneString = paramOne.toJSONString();
    	
    	
    	HttpPost mHttpPost = mHttpApi.createHttpPost(fullUrl(""), new BasicNameValuePair("data", paramOne.toString()));
    	return mHttpApi.doHttpResponse(mHttpPost);
//    	HttpGet mHttpGet = mHttpApi.createHttpGet(fullUrl(""),
//    			new BasicNameValuePair("data", paramOne.toString()));
//        return mHttpApi.doHttpResponse(mHttpPost);
    } 
    /**
     * 1.0.6.1 查询依申请公开项
     * by 果悦科技
     * */
    protected HttpResponse invokerQueryAskForOpenHttpResponse(String numberString) throws ParseException, BaseException, IOException{
    	JSONObject paramJsonObject = new JSONObject();
    	paramJsonObject.put("comStr", "getPublicItem");
    	
    	JSONObject paramJsonObject2 = new JSONObject();
    	paramJsonObject2.put("number", numberString);//用于查询的编号
    	
    	paramJsonObject.put("info", paramJsonObject2);
    	
    	JSONObject paramOne = new JSONObject();
    	paramOne.put("data", paramJsonObject); 
    	
    	HttpGet mHttpGet = mHttpApi.createHttpGet(fullUrl(""),
    			new BasicNameValuePair("data", paramOne.toString()));
        return mHttpApi.doHttpResponse(mHttpGet);
    }
    /**
     * 1.0.7 设置预约
     * by 果悦科技
     * */
    protected HttpResponse invokerSetReservationHttpResponse(OnlineBookEntity onlineBookEntity) throws ParseException, BaseException, IOException{
    	JSONObject paramJsonObject = new JSONObject();
    	paramJsonObject.put("comStr", "setReservation");
    	
    	JSONObject paramJsonObject2 = new JSONObject();
    	paramJsonObject2.put("userID", onlineBookEntity.getUserIdString());//用户id
    	paramJsonObject2.put("SQR_shenfen", onlineBookEntity.getSQR_shenfen());//申请人身份
    	paramJsonObject2.put("SQR_shiwusuoName", onlineBookEntity.getSQR_shiwusuoName());//事务所名称
    	paramJsonObject2.put("SQR_isLower", onlineBookEntity.getSQR_isLower());//职业律师,0否，1是
    	paramJsonObject2.put("SQR_IsFromHelpCenter", onlineBookEntity.getSQR_IsFromHelpCenter());//法律援助中心指派，0否，1是
    	paramJsonObject2.put("SQR_IsNeedHide", onlineBookEntity.getSQR_IsNeedHide());//需要回避，0否，1是
    	paramJsonObject2.put("realName", onlineBookEntity.getSQR_realName());//姓名
    	paramJsonObject2.put("IDCardNumber", onlineBookEntity.getSQR_IDCardNumber());//身份证号
    	paramJsonObject2.put("mobile", onlineBookEntity.getSQR_mobile());//移动电话号
    	paramJsonObject2.put("BG_Realname", onlineBookEntity.getBG_Realname());//被告姓名
    	paramJsonObject2.put("BG_Sex", onlineBookEntity.getBG_Sex());//被告性别
    	paramJsonObject2.put("BG_Shenfenzheng", onlineBookEntity.getBG_Shenfenzheng());//被告身份证
    	paramJsonObject2.put("BG_birthday", onlineBookEntity.getBG_Birthday());//被告人的生日
    	paramJsonObject2.put("BG_Jianchayuan", onlineBookEntity.getBG_Jianchayuan());//检察院名称
    	paramJsonObject2.put("BG_Jieduan", onlineBookEntity.getBG_Jieduan());//阶段
    	paramJsonObject2.put("BG_Cuoshi", onlineBookEntity.getBG_Cuoshi());//措施
    	paramJsonObject2.put("BG_Anyou", onlineBookEntity.getBG_Anyou());//案由
    	paramJsonObject2.put("WTR_Realname", onlineBookEntity.getWTR_Realname());//委托人姓名
    	paramJsonObject2.put("WTR_Shenfenzheng", onlineBookEntity.getWTR_Shenfenzheng());//委托人身份证
    	paramJsonObject2.put("Type", onlineBookEntity.getType());//分类
    	paramJsonObject2.put("Description", onlineBookEntity.getDescription());//详细信息
    	
    	paramJsonObject.put("info", paramJsonObject2);
    	
    	JSONObject paramOne = new JSONObject();
    	paramOne.put("data", paramJsonObject); 
    	
    	HttpPost mHttpPost = mHttpApi.createHttpPost(fullUrl(""), new BasicNameValuePair("data", paramOne.toString()));
    	return mHttpApi.doHttpResponse(mHttpPost);
    	
//    	HttpGet mHttpGet = mHttpApi.createHttpGet(fullUrl(""),
//    			new BasicNameValuePair("data", paramOne.toString()));
//        return mHttpApi.doHttpResponse(mHttpGet);
    }
    /**
     * 1.0.7.1 查询预约
     * by 果悦科技
     * */
    protected HttpResponse invokerQueryOnlineBookHttpResponse(String numberString) throws ParseException, BaseException, IOException{
    	JSONObject paramJsonObject = new JSONObject();
    	paramJsonObject.put("comStr", "getReservation");
    	
    	JSONObject paramJsonObject2 = new JSONObject();
    	paramJsonObject2.put("number", numberString);//预约的编号，用于查询已经处理过的预约
    	
    	paramJsonObject.put("info", paramJsonObject2);
    	
    	JSONObject paramOne = new JSONObject();
    	paramOne.put("data", paramJsonObject); 
    	
    	HttpGet mHttpGet = mHttpApi.createHttpGet(fullUrl(""),
    			new BasicNameValuePair("data", paramOne.toString()));
        return mHttpApi.doHttpResponse(mHttpGet);
    }
    /**
     * 1.0.8 查询使用须知
     * by 果悦科技
     * */
    protected HttpResponse invokerQueryUseRulesHttpResponse(String modelNameString) throws ParseException, BaseException, IOException{
    	JSONObject paramJsonObject = new JSONObject();
    	paramJsonObject.put("comStr", "getNotes");
    	
    	JSONObject paramJsonObject2 = new JSONObject();
    	paramJsonObject2.put("modelName", modelNameString);//须知类型
    	
    	paramJsonObject.put("info", paramJsonObject2);
    	
    	JSONObject paramOne = new JSONObject();
    	paramOne.put("data", paramJsonObject); 
    	
    	HttpGet mHttpGet = mHttpApi.createHttpGet(fullUrl(""),
    			new BasicNameValuePair("data", paramOne.toString()));
        return mHttpApi.doHttpResponse(mHttpGet);
    }
    /**
     * 1.0.9.0 代表建议
     * by 果悦科技
     * */
    protected HttpResponse invokerRepresentSuggestHttpResponse(String userIdString, String descriptionString) throws ParseException, BaseException, IOException{
    	JSONObject paramJsonObject = new JSONObject();
    	paramJsonObject.put("comStr", "setSuggestions");
    	
    	JSONObject paramJsonObject2 = new JSONObject();
    	paramJsonObject2.put("userID", userIdString);//用户ID
    	paramJsonObject2.put("Description", descriptionString);//意见内容
    	
    	paramJsonObject.put("info", paramJsonObject2);
    	
    	JSONObject paramOne = new JSONObject();
    	paramOne.put("data", paramJsonObject); 
    	
    	HttpPost mHttpPost = mHttpApi.createHttpPost(fullUrl(""), new BasicNameValuePair("data", paramOne.toString()));
    	return mHttpApi.doHttpResponse(mHttpPost);
    	
//    	HttpGet mHttpGet = mHttpApi.createHttpGet(fullUrl(""),
//    			new BasicNameValuePair("data", paramOne.toString()));
//        return mHttpApi.doHttpResponse(mHttpGet);
    }
    
    /**
     * 1.0.9.0 代表建议查询
     * by 果悦科技
     * */
    protected HttpResponse invokerQueryRepresentSuggestHttpResponse(String userIdString)throws ParseException, BaseException, IOException{
    	JSONObject paramJsonObject = new JSONObject();
    	paramJsonObject.put("comStr", "getSuggestions");
    	
    	JSONObject paramJsonObject2 = new JSONObject();
    	paramJsonObject2.put("userID", userIdString);//用户ID
    	
    	paramJsonObject.put("info", paramJsonObject2);
    	
    	JSONObject paramOne = new JSONObject();
    	paramOne.put("data", paramJsonObject); 
    	
    	HttpPost mHttpPost = mHttpApi.createHttpPost(fullUrl(""), new BasicNameValuePair("data", paramOne.toString()));
    	return mHttpApi.doHttpResponse(mHttpPost);
    }
    
    /**
     * 1.0.9.1 公共信息
     * by 果悦科技
     * */
    protected HttpResponse invokerPublicInformationHttpResponse(String daibiaoTypeString) throws ParseException, BaseException, IOException{
    	JSONObject paramJsonObject = new JSONObject();
    	paramJsonObject.put("comStr", "getBehalfArticle");
    	
    	JSONObject paramJsonObject2 = new JSONObject();
    	paramJsonObject2.put("DaibiaoType", daibiaoTypeString);//代表类型，0全部，1人大代表，2政协委员（暂定）
    	
    	paramJsonObject.put("info", paramJsonObject2);
    	
    	JSONObject paramOne = new JSONObject();
    	paramOne.put("data", paramJsonObject); 
    	
    	HttpGet mHttpGet = mHttpApi.createHttpGet(fullUrl(""),
    			new BasicNameValuePair("data", paramOne.toString()));
        return mHttpApi.doHttpResponse(mHttpGet);
    }
    /**
     * 1.0.10.0 设置网上举报
     * by 果悦科技
     * */
    protected HttpResponse invokerSetOnlineReportHttpResponse(OnlineReportEntity onlineReportEntity) throws ParseException, BaseException, IOException{
    	JSONObject paramJsonObject = new JSONObject();
    	paramJsonObject.put("comStr", "setReport");
    	
    	JSONObject paramJsonObject2 = new JSONObject();
    	paramJsonObject2.put("userID", onlineReportEntity.getUserIdString());//用户id
    	paramJsonObject2.put("realName", onlineReportEntity.getOnlineReportUserName());//用户姓名
    	paramJsonObject2.put("IDCardNumber", onlineReportEntity.getOnlineReportIDCardNumber());//身份证信息
    	paramJsonObject2.put("mobile", onlineReportEntity.getOnlineReportMobile());//电话信息
    	paramJsonObject2.put("tel", onlineReportEntity.getOnlineReportTel());//座机
    	paramJsonObject2.put("address", onlineReportEntity.getOnlineReportAddress());//地址
    	paramJsonObject2.put("email", onlineReportEntity.getOnlineReportEmail());//邮箱
    	paramJsonObject2.put("BJB_Person_Type", onlineReportEntity.getBJB_Person_Type());//被举报人类型（0职务犯罪线索，1违法违纪干警）暂定
    	paramJsonObject2.put("BJB_Person_Realname", onlineReportEntity.getBJB_Person_Realname());//被举报人姓名
    	paramJsonObject2.put("BJB_Person_Sex", onlineReportEntity.getBJB_Person_Sex());//被举报人性别0女，1男
    	paramJsonObject2.put("BJB_Person_CompanyNameOrBumen", onlineReportEntity.getBJB_Person_CompanyNameOrBumen());//被举报人部门
    	paramJsonObject2.put("BJB_Person_Zhiji", onlineReportEntity.getBJB_Person_Zhiji());//被举报人职级
    	paramJsonObject2.put("BJB_Person_Zhiwu", onlineReportEntity.getBJB_Person_Zhiwu());//被举报人职务
    	paramJsonObject2.put("BJB_Person_Address", onlineReportEntity.getBJB_Person_Address());//被举报人地址信息
    	paramJsonObject2.put("BJB_Person_Anyou", onlineReportEntity.getBJB_Person_Anyou());//被举报案由
    	paramJsonObject2.put("Description", onlineReportEntity.getBJB_Description());//举报详情
    	
    	paramJsonObject.put("info", paramJsonObject2);
    	
    	JSONObject paramOne = new JSONObject();
    	paramOne.put("data", paramJsonObject); 
    	
    	HttpPost mHttpPost = mHttpApi.createHttpPost(fullUrl(""), new BasicNameValuePair("data", paramOne.toString()));
    	return mHttpApi.doHttpResponse(mHttpPost);
    	
//    	HttpGet mHttpGet = mHttpApi.createHttpGet(fullUrl(""),
//    			new BasicNameValuePair("data", paramOne.toString()));
//        return mHttpApi.doHttpResponse(mHttpGet);
    }
    /**
     * 1.0.10.1 查询网上举报
     * by 果悦科技
     * */
    protected HttpResponse invokerQueryOnlineReportHttpResponse(String numberString) throws ParseException, BaseException, IOException{
    	JSONObject paramJsonObject = new JSONObject();
    	paramJsonObject.put("comStr", "getReport");
    	
    	JSONObject paramJsonObject2 = new JSONObject();
    	paramJsonObject2.put("number", numberString);//预约的编号，用于查询已经处理过的预约
    	
    	paramJsonObject.put("info", paramJsonObject2);
    	
    	JSONObject paramOne = new JSONObject();
    	paramOne.put("data", paramJsonObject); 
    	
    	HttpGet mHttpGet = mHttpApi.createHttpGet(fullUrl(""),
    			new BasicNameValuePair("data", paramOne.toString()));
        return mHttpApi.doHttpResponse(mHttpGet);
    }
    /**
     * 1.0.10.0 设置申诉
     * by 果悦科技
     * */
    protected HttpResponse invokerSetAppealHttpResponse(SetAppealEntity setAppealEntity) throws ParseException, BaseException, IOException{
    	JSONObject paramJsonObject = new JSONObject();
    	paramJsonObject.put("comStr", "setAppeal");
    	
    	JSONObject paramJsonObject2 = new JSONObject();
    	paramJsonObject2.put("userID", setAppealEntity.getUserIdString());//用户id
    	paramJsonObject2.put("realName", setAppealEntity.getSetAppealUserName());//用户姓名
    	paramJsonObject2.put("Birthday", setAppealEntity.getSetAppealBirthday());//出生年月
    	paramJsonObject2.put("sex", setAppealEntity.getSetAppealSex());//性别0女，1男
    	paramJsonObject2.put("Jiguan", setAppealEntity.getSetAppealJiGuan());//籍贯
    	paramJsonObject2.put("Company", setAppealEntity.getCompany());//工作单位
    	paramJsonObject2.put("Minzu", setAppealEntity.getMinzu());//民族
    	paramJsonObject2.put("address", setAppealEntity.getAddress());//地址
    	paramJsonObject2.put("mobile", setAppealEntity.getMobile());//电话
    	paramJsonObject2.put("IDCardNumber", setAppealEntity.getIDCardNumber());//身份证号
    	paramJsonObject2.put("email", setAppealEntity.getEmail());//邮箱
    	paramJsonObject2.put("AnjianType", setAppealEntity.getAnjianType());//案件类别
    	paramJsonObject2.put("Description", setAppealEntity.getDescription());//举报详情
    	
    	paramJsonObject.put("info", paramJsonObject2);
    	
    	JSONObject paramOne = new JSONObject();
    	paramOne.put("data", paramJsonObject); 
    	
    	HttpPost mHttpPost = mHttpApi.createHttpPost(fullUrl(""), new BasicNameValuePair("data", paramOne.toString()));
    	return mHttpApi.doHttpResponse(mHttpPost);
    	
//    	HttpGet mHttpGet = mHttpApi.createHttpGet(fullUrl(""),
//    			new BasicNameValuePair("data", paramOne.toString()));
//        return mHttpApi.doHttpResponse(mHttpGet);
    }
    /**
     * 1.0.10.1 查询申诉
     * by 果悦科技
     * */
    protected HttpResponse invokerQueryAppealHttpResponse(String numberString) throws ParseException, BaseException, IOException{
    	JSONObject paramJsonObject = new JSONObject();
    	paramJsonObject.put("comStr", "getAppeal");
    	
    	JSONObject paramJsonObject2 = new JSONObject();
    	paramJsonObject2.put("number", numberString);//预约的编号，用于查询已经处理过的预约
    	
    	paramJsonObject.put("info", paramJsonObject2);
    	
    	JSONObject paramOne = new JSONObject();
    	paramOne.put("data", paramJsonObject); 
    	
    	HttpGet mHttpGet = mHttpApi.createHttpGet(fullUrl(""),
    			new BasicNameValuePair("data", paramOne.toString()));
        return mHttpApi.doHttpResponse(mHttpGet);
    }
    /**
     * 1.0.11.0 检察长信箱
     * by 果悦科技
     * */
    protected HttpResponse invokerChiefMailBoxHttpResponse(ChiefMailBoxEntity chiefMailBoxEntity) throws ParseException, BaseException, IOException{
    	JSONObject paramJsonObject = new JSONObject();
    	paramJsonObject.put("comStr", "setMailbox");
    	
    	JSONObject paramJsonObject2 = new JSONObject();
    	paramJsonObject2.put("userID", chiefMailBoxEntity.getUserIdString());//用户名id
    	paramJsonObject2.put("RealName", chiefMailBoxEntity.getRealName());//真实姓名
    	paramJsonObject2.put("mobile", chiefMailBoxEntity.getMobile());//电话信息
    	paramJsonObject2.put("Type", chiefMailBoxEntity.getType());//类型，0检察长，1副检察长 （暂定）
    	paramJsonObject2.put("IsOpen", chiefMailBoxEntity.getIsOpen());//是否公开，0否，1是
    	paramJsonObject2.put("Title", chiefMailBoxEntity.getTitle());//信件标题信息
    	paramJsonObject2.put("Description", chiefMailBoxEntity.getDescription());//信件详细内容
    	
    	paramJsonObject.put("info", paramJsonObject2);
    	
    	JSONObject paramOne = new JSONObject();
    	paramOne.put("data", paramJsonObject); 
    	
    	HttpPost mHttpPost = mHttpApi.createHttpPost(fullUrl(""), new BasicNameValuePair("data", paramOne.toString()));
    	return mHttpApi.doHttpResponse(mHttpPost);
    	
//    	HttpGet mHttpGet = mHttpApi.createHttpGet(fullUrl(""),
//    			new BasicNameValuePair("data", paramOne.toString()));
//        return mHttpApi.doHttpResponse(mHttpGet);
    }
    /**
     * 1.0.11.1 检察长信箱公示
     * by 果悦科技
     * */
    protected HttpResponse invokerChiefMailShowHttpResponse() throws ParseException, BaseException, IOException{
    	JSONObject paramJsonObject = new JSONObject();
    	paramJsonObject.put("comStr", "getMailBox");
    	
    	JSONObject paramJsonObject2 = new JSONObject();
    	
    	paramJsonObject.put("info", paramJsonObject2);
    	
    	JSONObject paramOne = new JSONObject();
    	paramOne.put("data", paramJsonObject); 
    	
    	HttpPost mHttpPost = mHttpApi.createHttpPost(fullUrl(""), new BasicNameValuePair("data", paramOne.toString()));
    	return mHttpApi.doHttpResponse(mHttpPost);
    	
//    	HttpGet mHttpGet = mHttpApi.createHttpGet(fullUrl(""),
//    			new BasicNameValuePair("data", paramOne.toString()));
//        return mHttpApi.doHttpResponse(mHttpGet);
    }
    /**
     * 1.0.12 多媒体信息
     * by 果悦科技
     * */
    protected HttpResponse invokerVideoNewsHttpResponse(String pageNumberString) throws ParseException, BaseException, IOException{
    	JSONObject paramJsonObject = new JSONObject();
    	paramJsonObject.put("comStr", "getVedioNews");
    	
    	JSONObject paramJsonObject2 = new JSONObject();
    	paramJsonObject2.put("page", pageNumberString);//页码
    	
    	paramJsonObject.put("info", paramJsonObject2);
    	
    	JSONObject paramOne = new JSONObject();
    	paramOne.put("data", paramJsonObject); 
    	
    	HttpGet mHttpGet = mHttpApi.createHttpGet(fullUrl(""),
    			new BasicNameValuePair("data", paramOne.toString()));
        return mHttpApi.doHttpResponse(mHttpGet);
    }
    /**
     * 1.0.13 查询我的提交
     * by 果悦科技
     * */
    protected HttpResponse invokerMySubmitHttpResponse(String queryTypeString, String userIdString) throws ParseException, BaseException, IOException{
    	JSONObject paramJsonObject = new JSONObject();
    	paramJsonObject.put("comStr", queryTypeString);
    	
    	JSONObject paramJsonObject2 = new JSONObject();
    	paramJsonObject2.put("userID", userIdString);//页码
    	
    	paramJsonObject.put("info", paramJsonObject2);
    	
    	JSONObject paramOne = new JSONObject();
    	paramOne.put("data", paramJsonObject); 
    	
    	HttpGet mHttpGet = mHttpApi.createHttpGet(fullUrl(""),
    			new BasicNameValuePair("data", paramOne.toString()));
        return mHttpApi.doHttpResponse(mHttpGet);
    }
    /**
     * 1.0.14 案件信息查询
     * by 果悦科技
     * */
    protected HttpResponse invokerInfoQueryHttpResponse(String unitString, String tableString,String startTimeString, String endTimeString, String nameString) throws ParseException, BaseException, IOException{
    	JSONObject paramJsonObject = new JSONObject();
    	paramJsonObject.put("comStr", "getCase");
    	
    	JSONObject paramJsonObject2 = new JSONObject();
    	paramJsonObject2.put("unit", unitString);//单位
    	paramJsonObject2.put("table", tableString);//表
    	paramJsonObject2.put("name", nameString);//名字
    	paramJsonObject2.put("startTime", startTimeString);//开始时间
    	paramJsonObject2.put("endTime", endTimeString);//结束时间
    	
    	paramJsonObject.put("info", paramJsonObject2);
    	
    	JSONObject paramOne = new JSONObject();
    	paramOne.put("data", paramJsonObject); 
    	
    	HttpPost mHttpPost = mHttpApi.createHttpPost(fullUrl(""), new BasicNameValuePair("data", paramOne.toString()));
    	return mHttpApi.doHttpResponse(mHttpPost);
    	
//    	HttpGet mHttpGet = mHttpApi.createHttpGet(fullUrl(""),
//    			new BasicNameValuePair("data", paramOne.toString()));
//        return mHttpApi.doHttpResponse(mHttpGet);
    }
    
}
