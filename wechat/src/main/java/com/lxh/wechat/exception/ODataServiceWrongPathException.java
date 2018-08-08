package com.lxh.wechat.exception;

public class ODataServiceWrongPathException extends ODataServiceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -385106789655713818L;

	public ODataServiceWrongPathException(Exception e) {
		super(e);
		// TODO Auto-generated constructor stub
	}

	public ODataServiceWrongPathException(String url) {
		super("URL call was terminated because the corresponding service is not available: " + url);
		// TODO Auto-generated constructor stub
	}

}
