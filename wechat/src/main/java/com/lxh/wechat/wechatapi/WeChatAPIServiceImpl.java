package com.lxh.wechat.wechatapi;

import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.lxh.wechat.config.AppConfig;
import com.lxh.wechat.util.HttpClientUtils;
import com.lxh.wechat.util.Util;
import com.lxh.wechat.wechatapi.model.TokenInfo;
import com.lxh.wechat.wechatapi.model.UserInfo;
import com.lxh.wechat.wechatapi.model.WeChatTokenInfo;

@Service
public class WeChatAPIServiceImpl implements WeChatAPIService {
	private static final Logger LOGGER = LoggerFactory.getLogger(WeChatAPIServiceImpl.class);
	public static String BASE_URL = "https://qyapi.weixin.qq.com/cgi-bin";

	@Autowired
	private HttpJsonRequestService httpJsonReqService;

	@Override
	public UserInfo getUserInfo(String code) throws WeChatAPIInvalidCorpTokenException, WeChatAPIException {
		UserInfo userInfo = null;
		try {
			String uri = "/user/getuserinfo";
			String method = "GET";
			uri = this.appendURIWithAccessToken(uri, AppConfig.CORP_ID, AppConfig.SCRECT) + "&code=" + code;
			String response = httpJsonReqService.requestGetStrResponse(uri, method, null);
			userInfo = Util.getObjectFromJSONString(response, UserInfo.class);
		} catch (Exception e) {
			throw new WeChatAPIException("Exception in getUserInfo", e);
		}
		if (userInfo.hasError()) {
			if (Integer.valueOf(userInfo.getErrcode()) == ERROR_INVALID_ACCESS_TOKEN
					|| Integer.valueOf(userInfo.getErrcode()) == ERROR_ACCESS_TOKEN_EXPIRED) {
				throw new WeChatAPIInvalidCorpTokenException();
			}
			throw new WeChatAPIException("Failed to get userInfo: " + userInfo.getErrmsg());
		}
		return userInfo;
	}

	private String appendURIWithAccessToken(String uri, String corpId, String corpsecret) throws WeChatAPIException {
		return Util.addParameterForURI(uri, "access_token", this.getWeChatAccessToken(corpId, corpsecret).getToken());
	}

	@Override
	public WeChatTokenInfo createNewWeChatAccessToken(String corpId, String corpsecret) throws WeChatAPIException {
		// db get wechat_access_token
		try {
			String url = "/gettoken";
			url = BASE_URL + url;
			url = Util.addParameterForURI(url, "corpId", corpId);
			url = Util.addParameterForURI(url, "corpsecret", corpsecret);
			JSONObject result = HttpClientUtils.httpGet(url);
			WeChatTokenInfo newTokenInfo = new WeChatTokenInfo(result.getString("access_token"),
					result.getLong("expires_in"));
			return newTokenInfo;
		} catch (Exception e) {
			throw new WeChatAPIException("Exception in getCorpAccessToken", e);
		}

	}

	@Override
	public WeChatTokenInfo getWeChatAccessToken(String corpId, String corpsecret) throws WeChatAPIException {
		Map<String, WeChatTokenInfo> tempMap = AppConfig.weChatTokenMap;
		WeChatTokenInfo token = null;
		if (MapUtils.isEmpty(tempMap)) {
			token = this.createNewWeChatAccessToken(corpId, corpsecret);
			tempMap.put(AppConfig.APP_ID, token);
		} else {
			token = tempMap.get(AppConfig.APP_ID);
			if (token.isExpired()) {
				token = this.createNewWeChatAccessToken(corpId, corpsecret);
				tempMap.put(AppConfig.APP_ID, token);
			}
		}
		return token;
	}

	@Override
	public boolean isWeChatAccessTokenExpired() {
		Map<String, WeChatTokenInfo> tempMap = AppConfig.weChatTokenMap;
		if (MapUtils.isEmpty(tempMap)) {
			return true;
		}

		if (tempMap.get(AppConfig.APP_ID).isExpired()) {
			return true;
		} else {
			return false;
		}
	}
}
