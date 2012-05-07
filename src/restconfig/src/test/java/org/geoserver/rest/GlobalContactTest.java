package org.geoserver.rest;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;
import net.sf.json.JSON;
import net.sf.json.JSONObject;

import org.geoserver.catalog.rest.CatalogRESTTestSupport;
import org.geoserver.config.ContactInfo;
import org.geoserver.config.GeoServer;
import org.geoserver.config.GeoServerInfo;
import org.geoserver.config.SettingsInfo;
import org.geoserver.config.impl.ContactInfoImpl;
import org.geoserver.platform.GeoServerExtensions;
import org.w3c.dom.Document;

import com.mockrunner.mock.web.MockHttpServletResponse;

public class GlobalContactTest extends CatalogRESTTestSupport {

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

    public void testGetAsJSON() throws Exception {
        JSON json = getAsJSON("/rest/settings/contact.json");
        JSONObject jsonObject = (JSONObject) json;
        assertNotNull(jsonObject);
        JSONObject contactInfo = jsonObject.getJSONObject("contactinfo");
        assertNotNull(contactInfo);
        assertEquals("United States", contactInfo.get("addressCountry"));
        assertEquals("1600 Pennsylvania Avenue", contactInfo.get("address"));
        assertEquals("Washington", contactInfo.get("addressCity"));
        assertEquals("DC", contactInfo.get("addressState"));
        assertEquals("20001", contactInfo.get("addressPostalCode").toString());
    }

    public void testGetAsXML() throws Exception {
        Document dom = getAsDOM("/rest/settings/contact.xml");
        assertEquals("contactinfo", dom.getDocumentElement().getLocalName());
        assertXpathEvaluatesTo("United States", "/contactinfo/addressCountry", dom);
        assertXpathEvaluatesTo("Washington", "/contactinfo/addressCity", dom);
        assertXpathEvaluatesTo("1600 Pennsylvania Avenue", "/contactinfo/address", dom);
        assertXpathEvaluatesTo("DC", "/contactinfo/addressState", dom);
        assertXpathEvaluatesTo("20001", "/contactinfo/addressPostalCode", dom);
    }

    public void testPostAsJSON() throws Exception {
        deleteContactInfo();
        String inputJson = "{'contactinfo':" + "{'id': 'contact',"
                + "'address': '500 Market Street'," + "'addressCity': 'Philadelphia',"
                + "'addressCountry': 'United States'," + "'addressPostalCode': '19106',"
                + "'addressState': 'PA'" + "}" + "}";
        MockHttpServletResponse response = postAsServletResponse("/rest/settings/contact",
                inputJson, "text/json");
        assertEquals(201, response.getStatusCode());
        JSON jsonMod = getAsJSON("/rest/settings/contact.json");
        JSONObject jsonObject = (JSONObject) jsonMod;
        assertNotNull(jsonObject);
        JSONObject contactInfo = jsonObject.getJSONObject("contactinfo");
        assertEquals("United States", contactInfo.get("addressCountry"));
        assertEquals("500 Market Street", contactInfo.get("address"));
        assertEquals("Philadelphia", contactInfo.get("addressCity"));
        assertEquals("PA", contactInfo.get("addressState"));
        assertEquals("19106", contactInfo.get("addressPostalCode").toString());

    }

    public void testPostAsXML() throws Exception {
        deleteContactInfo();
        String xml = "<contactinfo>" + "<address>1600 Pennsylvania Avenue</address>"
                + "<addressCity>Washington</addressCity>"
                + "<addressCountry>United States</addressCountry>"
                + "<addressPostalCode>20001</addressPostalCode>"
                + "<addressState>DC</addressState>" + "<addressType>Avenue</addressType>"
                + "<contactEmail>chief.geographer@mail.com</contactEmail>"
                + "<contactOrganization>GeoServer</contactOrganization>"
                + "<contactPerson>ContactPerson</contactPerson>"
                + "<contactPosition>Chief Geographer</contactPosition>" + "</contactinfo>";
        MockHttpServletResponse response = postAsServletResponse("/rest/settings/contact", xml,
                "text/xml");
        assertEquals(200, response.getStatusCode());
        JSON json = getAsJSON("/rest/settings/contact.json");
        JSONObject jsonObject = (JSONObject) json;
        assertNotNull(jsonObject);
        JSONObject contactInfo = jsonObject.getJSONObject("contactinfo");
        assertEquals("United States", contactInfo.get("addressCountry"));
        assertEquals("1600 Pennsylvania Avenue", contactInfo.get("address"));
        assertEquals("Washington", contactInfo.get("addressCity"));
        assertEquals("DC", contactInfo.get("addressState"));
        assertEquals("20001", contactInfo.get("addressPostalCode").toString());
        assertEquals("Chief Geographer", contactInfo.get("contactPosition").toString());
        assertEquals("ContactPerson", contactInfo.get("contactPerson").toString());

    }

