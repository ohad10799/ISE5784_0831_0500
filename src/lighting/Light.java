package lighting;

import primitives.Color;
import primitives.Point;

public abstract class Light {
    protected Color intensity = Color.BLACK;

    protected Light(Color intensity) {
        this.intensity = intensity;
    }

    public Color getIntensity() {
        return intensity;
    }

    public double getDistance(Point p) {
        // default distance value for light
        return Double.POSITIVE_INFINITY;
    }


//    public Light setIntensity(double r, double g, double b) {
//    public Light setIntensity(Color intensity) {
//        this.intensity = intensity;
//        return this;
//    }

}
