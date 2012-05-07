package org.geoserver.rest;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;
import net.sf.json.JSON;
import net.sf.json.JSONObject;

import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.catalog.rest.CatalogRESTTestSupport;
import org.geoserver.config.ContactInfo;
import org.geoserver.config.GeoServer;
import org.geoserver.config.GeoServerInfo;
import org.geoserver.config.SettingsInfo;
import org.geoserver.config.impl.ContactInfoImpl;
import org.geoserver.config.impl.SettingsInfoImpl;
import org.geoserver.ows.LocalWorkspace;
import org.geoserver.ows.util.OwsUtils;
import org.geoserver.platform.GeoServerExtensions;
import org.w3c.dom.Document;

import com.mockrunner.mock.web.MockHttpServletResponse;

public class LocalSettingsTest extends CatalogRESTTestSupport {

    protected GeoServer geoServer;

    @Override
    public void setUpInternal() throws Exception {
        super.oneTimeSetUp();
        geoServer = GeoServerExtensions.bean(GeoServer.class, applicationContext);
        ContactInfo contactInfo = new ContactInfoImpl();
        contactInfo.setAddress("1600 Pennsylvania Avenue");
        contactInfo.setAddressCity("Washington");
        contactInfo.setAddressPostalCode("20001");
        contactInfo.setAddressCountry("United States");
        contactInfo.setAddressState("DC");
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
        JSON json = getAsJSON("/rest/sf/settings.json");
        JSONObject jsonObject = (JSONObject) json;
        assertNotNull(jsonObject);
        JSONObject settings = jsonObject.getJSONObject("settings");
        assertNotNull(settings);
        JSONObject contactInfo = settings.getJSONObject("contact");
        assertNotNull(contactInfo);
        assertEquals("Andrea Aime", contactInfo.get("contactPerson"));
    }

    public void testGetAsXML() throws Exception {
        Document dom = getAsDOM("/rest/sf/settings.xml");
        assertEquals("settings", dom.getDocumentElement().getLocalName());
        assertXpathEvaluatesTo("sf", "/settings/workspace/name", dom);
        assertXpathEvaluatesTo("Andrea Aime", "/settings/contact/contactPerson", dom);
        assertXpathEvaluatesTo("false", "/settings/verbose", dom);
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
        MockHttpServletResponse response = postAsServletResponse("/rest/sf/settings", xml, "text/xml");
        assertEquals(200, response.getStatusCode());

        Document dom = getAsDOM("/rest/sf/settings.xml");
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

        MockHttpServletResponse response = putAsServletResponse("/rest/sf/settings", inputJson,
                "text/json");
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
        MockHttpServletResponse response = putAsServletResponse("/rest/sf/settings", xml, "text/xml");
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
        JSON json = getAsJSON("/rest/sf/settings.json");
        JSONObject jsonObject = (JSONObject) json;
        assertNotNull(jsonObject);
        assertEquals(200, deleteAsServletResponse("/rest/sf/settings").getStatusCode());
        json = getAsJSON("/rest/sf/settings.json");
        JSONObject deletedJson = (JSONObject) json;
        assertEquals("", deletedJson.get("null"));
    }
}
