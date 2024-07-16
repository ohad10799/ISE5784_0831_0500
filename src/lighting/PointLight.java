package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * The PointLight class represents a point light source in a scene.
 * A point light has a specific position and its intensity diminishes with distance.
 */
public class PointLight extends Light implements LightSource {
    protected final Point position;
    private double kc = 1d;
    private double kl = 0;
    private double kq = 0;

    /**
     * Constructs a point light with the specified intensity and position.
     *
     * @param intensity the intensity of the light
     * @param position the position of the light
     */
    public PointLight(Color intensity, Point position) {
        super(intensity);
        this.position = position;
    }

    @Override
    public Color getIntensity(Point p) {
        double d = position.distance(p);
        return intensity.scale((1d / (kc + kl * d + kq * d * d)));
    }

    @Override
    public Vector getL(Point targetPoint) {
        try {
            return targetPoint.subtract(position).normalize();
        } catch (IllegalArgumentException zeroVectorException) {
            return null;
        }
    }

    @Override
    public double getDistance(Point p) {
        return position.distance(p);
    }

    /**
     * Sets the linear attenuation factor and returns the PointLight object for chaining.
     *
     * @param kl the linear attenuation factor
     * @return the PointLight object with the updated attenuation factor
     */
    public PointLight setKl(double kl) {
        this.kl = kl;
        return this;
    }

    /**
     * Sets the quadratic attenuation factor and returns the PointLight object for chaining.
     *
     * @param kq the quadratic attenuation factor
     * @return the PointLight object with the updated attenuation factor
     */
    public PointLight setKq(double kq) {
        this.kq = kq;
        return this;
    }

    /**
     * Sets the constant attenuation factor and returns the PointLight object for chaining.
     *
     * @param kc the constant attenuation factor
     * @return the PointLight object with the updated attenuation factor
     */
    public PointLight setKc(double kc) {
        this.kc = kc;
        return this;
    }
}

