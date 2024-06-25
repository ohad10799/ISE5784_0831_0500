package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the primitives.Point class.
 *
 * @author Ohad and Meir
 */
class PointTest {

    Point p1 = new Point(1, 2, 3);

    /**
     * Test method for {@link primitives.Point#subtract(primitives.Point)}.
     */
    @Test
    void testSubtract() {
        // ============ Equivalence Partitions Tests ==============
        // subtract two non-zero not equal or opposite Points
        assertEquals(new Vector(-1, -1, -1), p1.subtract(new Point(2, 3, 4)), "ERROR: subtract two regular points function not doing well");

        // =============== Boundary Values Tests ==================
        // subtract points with itself
        assertThrows(IllegalArgumentException.class, () -> p1.subtract(p1),
                "ERROR: point + -itself does not throw an exception");
    }

    /**
     * Test method for {@link primitives.Point#add(primitives.Vector)}.
     */
    @Test
    void testAdd() {
        // ============ Equivalence Partitions Tests ==============
        // add regular Point and vector
        assertEquals(new Point(3, 5, 7), p1.add(new Vector(2, 3, 4)), "ERROR: testAdd() regular point and vector not doing well");
    }

    /**
     * Test method for {@link primitives.Point#distanceSquared(primitives.Point)}.
     */
    @Test
    void testDistanceSquared() {
        // ============ Equivalence Partitions Tests ==============
        // distance of 2 regular points
        assertEquals(3, p1.distanceSquared(new Point(2, 3, 4)), "ERROR: testAdd() regular point and vector not doing well");

    }

    /**
     * Test method for {@link primitives.Point#distance(primitives.Point)}.
     */
    @Test
    void testDistance() {
        // ============ Equivalence Partitions Tests ==============
        // distance of 2 regular points
        assertEquals(Math.sqrt(3), p1.distance(new Point(2, 3, 4)), "ERROR: testAdd() regular point and vector not doing well");

    }
}
