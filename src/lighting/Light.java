package lighting;

import primitives.Color;

public abstract class Light {
    protected  Color intensity = Color.BLACK;

    public Color getIntensity() {
        return intensity;
    }

    public Light(Color intensity) {
        this.intensity = intensity;
    }

    public Light setIntensity(Color intensity) {
        this.intensity = intensity;
        return this;
    }

}
