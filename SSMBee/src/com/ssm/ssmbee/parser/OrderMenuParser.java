package com.ssm.ssmbee.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ssm.ssmbee.entity.BaseResponse;
import com.ssm.ssmbee.entity.OrderMenu;
import com.ssm.ssmbee.exception.ParseException;

public class OrderMenuParser extends AbstractParser<BaseResponse<OrderMenu>> {
	
	BaseResponse<OrderMenu> baseResponse = null;
	@Override
	public BaseResponse<OrderMenu> parse(JSONObject json)
			throws ParseException, JSONException {
		// TODO Auto-generated method stub
		baseResponse = new BaseResponse<OrderMenu>();
		if (json.has("action")) {
			baseResponse.setAction(json.getInt("action"));
		}
		if (json.has("code")) {
			baseResponse.setCode(json.getInt("code"));
		}
		if (json.has("data")) {
			JSONArray array = json.getJSONArray("data");
			List<OrderMenu> orderMenuList = new ArrayList<OrderMenu>();
			for(int i=0;i<array.length();i++){
				OrderMenu orderMenu = new OrderMenu();
				JSONObject obj = array.getJSONObject(i);
				if(obj.has("id")){
					orderMenu.setOrderId(obj.getString("id"));
				}
				if(obj.has("shopName")){
					orderMenu.setShopName(obj.getString("shopName"));
				}
				if(obj.has("shopAddress")){
					orderMenu.setShopAddress(obj.getString("shopAddress"));
				}
				if(obj.has("shopPhone")){
					orderMenu.setShopPhone(obj.getString("shopPhone"));
				}
				if(obj.has("userName")){
					orderMenu.setBuyName(obj.getString("userName"));
				}
				if(obj.has("userAddress")){
					orderMenu.setBuyAddress(obj.getString("userAddress"));
				}
				if(obj.has("userPhone")){
					orderMenu.setBuyPhone(obj.getString("userPhone"));
				}
				if(obj.has("date")){
					orderMenu.setDateString(obj.getString("date"));
				}
				
				orderMenuList.add(orderMenu);
			}
			baseResponse.setData(orderMenuList);
		}
		return baseResponse;
	}
	
}
