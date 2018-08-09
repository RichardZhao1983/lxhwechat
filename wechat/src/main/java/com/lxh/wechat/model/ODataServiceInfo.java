package com.lxh.wechat.model;

import org.apache.olingo.odata2.api.edm.EdmEntityContainer;


public class ODataServiceInfo {
	private String service;
	private String scope;
	private EdmEntityContainer edmEntityContainer;

	public ODataServiceInfo() {
	}

	public ODataServiceInfo(String service, String scope) {
		this.service = service;
		this.scope = scope;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public EdmEntityContainer getEdmEntityContainer() {
		return edmEntityContainer;
	}

	public void setEdmEntityContainer(EdmEntityContainer edmEntityContainer) {
		this.edmEntityContainer = edmEntityContainer;
	}
}
