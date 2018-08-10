package com.lxh.wechat.security.impl;

import java.security.KeyStore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.lxh.wechat.exception.WeChatIntegrationException;
import com.lxh.wechat.security.MainTenantKeyStoreService;

@Service
public class MainTenantKeyStoreServiceImpl implements MainTenantKeyStoreService {
	public static KeyStore keyStore;

//	@Autowired
//	private KeyStoreService keystoreService;

	@Autowired
	private Environment env;

	@Override
	public KeyStore getKeyStore() throws WeChatIntegrationException {
//		Callable<KeyStore> callable = new Callable<KeyStore>() {
//			@Override
//			public KeyStore call() throws Exception {
//				Context ctx = new InitialContext();
//				TenantContext tenantctx = (TenantContext) ctx.lookup("java:comp/env/TenantContext");
//				return tenantctx.execute(InitServiceImpl.mainTenantId, new Callable<KeyStore>() {
//					@Override
//					public KeyStore call() throws Exception {
//						if (keyStore == null) {
//							keyStore = keystoreService.getKeyStore(env.getProperty(AppConfig.CFG_KEYSTORE_FILE_NAME),
//									env.getProperty(AppConfig.CFG_KEYSTORE_PASSWORD).toCharArray());
//						}
//						return keyStore;
//					}
//				});
//			}
//		};
//
//		try {
//			FutureTask<KeyStore> futureTask = new FutureTask<KeyStore>(callable);
//			new Thread(futureTask).start();
//			return futureTask.get();
//		} catch (Exception e) {
//			throw new WeChatIntegrationException(e);
//		}
		return null;
	}
}
