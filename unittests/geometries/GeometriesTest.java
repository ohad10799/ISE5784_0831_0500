package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GeometriesTest {

    @Test
    void testFindIntersections() {

        Ray testRay1 = new Ray(new Point(1, 1, 1), new Vector(1, 1, 1));
        Geometries geometries1 = new Geometries(new Plane(new Point(2, 2, 2), new Vector(-1, -1, -1)), new Sphere(1, new Point(2, 2, 2)), new Plane(new Point(3, 3, 3), new Vector(0, 1, 0)));
        Geometries geometries3 = new Geometries(new Sphere(1, new Point(3, 3, 3)), new Sphere(1, new Point(2, 2, 2)));
        Geometries geometries4 = new Geometries(new Sphere(1, new Point(2, 2, 2)), new Sphere(1, new Point(1, 1, 1)));
        Geometries geometries5 = new Geometries(new Sphere(1, new Point(1, 1, 1)), new Triangle(new Point(2, 1, 1), new Point(1, 2, 1), new Point(1, 1, 2)));

        // ============ Equivalence Partitions Tests ==============
        // Some shapes (but not all) are intersected
        List<Point> intersections1 = geometries1.findIntersections(testRay1);
        assertNotNull(intersections1, "Expected intersections when some shapes intersect");
        assertEquals(2, intersections1.size(), "Expected 2 intersection point");

        // Empty collection of geometries
        assertNull(new Geometries().findIntersections(testRay1), "Expected no intersections with an empty collection");
        // No shape is intersected
        assertNull(geometries3.findIntersections(testRay1), "Expected no intersections with the given ray");

        // Only one shape is intersected
        List<Point> intersections4 = geometries4.findIntersections(testRay1);
        assertNotNull(intersections4, "Expected intersections when some shapes intersect");
        assertEquals(1, intersections4.size(), "Expected 1 intersection point");

        // All shapes are intersected
        List<Point> intersections5 = geometries5.findIntersections(testRay1);
        assertNotNull(intersections5, "Expected intersections with all shapes");
        assertEquals(2, intersections5.size(), "Expected 2 intersection points");
    }
}