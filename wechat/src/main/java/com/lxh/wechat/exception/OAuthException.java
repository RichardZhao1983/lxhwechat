package com.lxh.wechat.exception;

public class OAuthException extends WeChatIntegrationException {

	private static final long serialVersionUID = -2134339666231389307L;

	public OAuthException(Exception e) {
		super(e);
	}

	public OAuthException(String s) {
		super(s);
	}

	public OAuthException(String s, Exception e) {
		super(s, e);
	}
}
