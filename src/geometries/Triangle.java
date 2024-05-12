package geometries;

import primitives.Point;

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
}
