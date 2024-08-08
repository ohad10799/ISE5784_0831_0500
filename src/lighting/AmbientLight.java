package lighting;

import primitives.Color;
import primitives.Double3;

/**
 * The AmbientLight class represents ambient light in a scene.
 * Ambient light is a background light that affects all objects equally.
 *
 * This class provides methods to create ambient light with various intensities.
 */
public class AmbientLight extends Light {

    /**
     * Represents a special case of ambient light with no intensity.
     * This constant is used to indicate the absence of ambient light in a scene.
     * It is effectively a black ambient light with zero intensity.
     */
    public final static AmbientLight NONE = new AmbientLight(Color.BLACK, 0.0);

    /**
     * Constructs an ambient light with the specified intensity and attenuation coefficient.
     *
     * @param iA the intensity of the ambient light
     * @param kA the attenuation coefficient
     */
    public AmbientLight(Color iA, Double3 kA) {
        super(iA.scale(kA));
    }

    /**
     * Constructs an ambient light with the specified intensity and attenuation coefficient.
     *
     * @param iA the intensity of the ambient light
     * @param kA the attenuation coefficient
     */
    public AmbientLight(Color iA, double kA) {
        super(iA.scale(kA));
    }


}
