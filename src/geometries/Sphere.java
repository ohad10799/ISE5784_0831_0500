package geometries;

import primitives.*;
import java.util.List;
import static primitives.Util.alignZero;

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
        Vector u = center.subtract(ray.getHead());
        double tm = alignZero(ray.getDirection().dotProduct(u));
        double d = alignZero(Math.sqrt(u.lengthSquared()-tm*tm));
        if (d>=radius){
            return null;
        }
        double th = alignZero(Math.sqrt(radius*radius-d*d));
        double t1 = tm + th;
        double t2 = tm - th;
        if (t1>0 && t2>0){
            return List.of(ray.getPoint(t1),ray.getPoint(t2));
        }
        if (t1>0){
            return List.of(ray.getPoint(t1));
        }
        if (t2>0){
            return List.of(ray.getPoint(t2));
        }
        return null;
    }
}
