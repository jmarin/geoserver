package org.geoserver.service.rest;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

import org.geoserver.catalog.rest.CatalogRESTTestSupport;
import org.geoserver.config.GeoServer;
import org.geoserver.platform.GeoServerExtensions;

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
        assertEquals("OGC:WMS", wmsinfo.get("name"));
        JSONObject watermark = (JSONObject) wmsinfo.get("watermark");
        assertEquals("false", watermark.get("enabled").toString().trim());
        assertEquals("Nearest", wmsinfo.get("interpolation"));
    }
}
