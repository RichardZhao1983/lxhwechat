package com.lxh.wechat.config;

import com.lxh.wechat.services.HttpJsonRequestService;
import com.lxh.wechat.services.HttpJsonRequestServiceImpl;
import com.lxh.wechat.services.WeChatAPIService;
import com.lxh.wechat.services.WeChatAPIServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by i317632 on 2018/8/7.
 */
@Configuration
public class Config {

    @Bean
    public HttpJsonRequestService getHttpJsonRequestService(){
        return new HttpJsonRequestServiceImpl();
    }

    @Bean
    public WeChatAPIService getWeChatAPIService(){
        return new WeChatAPIServiceImpl();
    }

}
