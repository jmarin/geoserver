package org.geoserver.service.rest;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

import org.geoserver.catalog.rest.CatalogRESTTestSupport;
import org.geoserver.config.GeoServer;
import org.geoserver.platform.GeoServerExtensions;

import com.mockrunner.mock.web.MockHttpServletResponse;

public class WMSSettingsTest extends CatalogRESTTestSupport {

    protected GeoServer geoServer;

    @Override
    protected void oneTimeSetUp() throws Exception {
        super.oneTimeSetUp();
        geoServer = GeoServerExtensions.bean(GeoServer.class, applicationContext);
    }

    public void testGetASJSON() throws Exception {
        JSON json = getAsJSON("/rest/services/wms/settings.json");
        JSONObject jsonObject = (JSONObject) json;
        assertNotNull(jsonObject);
        JSONObject wmsinfo = (JSONObject) jsonObject.get("wmsinfo");
        assertEquals("wms", wmsinfo.get("id"));
        assertEquals("true", wmsinfo.get("enabled").toString().trim());
        assertEquals("OGC:WMS", wmsinfo.get("name"));
        JSONObject watermark = (JSONObject) wmsinfo.get("watermark");
        assertEquals("false", watermark.get("enabled").toString().trim());
        assertEquals("Nearest", wmsinfo.get("interpolation"));
    }

    public void testPutASXML() throws Exception {
        String xml = "<wmsinfo>"
                + "<id>wms</id>"
                + "<enabled>disabled</enabled>"
                + "<name>WMS</name><title>GeoServer Web Map Service</title>"
                + "<maintainer>http://jira.codehaus.org/secure/BrowseProject.jspa?id=10311</maintainer>"
                + "</wmsinfo>";
        MockHttpServletResponse response = putAsServletResponse("/rest/services/wms/settings", xml, "text/xml");
        assertEquals(200, response.getStatusCode());
        JSON json = getAsJSON("/rest/services/wms/settings.json");
        JSONObject jsonObject = (JSONObject) json;
        assertNotNull(jsonObject);
        JSONObject wmsinfo = (JSONObject) jsonObject.get("wmsinfo");
        assertEquals("false", wmsinfo.get("enabled").toString().trim());
    }
}
