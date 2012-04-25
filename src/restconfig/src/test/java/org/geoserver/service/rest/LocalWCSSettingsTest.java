package org.geoserver.service.rest;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;
import net.sf.json.JSON;
import net.sf.json.JSONObject;

import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.catalog.rest.CatalogRESTTestSupport;
import org.geoserver.config.GeoServer;
import org.geoserver.ows.LocalWorkspace;
import org.geoserver.platform.GeoServerExtensions;
import org.geoserver.wcs.WCSInfo;
import org.w3c.dom.Document;

import com.mockrunner.mock.web.MockHttpServletResponse;

public class LocalWCSSettingsTest extends CatalogRESTTestSupport {

    protected GeoServer geoServer;

    @Override
    protected void setUpInternal() throws Exception {
        super.setUpInternal();
        geoServer = GeoServerExtensions.bean(GeoServer.class, applicationContext);
        WorkspaceInfo ws = geoServer.getCatalog().getWorkspaceByName("sf");
        LocalWorkspace.set(ws);
        WCSInfo wcsInfo = geoServer.getService(WCSInfo.class);
        wcsInfo.setWorkspace(ws);
        geoServer.save(wcsInfo);
    }

    @Override
    protected void tearDownInternal() throws Exception {
        LocalWorkspace.remove();
    }

    public void testGetAsJSON() throws Exception {
        JSON json = getAsJSON("/rest/services/wcs/sf/settings.json");
        JSONObject jsonObject = (JSONObject) json;
        assertNotNull(jsonObject);
        JSONObject wcsinfo = (JSONObject) jsonObject.get("wcsinfo");
        assertEquals("wcs", wcsinfo.get("id"));
        assertEquals("My GeoServer WCS", wcsinfo.get("name"));
        JSONObject workspace = (JSONObject) wcsinfo.get("workspace");
        assertNotNull(workspace);
        assertEquals("sf", workspace.get("name"));
        assertEquals("false", wcsinfo.get("verbose").toString().trim());
    }

    public void testGetAsXML() throws Exception {
        Document dom = getAsDOM("/rest/services/wcs/sf/settings.xml");
        assertEquals("wcsinfo", dom.getDocumentElement().getLocalName());
        assertXpathEvaluatesTo("true", "/wcsinfo/enabled", dom);
        assertXpathEvaluatesTo("sf", "/wcsinfo/workspace/name", dom);
        assertXpathEvaluatesTo("My GeoServer WCS", "/wcsinfo/name", dom);
        assertXpathEvaluatesTo("false", "/wcsinfo/verbose", dom);
    }

    public void testPutAsJSON() throws Exception {
        String json = "{'wcsinfo': {'id':'wcs','workspace':{'name':'sf'},'enabled':'false','name':'WCS'}}";
        MockHttpServletResponse response = putAsServletResponse("/rest/services/wcs/sf/settings/",
                json, "text/json");
        assertEquals(200, response.getStatusCode());
        JSON jsonMod = getAsJSON("/rest/services/wcs/sf/settings.json");
        JSONObject jsonObject = (JSONObject) jsonMod;
        assertNotNull(jsonObject);
        JSONObject wcsinfo = (JSONObject) jsonObject.get("wcsinfo");
        assertEquals("wcs", wcsinfo.get("id"));
        assertEquals("false", wcsinfo.get("enabled").toString().trim());
    }

    public void testPutAsXML() throws Exception {
        String xml = "<wcsinfo>" + "<id>wcs</id>" + "<workspace>" + "<name>sf</name>"
                + "</workspace>" + "<enabled>false</enabled>" + "</wcsinfo>";
        MockHttpServletResponse response = putAsServletResponse("/rest/services/wcs/sf/settings",
                xml, "text/xml");
        assertEquals(200, response.getStatusCode());
        Document dom = getAsDOM("/rest/services/wcs/sf/settings.xml");
        assertXpathEvaluatesTo("false", "/wcsinfo/enabled", dom);
    }

}
