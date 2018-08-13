package com.lxh.wechat.wechatapi;

import com.lxh.wechat.wechatapi.model.UserInfo;
import com.lxh.wechat.wechatapi.model.WeChatTokenInfo;

/**
 * The interface to send API request and get response result to WeChat server.
 * The service only serves one WeChat suite, so we don't consider the complexity
 * of managing tokens for multiple suite
 */
public interface WeChatAPIService {
	static int ERROR_INVALID_ACCESS_TOKEN = 40014;
	static int ERROR_ACCESS_TOKEN_EXPIRED = 42001;
	static int ERROR_INVALID_PERMANENT_CODE = 40084;

	/**
	 * WeChat User Identification
	 * 
	 * @param code
	 * @return
	 * @throws WeChatAPIException
	 */
	UserInfo getUserInfo(String code) throws WeChatAPIException;

	public WeChatTokenInfo createNewWeChatAccessToken(String corpId, String corpsecret) throws WeChatAPIException;

	public WeChatTokenInfo getWeChatAccessToken(String corpId, String corpsecret) throws WeChatAPIException;

	public boolean isWeChatAccessTokenExpired();

}
