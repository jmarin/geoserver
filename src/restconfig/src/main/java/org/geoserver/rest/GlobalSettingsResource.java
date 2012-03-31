/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.rest;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.catalog.rest.AbstractCatalogResource;
import org.geoserver.config.ContactInfo;
import org.geoserver.config.GeoServer;
import org.geoserver.config.GeoServerFacade;
import org.geoserver.config.GeoServerInfo;
import org.geoserver.config.SettingsInfo;
import org.geoserver.config.util.XStreamPersister;
import org.geoserver.rest.format.DataFormat;
import org.restlet.Context;
import org.restlet.data.Request;
import org.restlet.data.Response;

/**
 * 
 * @author Juan Marin, OpenGeo
 *
 */
public class GlobalSettingsResource extends AbstractCatalogResource {

    private GeoServer geoServer;

    public GlobalSettingsResource(Context context, Request request, Response response, Class clazz,
            GeoServer geoServer) {
        super(context, request, response, clazz, geoServer.getCatalog());
        this.geoServer = geoServer;
    }

    @Override
    protected Object handleObjectGet() throws Exception {
        String workspace = getAttribute("workspace");
        if (workspace != null) {
            Catalog catalog = geoServer.getCatalog();
            GeoServerFacade geoServerFacade = geoServer.getFacade();
            WorkspaceInfo workspaceInfo = catalog.getWorkspaceByName(workspace);
            SettingsInfo settingsInfo = geoServerFacade.getSettings(workspaceInfo);
            return settingsInfo.getContact();
        }
        ContactInfo contactInfo = geoServer.getGlobal().getSettings().getContact();
        return contactInfo;
    }

    protected String handleObjectPost(Object object) throws Exception {
        String value = "";
        if (object instanceof SettingsInfo) {
            SettingsInfo settingsInfo = (SettingsInfo) object;
            geoServer.save(settingsInfo);
            value = settingsInfo.getTitle();
        } else if (object instanceof ContactInfo) {
            ContactInfo contactInfo = (ContactInfo) object;
            value = contactInfo.getContactPerson();
            SettingsInfo settingsInfo = geoServer.getGlobal().getSettings();
            settingsInfo.setContact(contactInfo);
            geoServer.save(settingsInfo);
        }
        return value;
    }

    @Override
    protected void configurePersister(XStreamPersister persister, DataFormat format) {
    }
}
