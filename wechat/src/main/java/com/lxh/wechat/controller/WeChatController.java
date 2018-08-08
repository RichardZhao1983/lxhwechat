package com.lxh.wechat.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lxh.wechat.services.WeChatAPIService;
import com.lxh.wechat.util.HttpClientUtils;

/**
 * Created by i317632 on 2018/8/7.
 */
@RestController
public class WeChatController {

	private final static Logger LOGGER = LoggerFactory.getLogger(WeChatController.class);
	public final static String REDIRECT_URI = "localhost:8082/getweChatCode";

	@Autowired
	WeChatAPIService weChatAPIService;

	@GetMapping("/weChatLogon")
	public void weChatUserlogon(HttpServletResponse response) throws IOException {
    	String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=CORPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&agentid=AGENTID&state=STATE#wechat_redirect";	
		HttpClientUtils.httpGet(url);
    	response.sendRedirect("getweChatCode");
	}

	@GetMapping("/weChatCode")
	public String getWeChatCode(@RequestParam("code") String code) throws IOException {
		return code;
	}

}
