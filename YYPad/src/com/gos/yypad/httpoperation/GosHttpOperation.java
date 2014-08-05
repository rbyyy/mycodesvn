package com.gos.yypad.httpoperation;

import java.io.IOException;

import org.apache.http.HttpResponse;

import android.content.Context;
import android.preference.PreferenceManager;

import com.gos.yypad.SettingActivity;
import com.gos.yypad.entity.ChooseOrder;
import com.gos.yypad.entity.Group;
import com.gos.yypad.exception.BaseException;
import com.gos.yypad.exception.ParseException;
import com.gos.yypad.helper.GOSHelper;


public class GosHttpOperation {
	public static final String API_DOMAIN = "yykj.zzfeidu.com/interface/getInterface.aspx";//"59.188.87.54:8012/interface/getInterface.aspx"; //"yykj.zzfeidu.com/interface/getInterface.aspx"

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
     *1.0.0 获得店面分区，设置店面所在地
     *by 果悦科技 
     * */
    public HttpResponse invokerShopsList(String dateString) throws ParseException, BaseException, IOException{
    	return apiInvoker.invokerShopsList(dateString);
    }
    
    /**
     * 1.0.1 首页展示图片、公司展示、促销展示图
     * by 果悦科技
     * */
    public HttpResponse invokerShowPictureList(String dateString, String areaCodeString, String moduleCodeString) throws ParseException, BaseException, IOException{
    	return apiInvoker.invokerShowPictureList(dateString, areaCodeString, moduleCodeString);
    }
    
    /**
     * 1.0.2 产品展示（DIY专区 ，整机专区，精品专区）
     * by 果悦科技
     * */
    public HttpResponse invokerProductShowList(String dateString, String areaCodeString, String pageLargeString, String pageNumString, String moduleCodeString, 
    		String inModuleClassCodeString, String lastClassCodeString ) throws ParseException, BaseException, IOException{
    	return apiInvoker.invokerProductShowList(dateString, areaCodeString, pageLargeString, pageNumString, moduleCodeString, inModuleClassCodeString, lastClassCodeString);
    }

    /**
     * 1.0.3 订单上传 上报订单信息
     * by 果悦科技
     * */
    public HttpResponse invokerReportOrderInfo( String dateString, String areaCodeString, String codeString, 
    		String shopperString, String salesManString, String customerNameString, String customerPhoneString, String orderAmountString, String orderRemarkString, 
    		String addDateString, String payDateString,String orderStatusString, String orderIDsString, String zengsString,Group<ChooseOrder> chooseOrderGroup) throws ParseException, BaseException, IOException{
    	return apiInvoker.invokerReportOrderInfo(dateString, areaCodeString, codeString, shopperString, salesManString, customerNameString, 
    			customerPhoneString, orderAmountString, orderRemarkString, addDateString, payDateString, orderStatusString, 
    			orderIDsString, zengsString, chooseOrderGroup);
    }
    
    /**
     * 1.0.4 获得产品的类型字典
     * by 果悦科技
     * */
    public HttpResponse invokerProductTypeList(String dateString, String areaCodeString) throws ParseException, BaseException, IOException{
    	return apiInvoker.invokerProductTypeList(dateString, areaCodeString);
    }
    
    /**
     * 1.0.5 获得某店面下得所有产品列表
     * by 果悦科技
     * */
    public HttpResponse invokerAllProductDataList(String dateString, String areaCodeString, String pageLargeString, String pageNumString) throws ParseException, BaseException, IOException{
    	return apiInvoker.invokerAllProductDataList(dateString, areaCodeString, pageLargeString, pageNumString);
    }
    
}
