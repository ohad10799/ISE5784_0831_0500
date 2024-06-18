package lighting;

import primitives.Color;
import primitives.Double3;

public class AmbientLight {
    private final Color intensity;
    public final static AmbientLight NONE = new AmbientLight(Color.BLACK, 0.0);

    public AmbientLight(Color iA, double kA) {
        this.intensity = iA.scale(kA);
    }
    public AmbientLight(Color iA, Double3 kA) {
        this.intensity = iA.scale(kA);
    }
    public AmbientLight() {
        this.intensity = Color.BLACK;
    }

    public Color getIntensity() {
        return intensity;
    }
}
