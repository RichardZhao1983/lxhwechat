package com.lxh.wechat.wechatapi.model;

import java.util.Date;

public class WeChatTokenInfo {
	private String token;
	private long expireTimeStamp;
	private long expireIn;

	public WeChatTokenInfo(String token, long expireIn) {
		this.token = token;
		this.expireIn = expireIn;
		this.expireTimeStamp = new Date().getTime() + expireIn * 1000 - 60 * 1000;
	}

	public boolean isExpired() {
		if (this.expireTimeStamp > 0 && new Date().getTime() < this.expireTimeStamp) {
			return false;
		}
		return true;
	}

	public String getToken() {
		return token;
	}

	public long getExpireTimeStamp() {
		return this.expireTimeStamp;
	}

	public long getExpireIn() {
		return this.expireIn;
	}
}
