package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * Represents a tube in 3D space, defined by its radius and axis.
 * Extends RadialGeometry.
 */
public class Tube extends RadialGeometry {
    final protected Ray axis; // The axis of the tube.

    /**
     * Constructs a new Tube with the given radius and axis.
     *
     * @param radius The radius of the tube.
     * @param axis   The axis of the tube.
     */
    public Tube(double radius, Ray axis) {
        super(radius);
        this.axis = axis;
    }

    @Override
    public Vector getNormal(Point p1) {
        // Project p1 onto the tube's axis to find the closest point on the axis to p1
        Point p0 = axis.getHead(); // Starting point of the axis
        Vector v = axis.getDirection(); // Direction vector of the axis

        // Vector from p0 to p1
        Vector p0_p1 = p1.subtract(p0);

        // Projection of p0_p1 onto the axis direction vector
        double t = v.dotProduct(p0_p1);

        // Closest point on the axis to p1
        Point o = p0.add(v.scale(t));

        // The normal vector is the vector from o to p1
        return p1.subtract(o).normalize();

    }
}
