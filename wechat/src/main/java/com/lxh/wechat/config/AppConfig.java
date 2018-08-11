package com.lxh.wechat.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.lxh.wechat.wechatapi.WeChatAPIException;
import com.lxh.wechat.wechatapi.WeChatAPIService;
import com.lxh.wechat.wechatapi.model.TokenInfo;

/**
 * Created by i317632 on 2018/8/7.
 */
@Component
public class AppConfig implements ApplicationRunner{
	private static final Logger LOGGER = LoggerFactory.getLogger(AppConfig.class);

	public static final int MAP_SIZE = 1;
	public static Map<String, TokenInfo> weChatTokenMap = new ConcurrentHashMap<String, TokenInfo>(MAP_SIZE);
	public static final String APP_ID = "ITSM";
	public static final String CORP_ID = "wx8f5ad82327627866";
	public static final String SCRECT = "dPROB_f1sk0nZPfXOan6rjEMbDTDNld-R3oLjKQ_p_8";

	@Autowired
	private WeChatAPIService weChatAPIService;

	public AppConfig() {
		super();
	}

	private void initWeChatTokenMap() {
		try {
			TokenInfo newToken = weChatAPIService.createNewWeChatAccessToken(CORP_ID, SCRECT);
			LOGGER.info(newToken.getToken());
			weChatTokenMap.put(APP_ID, newToken);
		} catch (WeChatAPIException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		initWeChatTokenMap();
	}
	
	

}
