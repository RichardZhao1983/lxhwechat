package com.lxh.wechat.solmanoa2.test;

public class AccessTokenException extends Exception {

    public AccessTokenException(Exception ex) {
	super(ex);
    }

    public AccessTokenException(String message) {
	super(message);
    }

}
