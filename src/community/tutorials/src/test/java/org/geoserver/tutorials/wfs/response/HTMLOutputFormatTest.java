package org.geoserver.tutorials.wfs.response;

import java.io.InputStream;
import java.util.Collection;

import com.mockrunner.mock.web.MockHttpServletResponse;

import org.geoserver.data.test.MockData;
import org.geoserver.wfs.WFSTestSupport;
import org.geotools.data.FeatureSource;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;

/**
 * 
 * @author Juan Marin, OpenGeo
 * 
 */

public class HTMLOutputFormatTest extends WFSTestSupport {

    public void testHTMLOutputFormat() throws Exception {
        MockHttpServletResponse resp = getAsServletResponse("wfs?request=GetFeature&version=1.1.0&typeName=sf:PrimitiveGeoFeature&outputFormat=html");
        InputStream in = getBinaryInputStream(resp);

        // check the mime type
        assertEquals("text/html", resp.getContentType());

        FeatureSource fs = getFeatureSource(MockData.PRIMITIVEGEOFEATURE);
        System.out.println(fs.getFeatures().size());

        // check write operation

        FeatureIterator it = fs.getFeatures().features();
        while (it.hasNext()) {
            SimpleFeature f = (SimpleFeature) it.next();
            Collection values = f.getValue();
            // for (Object value : values){
            // System.out.println(value.toString());
            // }
        }

    }

}
