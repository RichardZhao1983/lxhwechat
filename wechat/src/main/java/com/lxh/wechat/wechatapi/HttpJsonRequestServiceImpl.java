package com.lxh.wechat.wechatapi;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class HttpJsonRequestServiceImpl implements HttpJsonRequestService {
	private String baseUrl = "https://qyapi.weixin.qq.com/cgi-bin";
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpJsonRequestServiceImpl.class);

	@Override
	public JSONObject requestGetJsonResponse(String uri, String method, JSONObject content) throws Exception {
		String response = requestGetStrResponse(uri, method, content.toString());
		return new JSONObject(response.toString());
	}

	@Override
	public String requestGetStrResponse(String uri, String method, String content) throws Exception {
		HttpURLConnection connection = null;

		String targetURL = baseUrl + uri.toString();

		URL url = new URL(targetURL);
		connection = (HttpURLConnection) url.openConnection();
		LOGGER.trace("calling API: " + targetURL);
		if (!method.equals("GET")) {
			connection.setRequestMethod(method);
			connection.setDoOutput(true);

			if (content != null) {
				DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
				wr.write(content.getBytes(Charset.forName("utf-8")));
				wr.close();
				LOGGER.trace("content is " + content.toString());
			}
		}
		// Send request Get Response
		InputStream is = connection.getInputStream();
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		StringBuilder response = new StringBuilder();

		String line;
		while ((line = rd.readLine()) != null) {
			response.append(line);
			response.append(System.getProperty("line.separator"));
		}
		rd.close();

		if (connection != null) {
			connection.disconnect();
		}
		LOGGER.trace("response is " + response.toString());
		return response.toString();
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	@Override
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
}
