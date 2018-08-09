package com.lxh.wechat.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Configuration
@ConfigurationProperties(prefix = "solmanserver", ignoreUnknownFields = false)
@PropertySource("classpath:config/solmanserver.properties")
@Component
public class SolmanServerProperties {
	private String domain;
	private String odataPath;
	private String solmanUser;
	private String solmanPassword;

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getOdataPath() {
		return odataPath;
	}

	public void setOdataPath(String odataPath) {
		this.odataPath = odataPath;
	}

	public String getSolmanUser() {
		return solmanUser;
	}

	public void setSolmanUser(String solmanUser) {
		this.solmanUser = solmanUser;
	}

	public String getSolmanPassword() {
		return solmanPassword;
	}

	public void setSolmanPassword(String solmanPassword) {
		this.solmanPassword = solmanPassword;
	}

}
