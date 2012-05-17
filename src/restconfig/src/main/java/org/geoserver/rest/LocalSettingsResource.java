/* Copyright (c) 2001 - 2012 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

package org.geoserver.rest;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.catalog.rest.AbstractCatalogResource;
import org.geoserver.config.GeoServer;
import org.geoserver.config.SettingsInfo;
import org.geoserver.config.impl.SettingsInfoImpl;
import org.geoserver.ows.util.OwsUtils;
import org.restlet.Context;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;

/**
 * 
 * @author Juan Marin, OpenGeo
 * 
 */
public class LocalSettingsResource extends AbstractCatalogResource {

    private GeoServer geoServer;

    public LocalSettingsResource(Context context, Request request, Response response, Class clazz,
            GeoServer geoServer) {
        super(context, request, response, clazz, geoServer.getCatalog());
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
        return allowExisting();
    }

    private boolean allowNew() {
        String workspace = getAttribute("workspace");
        WorkspaceInfo ws = geoServer.getCatalog().getWorkspaceByName(workspace);
        return geoServer.getSettings(ws) == null;
    }

    private boolean allowExisting() {
        String workspace = getAttribute("workspace");
        if (workspace != null) {
            WorkspaceInfo workspaceInfo = geoServer.getCatalog().getWorkspaceByName(workspace);
            return geoServer.getSettings(workspaceInfo) != null;
        }
        return false;
    }

    @Override
    protected Object handleObjectGet() throws Exception {
        String workspace = getAttribute("workspace");
        if (workspace != null) {
            WorkspaceInfo workspaceInfo = geoServer.getCatalog().getWorkspaceByName(workspace);
            SettingsInfo settingsInfo = geoServer.getSettings(workspaceInfo);
            if (settingsInfo == null) {
                settingsInfo = new SettingsInfoImpl();
                settingsInfo.setVerbose(false);
            }
            return settingsInfo;
        }
        throw new RestletException("Workspace " + workspace + " not found",
                Status.CLIENT_ERROR_BAD_REQUEST);
    }

    @Override
    protected String handleObjectPost(Object obj) throws Exception {
        String name = "";
        String workspace = getAttribute("workspace");
        if (workspace != null) {
            Catalog catalog = geoServer.getCatalog();
            WorkspaceInfo workspaceInfo = catalog.getWorkspaceByName(workspace);
            SettingsInfo settings = (SettingsInfo) obj;
            settings.setWorkspace(workspaceInfo);
            geoServer.add(settings);
            geoServer.save(geoServer.getSettings(workspaceInfo));
            name = settings.getWorkspace().getName();
        }
        return name;
    }

    @Override
    protected void handleObjectPut(Object object) throws Exception {
        String workspace = getAttribute("workspace");
        if (workspace != null) {
            WorkspaceInfo workspaceInfo = catalog.getWorkspaceByName(workspace);
            SettingsInfo settingsInfo = (SettingsInfo) object;
            SettingsInfo original = geoServer.getSettings(workspaceInfo);
            OwsUtils.copy(settingsInfo, original, SettingsInfo.class);
            original.setWorkspace(workspaceInfo);
            geoServer.save(original);
        }
    }

    @Override
    public void handleObjectDelete() throws Exception {
        String workspace = getAttribute("workspace");
        if (workspace != null) {
            WorkspaceInfo workspaceInfo = geoServer.getCatalog().getWorkspaceByName(workspace);
            SettingsInfo settingsInfo = geoServer.getSettings(workspaceInfo);
            geoServer.remove(settingsInfo);
        }
    }

}
