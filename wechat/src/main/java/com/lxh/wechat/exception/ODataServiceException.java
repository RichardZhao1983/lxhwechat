package com.lxh.wechat.exception;

public class ODataServiceException extends WeChatIntegrationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5913624374065309776L;

	public ODataServiceException(Exception e) {
		super(e);

	}

	public ODataServiceException(String s) {
		super(s);

	}
}
