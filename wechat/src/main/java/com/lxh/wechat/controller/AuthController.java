package com.lxh.wechat.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.lxh.wechat.exception.ODataServiceException;
import com.lxh.wechat.services.UserMappingODataService;
import com.lxh.wechat.solmanoa2.test.LocalSamlTokenFactory;
import com.lxh.wechat.solmanoa2.test.OAuth2SAML2AccessToken;
import com.lxh.wechat.util.HttpClientUtils;
import com.lxh.wechat.wechatapi.WeChatAPIException;
import com.lxh.wechat.wechatapi.WeChatAPIService;
import com.lxh.wechat.wechatapi.model.UserInfo;

@RestController
public class AuthController {

	private final static Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
	static Properties configurationProperties;
	static LocalSamlTokenFactory localSAMLTokenFactory;

	@Autowired
	private WeChatAPIService weChatAPIService;

	@Autowired
	private UserMappingODataService userMappingODataService;

	public AuthController() {
		// initialization saml properties
		configurationProperties = new Properties();
		try {
			System.out.println(getClass());
			InputStream in = getClass().getResourceAsStream("/saml.properties");
			configurationProperties.load(in);
			localSAMLTokenFactory = (LocalSamlTokenFactory) LocalSamlTokenFactory.getInstance(configurationProperties);
			LOGGER.info(String.valueOf(localSAMLTokenFactory));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@GetMapping("/solmanAccessToken")
	public String getSolmanAccessToken(@RequestParam("code") String code, HttpServletResponse response)
			throws IOException {
		// @RequestParam("corpId") String corpId, @RequestParam("code") String
		// @RequestParam("scope") String scope,
		String corpId = "wx8f5ad82327627866";
		String scope = "snsapi_base";
		String screct = "dPROB_f1sk0nZPfXOan6rjEMbDTDNld-R3oLjKQ_p_8";
		String agentId = "11";

		// getWechatCode
		// getWechatAccess
		// getwechatUserInfo
		// getSolmanUserIdByWeChatUserId
		// oAuthService.getAccessToken

		try {

			UserInfo userInfo = weChatAPIService.getUserInfo(code, corpId);
//			String solmanUserId = userMappingODataService.getSolmanUserIdByWeChatUserId("richard");
			// TokenInfo accessToken = oAuthService.getAccessToken(solmanUserId,
			// scope);

			JSONObject result = new JSONObject();
//			result.put("user_id", solmanUserId);
			result.put("wechat_user", userInfo.getUserId());
			// result.put("expires_in", accessToken.getExpireIn());
			return result.toString();
		} catch (WeChatAPIException e) {
			LOGGER.error("WeChatAPIException in getAccesToken", e);
			response.setStatus(403);
			return "Failed to authenticate WeChat user";
		} 
//		catch (ODataServiceException e) {
//			LOGGER.error("WeChatAPIException in ODataServiceException", e);
//			response.setStatus(403);
//			return "Failed to find a Solman user for this WeChat user";
//		}

	}

	@GetMapping("/getAccessTokenByCode")
	public String getAccessTokenByCode() throws IOException {
		// @RequestParam("corpId") String corpId, @RequestParam("code") String
		// code,
		// @RequestParam("scope") String scope,
		// getWechatCode
		// getWechatAccess
		// getwechatUserInfo
		// getSolmanUserIdByWeChatUserId
		// oAuthService.getAccessToken

		// UserInfo userInfo = weChatAPIService.getUserInfo(code, corpId);
		// String solmanUserId =
		// userMappingODataService.getSolmanUserIdByWeChatUserId(userInfo.getUserId());
		// TokenInfo accessToken = oAuthService.getAccessToken(solmanUserId,
		// scope);
		JSONObject result = new JSONObject();

		// result.put("user_id", solmanUserId);
		result.put("access_token", "test_access_token");
		// result.put("expires_in", accessToken.getExpireIn());
		return result.toString();

	}

	@RequestMapping("/testHttpService")
	public String testService() throws IOException {
		JSONObject result = HttpClientUtils.httpGet(
				"http://baike.baidu.com/api/openapi/BaikeLemmaCardApi?scope=103&format=json&appid=379020&bk_key=关键字&bk_length=600");
		return result.toJSONString();
	}

	public static void main(String[] args) {
		AuthController test = new AuthController();
		try {
			testGetAT2();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void testGetAT2() throws Exception {
		try {
			OAuth2SAML2AccessToken atf = new OAuth2SAML2AccessToken(localSAMLTokenFactory);
			configurationProperties.remove("saml_nameid");
			configurationProperties.setProperty("saml_nameid", "SAP_RICHARD");
			String at = atf.getAccessToken(configurationProperties, "ZLOCAL_TEST_SRV_0001");
			System.out.println(at);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
