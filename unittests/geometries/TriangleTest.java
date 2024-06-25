package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the geometries.Triangle class.
 *
 * @author Ohad and Meir
 */
class TriangleTest {
    private final double DELTA = 0.000001;

    /**
     * Test method for {@link geometries.Triangle#getNormal(primitives.Point)}.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: There is a simple single test here - using a quad
        Point[] pts =
                {new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0)};
        Triangle trn = new Triangle(pts[0], pts[1], pts[2]);
        // ensure there are no exceptions
        assertDoesNotThrow(() -> trn.getNormal(new Point(0, 0, 1)), "Error: getNormal() should not throw exceptions");
        // generate the test result
        Vector result = trn.getNormal(new Point(0, 0, 1));
        // ensure |result| = 1
        assertEquals(1, result.length(), DELTA, "Triangle's normal is not a unit vector");
        // ensure the result is orthogonal to all the edges
        for (int i = 0; i < 3; ++i)
            assertEquals(0d, result.dotProduct(pts[i].subtract(pts[i == 0 ? 2 : i - 1])), DELTA,
                    "Triangle's normal is not orthogonal to the plane");
    }

    @Test
    void testFindIntersections() {

        // ============ Equivalence Partitions Tests ==============
// Define the vertices of the triangle
        Point p1 = new Point(0, 0, 0);
        Point p2 = new Point(1, 0, 0);
        Point p3 = new Point(0, 1, 0);

        // Create the triangle
        Triangle triangle = new Triangle(p1, p2, p3);
        // ============ Equivalence Partitions Tests ==============

        // TC01: Intersection point inside the triangle
        Ray ray1 = new Ray(new Point(0.25, 0.25, 1), new Vector(0, 0, -1));
        List<Point> result1 = triangle.findIntersections(ray1);
        assertEquals(1, result1.size(), "Wrong number of intersection points for case 1");

        // TC02: Intersection point outside the triangle, in front of one of the edges
        Ray ray2 = new Ray(new Point(0.5, 0.5, 1), new Vector(0, 0, -1));
        List<Point> result2 = triangle.findIntersections(ray2);
        assertNull(result2, "Wrong number of intersection points for case 2");

        // TC03: Intersection point outside the triangle, in front of one of the vertices
        Ray ray3 = new Ray(new Point(-0.5, -0.5, 1), new Vector(0, 0, -1));
        List<Point> result3 = triangle.findIntersections(ray3);
        assertNull(result3, "Wrong number of intersection points for case 3");

        // =============== Boundary Values Tests ==================
        // TC04: Intersection point on an edge of the triangle
        Ray ray4 = new Ray(new Point(0.5, 0, 1), new Vector(0, 0, -1));
        List<Point> result4 = triangle.findIntersections(ray4);
        assertNull(result4, "Wrong number of intersection points for case 4");

        // TC05: Intersection point at one of the vertices of the triangle
        Ray ray5 = new Ray(new Point(0, 0, 1), new Vector(0, 0, -1));
        List<Point> result5 = triangle.findIntersections(ray5);
        assertNull(result5, "Wrong number of intersection points for case 5");

        // Case TC06: Intersection point on the extension of one of the edges of the triangle
        Ray ray6 = new Ray(new Point(0.5, 0.5, 1), new Vector(0, 0, -1));
        List<Point> result6 = triangle.findIntersections(ray6);
        assertNull(result6, "Wrong number of intersection points for case 6");

    }

}