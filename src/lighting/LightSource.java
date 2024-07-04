package lighting;

import primitives.*;

/**
 * The LightSource interface represents a light source in a scene.
 * It provides methods to get the light's intensity, direction, and distance at a specific point.
 */
public interface LightSource {

    /**
     * Returns the intensity of the light at a given point.
     *
     * @param p the point at which to get the light intensity
     * @return the intensity of the light
     */
    public Color getIntensity(Point p);

    /**
     * Returns the direction of the light at a given point.
     *
     * @param p the point at which to get the light direction
     * @return the direction of the light
     */
    public Vector getL(Point p);

    /**
     * Returns the distance from the light source to a given point.
     *
     * @param p the point to measure the distance to
     * @return the distance from the light source to the point
     */
    public double getDistance(Point p);
}
