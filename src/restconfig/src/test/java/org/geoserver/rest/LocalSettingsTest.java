package org.geoserver.rest;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;
import net.sf.json.JSON;
import net.sf.json.JSONObject;

import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.catalog.rest.CatalogRESTTestSupport;
import org.geoserver.config.GeoServer;
import org.geoserver.config.SettingsInfo;
import org.geoserver.config.impl.SettingsInfoImpl;
import org.geoserver.ows.LocalWorkspace;
import org.geoserver.ows.util.OwsUtils;
import org.geoserver.platform.GeoServerExtensions;
import org.geoserver.wcs.WCSInfo;
import org.w3c.dom.Document;

import com.mockrunner.mock.web.MockHttpServletResponse;

public class LocalSettingsTest extends CatalogRESTTestSupport {

    protected GeoServer geoServer;

    @Override
    public void setUpInternal() throws Exception {
        super.setUpInternal();
        geoServer = GeoServerExtensions.bean(GeoServer.class, applicationContext);
        WorkspaceInfo ws = geoServer.getCatalog().getWorkspaceByName("sf");
        LocalWorkspace.set(ws);
        SettingsInfo original = geoServer.getSettings();
        SettingsInfo settingsInfo = new SettingsInfoImpl();
        OwsUtils.copy(original, settingsInfo, SettingsInfo.class);
        settingsInfo.setWorkspace(ws);
        geoServer.add(settingsInfo);
    }

    @Override
    public void tearDownInternal() throws Exception {
        super.tearDownInternal();
        LocalWorkspace.remove();
    }

    public void testGetAsJSON() throws Exception {
        JSON json = getAsJSON("/rest/workspaces/sf/settings.json");
        JSONObject jsonObject = (JSONObject) json;
        assertNotNull(jsonObject);
        JSONObject settings = jsonObject.getJSONObject("settings");
        assertNotNull(settings);
        JSONObject workspace = settings.getJSONObject("workspace");
        assertEquals("sf", workspace.get("name"));
        assertEquals("UTF-8", settings.get("charset"));
        assertEquals("8", settings.get("numDecimals").toString().trim());
        assertEquals("false", settings.get("verbose").toString().trim());
        assertEquals("false", settings.get("verboseExceptions").toString().trim());
    }

    public void testGetAsXML() throws Exception {
        Document dom = getAsDOM("/rest/workspaces/sf/settings.xml");
        assertEquals("settings", dom.getDocumentElement().getLocalName());
        assertXpathEvaluatesTo("sf", "/settings/workspace/name", dom);
        assertXpathEvaluatesTo("UTF-8", "/settings/charset", dom);
        assertXpathEvaluatesTo("8", "/settings/numDecimals", dom);
        assertXpathEvaluatesTo("false", "/settings/verbose", dom);
        assertXpathEvaluatesTo("false", "/settings/verboseExceptions", dom);
    }

    public void testPostAsJSON() throws Exception {
        geoServer.remove(geoServer.getSettings(geoServer.getCatalog().getWorkspaceByName("sf")));
        String json = "{'settings': " + "{'workspace':" + " {'name': 'sf'},"
                + "'numDecimals': '8','onlineResource':'http://geoserver.org'}}}";
        MockHttpServletResponse response = postAsServletResponse("/rest/workspaces/sf/settings",
                json, "text/json");
        assertEquals(201, response.getStatusCode());
        JSON jsonMod = getAsJSON("/rest/workspaces/sf/settings.json");
        JSONObject jsonObject = (JSONObject) jsonMod;
        assertNotNull(jsonObject);
        JSONObject settings = jsonObject.getJSONObject("settings");
        assertNotNull(settings);
        JSONObject workspace = settings.getJSONObject("workspace");
        assertNotNull(workspace);
        assertEquals("sf", workspace.get("name"));
        assertEquals("8", settings.get("numDecimals").toString().trim());
        assertEquals("http://geoserver.org", settings.get("onlineResource"));

    }

    public void testPostAsXML() throws Exception {
        geoServer.remove(geoServer.getSettings(geoServer.getCatalog().getWorkspaceByName("sf")));
        String xml = "<settings>" + "<workspace><name>sf</name></workspace>"
                + "<charset>UTF-8</charset>" + "<numDecimals>8</numDecimals>"
                + "<onlineResource>http://geoserver.org</onlineResource>"
                + "<verbose>false</verbose>" + "<verboseExceptions>false</verboseExceptions>"
                + "</settings>";
        MockHttpServletResponse response = postAsServletResponse("/rest/workspaces/sf/settings",
                xml, "text/xml");
        assertEquals(201, response.getStatusCode());

        Document dom = getAsDOM("/rest/workspaces/sf/settings.xml");
        assertEquals("settings", dom.getDocumentElement().getLocalName());
        assertXpathEvaluatesTo("sf", "/settings/workspace/name", dom);
        assertXpathEvaluatesTo("false", "/settings/verbose", dom);
        assertXpathEvaluatesTo("false", "/settings/verboseExceptions", dom);
    }

    public void testPutAsJSON() throws Exception {
        String inputJson = "{'settings': " + "{'workspace':" + " {'name': 'sf'},"
                + "'numDecimals': '10','onlineResource':'http://geoserver2.org'}}}";

        MockHttpServletResponse response = putAsServletResponse("/rest/workspaces/sf/settings",
                inputJson, "text/json");
        assertEquals(200, response.getStatusCode());
        JSON jsonMod = getAsJSON("/rest/workspaces/sf/settings.json");
        JSONObject jsonObject = (JSONObject) jsonMod;
        assertNotNull(jsonObject);
        JSONObject settings = jsonObject.getJSONObject("settings");
        assertNotNull(settings);
        JSONObject workspace = settings.getJSONObject("workspace");
        assertNotNull(workspace);
        assertEquals("sf", workspace.get("name"));
        assertEquals("10", settings.get("numDecimals").toString().trim());
        assertEquals("http://geoserver2.org", settings.get("onlineResource"));

    }

    public void testPutAsXML() throws Exception {
        String xml = "<settings>" + "<workspace><name>sf</name></workspace>"
                + "<charset>UTF-8</charset>" + "<numDecimals>10</numDecimals>"
                + "<onlineResource>http://geoserver.org</onlineResource>"
                + "<verbose>true</verbose>" + "<verboseExceptions>true</verboseExceptions>"
                + "</settings>";
        MockHttpServletResponse response = putAsServletResponse("/rest/workspaces/sf/settings",
                xml, "text/xml");
        assertEquals(200, response.getStatusCode());
        JSON json = getAsJSON("/rest/workspaces/sf/settings.json");
        JSONObject jsonObject = (JSONObject) json;
        assertNotNull(jsonObject);
        JSONObject settings = jsonObject.getJSONObject("settings");
        assertNotNull(settings);
        JSONObject workspace = settings.getJSONObject("workspace");
        assertNotNull(workspace);
        assertEquals("sf", workspace.get("name"));
        assertEquals("true", settings.get("verbose").toString().trim());
        assertEquals("true", settings.get("verboseExceptions").toString().trim());
        assertEquals("10", settings.get("numDecimals").toString().trim());
    }

    public void testDelete() throws Exception {
        JSON json = getAsJSON("/rest/workspaces/sf/settings.json");
        JSONObject jsonObject = (JSONObject) json;
        assertNotNull(jsonObject);
        assertEquals(200, deleteAsServletResponse("/rest/workspaces/sf/settings").getStatusCode());
        json = getAsJSON("/rest/workspaces/sf/settings.json");
        JSONObject deletedJson = (JSONObject) json;
        assertNull(deletedJson.get("workspace"));
    }

}
