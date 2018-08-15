package com.lxh.wechat.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeChatAPIResponse {
	@JsonProperty("errmsg")
	private String errmsg;

	@JsonProperty("errcode")
	private String errcode = "";

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	public String getErrcode() {
		return errcode;
	}

	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}

	public boolean hasError() {
		if (this.errcode.isEmpty() || this.errcode.equals("0")) {
			return false;
		} else {
			return true;
		}
	}
}
