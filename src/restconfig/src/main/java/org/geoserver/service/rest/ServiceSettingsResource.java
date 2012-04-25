package org.geoserver.service.rest;

import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.catalog.rest.AbstractCatalogResource;
import org.geoserver.config.GeoServer;
import org.geoserver.wms.WMSInfo;
import org.restlet.Context;
import org.restlet.data.Request;
import org.restlet.data.Response;

public class ServiceSettingsResource extends AbstractCatalogResource {

    private GeoServer geoServer;

    private Class clazz;

    public ServiceSettingsResource(Context context, Request request, Response response,
            Class clazz, GeoServer geoServer) {
        super(context, request, response, clazz, geoServer.getCatalog());
        this.clazz = clazz;
        this.geoServer = geoServer;
    }

    @Override
    public boolean allowPut() {
        return allowExisting();
    }

    private boolean allowExisting() {
        String workspace = getAttribute("workspace");
        if (workspace != null) {
            WorkspaceInfo ws = geoServer.getCatalog().getWorkspaceByName(workspace);
            return geoServer.getService(ws, clazz) != null;
        }
        return geoServer.getService(WMSInfo.class) != null;
    }

    @Override
    protected Object handleObjectGet() throws Exception {
        return null;
    }

}
