package com.lxh.wechat.exception;

public class OAuthBackendException extends OAuthException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8535087911562694306L;

	private String error;

	public OAuthBackendException(Exception e) {
		super(e);
		// TODO Auto-generated constructor stub
	}

	public OAuthBackendException(String error_description, String error) {
		super(error_description);
		this.error = error;
		// TODO Auto-generated constructor stub
	}

	public String getError() {
		return error;
	}
}
