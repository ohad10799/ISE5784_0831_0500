package lighting;

import primitives.Color;
import primitives.Double3;

public class AmbientLight  extends Light{
    public final static AmbientLight NONE = new AmbientLight(Color.BLACK, 0.0);

    public AmbientLight(Color iA, Double3 kA) {
        super(iA.scale(kA));
    }

    public AmbientLight(Color iA, double kA) {
        super(iA.scale(kA));
    }

//    //*********************
//    public AmbientLight() {
//        super( Color.BLACK);
//    }

}
