package com.ssm.songshangmen.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ssm.songshangmen.entity.BaseResponse;
import com.ssm.songshangmen.entity.GoodsType;
import com.ssm.songshangmen.exception.ParseException;

public class GoodsTypeParser extends AbstractParser<BaseResponse<GoodsType>> {
	BaseResponse<GoodsType> baseResponse = null;
	@Override
	public BaseResponse<GoodsType> parse(JSONObject json) throws ParseException,
			JSONException {
		// TODO Auto-generated method stub
		baseResponse = new BaseResponse<GoodsType>();
		if (json.has("action")) {
			baseResponse.setAction(json.getInt("action"));
		}
		if (json.has("code")) {
			baseResponse.setCode(json.getInt("code"));
		}
		if (json.has("data")) {
			JSONArray array = json.getJSONArray("data");
			List<GoodsType> goodsTypesList = new ArrayList<GoodsType>();
			for(int i=0;i<array.length();i++){
				GoodsType goodsType = new GoodsType();
				JSONObject obj = array.getJSONObject(i);
				if(obj.has("id")){
					goodsType.setId(obj.getInt("id"));
				}
				if(obj.has("name")){
					goodsType.setName(obj.getString("name"));
				}
				
				goodsTypesList.add(goodsType);
			}
			baseResponse.setData(goodsTypesList);
		}
		return baseResponse;
	}

}
