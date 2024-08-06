package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.ArrayList;
import java.util.List;

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
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray,double maxDistance) {
        List <Point> intersectionList = plane.findIntersections(ray);
        if(intersectionList == null)
            return null;
        Point intersectionPoint = intersectionList.getFirst();
        List <Double> listNum = new ArrayList<>();
        // Check if intersection point is inside the polygon
        for (int i = 0; i < vertices.size(); i++) {
            Vector v1 =  vertices.get(i).subtract(ray.getHead());
            Vector v2 =  vertices.get((i+1)%vertices.size()).subtract(ray.getHead());
            Vector n = v1.crossProduct(v2);
            double num = n.dotProduct(ray.getDirection());
            if (isZero(num)) {
                return null;
            }
            listNum.add(num);
        }
        if (listNum.stream().allMatch(value -> value > 0) || listNum.stream().allMatch(value -> value < 0)) {
            return List.of(new GeoPoint(this,intersectionPoint));
        }
        return null;
    }
}
