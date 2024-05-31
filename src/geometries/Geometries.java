package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.LinkedList;
import java.util.List;

public class Geometries implements Intersectable{
    final private List<Intersectable> l1 = new LinkedList<Intersectable>();

    public Geometries() {
    }
    public Geometries(Intersectable... geometries){

    }
    public void add(Intersectable... geometries){

    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        return null;
    }
}
