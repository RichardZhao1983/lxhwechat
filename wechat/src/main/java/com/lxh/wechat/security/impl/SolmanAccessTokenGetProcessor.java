package com.lxh.wechat.security.impl;

import static org.opensaml.xml.util.Base64.encodeBytes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.Callable;

import org.apache.commons.codec.Charsets;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.lxh.wechat.config.SamlProperties;
import com.lxh.wechat.controller.AuthController;
import com.lxh.wechat.exception.OAuthBackendException;
import com.lxh.wechat.exception.OAuthException;
import com.lxh.wechat.security.SAMLAssertionService;
import com.lxh.wechat.security.SamlTokenFactory;
import com.lxh.wechat.wechatapi.model.TokenInfo;

public class SolmanAccessTokenGetProcessor implements Callable<TokenInfo> {
	private final static Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

	private SamlProperties samlProperties;

	private SAMLAssertionService samlAssertionService;

	private String solmanEndUserId;

	private String scope;

	public SolmanAccessTokenGetProcessor(String solmanEndUserId, String scope,
			SAMLAssertionService samlAssertionService, SamlProperties samlProperties) {
		this.solmanEndUserId = solmanEndUserId;
		this.scope = scope;
		this.samlAssertionService = samlAssertionService;
		this.samlProperties = samlProperties;
	}

	@Override
	public TokenInfo call() throws Exception {
		String assertionString = samlAssertionService.createAssertion(solmanEndUserId);
		LOGGER.info(assertionString);
		LOGGER.info(samlProperties.getOa2_token_endpoint());
		String b64Data = URLEncoder.encode(encodeBytes(assertionString.getBytes()), Charsets.UTF_8.name());
		
		HttpURLConnection con = (HttpURLConnection) new URL(samlProperties.getOa2_token_endpoint()).openConnection();
		String data = "client_id=" + samlProperties.getOa2_client_id() + "&scope=" + scope
				+ "&grant_type=urn:ietf:params:oauth:grant-type:saml2-bearer&assertion=" + b64Data;
		con.addRequestProperty("Authorization", "Basic " + org.opensaml.xml.util.Base64.encodeBytes(
				(samlProperties.getOa2_client_id() + ":" + samlProperties.getOa2_client_secret()).getBytes()));
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setRequestProperty("Cookie", "");
		con.setRequestMethod("POST");
		OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
		wr.write(data);
		wr.flush();

		int respCode = con.getResponseCode();
		byte[] res = readData(con.getInputStream());
		String responseString = new String(res);

		if (respCode != 200) {
			try {
				JSONObject errorObject = new JSONObject(responseString);
				String error = errorObject.getString("error");
				String errorDescription = errorObject.getString("error_description");

				throw new OAuthBackendException(errorDescription, error);
			} catch (JSONException e1) {
				throw new OAuthException(responseString);
			}
		}

		JSONObject tokenJsonObj = new JSONObject(responseString);
		String tokenString = tokenJsonObj.getString("access_token");
		Long expiresIn = tokenJsonObj.getLong("expires_in");
		return new TokenInfo(tokenString, expiresIn);
	}

	private byte[] readData(InputStream is) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		int dataElement;
		while ((dataElement = is.read()) != -1) {
			bos.write(dataElement);
		}
		byte[] inData = bos.toByteArray();
		return inData;
	}

}
