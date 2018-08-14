package com.lxh.wechat.cronjob;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.lxh.wechat.config.AppConfig;
import com.lxh.wechat.wechatapi.WeChatAPIException;
import com.lxh.wechat.wechatapi.WeChatAPIService;

@Component
public class WechatTokenRefreshJob {
	private static final Logger LOGGER = LoggerFactory.getLogger(WechatTokenRefreshJob.class);

	@Autowired
	private WeChatAPIService weChatAPIService;

	@Scheduled(cron = "0 0/1 * * * ?")
	public void refreshWechatTokenJob() {
		for (String appId : AppConfig.weChatTokenMap.keySet()) {
			refreshWechatToken(appId);
		}
	}

	public void refreshWechatToken(String appId) {
		if (weChatAPIService.isWeChatAccessTokenExpired(appId)) {
			try {
				String screct = AppConfig.weChatAppMap.get(appId).getSecret();
				weChatAPIService.createNewWeChatAccessToken(appId, screct);
				LOGGER.debug("wechat access_token has been refresh successful at "
						+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis())));
			} catch (WeChatAPIException e) {
				e.printStackTrace();
			}
		} else {
			LOGGER.info("wechat access_token is not expire");
		}
	}

}
