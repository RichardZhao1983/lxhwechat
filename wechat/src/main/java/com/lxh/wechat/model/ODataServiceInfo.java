package com.lxh.wechat.model;

import org.apache.olingo.odata2.api.edm.EdmEntityContainer;

public class ODataServiceInfo {
	private String service;
	private String scope;
	private String destination;
	private EdmEntityContainer edmEntityContainer;

	public ODataServiceInfo() {
	}

	public ODataServiceInfo(String service, String scope, String destination) {
		this.service = service;
		this.scope = scope;
		this.destination = destination;
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

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public EdmEntityContainer getEdmEntityContainer() {
		return edmEntityContainer;
	}

	public void setEdmEntityContainer(EdmEntityContainer edmEntityContainer) {
		this.edmEntityContainer = edmEntityContainer;
	}
}
