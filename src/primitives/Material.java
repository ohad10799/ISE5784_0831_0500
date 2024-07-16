package primitives;

/**
 * The Material class represents the material properties of an object.
 * It includes parameters such as diffuse reflection coefficient (kd),
 * specular reflection coefficient (ks), transmittance coefficient (kt),
 * reflectance coefficient (kr), and shininess.
 */
public class Material {
    public Double3 kD = Double3.ZERO; // The diffuse reflection coefficient
    public Double3 kS = Double3.ZERO; // The specular reflection coefficient
    public Double3 kT  = Double3.ZERO; // The transmittance coefficient
    public Double3 kR = Double3.ZERO; // The reflectance coefficient

    public int shininess = 1; // The shininess of the material

    /**
     * Sets the diffuse reflection coefficient of the material.
     *
     * @param kD the diffuse reflection coefficient
     * @return the Material object with the updated diffuse reflection coefficient
     */
    public Material setkD(Double3 kD) {
        this.kD = kD;
        return this;
    }

    /**
     * Sets the diffuse reflection coefficient of the material with a scalar value.
     *
     * @param kd the scalar value for the diffuse reflection coefficient
     * @return the Material object with the updated diffuse reflection coefficient
     */
    public Material setkD(double kd) {
        this.kD = new Double3(kd);
        return this;
    }

    /**
     * Sets the specular reflection coefficient of the material.
     *
     * @param kS the specular reflection coefficient
     * @return the Material object with the updated specular reflection coefficient
     */
    public Material setkS(Double3 kS) {
        this.kS = kS;
        return this;
    }

    /**
     * Sets the specular reflection coefficient of the material with a scalar value.
     *
     * @param ks the scalar value for the specular reflection coefficient
     * @return the Material object with the updated specular reflection coefficient
     */
    public Material setkS(double ks) {
        this.kS = new Double3(ks);
        return this;
    }

    /**
     * Sets the transmittance coefficient of the material.
     *
     * @param kT the transmittance coefficient
     * @return the Material object with the updated transmittance coefficient
     */
    public Material setkT(Double3 kT) {
        this.kT = kT;
        return this;
    }

    /**
     * Sets the transmittance coefficient of the material with a scalar value.
     *
     * @param kT the scalar value for the transmittance coefficient
     * @return the Material object with the updated transmittance coefficient
     */
    public Material setkT(double kT) {
        this.kT = new Double3(kT);
        return this;
    }

    /**
     * Sets the reflectance coefficient of the material.
     *
     * @param kR the reflectance coefficient
     * @return the Material object with the updated reflectance coefficient
     */
    public Material setkR(Double3 kR) {
        this.kR = kR;
        return this;
    }

    /**
     * Sets the reflectance coefficient of the material with a scalar value.
     *
     * @param kR the scalar value for the reflectance coefficient
     * @return the Material object with the updated reflectance coefficient
     */
    public Material setkR(double kR) {
        this.kR = new Double3(kR);
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
