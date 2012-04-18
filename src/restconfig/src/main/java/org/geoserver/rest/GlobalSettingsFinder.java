/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.rest;

import org.geoserver.config.GeoServer;
import org.geoserver.config.GeoServerInfo;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Resource;

/**
 * 
 * @author Juan Marin, OpenGeo
 *
 */
public class GlobalSettingsFinder extends AbstractGeoServerFinder {

    public GlobalSettingsFinder(GeoServer geoServer) {
        super(geoServer);
    }

    @Override
    public Resource findTarget(Request request, Response response) {
        return new GlobalSettingsResource(getContext(), request, response, GeoServerInfo.class, geoServer);
    }
}
