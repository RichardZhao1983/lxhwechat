package com.lxh.wechat.wechatapi;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lxh.wechat.util.Util;
import com.lxh.wechat.wechatapi.model.TokenInfo;
import com.lxh.wechat.wechatapi.model.UserInfo;

@Service
public class WeChatAPIServiceImpl implements WeChatAPIService {
	private static final Logger LOGGER = LoggerFactory.getLogger(WeChatAPIServiceImpl.class);

	@Autowired
	private HttpJsonRequestService httpJsonReqService;

	@Override
	public void ticketUpdated() {

	}

	@Override
	public UserInfo getUserInfo(String code, String corpId)
			throws WeChatAPIInvalidCorpTokenException, WeChatAPIException {
		UserInfo userInfo = null;
		try {
			String uri = "/user/getuserinfo";
			String method = "GET";
			String corpsecret = "dPROB_f1sk0nZPfXOan6rjEMbDTDNld-R3oLjKQ_p_8";
			uri = this.appendURIWithAccessToken(uri, corpId, corpsecret) + "&code=" + code;
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

	public TokenInfo getWeChatAccessToken(String corpId, String corpsecret) throws WeChatAPIException {
		// db get wechat_access_token
		try {
			String uri = "/gettoken";
			String method = "GET";
			JSONObject content = new JSONObject();
			JSONObject result = null;
			content.put("corpid", corpId);
			content.put("corpsecret", corpsecret);
			result = this.httpJsonReqService.requestGetJsonResponse(uri, method, content);
			TokenInfo newTokenInfo = new TokenInfo(result.getString("access_token"), result.getLong("expires_in"));
			return newTokenInfo;
		} catch (Exception e) {
			throw new WeChatAPIException("Exception in getCorpAccessToken", e);
		}

	}

	private String appendURIWithAccessToken(String uri, String corpId, String corpsecret) throws WeChatAPIException {
		return Util.addParameterForURI(uri, "access_token", this.getWeChatAccessToken(corpId, corpsecret).getToken());
	}

}
