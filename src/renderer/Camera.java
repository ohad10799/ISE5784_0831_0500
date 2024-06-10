package renderer;

import primitives.*;

import java.util.MissingResourceException;

import static primitives.Util.isZero;

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

        private final Camera camera = new Camera();

        public Builder setLocation(Point location) {
            camera.location = location;
            return this;
        }

        public Builder setDirection(Vector vTo, Vector vUp) {
           if (vTo.dotProduct(vUp) != 0)
               throw new IllegalArgumentException("vTo and vUp are not orthogonal");
            camera.vTo = vTo.normalize();
            camera.vUp = vUp.normalize();
            return this;
        }

        public Builder setVpSize(double width, double height) {
            camera.width = width;
            camera.height = height;
            return this;
        }

        public Builder setVpDistance(double distance) {
            camera.distance = distance;
            return this;
        }


        //************************ need check if 0!!!! ************************
        public Camera build() {
            if (camera.location == null)
                throw new MissingResourceException("Missing parameter", "Camera", "location");
            if (camera.vTo == null)
                throw new MissingResourceException("Missing parameter", "Camera", "vTo");
            if (camera.vUp == null)
                throw new MissingResourceException("Missing parameter", "Camera", "vUp");
            if (isZero(camera.width))
                throw new MissingResourceException("Missing parameter", "Camera", "width");
            if (isZero(camera.height))
                throw new MissingResourceException("Missing parameter", "Camera", "height");
            if (isZero(camera.distance))
                throw new MissingResourceException("Missing parameter", "Camera", "distance");
            if (camera.width<0){
                throw new IllegalArgumentException("width can't be negative");
            }
            if (camera.height<0){
                throw new IllegalArgumentException("height can't be negative");
            }
            if (camera.distance<0){
                throw new IllegalArgumentException("distance can't be negative");
            }
            // **************** check if needed!! *****************
            if (camera.vTo.dotProduct(camera.vUp) != 0)
                throw new IllegalArgumentException("vTo and vUp are not orthogonal");

            camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();
            return (Camera) camera.clone();
        }

        public Builder setRayTracer(SimpleRayTracer test) {
            return this;
        }
    }
    @Override
    public Camera clone(){
        return this;
    }

}
