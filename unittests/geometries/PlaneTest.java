package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the geometries.Plane class.
 * @author Ohad and Meir
 */
class PlaneTest {

    Point p1 = new Point(1,2,3);
    private final double DELTA = 0.000001;
    /**
     * Test method for {@link geometries.Plane(primitives.Point,primitives.Point,primitives.Point)#}.
     */
    @Test
    void testConstructor() {
        // =============== Boundary Values Tests ==================
        // The first and second points are the same
        assertThrows(IllegalArgumentException.class, () -> new Plane(p1,p1,new Point(2,3,4)),
                "Creating plane with 2 same points not throw IllegalArgumentException");
        // All the point are on the same line
        assertThrows(IllegalArgumentException.class, () -> new Plane(p1,new Point(2,3,4),new Point(0,1,2)),
                "Creating plane with points in the same line not throw IllegalArgumentException");
    }

    /**
     * Test method for {@link geometries.Plane#getNormal(primitives.Point)}.
     */
    @Test
    public void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: There is a simple single test here - using a quad
        Point[] pts =
                { new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0) };
        Plane pln = new Plane(pts[0], pts[1], pts[2]);
        // ensure there are no exceptions
        assertDoesNotThrow(() -> pln.getNormal(new Point(0, 0, 1)), "Error: getNormal() should not throw exceptions");
        // generate the test result
        Vector result = pln.getNormal(new Point(0, 0, 1));
        // ensure |result| = 1
        assertEquals(1, result.length(), DELTA, "Polygon's normal is not a unit vector");
        // ensure the result is orthogonal to all the edges
        for (int i = 0; i < 3; ++i)
            assertEquals(0d, result.dotProduct(pts[i].subtract(pts[i == 0 ? 2 : i - 1])), DELTA,
                    "Plane's normal is not orthogonal to the plane");
    }


}
