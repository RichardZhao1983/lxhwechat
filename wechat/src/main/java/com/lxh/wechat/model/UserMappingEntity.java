package com.lxh.wechat.model;

import java.util.HashMap;
import java.util.Map;

public class UserMappingEntity implements ODataSerializable {

	private String wechatUserId;
	private String solmanUserId;

	@Override
	public Map<String, Object> getAttributeMap() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("UserSolman", solmanUserId);
		data.put("UserExternal", wechatUserId);
		return data;
	}

	@Override
	public void fromAttributeMap(Map<String, Object> attributes) {
		this.setWechatUserId((String) attributes.get("UserExternal"));
		this.setSolmanUserId((String) attributes.get("UserSolman"));
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

}
