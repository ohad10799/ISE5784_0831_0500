package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.ArrayList;
import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;


/**
 * Represents a triangle in 3D space, defined by its three vertices.
 * Extends Polygon.
 */
public class Triangle extends Polygon {

    /**
     * Constructs a new Triangle with the given vertices.
     *
     * @param vertices The vertices of the triangle. Must contain exactly 3 points.
     * @throws IllegalArgumentException if the number of vertices is not 3.
     */
    public Triangle(Point... vertices) {
        super(vertices);
        if (vertices.length != 3) {
            throw new IllegalArgumentException("A triangle must have 3 vertices");
        }
    }

    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray,double maxDistance) {

        // Find intersections with the underlying plane
        List <GeoPoint> lst = plane.findGeoIntersectionsHelper(ray,maxDistance);
        if(lst == null)
            return null;

        // Get the origin point of the ray
        Point p0 = ray.getHead();
        Vector v = ray.getDirection();

        // Define vectors from the origin point to each vertex of the polygon
        Point p1 = this.vertices.get(0);
        Point p2 = this.vertices.get(1);
        Point p3 = this.vertices.get(2);

        // Calculate vectors from the origin point to each vertex
        Vector v1 = p0.subtract(p1);
        Vector v2 = p0.subtract(p2);
        Vector v3 = p0.subtract(p3);

        // Calculate normals for each edge of the polygon
        Vector n1 = v1.crossProduct(v2).normalize();
        Vector n2 = v2.crossProduct(v3).normalize();
        Vector n3 = v3.crossProduct(v1).normalize();

        // Calculate dot products between ray direction and each normal
        double num1 = v.dotProduct(n1);
        double num2 = v.dotProduct(n2);
        double num3 = v.dotProduct(n3);

        // Check if all dot products have the same sign, if so, return the intersections
        if(alignZero(num1) > 0 && alignZero(num2) > 0 && alignZero(num3) > 0 ||
                alignZero(num1) < 0 && alignZero(num2) < 0 && alignZero(num3) < 0)
            return List.of(new GeoPoint(this, lst.get(0).point));

        // Otherwise, return null indicating no intersections
        return null;
    }
}
