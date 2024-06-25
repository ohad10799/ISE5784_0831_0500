package primitives;

public class Material {
    private Double3 kd = new Double3(0d);
    private Double3 ks = new Double3(0d);
    private int shininess =1;

    public Material setKd(Double3 kd) {
        this.kd = kd;
        return this;
    }
 public Material setKd(double kd) {
        this.kd = new Double3(kd);
        return this;
    }

    public Material setKs(Double3 ks) {
        this.ks = ks;
        return this;
    }
    public Material setKs(double ks) {
        this.ks = new Double3(ks);
        return this;
    }

    public Material setShininess(int shininess) {
        this.shininess = shininess;
        return this;
    }
}
