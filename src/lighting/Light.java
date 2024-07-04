package lighting;

import primitives.Color;
import primitives.Point;

/**
 * The Light class represents a generic light source.
 * It provides the basic properties and methods that all types of lights share.
 */
public abstract class Light {
    protected Color intensity = Color.BLACK;

    /**
     * Constructs a light with the specified intensity.
     *
     * @param intensity the intensity of the light
     */
    protected Light(Color intensity) {
        this.intensity = intensity;
    }

    /**
     * Returns the intensity of the light.
     *
     * @return the intensity of the light
     */
    public Color getIntensity() {
        return intensity;
    }

    /**
     * Returns the distance from the light source to a given point.
     * The default implementation returns positive infinity, indicating that the light
     * source is very far away (e.g., directional light).
     *
     * @param p the point to measure the distance to
     * @return the distance from the light source to the point
     */
    public double getDistance(Point p) {
        // default distance value for light
        return Double.POSITIVE_INFINITY;
    }


}
