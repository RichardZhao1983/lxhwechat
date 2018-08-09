package com.lxh.wechat.services;

import java.util.List;
import java.util.Map;

import com.lxh.wechat.exception.ODataServiceException;
import com.lxh.wechat.model.ODataSerializable;
import com.lxh.wechat.model.ODataServiceInfo;

public interface ODataService {

	<T extends ODataSerializable> T getEntity(ODataServiceInfo oDataServiceInfo, String id, String entitySetName,
			Class<T> cl) throws ODataServiceException;

	<T extends ODataSerializable> List<T> getEntitySet(ODataServiceInfo oDataServiceInfo, String entitySetName,
			Map<String, String> params, Class<T> cl) throws ODataServiceException;

	String getBackendMetadata(ODataServiceInfo oDataServiceInfo) throws ODataServiceException;

}
