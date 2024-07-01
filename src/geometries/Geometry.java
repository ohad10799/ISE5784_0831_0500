package geometries;

import primitives.Color;
import primitives.Material;
import primitives.Point;
import primitives.Vector;

/**
 * The Geometry interface represents geometric shapes.
 */
public abstract class Geometry extends Intersectable {
    protected Color emission = Color.BLACK;
    private Material material = new Material();
    /**
     * Calculates the normal vector to the surface of the geometric shape
     * at the specified point.
     *
     * @param p1 the point on the surface of the geometric shape where the normal is to be calculated
     * @return the normal vector to the surface at the specified point
     */
    public abstract Vector getNormal(Point p1);

    public Color getEmission() {
        return emission;
    }

    public Geometry setEmission(Color emission) {
        this.emission = emission;
        return this;
    }

    public Geometry setMaterial(Material material) {
        this.material = material;
        return this;
    }

    public Material getMaterial() {
        return material;
    }
}
