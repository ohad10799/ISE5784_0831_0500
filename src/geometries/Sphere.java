package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

/**
 * Represents a sphere in 3D space, defined by its center point and radius.
 * Extends RadialGeometry.
 */
public class Sphere extends RadialGeometry {
    final private Point center; // The center point of the sphere.

    /**
     * Constructs a new Sphere with the given radius and center point.
     *
     * @param radius The radius of the sphere.
     * @param center The center point of the sphere.
     */
    public Sphere(double radius, Point center) {
        super(radius);
        this.center = center;
    }

    @Override
    public Vector getNormal(Point p1) {
        // Calculate the normal vector by subtracting the center from the point and normalize the vector
        return p1.subtract(center).normalize();
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        return null;
    }
}
