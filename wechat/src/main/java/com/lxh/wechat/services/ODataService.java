package com.lxh.wechat.services;

import com.lxh.wechat.exception.ODataServiceException;
import com.lxh.wechat.model.ODataSerializable;
import com.lxh.wechat.model.ODataServiceInfo;

public interface ODataService {

	<T extends ODataSerializable> T getEntity(ODataServiceInfo oDataServiceInfo, String id, String entitySetName, Class<T> cl) throws ODataServiceException;

}
