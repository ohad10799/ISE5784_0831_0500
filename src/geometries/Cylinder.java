package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

public class Cylinder extends Tube{
    final double height;

    public Cylinder(double radius, Ray axis, double height) {
        super(radius, axis);
        this.height = height;
    }
    @Override
    public Vector getNormal(Point p1) {
        return null;
    }
}
