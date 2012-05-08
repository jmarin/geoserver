package org.geoserver.rest;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;
import net.sf.json.JSON;
import net.sf.json.JSONObject;

import org.geoserver.catalog.rest.CatalogRESTTestSupport;
import org.geoserver.config.GeoServer;
import org.w3c.dom.Document;

import com.mockrunner.mock.web.MockHttpServletResponse;

public class GlobalSettingsTest extends CatalogRESTTestSupport {

    protected GeoServer geoServer;

    @Override
    protected void oneTimeSetUp() throws Exception {
        super.oneTimeSetUp();
    }

    public void testGetAsJSON() throws Exception {
        JSON json = getAsJSON("/rest/settings.json");
        JSONObject jsonObject = (JSONObject) json;
        assertNotNull(jsonObject);
        JSONObject global = jsonObject.getJSONObject("global");
        assertNotNull(global);
        assertEquals("true", global.get("globalServices").toString().trim());
        assertEquals("1024", global.get("xmlPostRequestLogBufferSize").toString().trim());

        JSONObject settings = global.getJSONObject("settings");
        assertNotNull(settings);
        assertEquals("UTF-8", settings.get("charset"));
        assertEquals("8", settings.get("numDecimals").toString().trim());
        assertEquals("http://geoserver.org", settings.get("onlineResource"));

        JSONObject jaiInfo = global.getJSONObject("jai");
        assertNotNull(jaiInfo);
        assertEquals("false", jaiInfo.get("allowInterpolation").toString().trim());
        assertEquals("0.75", jaiInfo.get("memoryThreshold").toString().trim());
        assertEquals("false", jaiInfo.get("imageIOCache").toString().trim());

        JSONObject covInfo = global.getJSONObject("coverageAccess");
        assertEquals("UNBOUNDED", covInfo.get("queueType"));
    }

    public void testGetAsXML() throws Exception {
        Document dom = getAsDOM("/rest/settings.xml");
        assertEquals("global", dom.getDocumentElement().getLocalName());
        assertXpathEvaluatesTo("true", "/global/globalServices", dom);
        assertXpathEvaluatesTo("1024", "/global/xmlPostRequestLogBufferSize", dom);
        assertXpathEvaluatesTo("UTF-8", "/global/settings/charset", dom);
        assertXpathEvaluatesTo("8", "/global/settings/numDecimals", dom);
        assertXpathEvaluatesTo("http://geoserver.org", "/global/settings/onlineResource", dom);
        assertXpathEvaluatesTo("false", "/global/jai/allowInterpolation", dom);
        assertXpathEvaluatesTo("0.75", "/global/jai/memoryThreshold", dom);
        assertXpathEvaluatesTo("false", "/global/jai/imageIOCache", dom);
        assertXpathEvaluatesTo("UNBOUNDED", "/global/coverageAccess/queueType", dom);

    }

    public void testPutAsJSON() throws Exception {
        String inputJson = "{'global':"
                + "{'settings':"
                + "{'charset':'UTF-8','numDecimals':10,'onlineResource':'http://geoserver2.org','verbose':true,'verboseExceptions':true},"
                + "'jai':{'allowInterpolation':true,'recycling':true,'tilePriority':5,'tileThreads':7,'memoryCapacity':0.5,'memoryThreshold':0.75,"
                + "'imageIOCache':false,'pngAcceleration':true,'jpegAcceleration':true,'allowNativeMosaic':false},"
                + "'coverageAccess':{'maxPoolSize':10,'corePoolSize':5,'keepAliveTime':30000,'queueType':'UNBOUNDED','imageIOCacheThreshold':10240},"
                + "'updateSequence':97,'featureTypeCacheSize':0,'globalServices':true,'xmlPostRequestLogBufferSize':1024}}";
        MockHttpServletResponse response = putAsServletResponse("/rest/settings/", inputJson,
                "text/json");
        assertEquals(200, response.getStatusCode());
        JSON json = getAsJSON("/rest/settings.json");
        JSONObject jsonObject = (JSONObject) json;
        assertNotNull(jsonObject);
        JSONObject global = jsonObject.getJSONObject("global");
        assertNotNull(global);
        assertEquals("true", global.get("globalServices").toString().trim());
        assertEquals("1024", global.get("xmlPostRequestLogBufferSize").toString().trim());

        JSONObject settings = global.getJSONObject("settings");
        assertNotNull(settings);
        assertEquals("UTF-8", settings.get("charset"));
        assertEquals("10", settings.get("numDecimals").toString().trim());
        assertEquals("http://geoserver2.org", settings.get("onlineResource"));

        JSONObject jaiInfo = global.getJSONObject("jai");
        assertNotNull(jaiInfo);
        assertEquals("true", jaiInfo.get("allowInterpolation").toString().trim());
        assertEquals("0.75", jaiInfo.get("memoryThreshold").toString().trim());
        assertEquals("true", jaiInfo.get("recycling").toString().trim());

        JSONObject covInfo = global.getJSONObject("coverageAccess");
        assertEquals("UNBOUNDED", covInfo.get("queueType"));

    }

