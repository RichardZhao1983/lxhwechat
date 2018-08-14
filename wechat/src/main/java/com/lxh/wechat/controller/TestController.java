package com.lxh.wechat.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.lxh.wechat.exception.ODataServiceException;

@RestController
public class TestController {

	@GetMapping("testHttps")
	public String testOdataService() throws ODataServiceException {
		JSONObject result = new JSONObject();
		result.put("test", "successfully");
		return result.toString();
	}

}
