/* Copyright (c) 2001 - 2012 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.service.rest;

import java.util.ArrayList;
import java.util.List;

import org.geoserver.catalog.AuthorityURLInfo;
import org.geoserver.catalog.LayerIdentifierInfo;
import org.geoserver.config.GeoServer;
import org.geoserver.config.GeoServerInfo;
import org.geoserver.config.ServiceInfo;
import org.geoserver.config.util.XStreamPersister;
import org.geoserver.rest.RestletException;
import org.geoserver.rest.format.DataFormat;
import org.geoserver.wms.WMSInfoImpl;
import org.geoserver.wms.WatermarkInfo;
import org.geoserver.wms.WatermarkInfoImpl;
import org.restlet.Context;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;

/**
 * 
 * @author Juan Marin, OpenGeo
 * 
 */
public class WMSSettingsResource extends ServiceSettingsResource {

    public WMSSettingsResource(Context context, Request request, Response response, Class clazz,
            GeoServer geoServer) {
        super(context, request, response, clazz, geoServer);
    }

    @Override
    protected void configurePersister(XStreamPersister persister, DataFormat format) {
        persister.setHideFeatureTypeAttributes();
        persister.getXStream().alias("wmsinfo", WMSInfoImpl.class);
        persister.getXStream().alias("class", WatermarkInfo.class, WatermarkInfoImpl.class);
    }

    @Override
    public String handleObjectPost(Object object) throws Exception {
        String name = "";
        ServiceInfo serviceInfo = handlePost(object);
        if (serviceInfo instanceof WMSInfoImpl) {
            WMSInfoImpl wmsInfo = (WMSInfoImpl) serviceInfo;
            if (wmsInfo.getAuthorityURLs() == null) {
                List<AuthorityURLInfo> authorityURLS = new ArrayList<AuthorityURLInfo>();
                wmsInfo.setAuthorityURLs(authorityURLS);
            }
            if (wmsInfo.getIdentifiers() == null) {
                List<LayerIdentifierInfo> identifiers = new ArrayList<LayerIdentifierInfo>();
                wmsInfo.setIdentifiers(identifiers);
            }
            if (wmsInfo.getSRS() == null) {
                List<String> srsList = new ArrayList<String>();
                wmsInfo.setSRS(srsList);
            }
        }
        name = serviceInfo.getName();
        if (name == null) {
            throw new RestletException("Service name cannot be null",
                    Status.CLIENT_ERROR_BAD_REQUEST);
        }
        geoServer.add(serviceInfo);
        return name;
    }

}
