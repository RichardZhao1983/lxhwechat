package com.lxh.wechat.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.lxh.wechat.exception.ODataServiceException;

@RestController
public class TestController {

	private final static Logger LOGGER = LoggerFactory.getLogger(TestController.class);

	@GetMapping("testHttps")
	public String testOdataService() throws ODataServiceException {
		LOGGER.debug("Hello! debug!");
		LOGGER.info("Hello! info!");
		JSONObject result = new JSONObject();
		result.put("test", "successfully");
		return result.toString();
	}

}
