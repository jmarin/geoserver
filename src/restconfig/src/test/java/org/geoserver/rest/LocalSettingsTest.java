package org.geoserver.rest;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;
import net.sf.json.JSON;
import net.sf.json.JSONObject;

import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.catalog.rest.CatalogRESTTestSupport;
import org.geoserver.config.GeoServer;
import org.geoserver.config.SettingsInfo;
import org.geoserver.ows.LocalWorkspace;
import org.geoserver.platform.GeoServerExtensions;
import org.w3c.dom.Document;

import com.mockrunner.mock.web.MockHttpServletResponse;

public class LocalSettingsTest extends CatalogRESTTestSupport {

    protected GeoServer geoServer;

    @Override
    public void setUpInternal() throws Exception {
        super.oneTimeSetUp();
        geoServer = GeoServerExtensions.bean(GeoServer.class, applicationContext);
        WorkspaceInfo workspace = geoServer.getCatalog().getWorkspaceByName("sf");
        LocalWorkspace.set(workspace);
        SettingsInfo settingsInfo = geoServer.getSettings();
        settingsInfo.setWorkspace(workspace);
        geoServer.add(settingsInfo);
        geoServer.save(settingsInfo);
    }

    @Override
    public void tearDownInternal() throws Exception {
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
        fail("Not Implemented");
    }

    public void testPostAsXML() throws Exception {
        geoServer.remove(geoServer.getSettings(geoServer.getCatalog().getWorkspaceByName("sf")));
        String xml = "<settings><workspace><name>sf</name></workspace><contact>"
                + "<address>500 Market Street</address>"
                + "<addressCity>Philadelphia</addressCity>"
                + "<addressCountry>United States</addressCountry>"
                + "<addressPostalCode>19106</addressPostalCode>"
                + "<addressState>PA</addressState>" + "<addressType>Street</addressType>"
                + "<contactEmail>chief.geographer@mail.com</contactEmail>"
                + "<contactOrganization>GeoServer</contactOrganization>"
                + "<contactPerson>ContactPerson</contactPerson>"
                + "<contactPosition>Chief Geographer</contactPosition>" + "</contact>"
                + "</settings>";
        MockHttpServletResponse response = postAsServletResponse("/rest/workspaces/sf/settings",
                xml, "text/xml");
        assertEquals(200, response.getStatusCode());

        Document dom = getAsDOM("/rest/workspaces/sf/settings.xml");
        assertEquals("settings", dom.getDocumentElement().getLocalName());
        assertXpathEvaluatesTo("sf", "/settings/workspace/name", dom);
        assertXpathEvaluatesTo("Chief Geographer", "/settings/contact/contactPosition", dom);

    }

    public void testPutAsJSON() throws Exception {
        String inputJson = "{'settings': " + "{'workspace':" + " {'name': 'sf'}," + "'contact':"
                + "   {'id':" + " 'contact','contactPerson': 'Chief Geographer'},"
                + "'charset': 'UTF-8'," + "'numDecimals': '8',"
                + "'onlineResource': 'http://geoserver.org'," + "'verbose': 'true',"
                + "'verboseExceptions': 'false'}}";

        MockHttpServletResponse response = putAsServletResponse("/rest/workspaces/sf/settings",
                inputJson, "text/json");
        assertEquals(200, response.getStatusCode());
        JSON jsonMod = getAsJSON("/rest/sf/settings.json");
        JSONObject jsonObject = (JSONObject) jsonMod;
        assertNotNull(jsonObject);
        JSONObject settings = jsonObject.getJSONObject("settings");
        assertNotNull(settings);
        JSONObject contactInfo = settings.getJSONObject("contact");
        assertEquals("United States", contactInfo.get("addressCountry"));
        assertEquals("500 Market Street", contactInfo.get("address"));
        assertEquals("Philadelphia", contactInfo.get("addressCity"));
        assertEquals("PA", contactInfo.get("addressState"));
        assertEquals("19106", contactInfo.get("addressPostalCode").toString());
    }

    public void testPutAsXML() throws Exception {
        String xml = "<settings><workspace><name>sf</name></workspace><contact>"
                + "<address>500 Market Street</address>"
                + "<addressCity>Philadelphia</addressCity>"
                + "<addressCountry>United States</addressCountry>"
                + "<addressPostalCode>19106</addressPostalCode>"
                + "<addressState>PA</addressState>" + "<addressType>Street</addressType>"
                + "<contactEmail>chief.geographer@mail.com</contactEmail>"
                + "<contactOrganization>GeoServer</contactOrganization>"
                + "<contactPerson>ContactPerson</contactPerson>"
                + "<contactPosition>Chief Geographer</contactPosition>" + "</contact>"
                + "</settings>";
        MockHttpServletResponse response = putAsServletResponse("/rest/sf/settings", xml,
                "text/xml");
        assertEquals(200, response.getStatusCode());
        JSON json = getAsJSON("/rest/sf/settings.json");
        JSONObject jsonObject = (JSONObject) json;
        assertNotNull(jsonObject);
        JSONObject settings = jsonObject.getJSONObject("settings");
        assertNotNull(settings);
        JSONObject contactInfo = settings.getJSONObject("contact");
        assertEquals("United States", contactInfo.get("addressCountry"));
        assertEquals("500 Market Street", contactInfo.get("address"));
        assertEquals("Philadelphia", contactInfo.get("addressCity"));
        assertEquals("PA", contactInfo.get("addressState"));
        assertEquals("19106", contactInfo.get("addressPostalCode").toString());
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
