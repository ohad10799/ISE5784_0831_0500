package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Geometries implements Intersectable{
    final private List<Intersectable> geometries = new LinkedList<Intersectable>();

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
        List<Point> result = null;
        for (Intersectable geo:geometries){
            List<Point> geoPoints = geo.findIntersections(ray);
            if(geoPoints!=null){
                if (result==null){
                    result = new LinkedList<>();
                }
                result.addAll(geoPoints);

            }
        }
        return result;
    }
}
