package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.MissingResourceException;

import static primitives.Util.alignZero;
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
    private SimpleRayTracer rayTracer;
    private ImageWriter imageWriter;

//    /**
//     * Private constructor using Builder pattern.
//     *
//     * @param builder The builder instance.
//     */
//    private Camera(Builder builder) {
//        location = builder.location;
//        vTo = builder.vTo;
//        vUp = builder.vUp;
//        vRight = builder.vRight;
//        distance = builder.distance;
//        width = builder.width;
//        height = builder.height;
//    }

    private Camera() {
    }

    /**
     * @return A new Builder instance to construct a Camera.
     */
    public static Builder getBuilder() {
        return new Builder();
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
     * Constructs a ray through a specific pixel in the view plane.
     *
     * @param nX Number of horizontal pixels in the view plane.
     * @param nY Number of vertical pixels in the view plane.
     * @param j  Column index of the pixel.
     * @param i  Row index of the pixel.
     * @return The ray through the pixel (j, i).
     */
    public Ray constructRay(int nX, int nY, int j, int i) {
        Point pC = location.add(vTo.scale(distance));

        double rY = height / nY;
        double rX = width / nX;

        double yi = alignZero(-(i - (nY - 1) / 2d) * rY);
        double xj =alignZero( (j - (nX - 1) / 2d) * rX);

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
     * Prints a grid on the image.
     *
     * @param interval The interval between grid lines.
     * @param color    The color of the grid lines.
     * @throws MissingResourceException if the image writer is not set.
     */
    public Camera printGrid(int interval, Color color) {
        if (imageWriter == null) {
            throw new MissingResourceException("Missing parameter", "Camera", "imageWriter");
        }

        int nX = imageWriter.getNx();
        int nY = imageWriter.getNy();

        for (int i = 0; i < nY; ++i) {
            for (int j = 0; j < nX; ++j) {
                if (i % interval == 0 || j % interval == 0)
                    imageWriter.writePixel(j, i, color);
            }
        }

        imageWriter.writeToImage();

        return this;
    }

    /**
     * Renders the image by casting rays through all pixels in the view plane.
     *
     * @throws MissingResourceException if the ray tracer or image writer is not set.
     */
    public Camera renderImage() {
        if (rayTracer == null) {
            throw new MissingResourceException("Missing parameter", "Camera", "rayTracer");
        }
        if (imageWriter == null) {
            throw new MissingResourceException("Missing parameter", "Camera", "imageWriter");
        }
        int nX = imageWriter.getNx();
        int nY = imageWriter.getNy();
        for (int i = 0; i < nY; ++i) {
            for (int j = 0; j < nX; ++j) {
                castRay(nX, nY, j, i);
            }
        }
        return this;
    }

    /**
     * Casts a ray through a specific pixel in the view plane and writes the color to the image.
     *
     * @param Nx     Number of horizontal pixels in the view plane.
     * @param Ny     Number of vertical pixels in the view plane.
     * @param column Column index of the pixel.
     * @param row    Row index of the pixel.
     */
    private void castRay(int Nx, int Ny, int column, int row) {
        Ray ray = constructRay(Nx, Ny, column, row);
        Color color = rayTracer.traceRay(ray);
        imageWriter.writePixel(column, row, color);
    }

    /**
     * Writes the image to a file.
     *
     * @throws MissingResourceException if the image writer is not set.
     */
    public void writeToImage() {
        if (imageWriter == null) {
            throw new MissingResourceException("Missing parameter", "Camera", "imageWriter");
        }
        imageWriter.writeToImage();
    }

    /**
     * Builder class for constructing a Camera instance.
     */
    public static class Builder {

        private final Camera camera = new Camera();

        /**
         * Sets the location of the camera.
         *
         * @param location The location to set.
         * @return The builder instance.
         */
        public Builder setLocation(Point location) {
            camera.location = location;
            return this;
        }

        /**
         * Sets the direction vectors of the camera.
         *
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
         *
         * @param width  The width of the view plane.
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
         *
         * @param distance The distance to set.
         * @return The builder instance.
         */
        public Builder setVpDistance(double distance) {
            camera.distance = distance;
            return this;
        }

        /**
         * Builds and returns the Camera instance.
         *
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
            if (camera.width < 0) {
                throw new IllegalArgumentException("width can't be negative");
            }
            if (camera.height < 0) {
                throw new IllegalArgumentException("height can't be negative");
            }
            if (camera.distance < 0) {
                throw new IllegalArgumentException("distance can't be negative");
            }
            if (camera.vTo.dotProduct(camera.vUp) != 0)
                throw new IllegalArgumentException("vTo and vUp are not orthogonal");

            if (camera.imageWriter == null) {
                throw new MissingResourceException("Missing parameter", "Camera", "imageWriter");
            }
            if (camera.rayTracer == null) {
                throw new MissingResourceException("Missing parameter", "Camera", "rayTracer");
            }

            camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();

            try {
                return (Camera) camera.clone();
            } catch (CloneNotSupportedException ignore) {
                return null;
            }

        }

        /**
         * Sets the ray tracer for the camera.
         *
         * @param rayTracer The ray tracer to set.
         * @return The builder instance.
         */
        public Builder setRayTracer(SimpleRayTracer rayTracer) {
            camera.rayTracer = rayTracer;
            return this;
        }

        /**
         * Sets the image writer for the camera.
         *
         * @param imageWriter The image writer to set.
         * @return The builder instance.
         */
        public Builder setImageWriter(ImageWriter imageWriter) {
            camera.imageWriter = imageWriter;
            return this;
        }

    }

}
