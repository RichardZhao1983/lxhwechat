package com.lxh.wechat.controller;

import com.lxh.wechat.solmanoa2.LocalSamlTokenFactory;

import com.lxh.wechat.solmanoa2.OAuth2SAML2AccessToken;
import org.junit.Assert;
import org.opensaml.xml.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class AuthController {

	private final static Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
	static Properties configurationProperties;
	static LocalSamlTokenFactory localSAMLTokenFactory;

	public AuthController()  {
		//initialization saml properties
		configurationProperties = new Properties();
		try {
			System.out.println(getClass());
			InputStream in = getClass().getResourceAsStream("/saml.properties");
			configurationProperties.load(in);
			localSAMLTokenFactory = (LocalSamlTokenFactory) LocalSamlTokenFactory.getInstance(configurationProperties);
			LOGGER.info(String.valueOf(localSAMLTokenFactory));
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}



	@GetMapping("/weChatLogon")
	public void weChatUserlogon(HttpServletResponse response) throws IOException {
		//getWechatCode
		//getWechatAccess
		//getwechatUserIdByCode
		//Odata -> getSolmanUID
		//getAccessToken
		response.sendRedirect("index");
	}

	public static void main(String[] args){
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
