/* Copyright (c) 2001 - 2012 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.service.rest;

import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.catalog.rest.AbstractCatalogResource;
import org.geoserver.config.GeoServer;
import org.geoserver.config.ServiceInfo;
import org.geoserver.config.impl.ServiceInfoImpl;
import org.geoserver.ows.util.OwsUtils;
import org.geoserver.rest.RestletException;
import org.geoserver.wms.WMSInfo;
import org.geoserver.wms.WMSInfoImpl;
import org.restlet.Context;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;

/**
 * 
 * @author Juan Marin, OpenGeo
 * 
 */
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
    public boolean allowPost() {
        return allowNew();
    }

    @Override
    public boolean allowPut() {
        return allowExisting();
    }

    @Override
    public boolean allowDelete() {
        return allowNew();
    }

    private boolean allowNew() {
        return geoServer.getService(clazz) != null;
    }

    private boolean allowExisting() {
        String workspace = getAttribute("workspace");
        if (workspace != null) {
            WorkspaceInfo ws = geoServer.getCatalog().getWorkspaceByName(workspace);
            return geoServer.getService(ws, clazz) != null;
        }
        return geoServer.getService(clazz) != null;
    }

    @Override
    protected Object handleObjectGet() throws Exception {
        String workspace = getAttribute("workspace");
        if (workspace != null) {
            WorkspaceInfo ws = geoServer.getCatalog().getWorkspaceByName(workspace);
            return geoServer.getService(ws, clazz);
        }
        return (ServiceInfo) geoServer.getService(clazz);
    }

    @Override
    protected String handleObjectPost(Object object) throws Exception {
        String name = "";
        String workspace = getAttribute("workspace");
        ServiceInfo original = null;
        ServiceInfoImpl newInfo = null;
        if (workspace != null) {
            WorkspaceInfo ws = geoServer.getCatalog().getWorkspaceByName(workspace);
            if (geoServer.getService(ws, clazz) != null) {
                throw new RestletException(
                        "Service information already exists, creation of a new object is not allowed",
                        Status.CLIENT_ERROR_FORBIDDEN);
            }
            original = geoServer.getService(clazz);
            ServiceInfo serviceInfo = (ServiceInfo) object;
            name = serviceInfo.getName();
            if (name == null) {
                throw new RestletException("Service name cannot be null",
                        Status.CLIENT_ERROR_BAD_REQUEST);
            }
            newInfo = geoServer.getFactory().create(ServiceInfoImpl.class);
            OwsUtils.copy(geoServer.getService(clazz), newInfo, clazz);
            newInfo.setId(null);
            OwsUtils.copy(object, newInfo, clazz);
            geoServer.add(newInfo);
        }
        return name;
    }

    @Override
    protected void handleObjectPut(Object object) throws Exception {
        String workspace = getAttribute("workspace");
        ServiceInfo original = null;
        if (workspace != null) {
            WorkspaceInfo ws = geoServer.getCatalog().getWorkspaceByName(workspace);
            original = geoServer.getService(ws, clazz);
            OwsUtils.copy(object, original, clazz);
            original.setWorkspace(ws);
        } else {
            original = geoServer.getService(clazz);
        }
        OwsUtils.copy(object, original, clazz);
        geoServer.save(original);
    }

    @Override
    protected void handleObjectDelete() throws Exception {
        String workspace = getAttribute("workspace");
        if (workspace != null) {
            WorkspaceInfo ws = geoServer.getCatalog().getWorkspaceByName(workspace);
            ServiceInfo serviceInfo = geoServer.getService(ws, clazz);
            if (serviceInfo != null) {
                geoServer.remove(serviceInfo);
            }
        }
    }

}
