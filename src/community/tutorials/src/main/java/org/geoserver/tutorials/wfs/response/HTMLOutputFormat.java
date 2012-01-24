package org.geoserver.tutorials.wfs.response;

import java.io.OutputStream;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.geoserver.config.GeoServer;
import org.geoserver.platform.Operation;
import org.geoserver.platform.ServiceException;
import org.geoserver.wfs.WFSGetFeatureOutputFormat;
import org.geoserver.wfs.request.FeatureCollectionResponse;
import org.geotools.data.simple.SimpleFeatureCollection;


/**
 * @author Juan Marin, OpenGeo 
 * Extends base class for HTML output format (table with attributes)
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
    public String getPreferredDisposition(Object value, Operation operation) {
        return DISPOSITION_INLINE;
    }

    @Override
    public void write(FeatureCollectionResponse featureCollection, OutputStream output,
            Operation getFeature) {
        for (Iterator it = featureCollection.getFeature().iterator(); it.hasNext();) {
            SimpleFeatureCollection fc = (SimpleFeatureCollection) it.next();
                        
            
        }
    }
    

}
