package org.geoserver.rest;

import org.geoserver.config.GeoServer;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Resource;

public class GlobalSettingsFinder extends AbstractGeoServerFinder {

    public GlobalSettingsFinder(GeoServer geoServer) {
        super(geoServer);
    }
    
    @Override
    public Resource findTarget(Request request, Response response) {
        return new GlobalSettingsResource(null,request,response,geoServer);
    }
}
