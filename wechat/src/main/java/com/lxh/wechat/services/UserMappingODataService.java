package com.lxh.wechat.services;

import java.util.List;

import com.lxh.wechat.exception.ODataServiceException;
import com.lxh.wechat.model.UserMappingEntity;

public interface UserMappingODataService {
	List<UserMappingEntity> getAllUserMapping() throws ODataServiceException;

	String getSolmanUserIdByWeChatUserId(String wechatUserId) throws ODataServiceException;

	String getBackendMetadata() throws ODataServiceException;
}
