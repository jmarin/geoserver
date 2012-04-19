/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.rest;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.CatalogBuilder;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.catalog.rest.AbstractCatalogResource;
import org.geoserver.config.ContactInfo;
import org.geoserver.config.GeoServer;
import org.geoserver.config.GeoServerFacade;
import org.geoserver.config.GeoServerInfo;
import org.geoserver.config.SettingsInfo;
import org.geoserver.config.impl.ContactInfoImpl;
import org.geoserver.config.util.XStreamPersister;
import org.geoserver.ows.util.OwsUtils;
import org.geoserver.rest.format.DataFormat;
import org.restlet.Context;
import org.restlet.data.Request;
import org.restlet.data.Response;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * 
 * @author Juan Marin, OpenGeo
 * 
 */
public class SettingsResource extends AbstractCatalogResource {

    private GeoServer geoServer;

    public SettingsResource(Context context, Request request, Response response, Class clazz,
            GeoServer geoServer) {
        super(context, request, response, clazz, geoServer.getCatalog());
        this.geoServer = geoServer;
    }

    @Override
    public boolean allowPost() {
        return false;
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
            return geoServer.getSettings() != null;
        }
        return geoServer.getGlobal().getSettings().getContact() != null;
    }

    @Override
    protected Object handleObjectGet() throws Exception {
        String workspace = getAttribute("workspace");
        if (workspace != null) {
            Catalog catalog = geoServer.getCatalog();
            GeoServerFacade geoServerFacade = geoServer.getFacade();
            WorkspaceInfo workspaceInfo = catalog.getWorkspaceByName(workspace);
            SettingsInfo settingsInfo = geoServerFacade.getSettings(workspaceInfo);
            return settingsInfo;
        }
        return geoServer.getGlobal();
    }

    @Override
    public void handleObjectPut(Object object) {
        String workspace = getAttribute("workspace");
        if (workspace != null) {
            GeoServerFacade geoServerFacade = geoServer.getFacade();
            WorkspaceInfo workspaceInfo = catalog.getWorkspaceByName(workspace);
            SettingsInfo settingsInfo = (SettingsInfo) object;
            SettingsInfo original = geoServerFacade.getSettings(workspaceInfo);
            OwsUtils.copy(settingsInfo, original, SettingsInfo.class);
            geoServer.save(original);
        }
        GeoServerInfo geoServerInfo = (GeoServerInfo) object;
        GeoServerInfo original = geoServer.getGlobal();
        OwsUtils.copy(geoServerInfo, original, GeoServerInfo.class);
        geoServer.save(original);
    }

    @Override
    public void handleObjectDelete() {
        String workspace = getAttribute("workspace");
        if (workspace != null) {

        }
        GeoServerInfo geoServerInfo = geoServer.getGlobal();
        ContactInfo contactInfo = new ContactInfoImpl();
        geoServerInfo.getSettings().setContact(contactInfo);
        geoServer.save(geoServerInfo);
    }

}
