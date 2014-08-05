package com.ssm.songshangmen.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ssm.songshangmen.entity.BaseResponse;
import com.ssm.songshangmen.entity.Goods;
import com.ssm.songshangmen.entity.GoodsType;
import com.ssm.songshangmen.exception.ParseException;

public class GoodsParser extends AbstractParser<BaseResponse<Goods>> {
	BaseResponse<Goods> baseResponse = null;
	@Override
	public BaseResponse<Goods> parse(JSONObject json) throws ParseException,
			JSONException {
		// TODO Auto-generated method stub
		baseResponse = new BaseResponse<Goods>();
		if (json.has("action")) {
			baseResponse.setAction(json.getInt("action"));
		}
		if (json.has("code")) {
			baseResponse.setCode(json.getInt("code"));
		}
		if (json.has("data")) {
			JSONArray oneArray = json.getJSONArray("data");
			JSONObject oneJsonObject = oneArray.getJSONObject(0);
			if (oneJsonObject.has("totalPages")) {
				baseResponse.setTotalPage(oneJsonObject.getInt("totalPages"));
			}
			JSONArray array = oneJsonObject.getJSONArray("list");
			List<Goods> goodsList = new ArrayList<Goods>();
			for(int i=0;i<array.length();i++){
				Goods goods = new Goods();
				JSONObject obj = array.getJSONObject(i);
				if(obj.has("id")){
					goods.setId(obj.getInt("id"));
				}
				if(obj.has("name")){
					goods.setName(obj.getString("name"));
				}
				if (obj.has("type")) {
					goods.setType(obj.getInt("type"));
				}
				if (obj.has("price")) {
					goods.setPrice(obj.getDouble("price"));
				}
				if (obj.has("salePrice")) {
					goods.setSalePrice(obj.getDouble("salePrice"));
				}
				if (obj.has("picture")) {
					goods.setPicture(obj.getString("picture"));
				}
				if (obj.has("description")) {
					goods.setDescription(obj.getString("description"));
				}
				if (obj.has("shopId")) {
					goods.setShopId(obj.getString("shopId"));
				}
				
				goodsList.add(goods);
			}
			baseResponse.setData(goodsList);
		}
		return baseResponse;
	}

}
