package com.lxh.wechat.security;

import java.security.KeyStore;

import com.lxh.wechat.exception.WeChatIntegrationException;

public interface MainTenantKeyStoreService {
	/**
	 * @return The keyStore in the main tenant
	 * @throws Exception
	 */
	KeyStore getKeyStore() throws WeChatIntegrationException;
}
