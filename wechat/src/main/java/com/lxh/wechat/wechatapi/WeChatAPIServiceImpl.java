package com.lxh.wechat.wechatapi;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.lxh.wechat.config.AppConfig;
import com.lxh.wechat.util.HttpClientUtils;
import com.lxh.wechat.util.Util;
import com.lxh.wechat.wechatapi.model.UserInfo;
import com.lxh.wechat.wechatapi.model.WeChatTokenInfo;

@Service
public class WeChatAPIServiceImpl implements WeChatAPIService {
	private static final Logger LOGGER = LoggerFactory.getLogger(WeChatAPIServiceImpl.class);
	public static String BASE_URL = "https://qyapi.weixin.qq.com/cgi-bin";

	@Autowired
	private HttpJsonRequestService httpJsonReqService;

	@Override
	public UserInfo getUserInfo(String code, String appId)
			throws WeChatAPIInvalidCorpTokenException, WeChatAPIException {
		UserInfo userInfo = null;
		try {
			String uri = "/user/getuserinfo";
			String method = "GET";
			String weChatScrect = AppConfig.weChatAppMap.get(appId).getSecret();
			uri = this.appendURIWithAccessToken(uri, appId, weChatScrect) + "&code=" + code;
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
	public WeChatTokenInfo createNewWeChatAccessToken(String appId, String corpsecret) throws WeChatAPIException {
		try {
			String corpId = AppConfig.weChatAppMap.get(appId).getCorpId();
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
	public WeChatTokenInfo getWeChatAccessToken(String appId, String corpsecret) throws WeChatAPIException {
		AppConfig appConfig =  AppConfig.getInstance();
		
		WeChatTokenInfo token = null;
		if (MapUtils.isEmpty(AppConfig.weChatTokenMap)) {
			token = this.createNewWeChatAccessToken(appId, corpsecret);
			appConfig.saveWeChatToken(appId, token);
		} else {
			token = appConfig.getWeChatToken(appId);
			if (token.isExpired()) {
				token = this.createNewWeChatAccessToken(appId, corpsecret);
				appConfig.saveWeChatToken(appId, token);
			}
		}
		return token;
	}

	@Override
	public boolean isWeChatAccessTokenExpired(String appId) {
		AppConfig appConfig =  AppConfig.getInstance();
		if (MapUtils.isEmpty(AppConfig.weChatTokenMap)) {
			return true;
		}

		if (appConfig.getWeChatToken(appId).isExpired()) {
			return true;
		} else {
			return false;
		}
	}

	// public void sendMessage(ResponseMessage message) throws
	// WeChatAPIInvalidCorpTokenException, WeChatAPIException {
	// try {
	// String uri = "/message/send";
	// String method = "POST";
	// WeChatTokenInfo weChatTokenInfo =
	// this.getWeChatAccessToken(AppConfig.CORP_ID, AppConfig.SCRECT);
	// uri = Util.addParameterForURI(uri, "access_token",
	// weChatTokenInfo.getToken());
	//
	// JSONObject result = httpJsonReqService.requestGetJsonResponse(uri,
	// method, message.toJSON());
	// if (result.getIntValue("errcode") != 0) {
	// if (result.getIntValue("errcode") == ERROR_INVALID_ACCESS_TOKEN) {
	// throw new WeChatAPIInvalidCorpTokenException();
	// }
	// throw new WeChatAPIException(result.getString("errmsg"));
	// }
	//
	// } catch (WeChatAPIInvalidCorpTokenException e) {
	// throw e;
	// } catch (Exception e) {
	// throw new WeChatAPIException("Exception in sendMessage", e);
	// }
	// }
}
