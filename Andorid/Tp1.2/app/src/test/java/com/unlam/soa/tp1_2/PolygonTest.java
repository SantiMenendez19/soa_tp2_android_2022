package com.unlam.soa.tp1_2;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.android.gms.maps.model.LatLng;
import com.unlam.soa.tp1_2.entities.UnlamPolygon;

import org.junit.Test;

public class PolygonTest {
    @Test
    public void polygonTestInside(){
        UnlamPolygon polygon = new UnlamPolygon();
        boolean inside = polygon.pointInside(new LatLng(-34.669353, -58.563846));
        assertTrue(inside);

    }
    @Test
    public void polygonTestOutside(){
        UnlamPolygon polygon = new UnlamPolygon();
        boolean inside = polygon.pointInside(new LatLng(-34.668573, -58.570825));
        assertFalse(inside);
    }
}
