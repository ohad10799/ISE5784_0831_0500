package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

public class PointLight extends Light implements LightSource {
    protected final Point position;
    private double kc = 1d;
    private double kl = 0;
    private double kq = 0;

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
    public Vector getL(Point p) {
        try {
            return p.subtract(position).normalize();
        } catch (IllegalArgumentException zeroVectorException) {
            return null;
        }
    }

    @Override
    public double getDistance(Point p) {
        return position.distance(p);
    }

    /**
     * Set the constant attenuation factor with chaining method
     *
     * @param kl
     * @return
     */
    public PointLight setKl(double kl) {
        this.kl = kl;
        return this;
    }

    public PointLight setKq(double kq) {
        this.kq = kq;
        return this;
    }

    public PointLight setKc(double kc) {
        this.kc = kc;
        return this;
    }
}

