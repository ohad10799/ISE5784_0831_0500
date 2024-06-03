package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RayTest {

    @Test
    void testGetPoint() {
        Ray ray = new Ray(new Point(1,0,0),new Vector(0,1,0));
        // ============ Equivalence Partitions Tests ==============
        // TC01: positive distance
        assertEquals(new Point(1,2,0),ray.getPoint(2),"Error: getPoint() for positive distance does not work correctly");

        // TC02: negative distance
        assertEquals(new Point(1,-2,0),ray.getPoint(-2),"Error: getPoint() for negative distance does not work correctly");
        // ================= Boundary Values Tests ==================
        // TC03: distance = 0
        assertEquals(new Point(1,0,0),ray.getPoint(0),"Error: getPoint() for distance = 0 does not work correctly");
    }
}

