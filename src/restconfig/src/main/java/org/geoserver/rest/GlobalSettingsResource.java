package org.geoserver.rest;

import org.geoserver.config.GeoServer;
import org.geoserver.config.GeoServerFacade;
import org.geoserver.config.GeoServerInfo;
import org.restlet.Context;
import org.restlet.data.Request;
import org.restlet.data.Response;

public class GlobalSettingsResource extends AbstractGeoServerResource {

    public GlobalSettingsResource(Context context, Request request, Response response,
            GeoServer geoServer) {
        super(context, request, response, GeoServerInfo.class, geoServer);
    }

    @Override
    protected Object handleObjectGet() throws Exception {
        GeoServerFacade geoServerFacade = geoServer.getFacade();
        GeoServerInfo serverInfo = geoServerFacade.getGlobal();
        return serverInfo.getSettings().getContact();
    }

}
