package com.ssm.ssmshop.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ssm.ssmshop.entity.Auth;
import com.ssm.ssmshop.entity.BaseResponse;
import com.ssm.ssmshop.exception.ParseException;

public class LoginUpAndInParser extends AbstractParser<BaseResponse<Auth>> {
	BaseResponse<Auth> baseResponse = null;
	@Override
	public BaseResponse<Auth> parse(JSONObject json) throws ParseException,
			JSONException {
		// TODO Auto-generated method stub
		baseResponse = new BaseResponse<Auth>();
		if (json.has("action")) {
			baseResponse.setAction(json.getInt("action"));
		}
		if (json.has("code")) {
			baseResponse.setCode(json.getInt("code"));
		}
		if (json.has("data")) {
			JSONArray array = json.getJSONArray("data");
			List<Auth> authList = new ArrayList<Auth>();
			for(int i=0;i<array.length();i++){
				Auth auth = new Auth();
				JSONObject obj = array.getJSONObject(i);
				if(obj.has("roleId")){
					auth.setRoleId(obj.getString("roleId"));
				}
				if(obj.has("username")){
					auth.setUsername(obj.getString("username"));
				}
				
				authList.add(auth);
			}
			baseResponse.setData(authList);
		}
		return baseResponse;
	}

}

