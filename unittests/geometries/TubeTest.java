package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the geometries.Tube class.
 * @author Ohad and Meir
 */
class TubeTest {

    /**
     * Test method for {@link geometries.Tube#getNormal(primitives.Point)}.
     */
    @Test
    void testGetNormal() {
        // Setup
        Ray axis = new Ray(new Point(0, 0, 0), new Vector(0, 1, 0));
        Tube tube = new Tube(1.0, axis);

        // ============ Equivalence Partitions Tests ==============
        // Point on the surface of the tube
        Point p1 = new Point(1, 2, 0);
        Vector expectedNormal = new Vector(1, 0, 0).normalize();
        assertEquals(expectedNormal, tube.getNormal(p1), "Bad normal to tube on the surface");

        // =============== Boundary Values Tests ==================
        // Point opposite the ray's head
        p1 = new Point(1, 0, 0);
        expectedNormal = new Vector(0, 1, 0).normalize();
        assertEquals(expectedNormal, tube.getNormal(p1), "Bad normal to tube when point is opposite to ray head");
    }

    @Test
    void testFindIntersections() {

    }
}
