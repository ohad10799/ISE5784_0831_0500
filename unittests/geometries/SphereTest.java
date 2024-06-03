package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the geometries.Sphere class.
 * @author Ohad and Meir
 */
class SphereTest {
    private final Point p001 = new Point(0, 0, 1);
    private final Point p100 = new Point(1, 0, 0);
    private final Vector v001 = new Vector(0, 0, 1);
    private final double DELTA = 0.000001;
    /**
     * Test method for {@link geometries.Sphere#getNormal(primitives.Point)}.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: There is a simple single test here - using a quad
        Point p1= new Point(0,1,0);
        Sphere spr=new Sphere(2,p1);
        // ensure there are no exceptions
        assertDoesNotThrow(() -> spr.getNormal(new Point(0, 0, 1)), "ERROR: getNormal() should not throw exceptions");
        // generate the test result
        Vector result = spr.getNormal(new Point(0, 0, 1));
        // ensure |result| = 1
        assertEquals(1, result.length(), DELTA, "sphare's normal is not a unit vector");

    }


    /**
     * Test method for {@link geometries.Sphere#findIntersections(primitives.Ray)}.
     */
    @Test
    public void testFindIntersections() {
        Sphere sphere = new Sphere( 1d,p100);
        final Point gp1 = new Point(0.0, 0.0, 0.0);
        final Point gp2 = new Point(2.0, 0.0, 0.0);
        final var exp1 = List.of(gp1, gp2);
        final var exp2 = List.of(gp2);
        final Vector v310 = new Vector(3, 1, 0);
        final Vector v100 = new Vector(1, 0, 0);
        final Vector v100opps = new Vector(-1, 0, 0);
        final Vector v110 = new Vector(1, 1, 0);
        final Point p01 = new Point(-1, 0, 0);
        final Point pInside = new Point(0.5, 0, 0);
        final Point gp3 = new Point(1.5, 1.5, 0);
        //final var exp2 = List.of(gp3);
        final Point pAfter = new Point(2, 2, 0);
        // ============ Equivalence Partitions Tests ==============

        // TC01: Ray's line is outside the sphere (0 points)
        assertNull(sphere.findIntersections(new Ray(p01, v110)), "Ray's line out of sphere");

        // TC02: Ray starts before and crosses the sphere (2 points)
        var result2=sphere.findIntersections(new Ray(p01, v100)).stream().sorted(Comparator.comparingDouble(p->p.distance(p01))).toList();
        assertEquals(2, result2.size(), "Wrong number of points");
        assertEquals(exp1, result2, "Ray crosses sphere");

        // TC03: Ray starts inside the sphere (1 point)
        var result3 = sphere.findIntersections(new Ray(pInside, v100)).stream().sorted(Comparator.comparingDouble(p -> p.distance(pInside))).toList();
        assertEquals(1, result3.size(), "Wrong number of points");
        assertEquals(exp2, result3, "Ray starts inside the sphere");

        // TC04: Ray starts after the sphere (0 points)
        assertNull(sphere.findIntersections(new Ray(pAfter, v110)), "Ray starts after the sphere");

        // ============ Boundary Values Tests ==============
        // **** Group: Ray's line crosses the sphere (but not the center)
        // TC11: Ray starts at sphere and goes inside (1 point)
        Point p11 = new Point(1, -1, 0);
        var result11 = sphere.findIntersections(new Ray(p11, v110));
        assertEquals(1, result11.size(), "Wrong number of points");
        assertEquals(exp2, result11, "Ray starts at sphere and goes inside");

        // TC12: Ray starts at sphere and goes outside (0 points)
        assertNull(sphere.findIntersections(new Ray(p11, v100opps)), "Ray starts at sphere and goes outside");

        // **** Group: Ray's line goes through the center
        // TC13: Ray starts before the sphere (2 points)
        Point p13 = new Point(3, 0, 0);
        Point pOn1 = new Point(2, 0, 0);
        Point pOn2 = new Point(0, 0, 0);
        var exp13 = List.of(pOn1, pOn2);
        var result13 = sphere.findIntersections(new Ray(p13, v100opps)).stream().sorted(Comparator.comparingDouble(p -> p.distance(p13))).toList();
        assertEquals(2, result13.size(), "Wrong number of points");
        assertEquals(exp13, result13, "Ray starts before the sphere");

        // TC14: Ray starts at sphere and goes inside (1 point)
        var result14 = sphere.findIntersections(new Ray(pOn1, v100opps));
        assertEquals(1, result14.size(), "Wrong number of points");
        assertEquals(List.of(pOn2), result14, "Ray starts at sphere and goes inside");

        // TC15: Ray starts inside (1 point)
        var result15 = sphere.findIntersections(new Ray(pInside, v100opps));
        assertEquals(1, result15.size(), "Wrong number of points");
        assertEquals(List.of(new Point(0,0,0)), result15,"Ray starts inside");

        // TC16: Ray starts at the center (1 point)
        var result16 = sphere.findIntersections(new Ray(new Point(1, 0, 0), v100));
        assertEquals(1, result16.size(), "Wrong number of points");
        assertEquals(exp2, result16, "Ray starts at the center");

        // TC17: Ray starts at sphere and goes outside (0 points)
        var result17 = sphere.findIntersections(new Ray(pInside, v310)).stream().sorted(Comparator.comparingDouble(p -> p.distance(pInside))).toList();
        assertEquals(0, result17.size(), "Wrong number of points");
        assertNull(sphere.findIntersections(new Ray(pInside, v310)), "Ray starts at sphere and goes outside");

        // TC18: Ray starts after sphere (0 points)
        Point p18 = new Point(2, 2, 2);
        assertNull(sphere.findIntersections(new Ray(p18, v110)), "Ray starts after sphere");

        // **** Group: Ray's line is tangent to the sphere (all tests 0 points)
        // TC19: Ray starts before the tangent point
        assertNull(sphere.findIntersections(new Ray(new Point(-1, 0, 1), v110)), "Ray starts before the tangent point");

        // TC20: Ray starts at the tangent point
        assertNull(sphere.findIntersections(new Ray(p01, new Vector(1, 1, 0))), "Ray starts at the tangent point");

        // TC21: Ray starts after the tangent point
        assertNull(sphere.findIntersections(new Ray(new Point(2, 0, 1), v110)), "Ray starts after the tangent point");

        // **** Group: Special cases
        // TC22: Ray's line is outside, ray is orthogonal to ray start to sphere's center line
        assertNull(sphere.findIntersections(new Ray(p01, new Vector(-1, 0, 0))), "Ray's line is outside, ray is orthogonal to ray start to sphere's center line");
    }

}
