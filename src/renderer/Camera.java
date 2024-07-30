package renderer;

import primitives.*;
import java.util.MissingResourceException;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * Camera class represents a camera in 3D space.
 * The camera is defined by its location, and three orthogonal direction vectors.
 */
public class Camera implements Cloneable {

    private Point location;  // The position of the camera in 3D space
    private Vector vTo;      // The forward direction vector
    private Vector vUp;      // The upward direction vector
    private Vector vRight;   // The rightward direction vector, perpendicular to vTo and vUp
    private double distance = 0;  // The distance from the camera to the view plane
    private double width = 0;      // The width of the view plane
    private double height = 0;     // The height of the view plane
    private SimpleRayTracer rayTracer; // Ray tracer to render the scene
    private ImageWriter imageWriter;   // Image writer for saving the rendered image

    // new parameters for DoF
    private boolean useDepthOfField = false; // Indicates if depth of field effects are enabled.
    private double focalLength = 1000; // The focal length for depth of field effects.
    private double apertureSize = 1; // The size of the aperture for depth of field effects.

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
     * Casts a ray through a specified pixel and writes the resulting color to the image.
     * If depth of field (DoF) effects are enabled, multiple rays are cast per pixel to average the colors for a realistic blur effect.
     *
     * @param Nx     Number of horizontal pixels in the view plane.
     * @param Ny     Number of vertical pixels in the view plane.
     * @param column Column index of the pixel.
     * @param row    Row index of the pixel.
     */
    private void castRay(int Nx, int Ny, int column, int row) {
        if (!useDepthOfField) {
            Ray ray = constructRay(Nx, Ny, column, row);
            Color color = rayTracer.traceRay(ray);
            imageWriter.writePixel(column, row, color);
            return;
        }

        // If using DoF, average the colors of multiple rays
        int numRays = 10;  // Number of rays to cast per pixel for DoF
        Color averageColor = Color.BLACK;
        for (int i = 0; i < numRays; i++) {
            Ray ray = constructRayDoF(Nx, Ny, column, row);
            Color color = rayTracer.traceRay(ray);
            averageColor = averageColor.add(color);
        }
        averageColor = averageColor.reduce(numRays);
        imageWriter.writePixel(column, row, averageColor);
    }

    /**
     * Constructs a ray with depth of field (DoF) effects through a specified pixel.
     * If DoF is enabled, the ray is offset based on a random point within the aperture to simulate a blurred focus effect.
     *
     * @param nX Number of horizontal pixels in the view plane.
     * @param nY Number of vertical pixels in the view plane.
     * @param j  Column index of the pixel.
     * @param i  Row index of the pixel.
     * @return The constructed ray with DoF effects, or a primary ray if DoF is disabled.
     */
    private Ray constructRayDoF(int nX, int nY, int j, int i) {
        Ray primaryRay = constructRay(nX, nY, j, i);
        if (!useDepthOfField) {
            return primaryRay;
        }
        Point focalPoint = primaryRay.getPoint(focalLength);
        double r = Math.sqrt(Math.random()) * apertureSize / 2;
        double theta = Math.random() * 2 * Math.PI;
        double xShift = r * Math.cos(theta);
        double yShift = r * Math.sin(theta);
        Point aperturePoint = location.add(vRight.scale(xShift)).add(vUp.scale(yShift));
        return new Ray(aperturePoint, focalPoint.subtract(aperturePoint));
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
     * Rotates the camera around the Z-axis by the specified angle.
     * This method updates the camera's location and orientation vectors based on the rotation angle.
     *
     * @param angle The angle of rotation around the Z-axis, in degrees. Positive values indicate counterclockwise rotation.
     * @return The updated Camera instance with the new location and orientation.
     */
    public Camera rotateAroundZAxis(double angle) {
        double radAngle = Math.toRadians(angle);
        double x = Math.cos(radAngle) * distance;
        double y = Math.sin(radAngle) * distance;

        this.location = new Point(x, y, 100);
        this.vTo = new Vector(-x, -y, 0).normalize();
        this.vUp = new Vector(0, 0, 1);
        this.vRight = vTo.crossProduct(vUp).normalize();

        return this;
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
         * Sets whether to use depth of field (DoF) effects.
         *
         * @param useDepthOfField True to enable DoF, false to disable.
         * @return The builder instance.
         */
        public Builder setUseDepthOfField(boolean useDepthOfField) {
            camera.useDepthOfField = useDepthOfField;
            return this;
        }

        /**
         * Sets the focal length for depth of field (DoF) effects.
         *
         * @param focalLength The focal length to set.
         * @return The builder instance.
         */
        public Builder setFocalLength(double focalLength) {
            camera.focalLength = focalLength;
            return this;
        }

        /**
         * Sets the aperture size for depth of field (DoF) effects.
         *
         * @param apertureSize The aperture size to set.
         * @return The builder instance.
         */
        public Builder setApertureSize(double apertureSize) {
            camera.apertureSize = apertureSize;
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