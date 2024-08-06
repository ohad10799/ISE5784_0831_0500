package geometries;

import primitives.Color;
import primitives.Material;
import primitives.Point;
import primitives.Vector;

/**
 * Abstract class representing geometric shapes in a 3D scene.
 * This class extends the Intersectable abstract class.
 */
public abstract class Geometry extends Intersectable {
    protected Color emission = Color.BLACK;
    private Material material = new Material();

    /**
     * Abstract method to calculate the normal vector to the surface of the geometry
     * at a specified point.
     *
     * @param p1 the point on the surface of the geometry
     * @return the normal vector to the surface at the specified point
     */
    public abstract Vector getNormal(Point p1);

    /**
     * Gets the emission color of the geometry.
     *
     * @return the emission color of the geometry
     */
    public Color getEmission() {
        return emission;
    }

    /**
     * Sets the emission color of the geometry.
     *
     * @param emission the new emission color to set
     * @return the geometry object for method chaining
     */
    public Geometry setEmission(Color emission) {
        this.emission = emission;
        return this;
    }

    /**
     * Gets the material properties of the geometry.
     *
     * @return the material properties of the geometry
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * Sets the material properties of the geometry.
     *
     * @param material the new material properties to set
     * @return the geometry object for method chaining
     */
    public Geometry setMaterial(Material material) {
        this.material = material;
        return this;
    }
}
