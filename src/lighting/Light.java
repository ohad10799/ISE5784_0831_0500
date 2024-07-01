package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

public abstract class Light {
    protected Color intensity = Color.BLACK;

    protected Light(Color intensity) {
        this.intensity = intensity;
    }
    public Color getIntensity() {
        return intensity;
    }


//    public Light setIntensity(double r, double g, double b) {
//    public Light setIntensity(Color intensity) {
//        this.intensity = intensity;
//        return this;
//    }

}
