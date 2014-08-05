package com.ssm.ssmshop.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ssm.ssmshop.entity.BaseResponse;
import com.ssm.ssmshop.entity.OrderPreview;
import com.ssm.ssmshop.exception.ParseException;

public class OrderPreviewParser extends AbstractParser<BaseResponse<OrderPreview>> {
	BaseResponse<OrderPreview> baseResponse = null;
	@Override
	public BaseResponse<OrderPreview> parse(JSONObject json)
			throws ParseException, JSONException {
		// TODO Auto-generated method stub
		baseResponse = new BaseResponse<OrderPreview>();
		if (json.has("action")) {
			baseResponse.setAction(json.getInt("action"));
		}
		if (json.has("code")) {
			baseResponse.setCode(json.getInt("code"));
		}
		if (json.has("data")) {
			JSONArray array = json.getJSONArray("data");
			JSONObject dataObject = array.getJSONObject(0);
			JSONArray oneArray = dataObject.getJSONArray("list");
			List<OrderPreview> orderPreviewList = new ArrayList<OrderPreview>();
			for(int i=0;i<oneArray.length();i++){
				OrderPreview orderPreview = new OrderPreview();
				JSONObject obj = oneArray.getJSONObject(i);
				if(obj.has("id")){
					orderPreview.setOrderId(obj.getString("id"));
				}
				if(obj.has("name")){
					orderPreview.setBuyName(obj.getString("name"));
				}
				if(obj.has("address")){
					orderPreview.setBuyAddress(obj.getString("address"));
				}
				if(obj.has("phone")){
					orderPreview.setBuyPhone(obj.getString("phone"));
				}
				if(obj.has("totalPrice")){
					orderPreview.setBuyTotalPrice(obj.getString("totalPrice"));
				}
				
				orderPreviewList.add(orderPreview);
			}
			baseResponse.setData(orderPreviewList);
		}
		return baseResponse;
	}

}
