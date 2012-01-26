package org.geoserver.tutorials.wfs.response;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.geoserver.config.GeoServer;
import org.geoserver.platform.Operation;
import org.geoserver.platform.ServiceException;
import org.geoserver.wfs.WFSGetFeatureOutputFormat;
import org.geoserver.wfs.WFSInfo;
import org.geoserver.wfs.request.FeatureCollectionResponse;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.type.GeometryDescriptorImpl;
import org.geotools.feature.type.GeometryTypeImpl;
import org.opengis.feature.GeometryAttribute;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.FeatureType;

import com.vividsolutions.jts.geom.Geometry;

/**
 * @author Juan Marin, OpenGeo Extends base class for HTML output format (table with attributes)
 */
public class HTMLOutputFormat extends WFSGetFeatureOutputFormat {

    private static Logger log = Logger.getLogger(HTMLOutputFormat.class);

    protected String fileExtension;

    protected String mimeType;

    public HTMLOutputFormat(GeoServer gs) {
        super(gs, "html");
        mimeType = "text/html";

    }

    @Override
    public String getMimeType(Object value, Operation operation) throws ServiceException {
        return mimeType;
    }

    @Override
    public void write(FeatureCollectionResponse featureCollection, OutputStream output,
            Operation getFeature) throws IOException {
        WFSInfo wfs = getInfo();

        Writer outWriter = new BufferedWriter(new OutputStreamWriter(output, wfs.getGeoServer()
                .getSettings().getCharset()));

        log.info("Creating HTML output");
        
        outWriter.write("<html>");
        outWriter.write("<head>");
        outWriter.write("<title>GeoServer Tutorials: WFS HTML Output Format</title>");
        outWriter.write("</head>");

        outWriter.write("<body>");
        outWriter.write("<table border='1'>");
        List<FeatureCollection> fcolls = featureCollection.getFeatures();
        for (FeatureCollection fc : fcolls) {

            try {
                FeatureIterator it = fc.features();
                SimpleFeatureType schema = (SimpleFeatureType) fc.getSchema();
                List<AttributeDescriptor> attributeDescriptors = schema.getAttributeDescriptors();
                outWriter.write("<tr>");
                for (AttributeDescriptor attributeDescriptor : attributeDescriptors) {
                    System.out.println(attributeDescriptor.getType());
                    if (attributeDescriptor instanceof GeometryDescriptorImpl) {
                      //ignore Geometry column
                    } else {
                        outWriter.write("<th>" + attributeDescriptor.getLocalName() + "</th>");
                    }
                }
                outWriter.write("</tr>");
                
                while (it.hasNext()) {
                    outWriter.write("<tr>");
                    SimpleFeature simpleFeature = (SimpleFeature) it.next();
                    SimpleFeatureType simpleFeatureType = simpleFeature.getType();
                    int attributeCount = simpleFeature.getAttributeCount();
                    for (int i = 0; i < attributeCount; i++) {
                        Object attributeValue = simpleFeature.getAttribute(i);
                        if (attributeValue instanceof Geometry) {
                          //ignore Geometry column
                        } else {
                            if (attributeValue != null) {
                                outWriter.write("<td>");
                                outWriter.write(attributeValue.toString());
                                outWriter.write("</td>");
                            }
                        }
                    }
                    outWriter.write("</tr>");
                }

            } catch (Exception exception) {
                ServiceException serviceException = new ServiceException("Error: "
                        + exception.getMessage());
                serviceException.initCause(exception);
                throw serviceException;
            } finally {
                outWriter.write("</table>");
                outWriter.write("</body>");
                outWriter.write("</html>");
                outWriter.flush();
            }

        }

    }
}
