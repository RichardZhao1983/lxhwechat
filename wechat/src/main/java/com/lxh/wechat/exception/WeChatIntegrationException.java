package com.lxh.wechat.exception;

public class WeChatIntegrationException extends Exception {

    private static final long serialVersionUID = 481259695635327182L;

    public WeChatIntegrationException(Exception e) {
        super(e);
    }

    public WeChatIntegrationException(String s) {
        super(s);
    }

    public WeChatIntegrationException(String s, Exception e) {
        super(s, e);
    }

}
