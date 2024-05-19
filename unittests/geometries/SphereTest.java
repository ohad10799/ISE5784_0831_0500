package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the geometries.Sphere class.
 * @author Ohad and Meir
 */
class SphereTest {
    private final double DELTA = 0.000001;
    /**
     * Test method for {@link geometries.Sphere#getNormal(primitives.Point)}.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: There is a simple single test here - using a quad
        Point p1= new Point(0,1,0);
        Sphere spr=new Sphere(2,p1);
        // ensure there are no exceptions
        assertDoesNotThrow(() -> spr.getNormal(new Point(0, 0, 1)), "ERROR: getNormal() should not throw exceptions");
        // generate the test result
        Vector result = spr.getNormal(new Point(0, 0, 1));
        // ensure |result| = 1
        assertEquals(1, result.length(), DELTA, "sphare's normal is not a unit vector");

    }

}
