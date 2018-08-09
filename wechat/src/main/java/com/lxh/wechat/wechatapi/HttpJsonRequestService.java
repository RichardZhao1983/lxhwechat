package com.lxh.wechat.wechatapi;

import org.json.JSONObject;


public interface HttpJsonRequestService {
	/**
	 * Make a HTTP request to a target on remote server. It gets the result
	 * content only in JSON format
	 * 
	 * @param uri
	 *            is the RELATIVE path to the domain url
	 * @param getCSRFToken
	 * @param method
	 *            is HTTP request method
	 * @param content
	 *            HTTP content in the request
	 * @return
	 * @throws Exception
	 */
	public JSONObject requestGetJsonResponse(String uri, String method, JSONObject content) throws Exception;

	public String requestGetStrResponse(String uri, String method, String content) throws Exception;

	void setBaseUrl(String baseUrl);
}
