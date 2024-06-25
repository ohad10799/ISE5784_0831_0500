package geometries;

/**
 * Abstract class representing a radial geometry in 3D space.
 * Includes a radius parameter common to all radial geometries.
 */
public abstract class RadialGeometry extends Geometry {
    final protected double radius; // The radius of the radial geometry.

    /**
     * Constructs a new radial geometry with the given radius.
     *
     * @param radius The radius of the radial geometry.
     */
    public RadialGeometry(double radius) {
        this.radius = radius;
    }
}
