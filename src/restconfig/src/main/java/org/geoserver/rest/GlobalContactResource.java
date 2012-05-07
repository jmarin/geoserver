package org.geoserver.rest;

import org.geoserver.catalog.rest.AbstractCatalogResource;
import org.geoserver.config.ContactInfo;
import org.geoserver.config.GeoServer;
import org.geoserver.config.util.XStreamPersister;
import org.geoserver.rest.format.DataFormat;
import org.restlet.Context;
import org.restlet.data.Request;
import org.restlet.data.Response;

public class GlobalContactResource extends AbstractCatalogResource {

    private GeoServer geoServer;
    
    public GlobalContactResource(Context context, Request request, Response response, Class clazz,
            GeoServer geoServer) {
        super(context, request, response, clazz, geoServer.getCatalog());
        this.geoServer = geoServer;
    }

    @Override
    protected Object handleObjectGet() throws Exception {
        return geoServer.getGlobal().getSettings().getContact();
    }
    
    @Override
    protected void configurePersister(XStreamPersister persister, DataFormat format) {
        persister.setHideFeatureTypeAttributes();
        persister.getXStream().alias("contactinfo", ContactInfo.class);
    }
}
