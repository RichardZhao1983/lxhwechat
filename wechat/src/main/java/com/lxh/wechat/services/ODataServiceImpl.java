package com.lxh.wechat.services;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
import org.apache.olingo.odata2.api.ep.EntityProvider;
import org.apache.olingo.odata2.api.ep.EntityProviderReadProperties;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.lxh.wechat.exception.ODataServiceException;
import com.lxh.wechat.exception.ODataServiceWrongPathException;
import com.lxh.wechat.model.ODataSerializable;
import com.lxh.wechat.model.ODataServiceInfo;
import com.lxh.wechat.util.Util;



@Service
public class ODataServiceImpl implements ODataService {

	private final static Logger LOGGER = LoggerFactory.getLogger(ODataServiceImpl.class);

//	@Autowired
//	private TenantResourceService tenantResourceService;
//
//	@Autowired
//	private DestinationService destinationService;


	
	@Override
	public <T extends ODataSerializable> T getEntity(ODataServiceInfo oDataServiceInfo, String id, String entitySetName, Class<T> cl)
			throws ODataServiceException {
		try {
			String accessToken = null;
//			String accessToken = tenantResourceService.getTenantResource().getSolmanToken(oDataServiceInfo.getScope());
			Map<String, String> header = new HashMap<String, String>();
			header.put("Authorization", "Bearer " + accessToken);

			String uri = "/" + oDataServiceInfo.getService() + "/" + entitySetName + "('" + id + "')?$format=json";

			// String responseString =
			// destinationService.execute(oDataServiceInfo.getDestination(),
			// oDataServiceInfo.getService(), uri, "GET", null, header);
			HttpStatusCodes statusCode = null;
			String responseString = null;
			HttpResponse httpResponse = null;
//			HttpResponse httpResponse = destinationService.request(oDataServiceInfo.getDestination(), oDataServiceInfo.getService(), uri, "GET", null, header);
			statusCode = HttpStatusCodes.fromStatusCode(httpResponse.getStatusLine().getStatusCode());
			responseString = Util.getStringFromInputStream(httpResponse.getEntity().getContent());
			if (statusCode.getStatusCode() >= 400) {
				handleError(statusCode, responseString, uri);
			}
			String content = new JSONObject(responseString).getJSONObject("d").toString();
			ByteArrayInputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));

			ODataEntry entry = EntityProvider.readEntry("application/json", oDataServiceInfo.getEdmEntityContainer().getEntitySet(entitySetName), inputStream,
					EntityProviderReadProperties.init().build());
			T result = cl.newInstance();
			result.fromAttributeMap(entry.getProperties());
			return result;
		} catch (ODataServiceException e) {
			throw e;
		} catch (Exception e) {
			LOGGER.error("Exception in functionImport", e);
			throw new ODataServiceException(e);
		}
	}


	private void handleError(HttpStatusCodes statusCode, String response, String uri) throws ODataServiceException {
		LOGGER.error("handleError, response code is {} ", statusCode);
		if (response == null || response.isEmpty()) {
			throw new ODataServiceException(statusCode.toString());
		}
		if (response != null && response.contains("Service cannot be reached")) {
			throw new ODataServiceWrongPathException(uri);
		}

		throw new ODataServiceException(response);
	}

}
