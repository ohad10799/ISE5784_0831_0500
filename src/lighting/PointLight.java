package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

public class PointLight  extends Light implements LightSource
{
    protected final Point position;
    private double kc =1d;
    private double kl =1d;
    private double kq;

    public PointLight(Color intensity, Point position) {
        super(intensity);
        this.position = position;
    }


    @Override
    public Color getIntensity(Point p) {
        return null;
    }

    @Override
    public Vector getL(Point p) {
        return null;
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

