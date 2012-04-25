/* Copyright (c) 2001 - 2012 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.service.rest;

import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.config.GeoServer;
import org.geoserver.config.ServiceInfo;
import org.geoserver.config.util.XStreamPersister;
import org.geoserver.ows.util.OwsUtils;
import org.geoserver.rest.format.DataFormat;
import org.geoserver.wms.WMSInfo;
import org.geoserver.wms.WMSInfoImpl;
import org.restlet.Context;
import org.restlet.data.Request;
import org.restlet.data.Response;

/**
 * 
 * @author Juan Marin, OpenGeo
 * 
 */
public class WMSSettingsResource extends ServiceSettingsResource {

    protected GeoServer geoServer;

    public WMSSettingsResource(Context context, Request request, Response response, Class clazz,
            GeoServer geoServer) {
        super(context, request, response, clazz, geoServer);
        this.geoServer = geoServer;
    }

    @Override
    protected Object handleObjectGet() throws Exception {
        String workspace = getAttribute("workspace");
        if (workspace != null) {
            WorkspaceInfo ws = geoServer.getCatalog().getWorkspaceByName(workspace);
            return geoServer.getService(ws, WMSInfo.class);
        }
        return (ServiceInfo) geoServer.getService(WMSInfo.class);
    }

    @Override
    protected void handleObjectPut(Object object) throws Exception {
        String workspace = getAttribute("workspace");
        WMSInfo wmsInfo = (WMSInfo) object;
        WMSInfo original = null;
        if (workspace != null) {
            WorkspaceInfo ws = geoServer.getCatalog().getWorkspaceByName(workspace);
            original = geoServer.getService(ws, WMSInfo.class);
            OwsUtils.copy(wmsInfo, original, WMSInfo.class);
            original.setWorkspace(ws);
        } else {
            original = geoServer.getService(WMSInfo.class);
        }
        OwsUtils.copy(wmsInfo, original, WMSInfo.class);
        geoServer.save(original);
    }

    @Override
    protected void configurePersister(XStreamPersister persister, DataFormat format) {
        persister.setHideFeatureTypeAttributes();
        persister.getXStream().alias("wmsinfo", WMSInfoImpl.class);
    }

}
