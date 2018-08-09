package com.lxh.wechat.solmanoa2.trace;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OAuthTracer {
    public final static int XML_TYPE = 1;
    public final static int HTTP_TYPE = 2;
    public final static int TEXT_TYPE = 3;

    static List<OAuthTraceData> traceList = new ArrayList<OAuthTraceData>();
    static int idCounter = 0;

    public static void trace(int type, String description, byte[] data) {
	OAuthTraceData traceData = new OAuthTraceData();
	traceData.type = type;
	traceData.description = description;
	traceData.data = data;
	traceData.dataText = new String(data);
	traceData.creationDate = new Date();
	traceData.id = idCounter++;
	traceList.add(traceData);
    }

    public static void trace(int type, String description, String data) {
	trace(type, description, data.getBytes());
    }

    public static List<OAuthTraceData> getTraceData() {
	return traceList;
    }

    public static OAuthTraceData getDatabyID(int id) {
	for (OAuthTraceData traceData : traceList) {
	    if (traceData.id == id)
		return traceData;
	}
	return null;

    }

    public static void clear() {
	traceList.clear();

    }
}
