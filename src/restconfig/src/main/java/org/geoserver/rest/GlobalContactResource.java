/* Copyright (c) 2001 - 2012 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.rest;

import org.geoserver.catalog.rest.AbstractCatalogResource;
import org.geoserver.config.ContactInfo;
import org.geoserver.config.GeoServer;
import org.geoserver.config.GeoServerInfo;
import org.geoserver.config.SettingsInfo;
import org.geoserver.config.impl.ContactInfoImpl;
import org.geoserver.config.util.XStreamPersister;
import org.geoserver.ows.util.OwsUtils;
import org.geoserver.rest.format.DataFormat;
import org.restlet.Context;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;

/**
 * 
 * @author Juan Marin, OpenGeo
 * 
 */
public class GlobalContactResource extends AbstractCatalogResource {

    private GeoServer geoServer;

    public GlobalContactResource(Context context, Request request, Response response, Class clazz,
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
        return true;
        //return allowExisting();
    }

    private boolean allowExisting() {
        return geoServer.getGlobal().getSettings().getContact() != null;
    }

    private boolean allowNew() {
        return geoServer.getGlobal().getSettings().getContact().getId() != null
                && geoServer.getGlobal().getSettings().getContact().getContactPerson() == null
                && geoServer.getGlobal().getSettings().getContact().getAddress() == null
                && geoServer.getGlobal().getSettings().getContact().getAddressCity() == null
                && geoServer.getGlobal().getSettings().getContact().getAddressCountry() == null
                && geoServer.getGlobal().getSettings().getContact().getAddressPostalCode() == null
                && geoServer.getGlobal().getSettings().getContact().getAddressState() == null
                && geoServer.getGlobal().getSettings().getContact().getAddressType() == null
                && geoServer.getGlobal().getSettings().getContact().getContactEmail() == null
                && geoServer.getGlobal().getSettings().getContact().getContactFacsimile() == null
                && geoServer.getGlobal().getSettings().getContact().getContactOrganization() == null
                && geoServer.getGlobal().getSettings().getContact().getContactPerson() == null
                && geoServer.getGlobal().getSettings().getContact().getContactPosition() == null
                && geoServer.getGlobal().getSettings().getContact().getContactVoice() == null;
    }

    @Override
    protected Object handleObjectGet() throws Exception {
        if (geoServer.getSettings().getContact() == null) {
            throw new RestletException("No contact information available",
                    Status.CLIENT_ERROR_NOT_FOUND);
        }
        return geoServer.getGlobal().getSettings().getContact();
    }

    @Override
    protected String handleObjectPost(Object obj) throws Exception {
        String contact = "";
        ContactInfo contactInfo = (ContactInfo) obj;
        GeoServerInfo geoServerInfo = geoServer.getGlobal();
        if (contactInfo != null)
            geoServerInfo.getSettings().setContact(contactInfo);
        geoServer.save(geoServerInfo);
        contact = geoServerInfo.getSettings().getContact().getId();
        return contact;
    }

    @Override
    protected void handleObjectPut(Object obj) throws Exception {
        GeoServerInfo geoServerInfo = geoServer.getGlobal();
        ContactInfo original = geoServerInfo.getSettings().getContact();
        OwsUtils.copy((ContactInfo) obj, original, ContactInfo.class);
        geoServer.save(geoServerInfo);
    }

    @Override
    protected void handleObjectDelete() throws Exception {
        GeoServerInfo geoServerInfo = geoServer.getGlobal();
        geoServerInfo.getSettings().setContact(null);
        geoServer.save(geoServerInfo);
    }

    @Override
    protected void configurePersister(XStreamPersister persister, DataFormat format) {
        persister.setHideFeatureTypeAttributes();
        persister.getXStream().alias("contactinfo", ContactInfo.class);
    }
}
