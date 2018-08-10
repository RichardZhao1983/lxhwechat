package com.lxh.wechat.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.lxh.wechat.exception.OAuthException;
import com.lxh.wechat.exception.ODataServiceException;
import com.lxh.wechat.security.SolmanOAuthService;
import com.lxh.wechat.services.UserMappingODataService;
import com.lxh.wechat.wechatapi.WeChatAPIException;
import com.lxh.wechat.wechatapi.WeChatAPIService;
import com.lxh.wechat.wechatapi.model.TokenInfo;
import com.lxh.wechat.wechatapi.model.UserInfo;

@RestController
public class AuthController {

	private final static Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private WeChatAPIService weChatAPIService;

	@Autowired
	private UserMappingODataService userMappingODataService;

	@Autowired
	private SolmanOAuthService solmanOAuthService;

	@GetMapping("/solmanAccessToken")
	public String getSolmanAccessToken(@RequestParam("code") String code, HttpServletResponse response)
			throws IOException {
		// @RequestParam("corpId") String corpId, @RequestParam("code") String
		// @RequestParam("scope") String scope,
		String corpId = "wx8f5ad82327627866";
		String scope = "snsapi_base";
		String screct = "dPROB_f1sk0nZPfXOan6rjEMbDTDNld-R3oLjKQ_p_8";
		String agentId = "11";

		try {
			UserInfo userInfo = weChatAPIService.getUserInfo(code, corpId);
			String solmanUserId = userMappingODataService.getSolmanUserIdByWeChatUserId("richard");
			TokenInfo accessToken = solmanOAuthService.getAccessToken("SAP_RICHARD", scope);
			JSONObject result = new JSONObject();
			result.put("user_id", solmanUserId);
			result.put("wechat_user", userInfo.getUserId());
			result.put("accessToken", accessToken.getToken());
			result.put("expires_in", accessToken.getExpireIn());
			return result.toString();
		} catch (WeChatAPIException e) {
			LOGGER.error("WeChatAPIException in getAccesToken", e);
			response.setStatus(403);
			return "Failed to authenticate WeChat user";
		} catch (ODataServiceException e) {
			LOGGER.error("WeChatAPIException in ODataServiceException", e);
			response.setStatus(403);
			return "Failed to find a Solman user for this WeChat user";
		} catch (OAuthException e) {
			LOGGER.error("WeChatAPIException in ODataServiceException", e);
			response.setStatus(403);
			return "Failed to find a Solman user for this WeChat user";
		}

	}

	@GetMapping("/weChatUserInfo")
	public String getWechatUserInfo(@RequestParam("code") String code) throws WeChatAPIException {
		String corpId = "wx8f5ad82327627866";
		UserInfo userInfo = weChatAPIService.getUserInfo(code, corpId);
		JSONObject result = new JSONObject();
		result.put("wechat_user", userInfo.getUserId());
		return result.toString();
	}

	@GetMapping("/testOdataService")
	public String testOdataService() throws ODataServiceException {
		String solmanUserId = userMappingODataService.getSolmanUserIdByWeChatUserId("richard");
		JSONObject result = new JSONObject();
		result.put("user_id", solmanUserId);
		return result.toString();
	}

	@GetMapping("/testSolmanToken")
	public String testSolmanToken() throws OAuthException {
		String scope = "ZLOCAL_TEST_SRV_0001";
		TokenInfo accessToken = solmanOAuthService.getAccessToken("SAP_RICHARD", scope);
		JSONObject result = new JSONObject();
		result.put("accessToken", accessToken.getToken());
		result.put("expires_in", accessToken.getExpireIn());
		return result.toString();
	}

}
