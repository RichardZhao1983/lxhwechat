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

	@GetMapping("/testOdataService")
	public String testOdataService() throws ODataServiceException {
		String solmanUserId = userMappingODataService.getSolmanUserIdByWeChatUserId("richard");
		JSONObject result = new JSONObject();
		result.put("user_id", solmanUserId);
		return result.toString();
	}

	@GetMapping("/testSolmanToken")
	public String testSolmanToken() throws OAuthException {
		// String scope = "ZLOCAL_TEST_SRV_0001";
		String scope = "AI_CRM_GW_CREATE_INCIDENT_SRV_0001";
		TokenInfo accessToken = solmanOAuthService.getAccessToken("SAP_SUE", scope);
		JSONObject result = new JSONObject();
		result.put("accessToken", accessToken.getToken());
		result.put("expires_in", accessToken.getExpireIn());
		return result.toString();
	}

}
