package com.ssm.ssmshop.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ssm.ssmshop.entity.BaseResponse;
import com.ssm.ssmshop.entity.Goods;
import com.ssm.ssmshop.entity.GoodsBaseResponse;
import com.ssm.ssmshop.entity.OrderPreview;
import com.ssm.ssmshop.exception.ParseException;

public class OrderGoodsParser extends AbstractParser<GoodsBaseResponse<Goods>> {
	GoodsBaseResponse<Goods> goodsBaseResponse = null;
	@Override
	public GoodsBaseResponse<Goods> parse(JSONObject json)
			throws ParseException, JSONException {
		// TODO Auto-generated method stub
		goodsBaseResponse = new GoodsBaseResponse<Goods>();
		if (json.has("action")) {
			goodsBaseResponse.setAction(json.getInt("action"));
		}
		if (json.has("code")) {
			goodsBaseResponse.setCode(json.getInt("code"));
		}
		if (json.has("data")) {
			JSONObject oneJsonObject = json.getJSONArray("data").getJSONObject(0);
			if (oneJsonObject.has("id")) {
				goodsBaseResponse.setOrderId(oneJsonObject.getString("id"));
			}
			if (oneJsonObject.has("shopName")) {
				goodsBaseResponse.setShopName(oneJsonObject.getString("shopName"));
			}
			if (oneJsonObject.has("userName")) {
				goodsBaseResponse.setUserName(oneJsonObject.getString("userName"));
			}
			if (oneJsonObject.has("userPhone")) {
				goodsBaseResponse.setUserPhone(oneJsonObject.getString("userPhone"));
			}
			if (oneJsonObject.has("beeName")) {
				goodsBaseResponse.setBeeName(oneJsonObject.getString("beeName"));
			}
			if (oneJsonObject.has("address")) {
				goodsBaseResponse.setUserAddress(oneJsonObject.getString("address"));
			}
			
			JSONArray array = oneJsonObject.getJSONArray("goodsList");
			List<Goods> orderGoodsList = new ArrayList<Goods>();
			for(int i=0;i<array.length();i++){
				Goods goods = new Goods();
				JSONObject obj = array.getJSONObject(i);
				if(obj.has("name")){
					goods.setName(obj.getString("name"));
				}
				if(obj.has("price")){
					goods.setPrice(obj.getString("price"));
				}
				if(obj.has("num")){
					goods.setNumber(obj.getString("num"));
				}
				
				orderGoodsList.add(goods);
			}
			goodsBaseResponse.setData(orderGoodsList);
		}
		return goodsBaseResponse;
	}

}
