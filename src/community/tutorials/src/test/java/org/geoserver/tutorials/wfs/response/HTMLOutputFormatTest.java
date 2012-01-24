package org.geoserver.tutorials.wfs.response;

import java.io.InputStream;

import com.mockrunner.mock.web.MockHttpServletResponse;

import org.geoserver.data.test.MockData;
import org.geoserver.wfs.WFSTestSupport;
import org.geotools.data.FeatureSource;


public class HTMLOutputFormatTest extends WFSTestSupport {

    public void testHTMLOutputFormat() throws Exception {
        MockHttpServletResponse resp = getAsServletResponse("wfs?request=GetFeature&version=1.1.0&typeName=sf:PrimitiveGeoFeature&outputFormat=html");
        InputStream in = getBinaryInputStream(resp);

        // check the mime type
        assertEquals("text/html", resp.getContentType());
        
        
        FeatureSource fs = getFeatureSource(MockData.PRIMITIVEGEOFEATURE);
        

    }
    
}
