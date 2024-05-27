package geometries;
import primitives.*;

import java.util.List;

/**
 * The Intersectable interface represents objects that can be intersected by rays.
 */
public interface Intersectable  {
    /**
     * Finds the intersection points of the specified ray with the geometric shape.
     *
     * @param ray the ray to check for intersections with the geometric shape
     * @return a list of points where the ray intersects the geometric shape, or an empty list if there are no intersections
     */
    List<Point> findIntersections(Ray ray);
}
