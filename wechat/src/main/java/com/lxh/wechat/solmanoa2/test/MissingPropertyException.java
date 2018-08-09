package com.lxh.wechat.solmanoa2.test;

public class MissingPropertyException extends Exception {
    public MissingPropertyException(String missingProperty) {
	super("Missing property: " + missingProperty);
    }
}
