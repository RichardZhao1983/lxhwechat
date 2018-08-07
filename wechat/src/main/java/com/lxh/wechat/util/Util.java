package com.lxh.wechat.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.codehaus.jackson.JsonParseException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class Util {
	public static String getStringFromInputStream(InputStream is) throws IOException {
		if (is == null) {
			return null;
		}

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		int dataElement;
		while ((dataElement = is.read()) != -1) {
			bos.write(dataElement);
		}
		byte[] inData = bos.toByteArray();
		return new String(inData, StandardCharsets.UTF_8);
	}

	public static String getStringFromInputStream(InputStream is, Charset charSet) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		int dataElement;
		while ((dataElement = is.read()) != -1) {
			bos.write(dataElement);
		}
		byte[] inData = bos.toByteArray();
		return new String(inData, charSet);
	}

	public static InputStream getInputStreamFromString(String str) {
		return new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8));
	}

	public static InputStream getInputStreamFromString(String str, Charset charSet) throws UnsupportedEncodingException {
		return new ByteArrayInputStream(str.getBytes(charSet));
	}

	public static String getContentFromHttpRequest(HttpServletRequest request) throws IOException {
		BufferedReader reader = request.getReader();
		StringBuffer sb = new StringBuffer();
		String tmp;
		while ((tmp = reader.readLine()) != null) {
			sb.append(tmp);
		}
		return sb.toString();
	}

	public static Element getXMLRootFromString(String xmlString) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		StringReader sr = new StringReader(xmlString);
		InputSource is = new InputSource(sr);
		Document document = db.parse(is);
		return document.getDocumentElement();
	}

	public static <T> T getObjectFromJSONString(String jsonString, Class<T> c) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return (T) mapper.readValue(jsonString, c);
	}

	public static String getStringFromJSONObject(Object obj) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(obj);
	}

	public static String addParameterForURI(String uri, String name, String value) {
		if (!uri.contains("?")) {
			uri += "?";
		} else {
			uri += "&";
		}
		return uri + name + "=" + value;
	}
}
