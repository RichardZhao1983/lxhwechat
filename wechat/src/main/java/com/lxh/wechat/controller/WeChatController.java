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
import com.lxh.wechat.wechatapi.WeChatAPIException;
import com.lxh.wechat.wechatapi.WeChatAPIService;
import com.lxh.wechat.wechatapi.model.UserInfo;

/**
 * Created by i317632 on 2018/8/7.
 */
@RestController
public class WeChatController {

	private final static Logger LOGGER = LoggerFactory.getLogger(WeChatController.class);

	@Autowired
	WeChatAPIService weChatAPIService;

	@GetMapping("/weChatUserInfo")
	public String getSolmanAccessToken(@RequestParam("code") String code, @RequestParam("sapAppl") String sapAppl,
			HttpServletResponse response) throws IOException {
		try {
			UserInfo userInfo = weChatAPIService.getUserInfo(code, sapAppl);
			JSONObject result = new JSONObject();
			result.put("wechat_user", userInfo.getUserId());
			return result.toString();
		} catch (WeChatAPIException e) {
			LOGGER.error("WeChatAPIException in getAccesToken", e);
			response.setStatus(403);
			return "Failed to authenticate WeChat user";
		}
	}
}
