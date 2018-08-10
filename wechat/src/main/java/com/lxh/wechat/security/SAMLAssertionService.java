package com.lxh.wechat.security;

import com.lxh.wechat.exception.OAuthException;

public interface SAMLAssertionService {
	String createAssertion(String solmanEndUserId) throws OAuthException;
}
