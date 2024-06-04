package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the geometries.Cylinder class.
 * @author Ohad and Meir
 */
public class CylinderTest {
    /**
     * Test method for {@link geometries.Cylinder#getNormal(Point)}}.
     */
    @Test
    public void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // Create a Cylinder with a radius of 1, axis along the Z-axis, and height of 2
        Ray axis = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
        Cylinder cylinder = new Cylinder(1, axis, 2);

        // Point on the curved surface
        Point pointOnSurface = new Point(1, 0, 1);
        Vector expectedNormal = new Vector(1, 0, 0);
        assertEquals(expectedNormal, cylinder.getNormal(pointOnSurface), "Wrong normal for point on the surface");

        // Point on the top base
        Point pointOnTopBase = new Point(0.5, 0.5, 2);
        expectedNormal = new Vector(0, 0, 1);
        assertEquals(expectedNormal, cylinder.getNormal(pointOnTopBase), "Wrong normal for point on the top base");

        // Point on the bottom base
        Point pointOnBottomBase = new Point(-0.5, -0.5, 0);
        expectedNormal = new Vector(0, 0, -1);
        assertEquals(expectedNormal, cylinder.getNormal(pointOnBottomBase), "Wrong normal for point on the bottom base");

        // =============== Boundary Values Tests ==================
        // Point on the edge of the top base
        Point pointOnTopEdge = new Point(1, 0, 2);
        expectedNormal = new Vector(0, 0, 1);
        assertEquals(expectedNormal, cylinder.getNormal(pointOnTopEdge), "Wrong normal for point on the edge of the top base");

        // Point on the edge of the bottom base
        Point pointOnBottomEdge = new Point(1, 0, 0);
        expectedNormal = new Vector(0, 0, -1);
        assertEquals(expectedNormal, cylinder.getNormal(pointOnBottomEdge), "Wrong normal for point on the edge of the bottom base");

        // Point at the center of the top base
        Point pointAtTopCenter = new Point(0, 0, 2);
        expectedNormal = new Vector(0, 0, 1);
        assertEquals(expectedNormal, cylinder.getNormal(pointAtTopCenter), "Wrong normal for point at the center of the top base");

        // Point at the center of the bottom base
        Point pointAtBottomCenter = new Point(0, 0, 0);
        expectedNormal = new Vector(0, 0, 1);
        assertEquals(expectedNormal, cylinder.getNormal(pointAtBottomCenter), "Wrong normal for point at the center of the bottom base");
    }

    @Test
    void testFindIntersections() {

    }
}

