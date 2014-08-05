package com.gos.yypad.httpoperation;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.ExecutionContext;

import android.R.string;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gos.yypad.entity.ChooseOrder;
import com.gos.yypad.entity.Group;
import com.gos.yypad.entity.ProductList;
import com.gos.yypad.exception.BaseException;
import com.gos.yypad.http.HttpApi;
import com.gos.yypad.http.AbstractHttpApi;
import com.gos.yypad.http.HttpApiWithBasicAuth;
import com.gos.yypad.parser.Parser;



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
     *1.0.0 获得店面分区，设置店面所在地
     *by 果悦科技 
     * */
    protected HttpResponse invokerShopsList(String dateString) throws ParseException, BaseException, IOException{
    	JSONObject paramJsonObject = new JSONObject();
    	paramJsonObject.put("comStr", "getSaleArea");
    	paramJsonObject.put("timeStamp", dateString);//"2014-03-07 15:14:53.993"时间格式
    	JSONObject paramOne = new JSONObject();
    	paramOne.put("data", paramJsonObject);
    	
    	HttpGet mHttpGet = mHttpApi.createHttpGet(fullUrl(""),
    			new BasicNameValuePair("data", paramOne.toString()));
        return mHttpApi.doHttpResponse(mHttpGet);
    }
    /**
     * 1.0.1 首页展示图片、公司展示、促销展示图
     * by 果悦科技
     * */
    protected HttpResponse invokerShowPictureList(String dateString, String areaCodeString, String moduleCodeString) throws ParseException, BaseException, IOException{
    	JSONObject paramJsonObject = new JSONObject();
    	paramJsonObject.put("comStr", "getIndexImg");
    	paramJsonObject.put("timeStamp", dateString);//"2014-03-07 15:14:53.993" 时间格式
    	
    	JSONObject paramJsonObject2 = new JSONObject();
    	paramJsonObject2.put("areaCode", areaCodeString);//区域ID
    	paramJsonObject2.put("moduleCode", moduleCodeString);//模块ID
    	
    	paramJsonObject.put("myInfoList", paramJsonObject2);
    	
    	JSONObject paramOne = new JSONObject();
    	paramOne.put("data", paramJsonObject);
    	
    	HttpGet mHttpGet = mHttpApi.createHttpGet(fullUrl(""),
    			new BasicNameValuePair("data", paramOne.toString()));
        return mHttpApi.doHttpResponse(mHttpGet);
    }
    /**
     * 1.0.2 产品展示（DIY专区 ，整机专区，精品专区）
     * by 果悦科技
     * */
    protected HttpResponse invokerProductShowList(String dateString, String areaCodeString, String pageLargeString, String pageNumString, 
    		 String moduleCodeString, String inModuleClassCodeString, String lastClassCodeString ) throws ParseException, BaseException, IOException{
    	JSONObject paramJsonObject = new JSONObject();
    	paramJsonObject.put("comStr", "getProductInfo");
    	paramJsonObject.put("timeStamp", dateString);//"2014-03-07 15:14:53.993" 时间格式
    	
    	JSONObject paramJsonObject2 = new JSONObject();
    	paramJsonObject2.put("areaCode", areaCodeString);//区域ID
    	paramJsonObject2.put("pageLarge", pageLargeString);//页面产品数量，限定每页最多多少
    	paramJsonObject2.put("pageNum", pageNumString);//第几页，从0开始
    	paramJsonObject2.put("moduleCode", moduleCodeString);//模块ID，区分是DIY专区，整机专区，精品专区
    	if (!inModuleClassCodeString.equals("") && inModuleClassCodeString != null) {
    		paramJsonObject2.put("inModuleClassCode", inModuleClassCodeString);//用于DIY专区划分电脑配件，如CPU，显卡
		}
    	if (!lastClassCodeString.equals("") && lastClassCodeString != null) {
    		paramJsonObject2.put("lastClassCode", lastClassCodeString);//用于区分产品分类，如CPU的 AMD ，intel
		}
    	paramJsonObject.put("myInfoList", paramJsonObject2);
    	
    	JSONObject paramOne = new JSONObject();
    	paramOne.put("data", paramJsonObject);
    	
    	HttpGet mHttpGet = mHttpApi.createHttpGet(fullUrl(""),
    			new BasicNameValuePair("data", paramOne.toString()));
        return mHttpApi.doHttpResponse(mHttpGet);
    }
    /**
     * 1.0.3 上报订单信息
     * by 果悦科技
     * */
    protected HttpResponse invokerReportOrderInfo(String dateString, String areaCodeString, String codeString, 
    		String shopperString, String salesManString, String customerNameString, String customerPhoneString, String orderAmountString, String orderRemarkString, 
    		String addDateString, String payDateString,String orderStatusString, String orderIDsString, String zengsString, Group<ChooseOrder> chooseOrderGroup) throws ParseException, BaseException, IOException{
    	JSONObject paramJsonObject = new JSONObject();
    	paramJsonObject.put("comStr", "setOderInfo");
    	paramJsonObject.put("timeStamp", dateString);
    	JSONObject paramOne = new JSONObject();
    	paramOne.put("areaCode", areaCodeString);
    	paramOne.put("code", codeString);
    	paramOne.put("shoppers", shopperString);
    	paramOne.put("salesMan", salesManString);
    	paramOne.put("customerName", customerNameString);
    	paramOne.put("customerPhone", customerPhoneString);
    	paramOne.put("orderAmount", orderAmountString);
    	paramOne.put("orderRemark", orderRemarkString);
    	paramOne.put("addDate", addDateString);
    	paramOne.put("payDate", payDateString);
    	paramOne.put("oderStatus", orderStatusString);
    	paramOne.put("zengs", zengsString);

    	JSONArray paramArray = new JSONArray();
    	for (int i = 0; i < chooseOrderGroup.size(); i++) {
    		JSONObject paramTwo = new JSONObject();
        	paramTwo.put("orderID", orderIDsString);
        	paramTwo.put("pID", chooseOrderGroup.get(i).getOrder_id());
        	paramTwo.put("pName", chooseOrderGroup.get(i).getBusiness_name());
        	paramTwo.put("pPrice", chooseOrderGroup.get(i).getBusiness_price());
        	paramTwo.put("pCount", chooseOrderGroup.get(i).getBusiness_number());

        	paramArray.add(paramTwo);
		}
    	
    	paramOne.put("orderDetail", paramArray);
    	
    	paramJsonObject.put("myInfoList", paramOne);
    	
    	JSONObject paramFour = new JSONObject();
    	paramFour.put("data", paramJsonObject);
    	
    	HttpGet mHttpGet = mHttpApi.createHttpGet(fullUrl(""),
    			new BasicNameValuePair("data", paramFour.toString()));
        return mHttpApi.doHttpResponse(mHttpGet);
    }
    /**
     * 1.0.4 获得产品的类型字典
     * by 果悦科技
     * */
    protected HttpResponse invokerProductTypeList(String dateString, String areaCodeString) throws ParseException, BaseException, IOException{
    	JSONObject paramJsonObject = new JSONObject();
    	paramJsonObject.put("comStr", "getProductClass");
    	paramJsonObject.put("timeStamp", dateString);//"2014-03-07 15:14:53.993" 时间格式
    	
    	JSONObject paramJsonObject2 = new JSONObject();
    	paramJsonObject2.put("areaCode", areaCodeString);//区域ID
    	
    	paramJsonObject.put("myInfoList", paramJsonObject2);
    	
    	JSONObject paramOne = new JSONObject();
    	paramOne.put("data", paramJsonObject);
    	
    	HttpGet mHttpGet = mHttpApi.createHttpGet(fullUrl(""),
    			new BasicNameValuePair("data", paramOne.toString()));
        return mHttpApi.doHttpResponse(mHttpGet);
    }
    /**
     * 1.0.5 获得在某个店面下面的全部数据
     * by 果悦科技
     * */
    protected HttpResponse invokerAllProductDataList(String dateString, String areaCodeString, String pageLargeString, String pageNumString) throws ParseException, BaseException, IOException{
    	JSONObject paramJsonObject = new JSONObject();
    	paramJsonObject.put("comStr", "getProductInfoByShop");
    	paramJsonObject.put("timeStamp", dateString);//"2014-03-07 15:14:53.993" 时间格式
    	
    	JSONObject paramJsonObject2 = new JSONObject();
    	paramJsonObject2.put("areaCode", areaCodeString);//区域ID
    	paramJsonObject2.put("pageLarge", pageLargeString);//页面产品数量，限定每页最多多少
    	paramJsonObject2.put("pageNum", pageNumString);//第几页，从0开始
    	
    	paramJsonObject.put("myInfoList", paramJsonObject2);
    	
    	JSONObject paramOne = new JSONObject();
    	paramOne.put("data", paramJsonObject);
    	
    	HttpGet mHttpGet = mHttpApi.createHttpGet(fullUrl(""),
    			new BasicNameValuePair("data", paramOne.toString()));
        return mHttpApi.doHttpResponse(mHttpGet);
    }
    
    
}
