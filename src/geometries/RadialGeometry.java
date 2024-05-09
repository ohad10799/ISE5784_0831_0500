package geometries;

public abstract class RadialGeometry implements Geometry {
    final double radius;

    public RadialGeometry(double radius) {
        this.radius = radius;
    }
}
