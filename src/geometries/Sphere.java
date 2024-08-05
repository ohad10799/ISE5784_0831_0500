package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import renderer.BoundingBox;

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
    public Vector getNormal(Point point) {
        // Calculate the normal vector by subtracting the center from the point and normalize the vector
        return point.subtract(center).normalize();
    }

    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double distance) {
        // Check if the ray's head coincides with the sphere's center
        if (ray.getHead().equals(center)) {
            // If so, return a list containing the point on the ray at a distance of the radius from its head
            return List.of(new GeoPoint(this, ray.getPoint(radius)));
        }
        // Calculate necessary parameters for intersection calculation
        Vector u = center.subtract(ray.getHead());
        double tm = alignZero(ray.getDirection().dotProduct(u));
        double d = alignZero(Math.sqrt(u.lengthSquared() - tm * tm));
        // Check for no intersections
        if (d >= radius || radius >= distance) {
            return null;
        }
        // Calculate distances from the intersection points to the ray's head
        double th = alignZero(Math.sqrt(radius * radius - d * d));
        double t1 = alignZero(tm + th);
        double t2 = alignZero(tm - th);

        // Determine intersection points based on distances
        if (t1 > 0 && t2 > 0 && t1 < distance && t2 < distance) {
            return List.of(new GeoPoint(this, ray.getPoint(t1)), new GeoPoint(this, ray.getPoint(t2)));
        }
        if (t1 > 0 && t1 < distance) {
            return List.of(new GeoPoint(this, ray.getPoint(t1)));
        }
        if (t2 > 0 && t2 < distance) {
            return List.of(new GeoPoint(this, ray.getPoint(t2)));
        }
        // If no valid intersections found, return null
        return null;
    }

    @Override
    public BoundingBox getBoundingBox() {
        Point min = new Point(center.getX() - radius, center.getY() - radius, center.getZ() - radius);
        Point max = new Point(center.getX() + radius, center.getY() + radius, center.getZ() + radius);
        return new BoundingBox(min, max);
    }
}
