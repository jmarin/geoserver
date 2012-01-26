package org.geoserver.tutorials.wfs.response;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.geoserver.config.GeoServer;
import org.geoserver.platform.Operation;
import org.geoserver.platform.ServiceException;
import org.geoserver.wfs.WFSGetFeatureOutputFormat;
import org.geoserver.wfs.WFSInfo;
import org.geoserver.wfs.request.FeatureCollectionResponse;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.type.GeometryTypeImpl;
import org.opengis.feature.GeometryAttribute;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;

import com.vividsolutions.jts.geom.Geometry;

/**
 * @author Juan Marin, OpenGeo Extends base class for HTML output format (table
 *         with attributes)
 */
public class HTMLOutputFormat extends WFSGetFeatureOutputFormat {

	private static Logger log = Logger.getLogger(HTMLOutputFormat.class);

	protected String fileExtension;

	protected String mimeType;

	public HTMLOutputFormat(GeoServer gs) {
		super(gs, "html");
		mimeType = "text/html";

	}

	@Override
	public String getMimeType(Object value, Operation operation)
			throws ServiceException {
		return mimeType;
	}

	@Override
	public String getPreferredDisposition(Object value, Operation operation) {
		return DISPOSITION_INLINE;
	}

	@Override
	public void write(FeatureCollectionResponse featureCollection,
			OutputStream output, Operation getFeature) throws IOException {
		WFSInfo wfs = getInfo();

		Writer outWriter = new BufferedWriter(new OutputStreamWriter(output,
				wfs.getGeoServer().getSettings().getCharset()));

		List<FeatureCollection> fcolls = featureCollection.getFeatures();
		for (FeatureCollection fc : fcolls) {
			FeatureIterator it = fc.features();
			AttributeDescriptor attributeDescriptor;
			while (it.hasNext()) {
				SimpleFeature simpleFeature = (SimpleFeature) it.next();
				SimpleFeatureType simpleFeatureType = simpleFeature.getType();

				List<AttributeDescriptor> attributeDescriptors = simpleFeatureType
						.getAttributeDescriptors();
				int attributeCount = simpleFeature.getAttributeCount();
				for (int i = 0; i < attributeCount; i++) {
					Object attributeValue = simpleFeature.getAttribute(i);
					if (attributeValue instanceof Geometry) {

					} else {
						if (attributeValue != null) {
							outWriter.write("<div>");
							outWriter.write(attributeValue.toString());
							outWriter.write("</div>");
							System.out.println(attributeValue.toString());
						}
					}
				}
			}
		}

	}
}
