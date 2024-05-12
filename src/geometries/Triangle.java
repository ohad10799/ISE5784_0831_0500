package geometries;

import primitives.Point;

public class Triangle extends Polygon{

    public Triangle(Point... vertices) {
        super(vertices);
        if (vertices.length!=3) {
            throw new IllegalArgumentException("A triangle must have 3 vertices");
        }

    }
}
