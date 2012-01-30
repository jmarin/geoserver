package org.geoserver.rest.tutorials;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

import org.geoserver.test.GeoServerTestSupport;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class HelloReflectiveResourceTest extends GeoServerTestSupport {

    public void testGetAsXML() throws Exception {
        // make the request, parsing the result as a dom
        Document dom = getAsDOM("/rest/helloReflective.xml");

        // print out the result
        print(dom);

        // make assertions
        Node message = getFirstElementByTagName(dom, "message");
        assertNotNull(message);
        assertEquals("Hello World", message.getFirstChild().getNodeValue());
    }
    
    public void testGetAsJSON() throws Exception {
        //make the request, parsing the result into a json object
        JSON json = getAsJSON("/rest/helloReflective.json");
        
        //print out the result
        print(json);
        
        //make assertions
        assertTrue(json instanceof JSONObject);
        JSONObject hello = ((JSONObject)json).getJSONObject("org.geoserver.rest.tutorials.Hello");
        assertEquals("Hello World", hello.get("message"));
        
    }

}
