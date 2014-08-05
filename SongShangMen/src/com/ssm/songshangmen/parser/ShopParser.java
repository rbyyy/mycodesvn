package com.ssm.songshangmen.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ssm.songshangmen.entity.BaseResponse;
import com.ssm.songshangmen.entity.Shop;
import com.ssm.songshangmen.exception.ParseException;

public class ShopParser extends AbstractParser<BaseResponse<Shop>> {
	BaseResponse<Shop> baseResponse = null;
	@Override
	public BaseResponse<Shop> parse(JSONObject json) throws ParseException,
			JSONException {
		// TODO Auto-generated method stub
		baseResponse = new BaseResponse<Shop>();
		if (json.has("action")) {
			baseResponse.setAction(json.getInt("action"));
		}
		if (json.has("code")) {
			baseResponse.setCode(json.getInt("code"));
		}
		if (json.has("data")) {
			JSONArray array = json.getJSONArray("data");
			List<Shop> shopList = new ArrayList<Shop>();
			for(int i=0;i<array.length();i++){
				Shop shop = new Shop();
				JSONObject obj = array.getJSONObject(i);
				if(obj.has("id")){
					shop.setId(obj.getString("id"));
				}
				if(obj.has("name")){
					shop.setName(obj.getString("name"));
				}
				if(obj.has("address")){
					shop.setAddress(obj.getString("address"));
				}
				if(obj.has("areaId")){
					shop.setAreaId(obj.getInt("areaId"));
				}
				if(obj.has("type")){
					shop.setType(obj.getInt("type"));
				}
				if(obj.has("phone")){
					shop.setPhone(obj.getString("phone"));
				}
				if(obj.has("businessHours")){
					shop.setBusinessHours(obj.getString("businessHours"));
				}
				if(obj.has("openStatus")){
					shop.setOpenStatus(obj.getInt("openStatus"));
				}
				if(obj.has("picture")){
					shop.setPicture(obj.getString("picture"));
				}
				if(obj.has("level")){
					shop.setLevel(obj.getInt("level"));
				}
				if(obj.has("tag")){
					shop.setTag(obj.getString("tag"));
				}
				if(obj.has("activity")){
					shop.setActivity(obj.getString("activity"));
				}
				if (obj.has("sendFee")) {
					shop.setSendFee(obj.getDouble("sendFee"));
				}
				if (obj.has("sendTime")) {
					shop.setSendTime(obj.getString("sendTime"));
				}
				if (obj.has("sendLimit")) {
					shop.setSendLimit(obj.getDouble("sendLimit"));
				}
				if (obj.has("distance")) {
					shop.setDistance(obj.getDouble("distance"));
				}
				if (obj.has("pageView")) {
					shop.setPageView(obj.getInt("pageView"));
				}
				if (obj.has("longitude")) {
					shop.setLongitude(obj.getDouble("longitude"));
				}
				if (obj.has("latitude")) {
					shop.setLatitude(obj.getDouble("latitude"));
				}
				if (obj.has("examine")) {
					shop.setExamine(obj.getInt("examine"));
				}
				if (obj.has("totalOrder")) {
					shop.setTotalOrder(obj.getInt("totalOrder"));
				}
				if (obj.has("notice")) {
					shop.setNotice(obj.getString("notice"));
				}
				shopList.add(shop);
			}
			baseResponse.setData(shopList);
		}
		return baseResponse;
	}

}
