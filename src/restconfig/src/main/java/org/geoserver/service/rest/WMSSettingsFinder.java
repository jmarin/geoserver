package org.geoserver.service.rest;

import org.geoserver.config.GeoServer;
import org.geoserver.rest.AbstractGeoServerFinder;
import org.geoserver.wms.WMSInfo;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Resource;

public class WMSSettingsFinder extends AbstractGeoServerFinder {

    protected WMSSettingsFinder(GeoServer geoServer) {
        super(geoServer);
    }

    @Override
    public Resource findTarget(Request request, Response response) {
        return new WMSSettingsResource(getContext(), request, response, WMSInfo.class, geoServer);
    }
}
