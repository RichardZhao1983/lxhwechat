package com.lxh.wechat.security.impl;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lxh.wechat.config.SamlProperties;
import com.lxh.wechat.exception.OAuthException;
import com.lxh.wechat.security.SAMLAssertionService;
import com.lxh.wechat.security.SolmanOAuthService;
import com.lxh.wechat.wechatapi.model.TokenInfo;

@Service
public class SolmanOAuthServiceImpl implements SolmanOAuthService {
	private final static Logger LOGGER = LoggerFactory.getLogger(SolmanOAuthServiceImpl.class);
	private ExecutorService solmanAccessTokenThreadPool;

	@Autowired
	private SAMLAssertionService samlAssertionService;

	@Autowired
	private SamlProperties samlProperties;

	public SolmanOAuthServiceImpl() {
		super();
		solmanAccessTokenThreadPool = Executors.newFixedThreadPool(5);
	}

	@Override
	public TokenInfo getAccessToken(String solmanEndUserId, String scope) throws OAuthException {
		try {
			Future<TokenInfo> future = solmanAccessTokenThreadPool.submit(
					new SolmanAccessTokenGetProcessor(solmanEndUserId, scope, samlAssertionService, samlProperties));
			return future.get();
		} catch (ExecutionException e) {
			Throwable cause = e.getCause();
			if (cause instanceof OAuthException) {
				throw (OAuthException) cause;
			} else {
				throw new OAuthException((Exception) cause);
			}
		}

		catch (Exception e) {
			throw new OAuthException(e);
		}
	}
}
