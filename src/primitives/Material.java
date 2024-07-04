package primitives;

/**
 * The Material class represents the material properties of an object.
 * It includes parameters such as diffuse reflection coefficient (kd),
 * specular reflection coefficient (ks), and shininess.
 */
public class Material {
    public Double3 kd = Double3.ZERO; // The diffuse reflection coefficient
    public Double3 ks = Double3.ZERO; // The specular reflection coefficient
    public int shininess = 1; // The shininess of the material

    /**
     * Sets the diffuse reflection coefficient of the material.
     *
     * @param kd the diffuse reflection coefficient
     * @return the Material object with the updated diffuse reflection coefficient
     */
    public Material setKd(Double3 kd) {
        this.kd = kd;
        return this;
    }

    /**
     * Sets the diffuse reflection coefficient of the material with a scalar value.
     *
     * @param kd the scalar value for the diffuse reflection coefficient
     * @return the Material object with the updated diffuse reflection coefficient
     */
    public Material setKd(double kd) {
        this.kd = new Double3(kd);
        return this;
    }

    /**
     * Sets the specular reflection coefficient of the material.
     *
     * @param ks the specular reflection coefficient
     * @return the Material object with the updated specular reflection coefficient
     */
    public Material setKs(Double3 ks) {
        this.ks = ks;
        return this;
    }

    /**
     * Sets the specular reflection coefficient of the material with a scalar value.
     *
     * @param ks the scalar value for the specular reflection coefficient
     * @return the Material object with the updated specular reflection coefficient
     */
    public Material setKs(double ks) {
        this.ks = new Double3(ks);
        return this;
    }

    /**
     * Sets the shininess of the material.
     *
     * @param shininess the shininess value
     * @return the Material object with the updated shininess
     */
    public Material setShininess(int shininess) {
        this.shininess = shininess;
        return this;
    }
}
