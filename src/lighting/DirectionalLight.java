package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * The DirectionalLight class represents a light source that has a specific direction and intensity.
 * This type of light is similar to sunlight, which has parallel rays and does not diminish with distance.
 */
public class DirectionalLight extends Light implements LightSource {
    private final Vector direction;

    /**
     * Constructs a directional light with the specified intensity and direction.
     *
     * @param intensity the intensity of the light
     * @param direction the direction of the light
     */
    public DirectionalLight(Color intensity, Vector direction) {
        super(intensity);
        this.direction = direction.normalize();
    }

    @Override
    public Color getIntensity(Point p) {
        return intensity;
    }

    @Override
    public Vector getL(Point p) {
        return direction;
    }

}