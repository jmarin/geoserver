package org.geoserver.rest.tutorials;

import java.util.HashMap;
import java.util.Map;

import org.geoserver.rest.MapResource;

public class HelloMapResource extends MapResource {

    @Override
    public Map getMap() throws Exception {
        HashMap map = new HashMap();
        map.put("message", "Hello World");
        return map;
    }

}
