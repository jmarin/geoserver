package org.geoserver.rest;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

import org.geoserver.catalog.rest.CatalogRESTTestSupport;
import org.geoserver.config.GeoServer;
import org.w3c.dom.Document;

import com.mockrunner.mock.web.MockHttpServletResponse;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;

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
        assertEquals("UTF-8", global.get("charset"));
        assertEquals("8", global.get("numDecimals").toString().trim());
        assertEquals("http://geoserver.org", global.get("onlineResource"));

        JSONObject contact = global.getJSONObject("contact");
        assertNotNull(contact);
        assertEquals("Andrea Aime", contact.get("contactPerson"));

        JSONObject jaiInfo = global.getJSONObject("jai");
        assertNotNull(jaiInfo);
        assertEquals("false", jaiInfo.get("allowInterpolation").toString().trim());
        assertEquals("0.75", jaiInfo.get("memoryThreshold").toString().trim());
        assertEquals("5", jaiInfo.get("tilePriority").toString().trim());

        JSONObject covInfo = global.getJSONObject("coverageAccess");
        assertEquals("UNBOUNDED", covInfo.get("queueType"));

    }

    public void testGetAsXML() throws Exception {
        Document dom = getAsDOM("/rest/settings.xml");
        assertEquals("global", dom.getDocumentElement().getLocalName());
        assertXpathEvaluatesTo("true", "/global/globalServices", dom);
        assertXpathEvaluatesTo("1024", "/global/xmlPostRequestLogBufferSize", dom);
        assertXpathEvaluatesTo("UTF-8", "/global/charset", dom);
        assertXpathEvaluatesTo("8", "/global/numDecimals", dom);
        assertXpathEvaluatesTo("http://geoserver.org", "/global/onlineResource", dom);
        assertXpathEvaluatesTo("Andrea Aime", "/global/contact/contactPerson", dom);
        assertXpathEvaluatesTo("false", "/global/jai/allowInterpolation", dom);
        assertXpathEvaluatesTo("0.75", "/global/jai/memoryThreshold", dom);
        assertXpathEvaluatesTo("UNBOUNDED", "/global/coverageAccess/queueType", dom);

    }

    public void testPutAsJSON() throws Exception {
        String inputJson = "{'global':"
                + "{'contact':{'contactPerson':'Claudius Ptolomaeus'},"
                + "'jai':{'allowInterpolation':'false','tilePriority':'5','tileThreads':'7','memoryCapacity':'0.5','memoryThreshold':'0.75'},"
                + "'coverageAccess':{'maxPoolSize':10,'corePoolSize':'5','keepAliveTime':'30000','queueType':'UNBOUNDED','imageIOCacheThreshold':'10240'},"
                + "'charset':'UTF-8','numDecimals':10,'onlineResource':'http://geoserver2.org','verbose':'false','verboseExceptions':'false','updateSequence':'78',"
                + "'featureTypeCacheSize':'0','globalServices':'true','xmlPostRequestLogBufferSize':'2048'}}";
        MockHttpServletResponse response = putAsServletResponse("/rest/settings/", inputJson,
                "text/json");
        assertEquals(200, response.getStatusCode());
        JSON json = getAsJSON("/rest/settings.json");
        JSONObject jsonObject = (JSONObject) json;
        assertNotNull(jsonObject);
        JSONObject global = jsonObject.getJSONObject("global");
        assertNotNull(global);
        assertEquals("true", global.get("globalServices").toString().trim());
        assertEquals("2048", global.get("xmlPostRequestLogBufferSize").toString().trim());

        assertEquals("UTF-8", global.get("charset"));
        assertEquals("10", global.get("numDecimals").toString().trim());
        assertEquals("http://geoserver2.org", global.get("onlineResource"));

        JSONObject contact = global.getJSONObject("contact");
        assertNotNull(contact);
        assertEquals("Claudius Ptolomaeus", contact.get("contactPerson"));

        JSONObject jaiInfo = global.getJSONObject("jai");
        assertNotNull(jaiInfo);
        assertEquals("false", jaiInfo.get("allowInterpolation").toString().trim());
        assertEquals("0.75", jaiInfo.get("memoryThreshold").toString().trim());

        JSONObject covInfo = global.getJSONObject("coverageAccess");
        assertEquals("UNBOUNDED", covInfo.get("queueType"));

    }

    public void testPutAsXML() throws Exception {
        String xml = "<global>" + "<charset>UTF-8</charset>" + "<numDecimals>10</numDecimals>"
                + "<onlineResource>http://geoserver.org</onlineResource>"
                + "<verbose>false</verbose>" + "<verboseExceptions>false</verboseExceptions>"
                + "<contact><contactPerson>Justin Deoliveira</contactPerson></contact>" + "<jai>"
                + "<allowInterpolation>true</allowInterpolation>" + "<recycling>false</recycling>"
                + "<tilePriority>5</tilePriority>" + "<tileThreads>7</tileThreads>"
                + "<memoryCapacity>0.5</memoryCapacity>"
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
        assertXpathEvaluatesTo("UTF-8", "/global/charset", dom);
        assertXpathEvaluatesTo("10", "/global/numDecimals", dom);
        assertXpathEvaluatesTo("http://geoserver.org", "/global/onlineResource", dom);
        assertXpathEvaluatesTo("Justin Deoliveira", "/global/contact/contactPerson", dom);
        assertXpathEvaluatesTo("true", "/global/jai/allowInterpolation", dom);
        assertXpathEvaluatesTo("0.85", "/global/jai/memoryThreshold", dom);
        assertXpathEvaluatesTo("UNBOUNDED", "/global/coverageAccess/queueType", dom);
    }


}
