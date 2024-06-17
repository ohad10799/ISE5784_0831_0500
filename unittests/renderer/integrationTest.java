package renderer;
import geometries.*;
import org.junit.jupiter.api.Test;
import primitives.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

/**
 *  * Unit tests for integration of geometries with camera rays.
 *  * This class tests the number of intersections between rays constructed by the camera
 *  * and various geometries.
 * @author Ohad and Meir
 */
class integrationsTest {
    /**
     * Calculates the number of intersection points between the rays constructed by the camera
     * and the given geometries.
     *
     * @param g The geometries to test for intersections.
     * @param camera The camera used to construct the rays.
     * @return A list of intersection points, or null if no intersections are found.
     */
   public List<Point> numOfIntersections(Geometries g, Camera camera){
       List<Point> result = new ArrayList<Point>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                List<Point> intersections = g.findIntersections(camera.constructRay(3, 3, j, i));
                if (intersections != null) {
                    result.addAll(intersections);
                }
            }
        }
       return result.isEmpty() ? null : result;
   }

    /**
     * Tests the number of intersections between the rays constructed by the camera
     * and various geometries.
     */
    @Test
    void testInterations() {

        // Sphere tests
        // case 1: small sphere, expect 2 intersections
        Sphere sphere1 = new Sphere(1d, new Point(0, 0, -3));
        Camera camera1 = Camera.getBuilder()
                .setLocation(new Point(0, 0, 0))
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpSize(3, 3)
                .setVpDistance(1)
                .build();

        assertEquals(2, numOfIntersections(new Geometries(sphere1), camera1).size(), "Wrong number of intersections");

        // case 2: large sphere, expect 18 intersections
        Sphere sphere2 = new Sphere(2.5d, new Point(0, 0, -2.5));
        Camera camera2 = Camera.getBuilder()
                .setLocation(new Point(0, 0, 0.5))
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpSize(3, 3)
                .setVpDistance(1)
                .build();
        assertEquals(18, numOfIntersections(new Geometries(sphere2), camera2).size(), "Wrong number of intersections");

        // case 3: medium sphere, expect 10 intersections
        Sphere sphere3 = new Sphere(2d, new Point(0, 0, -2));
        Camera camera3 = Camera.getBuilder()
                .setLocation(new Point(0, 0, 0.5))
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpSize(3, 3)
                .setVpDistance(1)
                .build();
        assertEquals(10, numOfIntersections(new Geometries(sphere3), camera3).size(), "Wrong number of intersections");

        // case 4: very large sphere, expect 9 intersections
        Sphere sphere4 = new Sphere(4d, new Point(0, 0, -1));
        Camera camera4 = Camera.getBuilder()
                .setLocation(new Point(0, 0, 0.5))
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpSize(3, 3)
                .setVpDistance(1)
                .build();
        assertEquals(9, numOfIntersections(new Geometries(sphere4), camera4).size(), "Wrong number of intersections");

        // case 5: small sphere in front of camera, expect no intersections
        Sphere sphere5 = new Sphere(0.5d, new Point(0, 0, 1));
        Camera camera5 = Camera.getBuilder()
                .setLocation(new Point(0, 0, 0))
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpSize(3, 3)
                .setVpDistance(1)
                .build();
        assertNull(numOfIntersections(new Geometries(sphere5), camera5), "Expected no intersections with the given ray");

        // plane testes

        // case 6: plane orthogonal to view direction, expect 9 intersections
        Plane plane6 = new Plane(new Point(0, 0, -3), new Vector(0, 0, -1));
        Camera camera6 = Camera.getBuilder()
                .setLocation(new Point(0, 0, 0))
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpSize(3, 3)
                .setVpDistance(1)
                .build();
        assertEquals(9, numOfIntersections(new Geometries(plane6), camera6).size(), "Wrong number of intersections");

        // case 7: plane facing view direction, expect 9 intersections
        Plane plane7 = new Plane(new Point(0, 0, -3), new Vector(0, 0, 1));
        Camera camera7 = Camera.getBuilder()
                .setLocation(new Point(0, 0, 0))
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpSize(3, 3)
                .setVpDistance(1)
                .build();
        assertEquals(9, numOfIntersections(new Geometries(plane7), camera7).size(), "Wrong number of intersections");

        // case 8: slanted plane, expect 6 intersections
        Plane plane8 = new Plane(new Point(0, 0, -3), new Vector(1, 1, 1));
        Camera camera8 = Camera.getBuilder()
                .setLocation(new Point(0, 0, 0))
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpSize(3, 3)
                .setVpDistance(1)
                .build();
        assertEquals(6, numOfIntersections(new Geometries(plane8), camera8).size(), "Wrong number of intersections");


        //triangle tests

        // case 9: small triangle, expect 1 intersection
        Triangle triangle9 = new Triangle(new Point(0, 1, -2), new Point(-1, -1, -2), new Point(1, -1, -2));
        Camera camera9 = Camera.getBuilder()
                .setLocation(new Point(0, 0, 0))
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpSize(3, 3)
                .setVpDistance(1)
                .build();
        assertEquals(1, numOfIntersections(new Geometries(triangle9), camera9).size(), "Wrong number of intersections");

        // case 10: large triangle, expect 2 intersections
        Triangle triangle10 = new Triangle(new Point(0, 20, -2), new Point(-1, -1, -2), new Point(1, -1, -2));
        Camera camera10 = Camera.getBuilder()
                .setLocation(new Point(0, 0, 0))
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpSize(3, 3)
                .setVpDistance(1)
                .build();
        assertEquals(2, numOfIntersections(new Geometries(triangle10), camera10).size(), "Wrong number of intersections");

    }

}