/*
 *  Copyright (C) 2007 - 2012 GeoSolutions S.A.S.
 *  http://www.geo-solutions.it
 * 
 *  GPLv3 + Classpath exception
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.geoserver.wcs2_0.util;

import java.util.List;
import org.geotools.util.MapEntry;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ETj (etj at geo-solutions.it)
 */
public class CoverageIdConverterTest {

    /**
     * Test of encode method, of class CoverageIdConverter.
     */
    @Test
    public void testEncode() {
        String result = NSNameResourceCodec.encode("ws", "name");
        assertEquals("ws__name", result);
    }

    @Test
    public void testDecode01() {
        String qualifiedName = "ws__name";

        List<MapEntry<String, String>> decode = NSNameResourceCodec.decode(qualifiedName);
        assertEquals(1, decode.size());
        assertEquals("ws", decode.get(0).getKey());
        assertEquals("name", decode.get(0).getValue());
    }

    @Test
    public void testDecode02() {
        String qualifiedName = "s1__s2__s3";

        List<MapEntry<String, String>> decode = NSNameResourceCodec.decode(qualifiedName);
        assertEquals(2, decode.size());
        assertEquals("s1__s2", decode.get(0).getKey());
        assertEquals("s3", decode.get(0).getValue());
        assertEquals("s1", decode.get(1).getKey());
        assertEquals("s2__s3", decode.get(1).getValue());
    }

    @Test // (expected=IllegalArgumentException.class)
    public void testDecodeBad() {
        String qualifiedName = "bad_qualified_name";
        List<MapEntry<String, String>> decode = NSNameResourceCodec.decode(qualifiedName);
        assertEquals(0, decode.size());
    }
}