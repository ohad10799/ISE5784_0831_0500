package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the primitives.Vector class.
 *
 * @author Ohad and Meir
 */
class VectorTest {
    Vector v1 = new Vector(1, 2, 3);
    Vector v1Opposite = new Vector(-1, -2, -3);
    Vector v2 = new Vector(2, 3, 4);

    /**
     * Test method for {@link primitives.Vector(double,double,double)#}.
     */
    @Test
    void testConstructorWithThreeValues() {
        // ============ Equivalence Partitions Tests ==============

        // Test normal vector creation
        assertEquals(new Double3(1, 2, 3), v1.xyz, "Vector should have correct coordinates");

        // =============== Boundary Values Tests ==================

        // Test zero vector creation, should throw an exception
        assertThrows(IllegalArgumentException.class, () -> new Vector(0, 0, 0),
                "Creating a zero vector should throw IllegalArgumentException");

        // Test non-zero values close to 0 does not throw an exception
        assertDoesNotThrow(() -> new Vector(0.001, 0.001, 0.001),
                "Creating a vector with non-zero values should not throw an IllegalArgumentException");
    }

    /**
     * Test method for {@link primitives.Vector(primitives.Double3)#}.
     */
    @Test
    void testConstructorWithWithDouble3() {
        // ============ Equivalence Partitions Tests ==============

        // Test normal vector creation
        Double3 xyz = new Double3(1, 2, 3);
        Vector v = new Vector(xyz);
        assertEquals(xyz, v.xyz, "Vector should have correct coordinates");

        // =============== Boundary Values Tests ==================

        // Test zero vector creation, should throw an exception
        Double3 zero = new Double3(0, 0, 0);
        assertThrows(IllegalArgumentException.class, () -> new Vector(zero),
                "Creating a zero vector should throw IllegalArgumentException");

        // Test non-zero values close to 0 does not throw an exception
        Double3 zero1 = new Double3(0.001, 0.001, 0.001);
        assertDoesNotThrow(() -> new Vector(zero1),
                "Creating a vector with non-zero values should not throw an exception");
    }

    /**
     * Test method for {@link primitives.Vector#add(primitives.Vector)}.
     */
    @Test
    void testAdd() {
        // ============ Equivalence Partitions Tests ==============
        // add two non zero not equal or opposite vectors
        assertEquals(new Vector(3, 5, 7), v1.add(new Vector(2, 3, 4)), "ERROR: add two regular vectors function not doing well");

        // =============== Boundary Values Tests ==================
        // add Vector with the opposite equal vector
        assertThrows(IllegalArgumentException.class, () -> v1.add(v1Opposite),
                "ERROR: Vector + -itself does not throw an exception");
        // add Vector with the opposite not equal vector
        assertEquals(new Vector(-2, -4, -6), v1.add(new Vector(-3, -6, -9)), "ERROR: Vector + -itself does not throw an exception");

    }

    /**
     * Test method for {@link primitives.Vector#scale(double)}.
     */
    @Test
    void testScale() {
        // ============ Equivalence Partitions Tests ==============
        // Vector with positive scale
        assertEquals(new Vector(2, 4, 6), v1.scale(2), "ERROR: scale() function not doing well");

        // Vector with negative scale
        assertEquals(new Vector(-2, -4, -6), v1.scale(-2), "ERROR: scale() function not doing well");

        // =============== Boundary Values Tests ==================
        // scale is zero
        assertThrows(IllegalArgumentException.class, () -> v1.scale(0),
                "ERROR: zero scale not throw exception!");

    }

    /**
     * Test method for {@link primitives.Vector#dotProduct(primitives.Vector)}.
     */
    @Test
    void testDotProduct() {
        // ============ Equivalence Partitions Tests ==============
        // dot product with regular vectors
        assertEquals(20, v1.dotProduct(v2), "ERROR: result of dotProduct() is not correct!");

        // =============== Boundary Values Tests ==================
        // vectors are vertical
        assertEquals(0, v1.dotProduct(new Vector(2, -1, 0)), "ERROR: dotProduct() for orthogonal vectors is not zero");
        // dot product with unit vector
        assertEquals(6, v1.dotProduct(new Vector(1, 1, 1)), "ERROR: result of dotProduct() is not correct for unit vector!");
    }

    /**
     * Test method for {@link primitives.Vector#crossProduct(primitives.Vector)}.
     */
    @Test
    void testCrossProduct() {
        // ============ Equivalence Partitions Tests ==============
        // cross product with regular vectors
        assertEquals(new Vector(-1, 2, -1), v1.crossProduct(v2), "ERROR: result of crossProduct() is not correct!");

        // =============== Boundary Values Tests ==================
        // crossProduct vector with itself
        assertThrows(IllegalArgumentException.class, () -> v1.crossProduct(v1),
                "ERROR: crossProduct() not throw exception with zero vector!");
        // crossProduct vector with same direction and different length
        assertThrows(IllegalArgumentException.class, () -> v1.crossProduct(new Vector(2, 4, 6)),
                "ERROR: crossProduct() not throw exception with zero vector!");
        // crossProduct with vectors with the equal vector in opposite direction
        assertThrows(IllegalArgumentException.class, () -> v1.crossProduct(v1Opposite),
                "ERROR: crossProduct() not throw exception with zero vector!");
        // cross product with regular vectors
        assertThrows(IllegalArgumentException.class, () -> v1.crossProduct(new Vector(-2, -4, -6)),
                "ERROR: crossProduct() not throw exception with zero vector!");
    }

    /**
     * Test method for {@link primitives.Vector#lengthSquared()}.
     */
    @Test
    void testLengthSquared() {
        // ============ Equivalence Partitions Tests ==============
        // lengthSquared with regular vector.
        assertEquals(14, v1.lengthSquared(), 0.0001, "ERROR: lengthSquared() wrong value");
    }

    /**
     * Test method for {@link primitives.Vector#length()}.
     */
    @Test
    void testLength() {
        // ============ Equivalence Partitions Tests ==============
        // lengthSquared with regular vector.
        assertEquals(Math.sqrt(14), v1.length(), 0.0001, "ERROR: length() wrong value");
    }

    /**
     * Test method for {@link primitives.Vector#normalize()}.
     */
    @Test
    void testNormalize() {
        // ============ Equivalence Partitions Tests ==============
        // normalize with regular positive vector
        assertEquals(new Vector(1 / Math.sqrt(14), 2 / Math.sqrt(14), 3 / Math.sqrt(14)), v1.normalize(), "ERROR: normalize() wrong value");
        // normalize with regular negative vector
        assertEquals(new Vector(-1 / Math.sqrt(14), -2 / Math.sqrt(14), -3 / Math.sqrt(14)), v1Opposite.normalize(), "ERROR: normalize() wrong value");
        // =============== Boundary Values Tests ==================
        // normalize with unit vector
        assertEquals(new Vector(1, 0, 0), new Vector(1, 0, 0).normalize(), "ERROR: normalize() wrong value");
    }
}
