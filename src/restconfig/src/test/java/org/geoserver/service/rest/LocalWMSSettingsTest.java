package org.geoserver.service.rest;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;
import net.sf.json.JSON;
import net.sf.json.JSONObject;

import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.catalog.rest.CatalogRESTTestSupport;
import org.geoserver.config.GeoServer;
import org.geoserver.ows.LocalWorkspace;
import org.geoserver.platform.GeoServerExtensions;
import org.geoserver.wms.WMSInfo;
import org.w3c.dom.Document;

import com.mockrunner.mock.web.MockHttpServletResponse;

public class LocalWMSSettingsTest extends CatalogRESTTestSupport {

    protected GeoServer geoServer;

    @Override
    protected void setUpInternal() throws Exception {
        super.setUpInternal();
        geoServer = GeoServerExtensions.bean(GeoServer.class, applicationContext);
        WorkspaceInfo ws = geoServer.getCatalog().getWorkspaceByName("sf");
        LocalWorkspace.set(ws);
        WMSInfo wmsInfo = geoServer.getService(WMSInfo.class);
        wmsInfo.setWorkspace(ws);
        geoServer.save(wmsInfo);
    }

    @Override
    protected void tearDownInternal() throws Exception {
        LocalWorkspace.remove();
    }

    public void testGetAsJSON() throws Exception {
        JSON json = getAsJSON("/rest/services/wms/sf/settings.json");
        JSONObject jsonObject = (JSONObject) json;
        assertNotNull(jsonObject);
        JSONObject wmsinfo = (JSONObject) jsonObject.get("wmsinfo");
        assertEquals("wms", wmsinfo.get("id"));
        assertEquals("OGC:WMS", wmsinfo.get("name"));
        JSONObject workspace = (JSONObject) wmsinfo.get("workspace");
        assertNotNull(workspace);
        assertEquals("sf", workspace.get("name"));
        JSONObject watermark = (JSONObject) wmsinfo.get("watermark");
        assertEquals("false", watermark.get("enabled").toString().trim());
        assertEquals("Nearest", wmsinfo.get("interpolation"));
    }

    public void testGetAsXML() throws Exception {
        Document dom = getAsDOM("/rest/services/wms/sf/settings.xml");
        assertEquals("wmsinfo", dom.getDocumentElement().getLocalName());
        assertXpathEvaluatesTo("true", "/wmsinfo/enabled", dom);
        assertXpathEvaluatesTo("sf", "/wmsinfo/workspace/name", dom);
        assertXpathEvaluatesTo("OGC:WMS", "/wmsinfo/name", dom);
        assertXpathEvaluatesTo("false", "/wmsinfo/watermark/enabled", dom);
        assertXpathEvaluatesTo("Nearest", "/wmsinfo/interpolation", dom);
    }

    public void testPutAsJSON() throws Exception {
        String json = "{'wmsinfo': {'id':'wms'},'workspace':{'name':'sf'},'enabled':'false','name':'WMS'}}";
        MockHttpServletResponse response = putAsServletResponse("/rest/services/wms/sf/settings/",
                json, "text/json");
        assertEquals(200, response.getStatusCode());
        JSON jsonMod = getAsJSON("/rest/services/wms/sf/settings.json");
        JSONObject jsonObject = (JSONObject) jsonMod;
        assertNotNull(jsonObject);
        JSONObject wmsinfo = (JSONObject) jsonObject.get("wmsinfo");
        assertEquals("wms", wmsinfo.get("id"));
        assertEquals("false", wmsinfo.get("enabled").toString().trim());
    }

    public void testPutAsXML() throws Exception {
        String xml = "<wmsinfo>" + "<id>wms</id>" + "<workspace>" + "<name>sf</name>"
                + "</workspace>" + "<enabled>false</enabled>" + "</wmsinfo>";
        MockHttpServletResponse response = putAsServletResponse("/rest/services/wms/sf/settings",
                xml, "text/xml");
        assertEquals(200, response.getStatusCode());
        Document dom = getAsDOM("/rest/services/wms/sf/settings.xml");
        assertXpathEvaluatesTo("false", "/wmsinfo/enabled", dom);
    }
}
