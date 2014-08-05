package com.ssm.ssmshop.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.ssm.ssmshop.entity.StateCode;
import com.ssm.ssmshop.exception.ParseException;
import com.ssm.ssmshop.parser.AbstractParser;

public class StateCodeParser extends AbstractParser<StateCode>{

	private StateCode stateCode = null;
	@Override
	public StateCode parse(JSONObject json) throws ParseException, JSONException {
		// TODO Auto-generated method stub
		stateCode = new StateCode();
		if (json.has("action")) {
			stateCode.setAction(json.getInt("action"));
		}
		if (json.has("code")) {
			stateCode.setCode(json.getInt("code"));
		}
		if (json.has("data")) {
			stateCode.setData(json.getString("data"));
		}
		
		return stateCode;
	}

}
