package com.lxh.wechat.services;


import com.lxh.wechat.model.UserInfo;



/**
 * The interface to send API request and get response result to WeChat server.
 * The service only serves one WeChat suite, so we don't consider the complexity
 * of managing tokens for multiple suite
 */
public interface WeChatAPIService {
	static int ERROR_INVALID_ACCESS_TOKEN = 40014;
	static int ERROR_ACCESS_TOKEN_EXPIRED = 42001;
	static int ERROR_INVALID_PERMANENT_CODE = 40084;

	// TODO change to an observer
	void ticketUpdated();

	/**
	 * WeChat User Identification
	 * 
	 * @param code
	 * @return
	 * @throws WeChatAPIException
	 */
	UserInfo getUserInfo(String code, String corpId) throws WeChatAPIException;




}
