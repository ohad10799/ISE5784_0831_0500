package geometries;

import primitives.Point;
import primitives.Vector;

/**
 * Represents a plane in 3D space.
 */
public class Plane implements Geometry {
    final private Point q; // A point on the plane.
    final private Vector normal; // The normal vector to the plane.

    /**
     * Constructs a plane from three points on the plane.
     *
     * @param p1 A point on the plane.
     * @param p2 A point on the plane.
     * @param p3 A point on the plane.
     */
    public Plane(Point p1, Point p2, Point p3) {
        // Vector from p1 to p2
        Vector v1 = p2.subtract(p1);
        // Vector from p1 to p3
        Vector v2 = p3.subtract(p1);
        // Cross product to get the normal vector
        normal = v1.crossProduct(v2).normalize();
        // One of the points on the plane
        q = p1;
    }

    /**
     * Constructs a plane from a point on the plane and its normal vector.
     *
     * @param q      A point on the plane.
     * @param normal The normal vector to the plane.
     */
    public Plane(Point q, Vector normal) {
        this.q = q;
        this.normal = normal.normalize();
    }

    /**
     * Returns the normal vector to the plane.
     *
     * @return The normal vector to the plane.
     */
    public Vector getNormal() {
        return normal;
    }

    @Override
    public Vector getNormal(Point p1) {
        return normal;
    }
}
