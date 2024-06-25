package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the geometries.Tube class.
 *
 * @author Ohad and Meir
 */
class TubeTest {

    /**
     * Test method for {@link geometries.Tube#getNormal(primitives.Point)}.
     */
    @Test
    void testGetNormal() {
        // Setup
        Tube tube = new Tube(1.0, new Ray(new Point(0, 0, 0), new Vector(0, 1, 0)));

        // ============ Equivalence Partitions Tests ==============
        // Point on the surface of the tube
        Point p1 = new Point(1, 2, 0);
        assertEquals(new Vector(1, 0, 0), tube.getNormal(p1), "Bad normal to tube on the surface");

        // =============== Boundary Values Tests ==================
        // Point opposite the ray's head
        Point p2 = new Point(-3, 0, 0);
        assertEquals(new Vector(-1, 0, 0), tube.getNormal(p2), "Bad normal to tube when point is opposite to ray head");
    }

    @Test
    void testFindIntersections() {

    }
}
