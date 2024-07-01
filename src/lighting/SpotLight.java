package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

public class SpotLight extends PointLight {
private final Vector direction;

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
}
