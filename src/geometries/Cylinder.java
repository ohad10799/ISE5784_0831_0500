package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * Represents a cylinder in 3D space, extending along a given axis with a specified height.
 * Inherits from the Tube class.
 */
public class Cylinder extends Tube {
    private final double height; // The height of the cylinder.

    /**
     * Constructs a new Cylinder with the given radius, axis, and height.
     *
     * @param radius The radius of the cylinder.
     * @param axis   The axis of the cylinder.
     * @param height The height of the cylinder.
     */
    public Cylinder(double radius, Ray axis, double height) {
        super(radius, axis);
        this.height = height;
    }

    /**
     * Returns the normal vector at a given point on the cylinder's surface.
     * Not implemented in this class.
     *
     * @param point The point on the cylinder's surface.
     * @return null since the normal vector calculation is not implemented.
     */
    @Override
    public Vector getNormal(Point point) {
        // Get the starting point and direction of the axis
        Point p0 = axis.getHead();
        Vector v = axis.getDirection();

        if (p0.equals(point)) {
            return v;
        }

        // Vector from p0 to p1
        Vector p0_p1 = point.subtract(p0);

        // Projection of p0_p1 onto the axis direction vector
        double t = v.dotProduct(p0_p1);

        // Check if the point is on the bottom base
        if (t <= 0) {
            return v.scale(-1); // The normal vector points downward
        }

        // Check if the point is on the top base
        if (t >= height) {
            return v; // The normal vector points upward
        }

        // The point is on the curved surface
        // Closest point on the axis to p1
        Point o = p0.add(v.scale(t));

        // The normal vector is the vector from o to p1
        return point.subtract(o).normalize();
    }
}

