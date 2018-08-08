package com.lxh.wechat.model;

import java.util.Date;


public class TokenInfo {

	private String token;
	private long expireTimeStamp;
	private long expireIn;

	/**
	 * Constructor, transform the expireIn to expireTimeStamp
	 * 
	 * @param token
	 *            String representation
	 * @param expireIn
	 *            (seconds)
	 */
	public TokenInfo(String token, long expireIn) {
		this.token = token;
		this.expireIn = expireIn;
		this.expireTimeStamp = new Date().getTime() + expireIn * 1000;
	}

	public boolean expired() {
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
