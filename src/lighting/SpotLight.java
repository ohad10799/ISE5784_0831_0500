package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

public class SpotLight extends PointLight {

public SpotLight(Color intensity, Point position, Vector direction) {
    super(intensity, position);
    }
}
