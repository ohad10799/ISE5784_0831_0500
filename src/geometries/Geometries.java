package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Geometries implements Intersectable{
    final private List<Intersectable> geometries = new LinkedList<>();

    public Geometries() {
    }
    public Geometries(Intersectable... geometries){
        add(geometries);
    }
    public void add(Intersectable... geometries){
        Collections.addAll(this.geometries, geometries);
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        // Initialize the result list to null
        List<Point> result = null;

        // Iterate through each geometry in the scene
        for (Intersectable geo:geometries){
            List<Point> geoPoints = geo.findIntersections(ray);

            // If intersection points exist for the current geometry
            if(geoPoints!=null){
                if (result==null){
                    result = new LinkedList<>();
                }

                // Add all intersection points of the current geometry to the result list
                result.addAll(geoPoints);

            }
        }
        // Return the list of intersection points found (may be null if no intersections exist)
        return result;
    }
}
