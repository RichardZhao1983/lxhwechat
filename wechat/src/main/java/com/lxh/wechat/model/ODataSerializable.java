package com.lxh.wechat.model;

import java.util.Map;

public interface ODataSerializable {
	public Map<String, Object> getAttributeMap();

	public void fromAttributeMap(Map<String, Object> attributes);

}
