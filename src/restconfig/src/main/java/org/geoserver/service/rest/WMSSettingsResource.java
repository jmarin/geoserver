package org.geoserver.service.rest;

import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.catalog.rest.AbstractCatalogResource;
import org.geoserver.config.GeoServer;
import org.geoserver.config.ServiceInfo;
import org.geoserver.config.util.XStreamPersister;
import org.geoserver.rest.format.DataFormat;
import org.geoserver.wms.WMSInfo;
import org.geoserver.wms.WMSInfoImpl;
import org.restlet.Context;
import org.restlet.data.Request;
import org.restlet.data.Response;

public class WMSSettingsResource extends AbstractCatalogResource {

    protected GeoServer geoServer;

    public WMSSettingsResource(Context context, Request request, Response response, Class clazz,
            GeoServer geoServer) {
        super(context, request, response, clazz, geoServer.getCatalog());
        this.geoServer = geoServer;
    }

    @Override
    protected Object handleObjectGet() throws Exception {
        String workspace = getAttribute("workspace");
        if (workspace != null) {
            WorkspaceInfo workspaceInfo = geoServer.getCatalog().getWorkspaceByName(workspace);
            return geoServer.getService(workspaceInfo, WMSInfo.class);
        }
        return (ServiceInfo) geoServer.getService(WMSInfo.class);
    }

    @Override
    protected void configurePersister(XStreamPersister persister, DataFormat format) {
        persister.setHideFeatureTypeAttributes();
        persister.getXStream().alias("wmsinfo", WMSInfoImpl.class);
        persister.getXStream().alias("", org.geotools.util.Version.class);
    }

}
