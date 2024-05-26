package geometries;

import primitives.Point;
import primitives.Vector;

/**
 * The Geometry interface represents geometric shapes.
 */
public interface Geometry extends Intersectable{
    /**
     * Calculates the normal vector to the surface of the geometric shape
     * at the specified point.
     *
     * @param p1 the point on the surface of the geometric shape where the normal is to be calculated
     * @return the normal vector to the surface at the specified point
     */
    public Vector getNormal(Point p1);
}
