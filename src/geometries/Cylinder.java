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

    @Override
    public Vector getNormal(Point p1) {
        return null;
    }
}

