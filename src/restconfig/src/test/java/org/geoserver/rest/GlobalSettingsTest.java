package org.geoserver.rest;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.catalog.rest.CatalogRESTTestSupport;
import org.geoserver.config.ContactInfo;
import org.geoserver.config.GeoServer;
import org.geoserver.config.GeoServerInfo;
import org.geoserver.config.SettingsInfo;
import org.geoserver.config.impl.ContactInfoImpl;
import org.geoserver.platform.GeoServerExtensions;
import org.geoserver.test.GeoServerTestSupport;
import org.w3c.dom.Document;

import com.mockrunner.mock.web.MockHttpServletResponse;

public class GlobalSettingsTest extends CatalogRESTTestSupport {

    protected GeoServer geoServer;

    @Override
    protected void oneTimeSetUp() throws Exception {
        super.oneTimeSetUp();
        geoServer = GeoServerExtensions.bean(GeoServer.class, applicationContext);
        ContactInfo contactInfo = new ContactInfoImpl();
        contactInfo.setAddress("1600 Pennsylvania Avenue");
        contactInfo.setAddressCity("Washington");
        contactInfo.setAddressPostalCode("20001");
        contactInfo.setAddressCountry("United States");
        contactInfo.setAddressState("DC");
        GeoServerInfo geoServerInfo = geoServer.getGlobal();
        SettingsInfo settingsInfo = geoServerInfo.getSettings();
        settingsInfo.setContact(contactInfo);
        geoServer.save(geoServerInfo);
    }

    public void testGetContactInfo() throws Exception {
        JSON json = getAsJSON("/rest/settings.json");
        JSONObject jsonObject = (JSONObject) json;
        assertNotNull(jsonObject);
        JSONObject global = jsonObject.getJSONObject("global");
        assertNotNull(global);
        JSONObject settings = global.getJSONObject("settings");
        assertNotNull(settings);
        JSONObject contactInfo = settings.getJSONObject("contact");
        assertNotNull(contactInfo);
        assertEquals("United States", contactInfo.get("addressCountry"));
        assertEquals("1600 Pennsylvania Avenue", contactInfo.get("address"));
        assertEquals("Washington", contactInfo.get("addressCity"));
        assertEquals("DC", contactInfo.get("addressState"));
        assertEquals("20001", contactInfo.get("addressPostalCode").toString());
    }

    public void testPutAsXML() throws Exception {
        String xml = "<global>" + "<settings>" + "<contact>" + "<address>500 Market Street, Philadelphia, PA</address>"
                + "<addressCity>Philadelphia</addressCity>"
                + "<addressCountry>United States</addressCountry>"
                + "<addressPostalCode>19106</addressPostalCode>"
                + "<addressState>PA</addressState>"
                + "<addressType>Street</addressType>"
                + "<contactEmail>chief.geographer@mail.com</contactEmail>"
                + "<contactOrganization>GeoServer</contactOrganization>"
                + "<contactPerson>ContactPerson</contactPerson>"
                + "<contactPosition>Chief Geographer</contactPosition>" + "</contact>"
                + "</settings>" + "</global>";
        MockHttpServletResponse response = putAsServletResponse("/rest/settings", xml, "text/xml");
        assertEquals(200, response.getStatusCode());
        JSON json = getAsJSON("/rest/settings.json");
        JSONObject jsonObject = (JSONObject) json;
        assertNotNull(jsonObject);
        JSONObject global = jsonObject.getJSONObject("global");
        assertNotNull(global);
        JSONObject settings = global.getJSONObject("settings");
        assertNotNull(settings);
        JSONObject contactInfo = settings.getJSONObject("contact");
        assertEquals("United States", contactInfo.get("addressCountry"));
        assertEquals("500 Market Street, Philadelphia, PA", contactInfo.get("address"));
        assertEquals("Philadelphia", contactInfo.get("addressCity"));
        assertEquals("PA", contactInfo.get("addressState"));
        assertEquals("19106", contactInfo.get("addressPostalCode").toString());
    }

    public void testDeleteContactInfo() throws Exception {
        JSON json = getAsJSON("/rest/settings.json");
        JSONObject jsonObject = (JSONObject) json;
        assertNotNull(jsonObject);
        assertEquals(200, deleteAsServletResponse("/rest/settings").getStatusCode());
        json = getAsJSON("/rest/settings.json");
        assertNotNull(json);
        JSONObject global = jsonObject.getJSONObject("global");
        assertNotNull(global);
        JSONObject settings = global.getJSONObject("settings");
        assertNotNull(settings);
        JSONObject contactInfo = settings.getJSONObject("contact");
        assertEquals("contact", contactInfo.get("id"));
    }
}
