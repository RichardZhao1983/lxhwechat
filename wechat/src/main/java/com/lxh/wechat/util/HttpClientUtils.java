package com.lxh.wechat.util;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.httpclient.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

public class HttpClientUtils {
	private static Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

	private static RequestConfig requestConfig = null;

	static {
		requestConfig = RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(2000).build();
	}

	public static JSONObject httpPost(String url, JSONObject jsonParam) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		JSONObject jsonResult = null;
		HttpPost httpPost = new HttpPost(url);
		httpPost.setConfig(requestConfig);
		try {
			if (null != jsonParam) {
				StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");
				entity.setContentEncoding("UTF-8");
				entity.setContentType("application/json");
				httpPost.setEntity(entity);
			}
			CloseableHttpResponse result = httpClient.execute(httpPost);
			if (result.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String str = "";
				try {
					str = EntityUtils.toString(result.getEntity(), "utf-8");
					jsonResult = JSONObject.parseObject(str);
				} catch (Exception e) {
					logger.error("post request fail:" + url, e);
				}
			}
		} catch (IOException e) {
			logger.error("post request fail:" + url, e);
		} finally {
			httpPost.releaseConnection();
		}
		return jsonResult;
	}

	public static JSONObject httpPost(String url, String strParam) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		JSONObject jsonResult = null;
		HttpPost httpPost = new HttpPost(url);
		httpPost.setConfig(requestConfig);
		try {
			if (null != strParam) {
				StringEntity entity = new StringEntity(strParam, "utf-8");
				entity.setContentEncoding("UTF-8");
				entity.setContentType("application/x-www-form-urlencoded");
				httpPost.setEntity(entity);
			}
			CloseableHttpResponse result = httpClient.execute(httpPost);
			if (result.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String str = "";
				try {
					str = EntityUtils.toString(result.getEntity(), "utf-8");
					jsonResult = JSONObject.parseObject(str);
				} catch (Exception e) {
					logger.error("post fail:" + url, e);
				}
			}
		} catch (IOException e) {
			logger.error("post fail:" + url, e);
		} finally {
			httpPost.releaseConnection();
		}
		return jsonResult;
	}

	public static JSONObject httpGet(String url) {
		JSONObject jsonResult = null;
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet request = new HttpGet(url);
		request.setConfig(requestConfig);
		try {
			CloseableHttpResponse response = client.execute(request);

			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				String strResult = EntityUtils.toString(entity, "utf-8");
				jsonResult = JSONObject.parseObject(strResult);
			} else {
				logger.error("get request fail:" + url);
			}
		} catch (IOException e) {
			logger.error("get request fail:" + url, e);
		} finally {
			request.releaseConnection();
		}
		return jsonResult;
	}

	public static HttpResponse httpGetResponse(String url, Map<String, String> headers) {
		CloseableHttpClient client = HttpClients.createDefault();

		HttpGet request = new HttpGet(url);
		if (MapUtils.isNotEmpty(headers)) {
			for (String name : headers.keySet()) {
				request.setHeader(name, headers.get(name));
			}
		}

		request.setConfig(requestConfig);
		CloseableHttpResponse response = null;
		try {
			response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				return response;
			} else {
				logger.error("get request fail:" + url);
			}
		} catch (IOException e) {
			logger.error("get request fail:" + url, e);
		} finally {
			request.releaseConnection();
		}
		return response;
	}

}
