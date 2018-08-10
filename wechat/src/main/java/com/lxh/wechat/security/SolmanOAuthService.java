package com.lxh.wechat.security;

import com.lxh.wechat.exception.OAuthException;
import com.lxh.wechat.wechatapi.model.TokenInfo;

public interface SolmanOAuthService {

	TokenInfo getAccessToken(String solmanEndUserId, String scope) throws OAuthException;

}
