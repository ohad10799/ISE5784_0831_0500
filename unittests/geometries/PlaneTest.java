package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;



import java.util.List;

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

        // Define the rays and points for intersection testing
        Ray rayOutsideNotParallel = new Ray(new Point(1, 1, 1), new Vector(1, 1, 1)); // Ray outside, not parallel
        Ray rayOutsideNotParallelNoIntersection = new Ray(new Point(1, 1, 1), new Vector(1, 1, -1)); // Ray outside, not parallel, no intersection
        Ray rayParallelOutside = new Ray(new Point(1, 1, 1), new Vector(1, 0, 0)); // Ray parallel, outside
        Ray rayParallelInside = new Ray(new Point(1, 1, 1), new Vector(-1, 0, 0)); // Ray parallel, inside
        Ray rayOrthogonalInside = new Ray(new Point(1, 1, 1), new Vector(0, 0, -1)); // Ray orthogonal, inside
        Ray rayOrthogonalAtOrigin = new Ray(new Point(0, 0, 0), new Vector(0, 0, -1)); // Ray orthogonal, at origin

        // Test for intersection points
        // ============ Equivalence Partitions Tests ==============
        //TC01:Intersection with a Ray Outside the Plane, Not Parallel
        List<Point> resultOutsideNotParallel = plane.findIntersections(rayOutsideNotParallel);
        assertNotNull(resultOutsideNotParallel, "Intersection with ray outside, not parallel should not be null");
        //TC02:Intersection with a Ray Outside the Plane, Not Parallel, No Intersection
        List<Point> resultOutsideNotParallelNoIntersection = plane.findIntersections(rayOutsideNotParallelNoIntersection);
        assertNotNull(resultOutsideNotParallelNoIntersection, "Intersection with ray outside, not parallel, no intersection should not be null");

        // =============== Boundary Values Tests ==================
        //TC03:Intersection with a Ray Parallel to the Plane, Outside the Plane
        List<Point> resultParallelOutside = plane.findIntersections(rayParallelOutside);
        assertNotNull(resultParallelOutside, "Intersection with ray parallel, outside should not be null");
        //TC04:Intersection with a Ray Parallel to the Plane, Inside the Plane
        List<Point> resultParallelInside = plane.findIntersections(rayParallelInside);
        assertNotNull(resultParallelInside, "Intersection with ray parallel, inside should not be null");
        //TC05:Intersection with a Ray Orthogonal to the Plane, Inside the Plane
        List<Point> resultOrthogonalInside = plane.findIntersections(rayOrthogonalInside);
        assertNotNull(resultOrthogonalInside, "Intersection with ray orthogonal, inside should not be null");
        //TC06:Intersection with a Ray Orthogonal to the Plane, at the Origin
        List<Point> resultOrthogonalAtOrigin = plane.findIntersections(rayOrthogonalAtOrigin);
        assertNotNull(resultOrthogonalAtOrigin, "Intersection with ray orthogonal, at origin should not be null");
    }



}
