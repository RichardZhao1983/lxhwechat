package com.lxh.wechat.wechatapi.model;

public class AppInfo {

	private String corpId;
	private String agentId;
	private String Secret;
	private String sapAppl;

	public String getCorpId() {
		return corpId;
	}

	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getSecret() {
		return Secret;
	}

	public void setSecret(String secret) {
		Secret = secret;
	}

	public String getSapAppl() {
		return sapAppl;
	}

	public void setSapAppl(String sapAppl) {
		this.sapAppl = sapAppl;
	}

	@Override
	public String toString() {
		return this.corpId + " " + this.sapAppl + " " + this.Secret + " " + this.agentId;
	}

}
