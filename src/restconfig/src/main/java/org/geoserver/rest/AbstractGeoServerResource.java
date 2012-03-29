package org.geoserver.rest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.geoserver.config.GeoServer;
import org.geoserver.config.util.XStreamPersister;
import org.geoserver.rest.format.DataFormat;
import org.geoserver.rest.format.ReflectiveJSONFormat;
import org.geoserver.rest.format.ReflectiveXMLFormat;
import org.restlet.Context;
import org.restlet.data.Request;
import org.restlet.data.Response;

public class AbstractGeoServerResource extends GeoServerResourceBase {

    public AbstractGeoServerResource(Context context, Request request, Response response,
            Class clazz, GeoServer geoServer) {
        super(context, request, response, clazz, geoServer);
    }

    @Override
    protected Object handleObjectGet() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    protected ReflectiveXMLFormat createXMLFormat(Request request,Response response) {
        return new ReflectiveXMLFormat() {
            @Override
            protected void write(Object data, OutputStream output) throws IOException  {
                XStreamPersister p = xpf.createXMLPersister();
                p.setGeoServer(geoServer);
                p.setReferenceByName(true);
                p.setExcludeIds();
                
                configurePersister(p,this);
                p.save( data, output );
            }
            
            @Override
            protected Object read(InputStream in)
                    throws IOException {
                XStreamPersister p = xpf.createXMLPersister();
                p.setGeoServer(geoServer);
                
                configurePersister(p,this);
                return p.load( in, clazz );
            }
        };
    }
    
    @Override
    protected ReflectiveJSONFormat createJSONFormat(Request request,Response response) {
        return new ReflectiveJSONFormat() {
            @Override
            protected void write(Object data, OutputStream output)
                    throws IOException {
                XStreamPersister p = xpf.createJSONPersister();
                p.setGeoServer(geoServer);
                p.setReferenceByName(true);
                p.setExcludeIds();
                
                configurePersister(p,this);
                p.save( data, output );
            }
            
            @Override
            protected Object read(InputStream input)
                    throws IOException {
                XStreamPersister p = xpf.createJSONPersister();
                p.setGeoServer(geoServer);
                
                configurePersister(p,this);
                return p.load( input, clazz );
            }
        };
    }
    
    /**
     * Method for subclasses to perform additional configuration of the 
     * xstream instance used for serializing/de-serializing objects.
     */
    protected void configurePersister(XStreamPersister persister, DataFormat format) {
    }

}