    public void testPutAsXML() throws Exception {
        String xml = "<global>"
                + "<settings>"
                + "<charset>UTF-8</charset>"
                + "<numDecimals>10</numDecimals>"
                + "<onlineResource>http://geoserver.org</onlineResource>"
                + "<verbose>false</verbose>"
                + "<verboseExceptions>false</verboseExceptions>"
                + "</settings>" + "<jai>" + "<allowInterpolation>true</allowInterpolation>"
                + "<recycling>false</recycling>" + "<tilePriority>5</tilePriority>"
                + "<tileThreads>7</tileThreads>" + "<memoryCapacity>0.5</memoryCapacity>"
                + "<memoryThreshold>0.85</memoryThreshold>" + "<imageIOCache>false</imageIOCache>"
                + "<pngAcceleration>true</pngAcceleration>"
                + "<jpegAcceleration>true</jpegAcceleration>"
                + "<allowNativeMosaic>false</allowNativeMosaic>" + "</jai>" + "<coverageAccess>"
                + "<maxPoolSize>10</maxPoolSize>" + "<corePoolSize>5</corePoolSize>"
                + "<keepAliveTime>30000</keepAliveTime>" + "<queueType>UNBOUNDED</queueType>"
                + "<imageIOCacheThreshold>10240</imageIOCacheThreshold>" + "</coverageAccess>"
                + "<updateSequence>97</updateSequence>"
                + "<featureTypeCacheSize>0</featureTypeCacheSize>"
                + "<globalServices>false</globalServices>"
                + "<xmlPostRequestLogBufferSize>2048</xmlPostRequestLogBufferSize>" + "</global>";

        MockHttpServletResponse response = putAsServletResponse("/rest/settings/", xml, "text/xml");
        assertEquals(200, response.getStatusCode());
        Document dom = getAsDOM("/rest/settings.xml");
        assertEquals("global", dom.getDocumentElement().getLocalName());
        assertXpathEvaluatesTo("false", "/global/globalServices", dom);
        assertXpathEvaluatesTo("2048", "/global/xmlPostRequestLogBufferSize", dom);
        assertXpathEvaluatesTo("UTF-8", "/global/settings/charset", dom);
        assertXpathEvaluatesTo("10", "/global/settings/numDecimals", dom);
        assertXpathEvaluatesTo("http://geoserver.org", "/global/settings/onlineResource", dom);
        assertXpathEvaluatesTo("true", "/global/jai/allowInterpolation", dom);
        assertXpathEvaluatesTo("0.85", "/global/jai/memoryThreshold", dom);
        assertXpathEvaluatesTo("false", "/global/jai/imageIOCache", dom);
        assertXpathEvaluatesTo("UNBOUNDED", "/global/coverageAccess/queueType", dom);
    }

    public void testDelete() throws Exception {
        JSON json = getAsJSON("/rest/settings.json");
        JSONObject jsonObject = (JSONObject) json;
        assertNotNull(jsonObject);
        assertEquals(200, deleteAsServletResponse("/rest/settings").getStatusCode());
        JSON jsonDeleted = getAsJSON("/rest/settings.json");
        JSONObject jsonObject2 = (JSONObject) jsonDeleted;
        assertNotNull(jsonObject2);
        JSONObject global = jsonObject2.getJSONObject("global");
        assertNotNull(global);
        assertEquals("true", global.get("globalServices").toString().trim());
        assertEquals("1024", global.get("xmlPostRequestLogBufferSize").toString().trim());

        JSONObject settings = global.getJSONObject("settings");
        assertNotNull(settings);
        assertEquals("UTF-8", settings.get("charset"));
        assertEquals("8", settings.get("numDecimals").toString().trim());
        assertEquals("http://geoserver.org", settings.get("onlineResource"));

        JSONObject jaiInfo = global.getJSONObject("jai");
        assertNotNull(jaiInfo);
        assertEquals("false", jaiInfo.get("allowInterpolation").toString().trim());
        assertEquals("0.75", jaiInfo.get("memoryThreshold").toString().trim());
        assertEquals("false", jaiInfo.get("imageIOCache").toString().trim());

        JSONObject covInfo = global.getJSONObject("coverageAccess");
        assertEquals("UNBOUNDED", covInfo.get("queueType"));

    }
}
