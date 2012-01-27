package org.geoserver.tutorials.wfs.response;

import org.geoserver.data.test.MockData;
import org.geoserver.wfs.WFSTestSupport;
import org.geotools.data.FeatureSource;
import org.springframework.util.StringUtils;

import com.mockrunner.mock.web.MockHttpServletResponse;

/**
 * 
 * @author Juan Marin, OpenGeo
 * 
 */

public class HTMLOutputFormatTest extends WFSTestSupport {

    public void testHTMLOutputFormat() throws Exception {
        MockHttpServletResponse resp = getAsServletResponse("wfs?request=GetFeature&version=1.1.0&typeName=cite:Lakes&outputFormat=html");
        // check for valid response
        assertEquals(resp.getStatusCode(), 200);
        String htmlResponse = resp.getOutputStreamContent();

        // check the mime type
        assertEquals("text/html", resp.getContentType());

        // Check number of rows in html table is equal to number of records in test data
        FeatureSource fs = getFeatureSource(MockData.LAKES);
        int numberOfRows = StringUtils.countOccurrencesOf(htmlResponse, "<tr>");
        assertEquals(numberOfRows - 1, fs.getFeatures().size());

    }

}
