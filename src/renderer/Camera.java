package renderer;
import primitives.*;
import java.util.MissingResourceException;
import static primitives.Util.isZero;

/**
 * Camera class represents a camera in 3D space.
 * The camera is defined by its location, and three orthogonal direction vectors.
 */
public class Camera implements Cloneable {

    private Point location;
    private Vector vTo;
    private Vector vUp;
    private Vector vRight;
    private double distance = 0;
    private double width = 0;
    private double height = 0;

    /**
     * Private constructor using Builder pattern.
     * @param builder The builder instance.
     */
    private Camera(Builder builder) {
        location = builder.location;
        vTo = builder.vTo;
        vUp = builder.vUp;
        vRight = builder.vRight;
        distance = builder.distance;
        width = builder.width;
        height = builder.height;
    }

    /**
     * @return The location of the camera.
     */
    public Point getLocation() {
        return location;
    }

    /**
     * @return The forward direction vector (vTo).
     */
    public Vector getvTo() {
        return vTo;
    }

    /**
     * @return The upward direction vector (vUp).
     */
    public Vector getvUp() {
        return vUp;
    }

    /**
     * @return The rightward direction vector (vRight).
     */
    public Vector getvRight() {
        return vRight;
    }

    /**
     * @return The distance of the camera to the view plane.
     */
    public double getDistance() {
        return distance;
    }

    /**
     * @return The width of the view plane.
     */
    public double getWidth() {
        return width;
    }

    /**
     * @return The height of the view plane.
     */
    public double getHeight() {
        return height;
    }

    /**
     * @return A new Builder instance to construct a Camera.
     */
    public static Builder getBuilder() {
        return new Builder();
    }

    /**
     * Constructs a ray through a specific pixel in the view plane.
     * @param nX Number of horizontal pixels in the view plane.
     * @param nY Number of vertical pixels in the view plane.
     * @param j Column index of the pixel.
     * @param i Row index of the pixel.
     * @return The ray through the pixel (j, i).
     */
    public Ray constructRay(int nX, int nY, int j, int i) {
        Point pC = location.add(vTo.scale(distance));
        double rY = height / nY;
        double rX = width / nX;
        double yi = -(i - ( nY - 1) / 2d) * rY;
        double xj = (j - (nX - 1) / 2d) * rX;
        Point pIJ = pC;
        if (!isZero(xj)) {
            pIJ = pIJ.add(vRight.scale(xj));
        }
        if (!isZero(yi)) {
            pIJ = pIJ.add(vUp.scale(yi));
        }
        return new Ray(location, pIJ.subtract(location));
    }

    /**
     * Builder class for constructing a Camera instance.
     */
    public static class Builder{

        private Point location;
        private Vector vTo;
        private Vector vUp;
        private Vector vRight;
        private  double distance = 0;
        private double width = 0;
        private double height = 0;
        private final Camera camera = new Camera(this);

        /**
         * Sets the location of the camera.
         * @param location The location to set.
         * @return The builder instance.
         */
        public Builder setLocation(Point location) {
            camera.location = location;
            return this;
        }

        /**
         * Sets the direction vectors of the camera.
         * @param vTo The forward direction vector.
         * @param vUp The upward direction vector.
         * @return The builder instance.
         * @throws IllegalArgumentException if vTo and vUp are not orthogonal.
         */
        public Builder setDirection(Vector vTo, Vector vUp) {
           if (vTo.dotProduct(vUp) != 0)
               throw new IllegalArgumentException("vTo and vUp are not orthogonal");
            camera.vTo = vTo.normalize();
            camera.vUp = vUp.normalize();
            return this;
        }

        /**
         * Sets the size of the view plane.
         * @param width The width of the view plane.
         * @param height The height of the view plane.
         * @return The builder instance.
         */
        public Builder setVpSize(double width, double height) {
            camera.width = width;
            camera.height = height;
            return this;
        }

        /**
         * Sets the distance of the camera to the view plane.
         * @param distance The distance to set.
         * @return The builder instance.
         */
        public Builder setVpDistance(double distance) {
            camera.distance = distance;
            return this;
        }

        /**
         * Builds and returns the Camera instance.
         * @return The built Camera instance.
         * @throws MissingResourceException if any required parameter is missing.
         * @throws IllegalArgumentException if any parameter is invalid.
         */
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
            if (camera.vTo.dotProduct(camera.vUp) != 0)
                throw new IllegalArgumentException("vTo and vUp are not orthogonal");

            camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();
            return (Camera) camera.clone();
        }

    }
    @Override
    public Camera clone(){
        return this;
    }

}
