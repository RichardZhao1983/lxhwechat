package com.lxh.wechat.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lxh.wechat.model.UserInfo;
import com.lxh.wechat.util.Util;

@Service
public class WeChatAPIServiceImpl implements WeChatAPIService {
	private static final Logger LOGGER = LoggerFactory.getLogger(WeChatAPIServiceImpl.class);

	@Autowired
	private HttpJsonRequestService httpJsonReqService;

	@Override
	public void ticketUpdated() {

	}

	@Override
	public UserInfo getUserInfo(String code, String corpId) throws WeChatAPIInvalidCorpTokenException, WeChatAPIException {
		UserInfo userInfo = null;
		try {
			String uri = "/user/getuserinfo";
			String method = "GET";
			//uri = this.appendURIWithCorpAccessToken(uri, corpId);
			uri += "&code=" + code;

			String response = httpJsonReqService.requestGetStrResponse(uri, method, null);

			userInfo = Util.getObjectFromJSONString(response, UserInfo.class);
		} catch (Exception e) {
			throw new WeChatAPIException("Exception in getUserInfo", e);
		}
		if (userInfo.hasError()) {
			if (Integer.valueOf(userInfo.getErrcode()) == ERROR_INVALID_ACCESS_TOKEN || Integer.valueOf(userInfo.getErrcode()) == ERROR_ACCESS_TOKEN_EXPIRED) {
				throw new WeChatAPIInvalidCorpTokenException();
			}
			throw new WeChatAPIException("Failed to get userInfo: " + userInfo.getErrmsg());
		}
		return userInfo;
	}

	@Override
	public String getWeChatCode() {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
//	public TokenInfo getCorpAccessToken(String corpId, boolean force) throws WeChatAPIException {
//		TokenInfo tokenInfo = this.corpTokenInfoList.get(corpId);
//		if (!force && tokenInfo != null && !tokenInfo.expired()) {
//			return tokenInfo;
//		}
//		try {
//			String uri = "/service/get_corp_token";
//			String method = "POST";
//
//			uri = appendURIWithSuiteAccessToken(uri);
//			JSONObject content = new JSONObject();
//
//			String permanentCode = suiteCorpAuthInfoRepository.findOne(corpId).getPermanentCode();
//
//			content.put("suite_id", suiteConfigRepository.findOne(SuiteConfigRepository.SUITE_ID).getValue());
//			content.put("auth_corpid", corpId);
//			content.put("permanent_code", permanentCode);
//
//			JSONObject result = null;
//
//			result = this.httpJsonReqService.requestGetJsonResponse(uri, method, content);
//
//			TokenInfo newTokenInfo = new TokenInfo(result.getString("access_token"), result.getLong("expires_in"));
//			this.corpTokenInfoList.put(corpId, newTokenInfo);
//			return newTokenInfo;
//		} catch (Exception e) {
//			throw new WeChatAPIException("Exception in getCorpAccessToken", e);
//		}
//	}
//
//	public TokenInfo getCorpAccessToken(String corpId) throws WeChatAPIException {
//		return getCorpAccessToken(corpId, false);
//	}
//
//	private String appendURIWithSuiteAccessToken(String uri) throws WeChatAPIException {
//		return Util.addParameterForURI(uri, "suite_access_token", this.getSuiteAccessToken().getToken());
//	}
//
//	private String appendURIWithCorpAccessToken(String uri, String corpId) throws WeChatAPIException {
//		return Util.addParameterForURI(uri, "access_token", this.getCorpAccessToken(corpId).getToken());
//	}

}
