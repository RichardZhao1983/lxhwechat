package com.lxh.wechat.model;

import java.util.HashMap;
import java.util.Map;

public class UserMappingEntity implements ODataSerializable {

	private String wechatUserId;
	private String solmanUserId;
	private String solmanUserName;
	private String corpId;

	@Override
	public Map<String, Object> getAttributeMap() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("SapUser", solmanUserId);
		data.put("WxUser", wechatUserId);
		data.put("SapUserName", solmanUserName);
		data.put("CorpId", corpId);
		return data;
	}

	@Override
	public void fromAttributeMap(Map<String, Object> attributes) {
		this.setWechatUserId((String) attributes.get("WxUser"));
		this.setSolmanUserId((String) attributes.get("SapUser"));
		this.setSolmanUserName((String) attributes.get("SapUserName"));
		this.setCorpId((String) attributes.get("CorpId"));
	}

	public String getWechatUserId() {
		return wechatUserId;
	}

	public void setWechatUserId(String wechatUserId) {
		this.wechatUserId = wechatUserId;
	}

	public String getSolmanUserId() {
		return solmanUserId;
	}

	public void setSolmanUserId(String solmanUserId) {
		this.solmanUserId = solmanUserId;
	}

	public String getSolmanUserName() {
		return solmanUserName;
	}

	public void setSolmanUserName(String solmanUserName) {
		this.solmanUserName = solmanUserName;
	}

	public String getCorpId() {
		return corpId;
	}

	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}

}
