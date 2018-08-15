package com.lxh.wechat.wechatapi.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lxh.wechat.model.WeChatAPIResponse;


@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInfo extends WeChatAPIResponse {
    @JsonProperty("UserId")
    private String userId;

    @JsonProperty("OpenId")
    private String openId;

    @JsonProperty("DeviceId")
    private String deviceId;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

}
