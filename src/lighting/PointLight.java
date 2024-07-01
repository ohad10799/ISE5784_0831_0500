package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

public class PointLight extends Light implements LightSource
{
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
//        double distance = position.distance(p);
//        double distanceSquared = distance * distance;
//        double factor = kc + kl * distance + kq * distanceSquared;
//
//        //Return the final intensity
//        return getIntensity().scale(1/factor);

        double d = position.distance(p);
        return intensity.scale((1/(kc + kl * d + kq * d * d)));
    }

    @Override
    public Vector getL(Point p) {
        try {
            return p.subtract(position).normalize();
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public double getDistance(Point p) {
        return position.distance(p);
    }

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

