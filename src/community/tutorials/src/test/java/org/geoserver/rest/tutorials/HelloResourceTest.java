package org.geoserver.rest.tutorials;

import org.geoserver.test.GeoServerTestSupport;

public class HelloResourceTest extends GeoServerTestSupport {

    public void test() throws Exception {
        String response = getAsString("/rest/hello.txt").trim();
        assertEquals("Hello World", response);
    }
}
