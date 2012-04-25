/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.rest;

import org.geoserver.config.GeoServer;
import org.geoserver.rest.util.RESTUtils;
import org.restlet.Finder;
import org.restlet.data.Request;

/**
 * 
 * @author Juan Marin, OpenGeo
 *
 */
public class AbstractGeoServerFinder extends Finder{

    protected GeoServer geoServer;
    
    protected AbstractGeoServerFinder(GeoServer geoServer) {
        this.geoServer = geoServer;
    }

    /**
     * Convenience method for subclasses to look up the (URL-decoded)value of
     * an attribute from the request, ie {@link Request#getAttributes()}.
     * 
     * @param attribute The name of the attribute to lookup.
     * 
     * @return The value as a string, or null if the attribute does not exist
     *     or cannot be url-decoded.
     */
    protected String getAttribute(Request request, String attribute) {
        return RESTUtils.getAttribute(request, attribute);
    }
}
