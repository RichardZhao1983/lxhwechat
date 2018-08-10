package com.lxh.wechat.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Configuration
@ConfigurationProperties
@PropertySource("classpath:saml.properties")
@Component
public class SamlProperties {
	private String ks_resource;
	private String ks_type;
	private String ks_pwd;
	private String ks_alias;

	private String saml_audience_restriction;
	private String saml_issuer;
	private String saml_session_authentication;
	private String oa2_client_id;

	private String oa2_client_secret;
	private String oa2_token_endpoint;

	public String getKs_resource() {
		return ks_resource;
	}

	public void setKs_resource(String ks_resource) {
		this.ks_resource = ks_resource;
	}

	public String getKs_type() {
		return ks_type;
	}

	public void setKs_type(String ks_type) {
		this.ks_type = ks_type;
	}

	public String getKs_pwd() {
		return ks_pwd;
	}

	public void setKs_pwd(String ks_pwd) {
		this.ks_pwd = ks_pwd;
	}

	public String getKs_alias() {
		return ks_alias;
	}

	public void setKs_alias(String ks_alias) {
		this.ks_alias = ks_alias;
	}

	public String getSaml_audience_restriction() {
		return saml_audience_restriction;
	}

	public void setSaml_audience_restriction(String saml_audience_restriction) {
		this.saml_audience_restriction = saml_audience_restriction;
	}

	public String getSaml_issuer() {
		return saml_issuer;
	}

	public void setSaml_issuer(String saml_issuer) {
		this.saml_issuer = saml_issuer;
	}

	public String getSaml_session_authentication() {
		return saml_session_authentication;
	}

	public void setSaml_session_authentication(String saml_session_authentication) {
		this.saml_session_authentication = saml_session_authentication;
	}

	public String getOa2_client_id() {
		return oa2_client_id;
	}

	public void setOa2_client_id(String oa2_client_id) {
		this.oa2_client_id = oa2_client_id;
	}

	public String getOa2_client_secret() {
		return oa2_client_secret;
	}

	public void setOa2_client_secret(String oa2_client_secret) {
		this.oa2_client_secret = oa2_client_secret;
	}

	public String getOa2_token_endpoint() {
		return oa2_token_endpoint;
	}

	public void setOa2_token_endpoint(String oa2_token_endpoint) {
		this.oa2_token_endpoint = oa2_token_endpoint;
	}

}
