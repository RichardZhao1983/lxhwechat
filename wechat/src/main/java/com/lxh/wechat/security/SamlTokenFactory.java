package com.lxh.wechat.security;

import java.util.Properties;

import com.lxh.wechat.exception.SAMLException;


public interface SamlTokenFactory {

    public abstract String getSamlAssertion(Properties cfgProperties) throws SAMLException;

}