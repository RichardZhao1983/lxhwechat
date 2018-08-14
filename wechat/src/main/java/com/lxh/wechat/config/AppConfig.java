package com.lxh.wechat.config;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lxh.wechat.wechatapi.WeChatAPIException;
import com.lxh.wechat.wechatapi.WeChatAPIService;
import com.lxh.wechat.wechatapi.model.AppInfo;
import com.lxh.wechat.wechatapi.model.WeChatTokenInfo;

/**
 * Created by i317632 on 2018/8/7.
 */
@Component
public class AppConfig implements ApplicationRunner {
	private static final Logger LOGGER = LoggerFactory.getLogger(AppConfig.class);

	public static final int MAP_SIZE = 6;
	public static Map<String, WeChatTokenInfo> weChatTokenMap = new ConcurrentHashMap<String, WeChatTokenInfo>(
			MAP_SIZE);
	public static Map<String, AppInfo> weChatAppMap = new ConcurrentHashMap<String, AppInfo>(MAP_SIZE);

	private static AppConfig appConfig;

	@Value("classpath:wechatApp_config.json")
	private Resource areaRes;

	@Autowired
	private WeChatAPIService weChatAPIService;

	private AppConfig() {
		super();
	}

	public static AppConfig getInstance() {
		if (appConfig == null) {
			synchronized (AppConfig.class) {
				if (appConfig == null)
					appConfig = new AppConfig();
			}
		}
		return appConfig;

	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		initWeChatAppMap();
		initWeChatTokenMap();
	}

	private void initWeChatAppMap() {
		try {
			String result = IOUtils.toString(areaRes.getInputStream(), "UTF-8");
			JSONObject object = JSON.parseObject(result).getJSONObject("d");
			JSONArray array = JSON.parseObject(result).getJSONObject("d").getJSONArray("results");
			for (Iterator iterator = array.iterator(); iterator.hasNext();) {
				JSONObject jsonObject = (JSONObject) iterator.next();
				AppInfo app = new AppInfo();
				app.setCorpId(object.getString("corpId"));
				app.setAgentId(jsonObject.getString("AgentId"));
				app.setSapAppl(jsonObject.getString("SapAppl"));
				app.setSecret(jsonObject.getString("Secret"));
				weChatAppMap.put(jsonObject.getString("SapAppl"), app);
				LOGGER.debug(app.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initWeChatTokenMap() {
		try {
			for (String key : weChatAppMap.keySet()) {
				AppInfo app = weChatAppMap.get(key);
				WeChatTokenInfo newToken = weChatAPIService.createNewWeChatAccessToken(app.getSapAppl(), app.getSecret());
				this.saveWeChatToken(app.getSapAppl(), newToken);
			}
			
			for (String key : weChatTokenMap.keySet()) {
				LOGGER.info("access_token     "+weChatTokenMap.get(key).getToken());
			}
		} catch (WeChatAPIException e) {
			e.printStackTrace();
		}
	}

	public void saveWeChatToken(String appId, WeChatTokenInfo tokenInfo) {
		weChatTokenMap.put(appId, tokenInfo);
	}

	public WeChatTokenInfo getWeChatToken(String appId) {
		return weChatTokenMap.get(appId);
	}

}
