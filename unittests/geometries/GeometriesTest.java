package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GeometriesTest {

    @Test
    void testFindIntersections() {

        Ray ray1 = new Ray(new Point(1, 1, 1), new Vector(0, 0, 1));
        //Ray ray2 = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
        Geometries geometries1 = new Geometries();
        Geometries geometries2 = new Geometries(new Sphere(1, new Point(0, 0, 5)), new Plane(new Point(0, 0, 10), new Vector(0, 1, 0)));
        Geometries geometries3 = new Geometries(new Sphere(1, new Point(0, 0, 5)), new Sphere(1, new Point(0, 0, 10)), new Plane(new Point(0, 0, 15), new Vector(0, 1, 0)));
        Geometries geometries4 = new Geometries(new Sphere(1, new Point(0, 0, 5)), new Sphere(1, new Point(0, 0, 10)), new Plane(new Point(0, 0, 15), new Vector(0, 0, 1)));

        // ============ Equivalence Partitions Tests ==============
        // TC01: Some Shapes Intersect
        List<Point> intersections2 = geometries3.findIntersections(ray2);
        assertNotNull(intersections2, "Expected intersections when some shapes intersect");
        assertEquals(4, intersections2.size(), "Expected four intersections with the spheres");

        // ============ Boundary Values Tests ==============
        // TC02: Empty Geometries List
        assertNull(geometries1.findIntersections(ray1), "Expected no intersections for empty geometries");

        // TC03: No Shape Intersects:
        assertNull(geometries2.findIntersections(ray1), "Expected no intersections when no shapes intersect");

        // TC04: One Shape Intersects:
        List<Point> intersections1 = geometries2.findIntersections(ray2);
        assertNotNull(intersections1, "Expected intersections when one shape intersects");
        assertEquals(2, intersections1.size(), "Expected two intersections with the sphere");

        // TC05: All Shapes Intersect
        List<Point> intersections4 = geometries4.findIntersections(ray2);
        assertNotNull(intersections4, "Expected intersections when all shapes intersect");
        assertEquals(5, intersections4.size(), "Expected five intersections in total");

    }
}