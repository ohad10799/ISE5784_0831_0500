package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a collection of intersectable geometries in a scene.
 * Extends the Intersectable abstract class to provide intersection
 * calculations for all contained geometries.
 */
public class Geometries extends Intersectable {
    protected List<Intersectable> geometries = new LinkedList<>();

    /**
     * Constructs an empty collection of geometries.
     */
    public Geometries() {
    }

    /**
     * Constructs a collection of geometries initialized with the given geometries.
     *
     * @param geometries the initial geometries to add to this collection
     */
    public Geometries(Intersectable... geometries) {
        add(geometries);
    }

    /**
     * Adds one or more geometries to this collection.
     *
     * @param geometries the geometries to add
     */
    public void add(Intersectable... geometries) {
        Collections.addAll(this.geometries, geometries);
    }

    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double distance) {
        // Initialize the result list to null
        List<GeoPoint> result = null;

        // Iterate through each geometry in the scene
        for (Intersectable geo : geometries) {
            List<GeoPoint> geoPoints = geo.findGeoIntersections(ray,distance);

            // If intersection points exist for the current geometry
            if (geoPoints != null) {
                if (result == null) {
                    result = new LinkedList<>();
                }

                // Add all intersection points of the current geometry to the result list
                result.addAll(geoPoints);
            }
        }
        // Return the list of intersection points found (may be null if no intersections exist)
        return result;
    }

    @Override
    public List<Point> minMaxPoints() {
        // set the objects to default values
        Point min = Point.POSITIVE_INFINITE;
        Point max = Point.NEGATIVE_INFINITE;

        for (Intersectable obj : geometries) {
            List<Point> objMinMax = obj.minMaxPoints();
            min = Point.findMinimum(List.of(min, objMinMax.get(0)));
            max = Point.findMaximum(List.of(max, objMinMax.get(1)));
        }

        return List.of(min, max);
    }
}
