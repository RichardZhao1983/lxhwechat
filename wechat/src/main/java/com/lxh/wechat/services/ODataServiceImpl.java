package com.lxh.wechat.services;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
import org.apache.olingo.odata2.api.ep.EntityProvider;
import org.apache.olingo.odata2.api.ep.EntityProviderReadProperties;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.api.ep.feed.ODataFeed;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lxh.wechat.config.SolmanServerProperties;
import com.lxh.wechat.exception.ODataServiceException;
import com.lxh.wechat.exception.ODataServiceWrongPathException;
import com.lxh.wechat.model.ODataSerializable;
import com.lxh.wechat.model.ODataServiceInfo;
import com.lxh.wechat.util.Util;

@Service("oDataService")
public class ODataServiceImpl implements ODataService {

	private final static Logger LOGGER = LoggerFactory.getLogger(ODataServiceImpl.class);
	
	@Autowired
	SolmanServerProperties solmanServerProperties;

	// @Autowired
	// private TenantResourceService tenantResourceService;
	//
	// @Autowired
	// private DestinationService destinationService;

	@Override
	public <T extends ODataSerializable> T getEntity(ODataServiceInfo oDataServiceInfo, String id, String entitySetName,
			Class<T> cl) throws ODataServiceException {
		try {
			String accessToken = null;
			// String accessToken =
			// tenantResourceService.getTenantResource().getSolmanToken(oDataServiceInfo.getScope());
			Map<String, String> header = new HashMap<String, String>();
			header.put("Authorization", "Bearer " + accessToken);

			String uri = "/" + oDataServiceInfo.getService() + "/" + entitySetName + "('" + id + "')?$format=json";

			// String responseString =
			// destinationService.execute(oDataServiceInfo.getDestination(),
			// oDataServiceInfo.getService(), uri, "GET", null, header);
			HttpStatusCodes statusCode = null;
			String responseString = null;
			HttpResponse httpResponse = null;
			// HttpResponse httpResponse =
			// destinationService.request(oDataServiceInfo.getDestination(),
			// oDataServiceInfo.getService(), uri, "GET", null, header);
			statusCode = HttpStatusCodes.fromStatusCode(httpResponse.getStatusLine().getStatusCode());
			responseString = Util.getStringFromInputStream(httpResponse.getEntity().getContent());
			if (statusCode.getStatusCode() >= 400) {
				handleError(statusCode, responseString, uri);
			}
			String content = new JSONObject(responseString).getJSONObject("d").toString();
			ByteArrayInputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));

			ODataEntry entry = EntityProvider.readEntry("application/json",
					oDataServiceInfo.getEdmEntityContainer().getEntitySet(entitySetName), inputStream,
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

	@Override
	public <T extends ODataSerializable> List<T> getEntitySet(ODataServiceInfo oDataServiceInfo, String entitySetName,
			Map<String, String> params, Class<T> cl) throws ODataServiceException {
		try {
			// String accessToken = oAuthService.getAccessToken(SAI_JAVA_USER,
			// oDataServiceInfo.getScope()).getToken();
//			String accessToken = tenantResourceService.getTenantResource().getSolmanToken(oDataServiceInfo.getScope());
			String accessToken="";
			Map<String, String> header = new HashMap<String, String>();
			header.put("Authorization", "Bearer " + accessToken);

			String uri = "/" + oDataServiceInfo.getService() + "/" + entitySetName + "?$format=json";
			if (params != null) {
				for (String key : params.keySet()) {
					String value = params.get(key);
					if (value == null) {
						value = "";
					}
					uri += "&" + key + "='" + params.get(key) + "'";
				}
			}

			HttpStatusCodes statusCode = null;
			String responseString = null;
			// String responseString =
			// destinationService.execute(oDataServiceInfo.getDestination(),
			// oDataServiceInfo.getService(), uri, "GET", null, header);
			HttpResponse httpResponse = null;
//			HttpResponse httpResponse = destinationService.request(oDataServiceInfo.getDestination(),
//					oDataServiceInfo.getService(), uri, "GET", null, header);
			statusCode = HttpStatusCodes.fromStatusCode(httpResponse.getStatusLine().getStatusCode());
			responseString = Util.getStringFromInputStream(httpResponse.getEntity().getContent());
			if (statusCode.getStatusCode() >= 400) {
				handleError(statusCode, responseString, uri);
			}

			String content = new JSONObject(responseString).getJSONObject("d").getJSONArray("results").toString();
			ByteArrayInputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));

			ODataFeed feed = EntityProvider.readFeed("application/json",
					oDataServiceInfo.getEdmEntityContainer().getEntitySet(entitySetName), inputStream,
					EntityProviderReadProperties.init().build());
			List<ODataEntry> entries = feed.getEntries();
			List<T> results = new ArrayList<T>();
			for (ODataEntry entry : entries) {
				T result = cl.newInstance();
				result.fromAttributeMap(entry.getProperties());
				results.add(result);
			}
			return results;
		} catch (ODataServiceException e) {
			throw e;
		} catch (Exception e) {
			LOGGER.error("Exception in getEntitySet", e);
			throw new ODataServiceException(e);
		}
	}

	@Override
	public String getBackendMetadata(ODataServiceInfo oDataServiceInfo) throws ODataServiceException {
		HttpStatusCodes statusCode = null;
		String result = null;
		String uri = null;
		try {
			// String accessToken =
			// tenantResourceService.getTenantResource().getSolmanToken(oDataServiceInfo.getScope());
			String accessToken = "token";
			Map<String, String> header = new HashMap<String, String>();
			header.put("Authorization", "Bearer " + accessToken);

			uri = "/" + oDataServiceInfo.getService() + "/" + "$metadata";
			HttpResponse httpResponse = null;
			// HttpResponse httpResponse =
			// destinationService.request(oDataServiceInfo.getDestination(),
			//		oDataServiceInfo.getService(), uri, "GET", null, header);

			statusCode = HttpStatusCodes.fromStatusCode(httpResponse.getStatusLine().getStatusCode());
			result = Util.getStringFromInputStream(httpResponse.getEntity().getContent());
		} catch (Exception e) {
			LOGGER.error("Exception in getBackendMetadata", e);
			// Omit the "OAuthException" part in the message text
			throw new ODataServiceException(e.getMessage());
		}
		if (!statusCode.equals(HttpStatusCodes.OK)) {
			handleError(statusCode, result, uri);
		}

		LOGGER.debug("getBackendMetadata: {}", result);
		return result;

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
