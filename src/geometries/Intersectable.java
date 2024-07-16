package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.List;

/**
 * Abstract class representing geometric shapes that can be intersected by rays.
 * Subclasses must implement methods to find intersection points with rays.
 */
public abstract class Intersectable {

    /**
     * Finds the intersection points of the specified ray with the geometric shape.
     *
     * @param ray the ray to check for intersections with the geometric shape
     * @return a list of points where the ray intersects the geometric shape,
     *         or {@code null} if there are no intersections
     */
    public List<Point> findIntersections(Ray ray) {
        var geoList = findGeoIntersections(ray);
        return geoList == null ? null : geoList.stream().map(gp -> gp.point).toList();
    }

    /**
     * Finds the intersection points of the specified ray with the geometric shape,
     * considering no maximum distance (infinity).
     *
     * @param ray the ray to check for intersections with the geometric shape
     * @return a list of GeoPoints representing intersections with the geometric shape
     */
    public final List<GeoPoint> findGeoIntersections(Ray ray) {
        return findGeoIntersectionsHelper(ray, Double.POSITIVE_INFINITY);
    }

    /**
     * Finds the intersection points of the specified ray with the geometric shape,
     * considering a maximum distance from the ray origin.
     *
     * @param ray      the ray to check for intersections with the geometric shape
     * @param distance the maximum distance from the ray origin to consider intersections
     * @return a list of GeoPoints representing intersections with the geometric shape
     */
    public final List<GeoPoint> findGeoIntersections(Ray ray, double distance) {
        return findGeoIntersectionsHelper(ray, distance);
    }

    /**
     * Helper method to be implemented by subclasses, finding intersection points
     * of the specified ray with the geometric shape.
     *
     * @param ray      the ray to check for intersections with the geometric shape
     * @param distance the maximum distance from the ray origin to consider intersections
     * @return a list of GeoPoints representing intersections with the geometric shape
     */
    protected abstract List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double distance);

    /**
     * Inner class representing a geometric intersection point with its corresponding geometry.
     */
    public static class GeoPoint {
        public final Geometry geometry; // The geometry that this point intersects
        public final Point point; // The actual intersection point

        /**
         * Constructor for GeoPoint.
         *
         * @param geometry The geometry that the point belongs to.
         * @param point    The intersection point.
         */
        public GeoPoint(Geometry geometry, Point point) {
            this.geometry = geometry;
            this.point = point;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof GeoPoint geoPoint)) return false;
            return geometry.equals(geoPoint.geometry) && point.equals(geoPoint.point);
        }


        @Override
        public String toString() {
            return "GeoPoint{" +
                    "geometry=" + geometry +
                    ", point=" + point +
                    '}';
        }
    }


}
