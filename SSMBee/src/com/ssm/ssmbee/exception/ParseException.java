package com.ssm.ssmbee.exception;

public class ParseException extends BaseException {

	private static final long serialVersionUID = 1L;

	public ParseException(String message) {
        super(message);
    }

	public ParseException(int code, String message) {
        super(message);
    }
	
}
