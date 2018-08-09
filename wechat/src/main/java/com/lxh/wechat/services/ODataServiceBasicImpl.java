package com.lxh.wechat.services;

import static org.opensaml.xml.util.Base64.encodeBytes;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
import org.apache.olingo.odata2.api.ep.EntityProvider;
import org.apache.olingo.odata2.api.ep.EntityProviderReadProperties;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import com.lxh.wechat.config.SolmanServerProperties;
import com.lxh.wechat.exception.ODataServiceException;
import com.lxh.wechat.exception.ODataServiceWrongPathException;
import com.lxh.wechat.model.ODataSerializable;
import com.lxh.wechat.model.ODataServiceInfo;
import com.lxh.wechat.util.HttpClientUtils;
import com.lxh.wechat.util.Util;

@EnableConfigurationProperties(SolmanServerProperties.class)
@Service("oDataBasicService")
public class ODataServiceBasicImpl implements ODataService {

	private final static Logger LOGGER = LoggerFactory.getLogger(ODataServiceBasicImpl.class);

	@Autowired
	SolmanServerProperties solmanServerProperties;

	@Override
	public <T extends ODataSerializable> T getEntity(ODataServiceInfo oDataServiceInfo, String id, String entitySetName,
			Class<T> cl) throws ODataServiceException {
		try {
			Map<String, String> header = new HashMap<String, String>();
			header.put("Authorization",
					"Basic " + encodeBytes(
							(solmanServerProperties.getSolmanUser() + ":" + solmanServerProperties.getSolmanPassword())
									.getBytes()));
			String corpId = "wx8f5ad82327627866";
			String param = "CorpId='" + corpId + "'," + "WxUser='" + id + "'";
			String url = solmanServerProperties.getDomain() + solmanServerProperties.getOdataPath()
					+ oDataServiceInfo.getService() + "/" + entitySetName + "(" + param + ")?$format=json";
			LOGGER.info(url);
			HttpStatusCodes statusCode = null;
			String responseString = null;
			HttpResponse httpResponse = HttpClientUtils.httpGetResponse(url, header);
			statusCode = HttpStatusCodes.fromStatusCode(httpResponse.getStatusLine().getStatusCode());
			responseString = Util.getStringFromInputStream(httpResponse.getEntity().getContent());
			if (statusCode.getStatusCode() >= 400) {
				handleError(statusCode, responseString, url);
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
		return null;
	}

	@Override
	public String getBackendMetadata(ODataServiceInfo oDataServiceInfo) throws ODataServiceException {
		HttpStatusCodes statusCode = null;
		String result = null;
		String uri = null;
		try {
			Map<String, String> header = new HashMap<String, String>();
			header.put("Authorization",
					"Basic " + encodeBytes(
							(solmanServerProperties.getSolmanUser() + ":" + solmanServerProperties.getSolmanPassword())
									.getBytes()));
			String url = solmanServerProperties.getDomain() + solmanServerProperties.getOdataPath()
					+ oDataServiceInfo.getService() + "/" + "$metadata";
			HttpResponse httpResponse = HttpClientUtils.httpGetResponse(url, header);
			statusCode = HttpStatusCodes.fromStatusCode(httpResponse.getStatusLine().getStatusCode());
			result = Util.getStringFromInputStream(httpResponse.getEntity().getContent());
		} catch (Exception e) {
			LOGGER.error("Exception in getBackendMetadata", e);
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
