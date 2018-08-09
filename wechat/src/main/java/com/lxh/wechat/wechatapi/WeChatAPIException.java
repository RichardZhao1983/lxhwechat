package com.lxh.wechat.wechatapi;


import com.lxh.wechat.exception.WeChatIntegrationException;

public class WeChatAPIException extends WeChatIntegrationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5808524800893463766L;

	public WeChatAPIException(Exception e) {
		super(e);
	}

	public WeChatAPIException(String s) {
		super(s);
	}

	public WeChatAPIException(String s, Exception e) {
		super(s, e);
	}
}
