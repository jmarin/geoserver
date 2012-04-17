package org.geoserver.rest;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

import org.geoserver.catalog.rest.CatalogRESTTestSupport;
import org.geoserver.config.ContactInfo;
import org.geoserver.config.GeoServer;
import org.geoserver.config.GeoServerInfo;
import org.geoserver.config.SettingsInfo;
import org.geoserver.config.impl.ContactInfoImpl;
import org.geoserver.platform.GeoServerExtensions;
import org.geoserver.test.GeoServerTestSupport;
import org.w3c.dom.Document;

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
        JSONObject contactInfo = jsonObject.getJSONObject("ContactInfo");
        assertEquals("United States", contactInfo.get("addressCountry"));
        assertEquals("1600 Pennsylvania Avenue", contactInfo.get("address"));
        assertEquals("Washington", contactInfo.get("addressCity"));
        assertEquals("DC", contactInfo.get("addressState"));
        assertEquals("20001", contactInfo.get("addressPostalCode").toString());
    }

    public void testPostContactInfo() throws Exception {
        fail("not yet implemented");
    }

    public void testPutContactInfo() throws Exception {
        fail("not yet implemented");
    }

    public void testDeleteContactInfo() throws Exception {
        JSON json = getAsJSON("/rest/settings.json");
        JSONObject jsonObject = (JSONObject) json;
        assertNotNull(jsonObject);
        assertEquals(200, deleteAsServletResponse("/rest/settings").getStatusCode());
        json = getAsJSON("/rest/settings.json");
        assertNotNull(json);
        JSONObject contactInfo = ((JSONObject) json).getJSONObject("ContactInfo");
        assertEquals("contact", contactInfo.get("id"));
    }
}
