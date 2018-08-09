package com.lxh.wechat.solmanoa2.test;

import java.util.Properties;


public interface SamlTokenFactory {

    public abstract String getSamlAssertion(Properties cfgProperties) throws SAMLException;

}