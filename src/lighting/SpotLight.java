package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * The SpotLight class represents a spotlight source in a scene.
 * A spotlight is a type of point light that emits light in a specific direction.
 * The intensity of the light is attenuated based on the direction of the spotlight.
 */
public class SpotLight extends PointLight {
    private final Vector direction;

    /**
     * Constructs a spotlight with the specified intensity, position, and direction.
     *
     * @param intensity the intensity of the light
     * @param position the position of the light source
     * @param direction the direction in which the spotlight is pointing
     */
    public SpotLight(Color intensity, Point position, Vector direction) {
        super(intensity, position);
        this.direction = direction.normalize();
    }

    @Override
    public SpotLight setKl(double kl) {
        super.setKl(kl);
        return this;
    }

    @Override
    public SpotLight setKq(double kq) {
        super.setKq(kq);
        return this;
    }

    @Override
    public SpotLight setKc(double kc) {
        super.setKc(kc);
        return this;
    }

    @Override
    public Color getIntensity(Point p) {
        Color baseIntensity = super.getIntensity(p);
        return baseIntensity.scale(Math.max(0, direction.dotProduct(getL(p))));
    }
}
