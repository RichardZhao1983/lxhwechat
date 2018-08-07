package com.lxh.wechat.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class AuthController {
	private final static Logger LOGGER = LoggerFactory.getLogger(AuthController.class);


	@GetMapping("/getAccessToken")
	public void getAccessToken(@RequestParam("corpId") String corpId, @RequestParam("code") String code,
                                 @RequestParam("scope") String scope, HttpServletResponse response) throws IOException {
		/*if (corpId == null || corpId.trim().length() == 0 || code == null || code.trim().length() == 0 || scope == null
				|| scope.trim().length() == 0) {
			response.setStatus(403);
			return "Invalid request parameter";
		}*/
		response.sendRedirect("index");
	}

}
