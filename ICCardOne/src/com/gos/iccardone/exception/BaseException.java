package com.gos.iccardone.exception;

public class BaseException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public BaseException(String message) {
		super(message);
	}

	public BaseException(int code, String message) {
		super(message);
	}

}
