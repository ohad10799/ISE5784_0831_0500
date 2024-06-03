package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GeometriesTest {

    @Test
    void testFindIntersections() {

        Ray testRay = new Ray(new Point(1, 1, 1), new Vector(1, 1, 1));
        Geometries geometries1 = new Geometries(new Plane(new Point(2, 2, 2), new Vector(-1, -1, -1)),new Sphere(1, new Point(2, 2, 2)),new Plane(new Point(3, 3, 3), new Vector(0, 1, 0)));
        Geometries geometries3 = new Geometries(new Sphere(1, new Point(3, 3, 3)));
        Geometries geometries4 = new Geometries(new Sphere(1, new Point(2, 2, 2)));
        Geometries geometries5 = new Geometries(new Sphere(1, new Point(2, 2, 2)),new Sphere(1, new Point(1, 1, 1)));

        // כמה צורות (אך לא כולן) נחתכות (EP)
        List<Point> intersections1 = geometries1.findIntersections(testRay);
        assertNotNull(intersections1, "Expected intersections when some shapes intersect");
        assertEquals(2, intersections1.size(), "Expected 2 intersection point");

        // אוסף גופים ריק (BVA)
        assertNull(new Geometries().findIntersections(testRay), "Expected no intersections with an empty collection");
        // אף צורה לא נחתכת (BVA)
        assertNull(geometries3.findIntersections(testRay), "Expected no intersections with the given ray");

        // צורה אחת בלבד נחתכת (BVA)
        List<Point> intersections4 = geometries4.findIntersections(testRay);
        assertNotNull(intersections4, "Expected intersections when some shapes intersect");
        assertEquals(1, intersections4.size(), "Expected 1 intersection point");

        // כל הצורות נחתכות (BVA)
        List<Point> intersections5 = geometries5.findIntersections(testRay);
        assertNotNull(intersections5, "Expected intersections with all shapes");
        assertEquals(2, intersections5.size(), "Expected 2 intersection points");
    }
}