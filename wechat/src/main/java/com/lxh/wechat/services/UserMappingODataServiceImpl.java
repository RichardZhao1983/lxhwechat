package com.lxh.wechat.services;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.annotation.Resource;

import org.apache.olingo.odata2.api.edm.Edm;
import org.apache.olingo.odata2.api.ep.EntityProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lxh.wechat.exception.ODataServiceException;
import com.lxh.wechat.model.ODataServiceInfo;
import com.lxh.wechat.model.UserMappingEntity;

@Service
public class UserMappingODataServiceImpl implements UserMappingODataService {
	public static final String ENTITY_SET = "UserMapSet";

	public static final String DESTINATION = "SOLMAN_ODATA_HTTP";

	public static final String SERVICE1 = "Z_SAI_USER_MAPPING_SERV";

	public static final String SERVICE = "ZWECHAT_SRV";

	public static final String SCOPE = "ZWECHAT_SRV_0001";

	@Resource(name = "oDataBasicService")
	private ODataService oDataBasicService;

	@Resource(name = "oDataService")
	private ODataService oDataService;
	
	private ODataServiceInfo oDataServiceInfo;

	public UserMappingODataServiceImpl() {
		oDataServiceInfo = new ODataServiceInfo(SERVICE, SCOPE);
	}

	private void updateODataServiceInfo() throws ODataServiceException {
		if (oDataServiceInfo.getEdmEntityContainer() != null) {
			return;
		}

		try {
			String metadata = getBackendMetadata();
			Edm edm = EntityProvider.readMetadata(new ByteArrayInputStream(metadata.getBytes(StandardCharsets.UTF_8)),
					false);
			oDataServiceInfo.setEdmEntityContainer(edm.getDefaultEntityContainer());
		} catch (Exception e) {
			throw new ODataServiceException(e);
		}
	}

	@Override
	public List<UserMappingEntity> getAllUserMapping() throws ODataServiceException {
		updateODataServiceInfo();
		return oDataService.getEntitySet(oDataServiceInfo, ENTITY_SET, null, UserMappingEntity.class);
	}

	@Override
	public String getSolmanUserIdByWeChatUserId(String wechatUserId) throws ODataServiceException {
		updateODataServiceInfo();
		return oDataBasicService.getEntity(oDataServiceInfo, wechatUserId, ENTITY_SET, UserMappingEntity.class)
				.getSolmanUserId();
	}

	@Override
	public String getBackendMetadata() throws ODataServiceException {
		return oDataBasicService.getBackendMetadata(oDataServiceInfo);
	}
}
