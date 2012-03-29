package org.geoserver.rest;

import java.util.logging.Logger;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.rest.CatalogFreemarkerHTMLFormat;
import org.geoserver.config.GeoServer;
import org.geoserver.config.util.XStreamPersisterFactory;
import org.geoserver.platform.GeoServerExtensions;
import org.geoserver.rest.format.DataFormat;
import org.geotools.util.logging.Logging;
import org.restlet.Context;
import org.restlet.data.Request;
import org.restlet.data.Response;

public abstract class GeoServerResourceBase extends ReflectiveResource {

    /**
     * logger
     */
    static Logger LOGGER = Logging.getLogger( "org.geoserver.rest");

    protected GeoServer geoServer;
    
    protected Class clazz;

    /**
     * xstream persister factory
     */
    protected XStreamPersisterFactory xpf;

    public GeoServerResourceBase(Context context,Request request, Response response, Class clazz,
            GeoServer geoServer) {
        super( context, request, response );
        this.clazz = clazz;
        this.geoServer = geoServer;
        this.xpf = GeoServerExtensions.bean(XStreamPersisterFactory.class);
    }

    @Override
    protected DataFormat createHTMLFormat(Request request,Response response) {
        return new CatalogFreemarkerHTMLFormat( clazz, request, response, this );
    }



}
