package com.gov.nzjcy.testutil;


import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gov.nzjcy.BaseActivity;
import com.gov.nzjcy.entity.AskForOpenEntity;
import com.gov.nzjcy.entity.ChiefMailBoxEntity;
import com.gov.nzjcy.entity.OnlineReportEntity;
import com.gov.nzjcy.entity.SetAppealEntity;
import com.gov.nzjcy.exception.BaseException;
import com.gov.nzjcy.exception.ParseException;
import com.gov.nzjcy.helper.GOSHelper;
import com.gov.nzjcy.httpoperation.GosHttpApplication;

import android.os.Bundle;
import android.util.Log;
  

import org.jsoup.Jsoup;  
import org.jsoup.nodes.Document;  
import org.jsoup.nodes.Element;  
import org.jsoup.select.Elements;


public class TestNetPort extends BaseActivity {
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//publishComment("002", "21321", "内容页面");
		new Thread()
		{
			public void run() {
				//askForOpenHttpResponse();
				//queryAskForOpen("601757");
				//queryMultimediaInfo("0");
				//jsonTopNews();
				//jsonHttpWeb();
				//queryOnlineBook("701044");
				//queryOnlineReport("387085");
				//queryAppeal("609151");
				//queryChiefMailBoxShow();
				//queryMyCommit("getPublicItemByUid", "65");
				//queryMyCommit("getReservationByUid", "54");
				//queryMyCommit("getReportByUid", "121");
				//queryMyCommit("getAppealByUid", "302");
				//queryMyCommit("getMailBoxByUid", "303");
				//onlineBookResponse();
			}
		}.start();
		
	}
	/**解析topnews*/
	protected void jsonTopNews() {
		String myString = null;  
        StringBuffer sff = new StringBuffer();//一定要new一个，我刚开始搞忘了，出不来。  
        try  
        {  
            Document doc = Jsoup.connect("http://www.nzjcy.gov.cn/index.asp").get();  
            Element contentElement = doc.getElementById("sina_roll");
            String urlString = contentElement.attr("abs:src");
            Document docOneDocument = Jsoup.connect(urlString).get();
            Element contentElementOne = docOneDocument.getElementById("KinSlideshow");
            Elements links = contentElementOne.select("a[href^=../nzcms_show_news]");  
            //注意这里是Elements不是Element。同理getElementById返回Element，getElementsByClass返回时Elements  
            for(Element link : links){ 
            	String urlString2 = link.select("img").attr("abs:src");
            	String altString = link.select("img").attr("alt");
                //这里没有什么好说的。  
                sff.append(link.attr("abs:href")).append("  ").append(link.text()).append(" ");  
            }  
            myString = sff.toString();  
        }  
        catch (Exception e)  
        {  
            myString = e.getMessage();  
            e.printStackTrace();  
        }
	}
	
	/**解析网页*/
	protected void jsonHttpWeb() {
		String myString = null;  
        StringBuffer sff = new StringBuffer();//一定要new一个，我刚开始搞忘了，出不来。  
        try  
        {  
            Document doc = Jsoup.connect("http://www.nzjcy.gov.cn/nzcms_list_news.asp?id=673&sort_id=658").get();  
            Elements links = doc.select("table.dx");  //^=nzcms_show_news
            //注意这里是Elements不是Element。同理getElementById返回Element，getElementsByClass返回时Elements  
            for(Element link : links){  
            	Elements linkOneElements = link.select("a[href]");
            	Elements linkTwoElements = link.select("[width=140]");
            	String urlString = linkOneElements.attr("abs:href");
            	String titleString = linkOneElements.text();
            	String contentString = linkTwoElements.text().replace("发稿：", "");
                //这里没有什么好说的。  
                sff.append(link.attr("abs:href")).append("  ").append(link.text()).append(" ");  
            }  
            myString = sff.toString();  
        }  
        catch (Exception e)  
        {  
            myString = e.getMessage();  
            e.printStackTrace();  
        }
	}
	
	/**发表评论*/
	protected void publishComment(String newIDString, String userIDString, String contentString) {
		try {
			HttpResponse aString = gosHttpOperation.invokerSendCommentResponseByNewsId(newIDString, userIDString, contentString);
			HttpEntity entity = aString.getEntity();
			InputStream is = entity.getContent();
			String reString = GOSHelper.convertStreamToString(is);
		
			if (reString == null || reString.equals("")) {
				
			}
			else {
				JSONObject jsonObject = JSON.parseObject(reString);
				JSONObject dataObject = jsonObject.getJSONObject("data");
				String stateString = dataObject.getString("state");
				if (stateString.equals("1")) {
//					JSONArray responeArray = dataObject.getJSONArray("responseList");
//					if (responeArray.size() > 0) {
//						for (int i = 0; i < responeArray.size(); i++) {
//							JSONObject oneObject = responeArray.getJSONObject(i);
//							newsCommentCount = oneObject.getString("count").trim();
//						}
//						
//					}
					Log.v("测试发表评论", "发表评论成功");
				}
				else
				{
					Log.v("测试发表评论", "发表评论失败");
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (BaseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**依申请公开项*/
	protected void askForOpenHttpResponse() {
		try {
			AskForOpenEntity askForOpenEntity = new AskForOpenEntity();
			String useridString = GOSHelper.getSharePreStr(TestNetPort.this, GosHttpApplication.USER_ID_STRING);
			askForOpenEntity.setUserID(useridString);
			askForOpenEntity.setRealName("2测试");
			askForOpenEntity.setAddress("河南郑州测试");
			askForOpenEntity.setIDcardNumber("431221312312321321测试");
			askForOpenEntity.setSex("1");
			askForOpenEntity.setEmail("231231@qq.com");
			askForOpenEntity.setMobile("31312312");
			askForOpenEntity.setTel("1312313323");
			askForOpenEntity.setMailingAddress("123123323");
			askForOpenEntity.setConDescription("对犯罪嫌疑人延长挤压期限或者变更强制措施");
			askForOpenEntity.setDescription("wajeoajwope");
			askForOpenEntity.setUsefulDescription("e1ddada");
			askForOpenEntity.setMedium("纸质版");
			askForOpenEntity.setGetMode("自行领取");
			
			
			HttpResponse aString = gosHttpOperation.invokerAskForOpenResponse(askForOpenEntity);
			HttpEntity entity = aString.getEntity();
			InputStream is = entity.getContent();
			String reString = GOSHelper.convertStreamToString(is);
		
			if (reString == null || reString.equals("")) {
				
			}
			else {
				JSONObject jsonObject = JSON.parseObject(reString);
				JSONObject dataObject = jsonObject.getJSONObject("data");
				String stateString = dataObject.getString("state");
				if (stateString.equals("1")) {
					Log.v("测试依申请公开项", "测试依申请公开项成功");
				}
				else
				{
					Log.v("测试依申请公开项", "测试依申请公开项失败");
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (BaseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/** 查询依申请公开项*/
	protected void queryAskForOpen(String numberString) {
		try {			
			HttpResponse aString = gosHttpOperation.invokerQueryAskForOpenResponse(numberString);
			HttpEntity entity = aString.getEntity();
			InputStream is = entity.getContent();
			String reString = GOSHelper.convertStreamToString(is);
		
			if (reString == null || reString.equals("")) {
				
			}
			else {
				JSONObject jsonObject = JSON.parseObject(reString);
				JSONObject dataObject = jsonObject.getJSONObject("data");
				String stateString = dataObject.getString("state");
				if (stateString.equals("1")) {
					Log.v("测试查询申请公开项", "测试查询申请公开项成功");
				}
				else
				{
					Log.v("测试查询申请公开项", "测试查询申请公开项失败");
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (BaseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**在线预约*/
	protected void onlineBookResponse() {
		try {
			AskForOpenEntity askForOpenEntity = new AskForOpenEntity();
			String useridString = GOSHelper.getSharePreStr(TestNetPort.this, GosHttpApplication.USER_ID_STRING);
			askForOpenEntity.setUserID(useridString);
			askForOpenEntity.setRealName("2测试");
			askForOpenEntity.setAddress("河南郑州");
			askForOpenEntity.setIDcardNumber("431221312312321321");
			askForOpenEntity.setSex("1");
			askForOpenEntity.setEmail("231231@qq.com");
			askForOpenEntity.setMobile("31312312");
			askForOpenEntity.setTel("1312313323");
			askForOpenEntity.setMailingAddress("123123323");
			askForOpenEntity.setConDescription("对犯罪嫌疑人延长挤压期限或者变更强制措施");
			askForOpenEntity.setDescription("wajeoajwope");
			askForOpenEntity.setUsefulDescription("e1ddada");
			askForOpenEntity.setMedium("纸质版");
			askForOpenEntity.setGetMode("自行领取");
			
			
			HttpResponse aString = gosHttpOperation.invokerAskForOpenResponse(askForOpenEntity);
			HttpEntity entity = aString.getEntity();
			InputStream is = entity.getContent();
			String reString = GOSHelper.convertStreamToString(is);
		
			if (reString == null || reString.equals("")) {
				
			}
			else {
				JSONObject jsonObject = JSON.parseObject(reString);
				JSONObject dataObject = jsonObject.getJSONObject("data");
				String stateString = dataObject.getString("state");
				if (stateString.equals("1")) {
					Log.v("测试依申请公开项", "测试依申请公开项成功");
				}
				else
				{
					Log.v("测试依申请公开项", "测试依申请公开项失败");
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (BaseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/** 查询预约*/
	protected void queryOnlineBook(String numberString) {
		try {			
			HttpResponse aString = gosHttpOperation.invokerQueryOnlineBookResponse(numberString);
			HttpEntity entity = aString.getEntity();
			InputStream is = entity.getContent();
			String reString = GOSHelper.convertStreamToString(is);
		
			if (reString == null || reString.equals("")) {
				
			}
			else {
				JSONObject jsonObject = JSON.parseObject(reString);
				JSONObject dataObject = jsonObject.getJSONObject("data");
				String stateString = dataObject.getString("state");
				if (stateString.equals("1")) {
					Log.v("测试查询申请公开项", "测试查询申请公开项成功");
				}
				else
				{
					Log.v("测试查询申请公开项", "测试查询申请公开项失败");
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (BaseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/** 查询使用须知*/
	protected void queryGetNotes(String modelNameString) {
		try {			
			HttpResponse aString = gosHttpOperation.invokerQueryUseRulesResponse(modelNameString);
			HttpEntity entity = aString.getEntity();
			InputStream is = entity.getContent();
			String reString = GOSHelper.convertStreamToString(is);
		
			if (reString == null || reString.equals("")) {
				
			}
			else {
				JSONObject jsonObject = JSON.parseObject(reString);
				JSONObject dataObject = jsonObject.getJSONObject("data");
				String stateString = dataObject.getString("state");
				if (stateString.equals("1")) {
					Log.v("测试查询申请公开项", "测试查询申请公开项成功");
				}
				else
				{
					Log.v("测试查询申请公开项", "测试查询申请公开项失败");
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (BaseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**代表建议*/
	protected void representSuggest(String descriptionString) {
		try {	
			String useridString = GOSHelper.getSharePreStr(TestNetPort.this, GosHttpApplication.USER_ID_STRING);
			HttpResponse aString = gosHttpOperation.invokerRepresentSuggestResponse(useridString, descriptionString);
			HttpEntity entity = aString.getEntity();
			InputStream is = entity.getContent();
			String reString = GOSHelper.convertStreamToString(is);
		
			if (reString == null || reString.equals("")) {
				
			}
			else {
				JSONObject jsonObject = JSON.parseObject(reString);
				JSONObject dataObject = jsonObject.getJSONObject("data");
				String stateString = dataObject.getString("state");
				if (stateString.equals("1")) {
					Log.v("测试查询申请公开项", "测试查询申请公开项成功");
				}
				else
				{
					Log.v("测试查询申请公开项", "测试查询申请公开项失败");
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (BaseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**公共信息*/
	protected void publicInformation(String daiBiaoTypeString) {
		try {	
			HttpResponse aString = gosHttpOperation.invokerPublicInformationResponse(daiBiaoTypeString);
			HttpEntity entity = aString.getEntity();
			InputStream is = entity.getContent();
			String reString = GOSHelper.convertStreamToString(is);
		
			if (reString == null || reString.equals("")) {
				
			}
			else {
				JSONObject jsonObject = JSON.parseObject(reString);
				JSONObject dataObject = jsonObject.getJSONObject("data");
				String stateString = dataObject.getString("state");
				if (stateString.equals("1")) {
					Log.v("测试查询申请公开项", "测试查询申请公开项成功");
				}
				else
				{
					Log.v("测试查询申请公开项", "测试查询申请公开项失败");
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (BaseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**设置网上举报*/
	protected void setOnlineReport() {
		try {	
			
			OnlineReportEntity onlineReportEntity = new OnlineReportEntity();
			String useridString = GOSHelper.getSharePreStr(TestNetPort.this, GosHttpApplication.USER_ID_STRING);
			onlineReportEntity.setUserIdString(useridString);
			onlineReportEntity.setOnlineReportUserName("zhangsan测试");
			onlineReportEntity.setOnlineReportIDCardNumber("2323232323232");
			onlineReportEntity.setOnlineReportMobile("13131313131");
			onlineReportEntity.setOnlineReportTel("21212112");
			onlineReportEntity.setOnlineReportAddress("河南郑州");
			onlineReportEntity.setOnlineReportEmail("2312323@qq.com");
			onlineReportEntity.setBJB_Person_Type("1");
			onlineReportEntity.setBJB_Person_Realname("王三");
			onlineReportEntity.setBJB_Person_Sex("1");
			onlineReportEntity.setBJB_Person_CompanyNameOrBumen("部门");
			onlineReportEntity.setBJB_Person_Zhiji("13");
			onlineReportEntity.setBJB_Person_Zhiwu("职务信息");
			onlineReportEntity.setBJB_Person_Address("住址信息");
			onlineReportEntity.setBJB_Person_Anyou("案由的详细信息");
			onlineReportEntity.setBJB_Description("举报内容信息");
			
			HttpResponse aString = gosHttpOperation.invokerSetReportResponse(onlineReportEntity);
			HttpEntity entity = aString.getEntity();
			InputStream is = entity.getContent();
			String reString = GOSHelper.convertStreamToString(is);
		
			if (reString == null || reString.equals("")) {
				
			}
			else {
				JSONObject jsonObject = JSON.parseObject(reString);
				JSONObject dataObject = jsonObject.getJSONObject("data");
				String stateString = dataObject.getString("state");
				if (stateString.equals("1")) {
					Log.v("测试查询申请公开项", "测试查询申请公开项成功");
				}
				else
				{
					Log.v("测试查询申请公开项", "测试查询申请公开项失败");
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (BaseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**查询网上举报*/
	protected void queryOnlineReport(String numberString) {
		try {			
			HttpResponse aString = gosHttpOperation.invokerQueryOnlineReportResponse(numberString);
			HttpEntity entity = aString.getEntity();
			InputStream is = entity.getContent();
			String reString = GOSHelper.convertStreamToString(is);
		
			if (reString == null || reString.equals("")) {
				
			}
			else {
				JSONObject jsonObject = JSON.parseObject(reString);
				JSONObject dataObject = jsonObject.getJSONObject("data");
				String stateString = dataObject.getString("state");
				if (stateString.equals("1")) {
					Log.v("测试查询申请公开项", "测试查询申请公开项成功");
				}
				else
				{
					Log.v("测试查询申请公开项", "测试查询申请公开项失败");
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (BaseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**设置申诉*/
	protected void setAppeal() {
		try {	
			SetAppealEntity setAppealEntity = new SetAppealEntity();
			
			String useridString = GOSHelper.getSharePreStr(TestNetPort.this, GosHttpApplication.USER_ID_STRING);
			setAppealEntity.setUserIdString(useridString);
			setAppealEntity.setSetAppealUserName("1122测试");
			setAppealEntity.setSetAppealBirthday("2014-11-02");
			setAppealEntity.setSetAppealSex("1");
			setAppealEntity.setSetAppealJiGuan("河南郑州");
			setAppealEntity.setCompany("工作单位");
			setAppealEntity.setMinzu("汉");
			setAppealEntity.setAddress("河南郑州");
			setAppealEntity.setMobile("13334343434");
			setAppealEntity.setIDCardNumber("4313131314234412421");
			setAppealEntity.setEmail("2312312@qq.com");
			setAppealEntity.setAnjianType("民事诉讼");
			setAppealEntity.setDescription("申诉详细信息");
			
			
			HttpResponse aString = gosHttpOperation.invokerSetAppealResponse(setAppealEntity);
			HttpEntity entity = aString.getEntity();
			InputStream is = entity.getContent();
			String reString = GOSHelper.convertStreamToString(is);
		
			if (reString == null || reString.equals("")) {
				
			}
			else {
				JSONObject jsonObject = JSON.parseObject(reString);
				JSONObject dataObject = jsonObject.getJSONObject("data");
				String stateString = dataObject.getString("state");
				if (stateString.equals("1")) {
					Log.v("测试查询申请公开项", "测试查询申请公开项成功");
				}
				else
				{
					Log.v("测试查询申请公开项", "测试查询申请公开项失败");
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (BaseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**查询申诉*/
	protected void queryAppeal(String numberString) {
		try {			
			HttpResponse aString = gosHttpOperation.invokerQueryAppealResponse(numberString);
			HttpEntity entity = aString.getEntity();
			InputStream is = entity.getContent();
			String reString = GOSHelper.convertStreamToString(is);
		
			if (reString == null || reString.equals("")) {
				
			}
			else {
				JSONObject jsonObject = JSON.parseObject(reString);
				JSONObject dataObject = jsonObject.getJSONObject("data");
				String stateString = dataObject.getString("state");
				if (stateString.equals("1")) {
					Log.v("测试查询申请公开项", "测试查询申请公开项成功");
				}
				else
				{
					Log.v("测试查询申请公开项", "测试查询申请公开项失败");
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (BaseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**检察长信箱*/
	protected void setChiefMailBox() {
		try {	
			
			ChiefMailBoxEntity chiefMailBoxEntity = new ChiefMailBoxEntity();
			
			String useridString = GOSHelper.getSharePreStr(TestNetPort.this, GosHttpApplication.USER_ID_STRING);
			chiefMailBoxEntity.setUserIdString(useridString);
			chiefMailBoxEntity.setRealName("zhangsan测试");
			chiefMailBoxEntity.setMobile("131232133");
			chiefMailBoxEntity.setType("0");
			chiefMailBoxEntity.setIsOpen("0");
			chiefMailBoxEntity.setTitle("信件标题信息");
			chiefMailBoxEntity.setTitle("信件详细内容");
			
			HttpResponse aString = gosHttpOperation.invokerChiefMailBoxResponse(chiefMailBoxEntity);
			HttpEntity entity = aString.getEntity();
			InputStream is = entity.getContent();
			String reString = GOSHelper.convertStreamToString(is);
		
			if (reString == null || reString.equals("")) {
				
			}
			else {
				JSONObject jsonObject = JSON.parseObject(reString);
				JSONObject dataObject = jsonObject.getJSONObject("data");
				String stateString = dataObject.getString("state");
				if (stateString.equals("1")) {
					Log.v("测试查询申请公开项", "测试查询申请公开项成功");
				}
				else
				{
					Log.v("测试查询申请公开项", "测试查询申请公开项失败");
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (BaseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**检察长信箱公示*/
	protected void queryChiefMailBoxShow() {
		try {			
			HttpResponse aString = gosHttpOperation.invokerChiefMailShowResponse();
			HttpEntity entity = aString.getEntity();
			InputStream is = entity.getContent();
			String reString = GOSHelper.convertStreamToString(is);
		
			if (reString == null || reString.equals("")) {
				
			}
			else {
				JSONObject jsonObject = JSON.parseObject(reString);
				String stateString = jsonObject.getString("state");
				if (stateString.equals("1")) {
					Log.v("测试查询申请公开项", "测试查询申请公开项成功");
					
				}
				else
				{
					Log.v("测试查询申请公开项", "测试查询申请公开项失败");
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (BaseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**多媒体信息*/
	protected void queryMultimediaInfo(String pageNumberString) {
		try {			
			HttpResponse aString = gosHttpOperation.invokerVideoNewsResponse(pageNumberString);
			HttpEntity entity = aString.getEntity();
			InputStream is = entity.getContent();
			String reString = GOSHelper.convertStreamToString(is);
		
			if (reString == null || reString.equals("")) {
				
			}
			else {
				JSONObject jsonObject = JSON.parseObject(reString);
				JSONObject dataObject = jsonObject.getJSONObject("data");
				String stateString = dataObject.getString("state");
				if (stateString.equals("1")) {
					Log.v("测试查询申请公开项", "测试查询申请公开项成功");
				}
				else
				{
					Log.v("测试查询申请公开项", "测试查询申请公开项失败");
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (BaseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**查询我的提交*/
	protected void queryMyCommit(String queryTypeString, String userIdString) {
		try {			
			HttpResponse aString = gosHttpOperation.invokerMySubmitResponse(queryTypeString, userIdString);
			HttpEntity entity = aString.getEntity();
			InputStream is = entity.getContent();
			String reString = GOSHelper.convertStreamToString(is);
		
			if (reString == null || reString.equals("")) {
				
			}
			else {
				JSONObject jsonObject = JSON.parseObject(reString);
				JSONObject dataObject = jsonObject.getJSONObject("data");
				String stateString = dataObject.getString("state");
				if (stateString.equals("1")) {
					Log.v("测试查询申请公开项", "测试查询申请公开项成功");
				}
				else
				{
					Log.v("测试查询申请公开项", "测试查询申请公开项失败");
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (BaseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}