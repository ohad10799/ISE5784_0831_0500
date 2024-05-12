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
        return null;
    }
}
