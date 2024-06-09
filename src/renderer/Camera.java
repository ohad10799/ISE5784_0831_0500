package renderer;

import primitives.*;

public class Camera implements Cloneable {


    private Point location;
    private Vector vTo;
    private Vector vUp;
    private Vector vRight;
    private  double distance = 0;
    private double width = 0;
    private double height = 0;


    private Camera() {
    }

    public Point getLocation() {
        return location;
    }

    public Vector getvTo() {
        return vTo;
    }

    public Vector getvUp() {
        return vUp;
    }

    public Vector getvRight() {
        return vRight;
    }

    public double getDistance() {
        return distance;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public Ray constructRay(int nX, int nY, int j, int i) {
        return null;
    }


    public static class Builder{
        public Builder setRayTracer(SimpleRayTracer test) {
            return this;
        }
    }
    @Override
    public Camera clone(){
        return this;
    }

}
