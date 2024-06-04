package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;





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
    /**
     * Test the findIntersections method of Plane class.
     */
    @Test
    void testFindIntersections() {
        // Define the plane
        Point p1 = new Point(1, 0, 0);
        Point p2 = new Point(0, 1, 0);
        Point p3 = new Point(0, 0, 1);
        Plane plane = new Plane(p1, p2, p3);
        Point pInside = new Point(0.5,0.5,0);

        // Test for intersection points
        // ============ Equivalence Partitions Tests ==============
        //TC01: Intersection with a Ray Outside the Plane, Not Parallel
        assertNotNull(plane.findIntersections(new Ray(new Point(2,2,2),new Vector(-1,-1,-2))), "Intersection with ray outside, not parallel should not be null");
        //TC02: Intersection with a Ray Outside the Plane, Not Parallel, No Intersection
        assertNull(plane.findIntersections(new Ray(new Point(2,2,2),new Vector(1,1,2))), "Intersection with ray outside, not parallel should be null");
        // =============== Boundary Values Tests ==================

        // **** Group: Ray Parallel to the Plane
        Vector VParallel = new Vector(-1,1,0);
        //TC03: Intersection with a Ray Parallel to the Plane, inside the Plane
        assertNull(plane.findIntersections(new Ray(pInside,VParallel)), "Intersection with ray Parallel to the Plane, inside the Plane should be null");
        //TC04:Intersection with a Ray Parallel to the Plane, outside the Plane
        assertNull(plane.findIntersections(new Ray(new Point(2,2,0),VParallel)), "Intersection with ray Parallel to the Plane, outside the Plane should be null");

        // **** Group: Ray Orthogonal to the plane
        Vector VOrthogonal = new Vector(1,1,1);
        //TC05:Intersection with a Ray Orthogonal to the Plane, before the Plane
        assertNotNull(plane.findIntersections(new Ray(new Point(-1,-1,-1),VOrthogonal)), "Intersection with ray before the plane and orthogonal should not be null");
        //TC06:Intersection with a Ray Orthogonal to the Plane, in the Plane
        assertNull(plane.findIntersections(new Ray(pInside,VParallel)), "Intersection with ray in the plane and orthogonal should be null");
        //TC07: Intersection with a Ray Orthogonal to the Plane, after the Plane
        assertNull(plane.findIntersections(new Ray(new Point(2,2,2),VParallel)), "Intersection with ray after the plane and orthogonal should be null");

        // **** Group: Ray not Orthogonal and not Parallel to the plane
        //TC08: The ray starts at the same point as the plane
        assertNull(plane.findIntersections(new Ray(p1,new Vector(1,2,3))), "Intersection with ray starts at the same point as the plane should be null");
        //TC09: The ray starts on the plane
        assertNull(plane.findIntersections(new Ray(pInside,new Vector(1,2,3))), "Intersection with ray starts on the plane should be null");

    }



}