    public void testPutAsJSON() throws Exception {
        String inputJson = "{'contactinfo':" + "{'id': 'contact',"
                + "'address': '500 Market Street'," + "'addressCity': 'Philadelphia',"
                + "'addressCountry': 'United States'," + "'addressPostalCode': '19106',"
                + "'addressState': 'PA'" + "}" + "}";
        MockHttpServletResponse response = putAsServletResponse("/rest/settings/contact",
                inputJson, "text/json");
        assertEquals(200, response.getStatusCode());
        JSON jsonMod = getAsJSON("/rest/settings/contact.json");
        JSONObject jsonObject = (JSONObject) jsonMod;
        assertNotNull(jsonObject);
        JSONObject contactInfo = jsonObject.getJSONObject("contactinfo");
        assertEquals("United States", contactInfo.get("addressCountry"));
        assertEquals("500 Market Street", contactInfo.get("address"));
        assertEquals("Philadelphia", contactInfo.get("addressCity"));
        assertEquals("PA", contactInfo.get("addressState"));
        assertEquals("19106", contactInfo.get("addressPostalCode").toString());
    }

    public void testPutAsXML() throws Exception {
        String xml = "<contactinfo>" + "<address>1600 Pennsylvania Avenue</address>"
                + "<addressCity>Washington</addressCity>"
                + "<addressCountry>United States</addressCountry>"
                + "<addressPostalCode>20001</addressPostalCode>"
                + "<addressState>DC</addressState>" + "<addressType>Avenue</addressType>"
                + "<contactEmail>chief.geographer@mail.com</contactEmail>"
                + "<contactOrganization>GeoServer</contactOrganization>"
                + "<contactPerson>ContactPerson</contactPerson>"
                + "<contactPosition>Chief Geographer</contactPosition>" + "</contactinfo>";
        MockHttpServletResponse response = putAsServletResponse("/rest/settings/contact", xml,
                "text/xml");
        assertEquals(200, response.getStatusCode());
        JSON json = getAsJSON("/rest/settings/contact.json");
        JSONObject jsonObject = (JSONObject) json;
        assertNotNull(jsonObject);
        JSONObject contactInfo = jsonObject.getJSONObject("contactinfo");
        assertEquals("United States", contactInfo.get("addressCountry"));
        assertEquals("1600 Pennsylvania Avenue", contactInfo.get("address"));
        assertEquals("Washington", contactInfo.get("addressCity"));
        assertEquals("DC", contactInfo.get("addressState"));
        assertEquals("20001", contactInfo.get("addressPostalCode").toString());
        assertEquals("Chief Geographer", contactInfo.get("contactPosition").toString());
        assertEquals("ContactPerson", contactInfo.get("contactPerson").toString());
    }

    public void testDelete() throws Exception {
        JSON json = getAsJSON("/rest/settings/contact.json");
        JSONObject jsonObject = (JSONObject) json;
        assertNotNull(jsonObject);
        assertEquals(200, deleteAsServletResponse("/rest/settings").getStatusCode());
        json = getAsJSON("/rest/settings/contact.json");
        assertNotNull(json);
        JSONObject contactInfo = ((JSONObject) json).getJSONObject("contactinfo");
        assertEquals("contact", contactInfo.get("id"));
        assertNull(contactInfo.get("address"));
    }

    private void deleteContactInfo() {
        GeoServerInfo geoServerInfo = geoServer.getGlobal();
        geoServerInfo.getSettings().setContact(new ContactInfoImpl());
        geoServer.save(geoServerInfo);
    }

}
