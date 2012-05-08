/* Copyright (c) 2001 - 2012 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

package org.geoserver.rest;

import org.geoserver.catalog.rest.AbstractCatalogResource;
import org.geoserver.config.ContactInfo;
import org.geoserver.config.GeoServer;
import org.geoserver.config.GeoServerInfo;
import org.geoserver.config.impl.SettingsInfoImpl;
import org.geoserver.ows.util.OwsUtils;
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
    public boolean allowPut() {
        return allowExisting();
    }

    @Override
    public boolean allowDelete() {
        return allowExisting();
    }

    private boolean allowExisting() {
        return geoServer.getGlobal().getSettings() != null;
    }

    @Override
    protected Object handleObjectGet() throws Exception {
        return geoServer.getGlobal();
    }

    @Override
    public void handleObjectPut(Object object) throws Exception {
        GeoServerInfo geoServerInfo = (GeoServerInfo) object;
        GeoServerInfo original = geoServer.getGlobal();
        ContactInfo contactInfo = original.getSettings().getContact();
        OwsUtils.copy(geoServerInfo, original, GeoServerInfo.class);
        original.getSettings().setContact(contactInfo);
        geoServer.save(original);
    }

    @Override
    public void handleObjectDelete() throws Exception {
        ContactInfo contactInfo = geoServer.getSettings().getContact();
        SettingsInfoImpl settingsInfo = new SettingsInfoImpl();
        settingsInfo.setContact(contactInfo);
        GeoServerInfo geoServerInfo = geoServer.getGlobal();
        geoServerInfo.setSettings(settingsInfo);
        geoServer.save(geoServerInfo);
    }
}
