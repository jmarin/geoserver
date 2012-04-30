package org.geoserver.rest;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.catalog.rest.AbstractCatalogResource;
import org.geoserver.config.GeoServer;
import org.geoserver.config.GeoServerFacade;
import org.geoserver.config.SettingsInfo;
import org.geoserver.ows.util.OwsUtils;
import org.restlet.Context;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;

public class LocalSettingsResource extends AbstractCatalogResource {

    private GeoServer geoServer;

    public LocalSettingsResource(Context context, Request request, Response response, Class clazz,
            GeoServer geoServer) {
        super(context, request, response, clazz, geoServer.getCatalog());
        this.geoServer = geoServer;
    }

    @Override
    public boolean allowPost() {
        String workspace = getAttribute("workspace");
        WorkspaceInfo ws = geoServer.getCatalog().getWorkspaceByName(workspace);
        return geoServer.getSettings(ws) == null;
    }

    @Override
    public boolean allowPut() {
        return allowExisting();
    }

    @Override
    public boolean allowDelete() {
        return allowExisting();
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
    protected String handleObjectPost(Object object) throws Exception {
        String name = "";
        String workspace = getAttribute("workspace");
        if (workspace != null) {
            Catalog catalog = geoServer.getCatalog();
            WorkspaceInfo workspaceInfo = catalog.getWorkspaceByName(workspace);
            SettingsInfo global = geoServer.getSettings();
            SettingsInfo newSettings = geoServer.getFactory().create(SettingsInfo.class);
            OwsUtils.copy(global, newSettings, SettingsInfo.class);
            OwsUtils.copy((SettingsInfo) object, newSettings, SettingsInfo.class);
            newSettings.setWorkspace(workspaceInfo);
            geoServer.add(newSettings);
            geoServer.save(newSettings);
            name = newSettings.getWorkspace().getName();
        }
        return name;
    }

    @Override
    protected Object handleObjectGet() throws Exception {
        String workspace = getAttribute("workspace");
        if (workspace != null) {
            Catalog catalog = geoServer.getCatalog();
            WorkspaceInfo workspaceInfo = catalog.getWorkspaceByName(workspace);
            SettingsInfo settingsInfo = geoServer.getSettings(workspaceInfo);
            return settingsInfo;
        }
        return new RestletException("Workspace " + workspace + " not found",
                Status.CLIENT_ERROR_BAD_REQUEST);
    }

    @Override
    protected void handleObjectPut(Object object) throws Exception {
        String workspace = getAttribute("workspace");
        if (workspace != null) {
            WorkspaceInfo workspaceInfo = catalog.getWorkspaceByName(workspace);
            SettingsInfo settingsInfo = (SettingsInfo) object;
            SettingsInfo original = geoServer.getSettings(workspaceInfo);
            OwsUtils.copy(settingsInfo, original, SettingsInfo.class);
            geoServer.remove(settingsInfo);
            geoServer.add(original);
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
