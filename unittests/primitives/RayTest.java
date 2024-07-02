package primitives;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class RayTest {

    @Test
    void testGetPoint() {
        Ray ray = new Ray(new Point(1, 0, 0), new Vector(0, 1, 0));
        // ============ Equivalence Partitions Tests ==============
        // TC01: positive distance
        assertEquals(new Point(1, 2, 0), ray.getPoint(2), "Error: getPoint() for positive distance does not work correctly");
        // TC02: negative distance
        assertEquals(new Point(1, -2, 0), ray.getPoint(-2), "Error: getPoint() for negative distance does not work correctly");
        // ================= Boundary Values Tests ==================
        // TC03: distance = 0
        assertEquals(new Point(1, 0, 0), ray.getPoint(0), "Error: getPoint() for distance = 0 does not work correctly");
    }

    @Test
    public void testfindClosestPoint() {
        Ray ray = new Ray(new Point(1, 0, 0), new Vector(0, 1, 0));
        // ============ Equivalence Partitions Tests ==============
        // TC01: point is in the middle of the list
        List<Point> l1 = List.of(new Point(1, 2, 0), new Point(1, 3, 0), new Point(1, 1, 0), new Point(1, 4, 0), new Point(1, 5, 0));
        assertEquals(new Point(1, 1, 0), ray.findClosestPoint(l1), "Error: findClosestPoint() for point in the middle of the list does not work correctly");
        // ================= Boundary Values Tests ==================
        // TC02: The list is empty
        List<Point> l2 = List.of();
        assertNull(ray.findClosestPoint(l2), "Error: findClosestPoint() for empty list does not work correctly");
        // TC03 The first point is the closest
        List<Point> l3 = List.of(new Point(1, 1, 0), new Point(1, 2, 0), new Point(1, 3, 0), new Point(1, 4, 0), new Point(1, 5, 0));
        assertEquals(new Point(1, 1, 0), ray.findClosestPoint(l3), "Error: findClosestPoint() for the first point is the closest does not work correctly");
        // TC04 The last point is the closest
        List<Point> l4 = List.of(new Point(1, 5, 0), new Point(1, 4, 0), new Point(1, 3, 0), new Point(1, 2, 0), new Point(1, 1, 0));
        assertEquals(new Point(1, 1, 0), ray.findClosestPoint(l4), "Error: findClosestPoint() for the last point is the closest does not work correctly");
    }
}

