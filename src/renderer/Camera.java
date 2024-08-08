package renderer;

import primitives.*;
import java.util.MissingResourceException;
import java.util.stream.IntStream;

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

    // new parameter for multithreading
    private int threadsCount = 0; // -2 auto, 0 no threads, 1+ number of threads
    private final int SPARE_THREADS = 2; // Spare threads if trying to use all the cores
    private double printInterval = 0; // printing progress percentage interval
    private PixelManager pixelManager; // pixel manager for multithreading

    /**
     * Private constructor for Camera.
     * Use the builder to create instances of Camera.
     */
    private Camera() {
    }

    /**
     * Returns a new builder instance for constructing a Camera.
     *
     * @return A new Builder instance.
     */
    public static Builder getBuilder() {
        return new Builder();
    }

    /**
     * Gets the location of the camera.
     *
     * @return The location of the camera.
     */
    public Point getLocation() {
        return location;
    }

    /**
     * Gets the forward direction vector of the camera.
     *
     * @return The forward direction vector.
     */
    public Vector getvTo() {
        return vTo;
    }

    /**
     * Gets the upward direction vector of the camera.
     *
     * @return The upward direction vector.
     */
    public Vector getvUp() {
        return vUp;
    }

    /**
     * Gets the rightward direction vector of the camera.
     *
     * @return The rightward direction vector.
     */
    public Vector getvRight() {
        return vRight;
    }

    /**
     * Gets the distance from the camera to the view plane.
     *
     * @return The distance to the view plane.
     */
    public double getDistance() {
        return distance;
    }

    /**
     * Gets the width of the view plane.
     *
     * @return The width of the view plane.
     */
    public double getWidth() {
        return width;
    }

    /**
     * Gets the height of the view plane.
     *
     * @return The height of the view plane.
     */
    public double getHeight() {
        return height;
    }

    /**
     * Constructs a ray from the camera through a specific pixel on the view plane.
     *
     * @param nX Number of pixels in the x direction.
     * @param nY Number of pixels in the y direction.
     * @param j The pixel's column index.
     * @param i The pixel's row index.
     * @return The constructed Ray.
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
     * Prints a grid on the image at specified intervals.
     *
     * @param interval The interval at which to print grid lines.
     * @param color The color of the grid lines.
     * @return The current Camera instance.
     * @throws MissingResourceException if imageWriter is not set.
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
     * Renders the image using the ray tracer and image writer.
     *
     * @return The current Camera instance.
     * @throws MissingResourceException if rayTracer or imageWriter are not set.
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
        pixelManager = new PixelManager(nY, nX, printInterval);
        if(threadsCount == 0) {
            for (int i = 0; i < nY; ++i) {
                for (int j = 0; j < nX; ++j) {
                    castRay(nX, nY, j, i);
                }
            }
        }
        else {
            IntStream.range(0, nY).parallel()
                    .forEach(i -> IntStream.range(0, nX).parallel() // for each row:
                            .forEach(j -> castRay(nX, nY, j, i))); // for each column in row
        }
        return this;
    }

    /**
     * Casts a ray for a specific pixel and writes the pixel color to the image.
     * If depth of field (DoF) is enabled, averages colors of multiple rays.
     *
     * @param Nx The number of pixels in the x direction.
     * @param Ny The number of pixels in the y direction.
     * @param column The column index of the pixel.
     * @param row The row index of the pixel.
     */
    private void castRay(int Nx, int Ny, int column, int row) {
        if (!useDepthOfField) {
            imageWriter.writePixel(column, row, rayTracer.traceRay(constructRay(imageWriter.getNx(), imageWriter.getNy(), column, row)));
            pixelManager.pixelDone();
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
        pixelManager.pixelDone();
    }

    /**
     * Constructs a ray with depth of field (DoF) effect for a specific pixel.
     *
     * @param nX Number of pixels in the x direction.
     * @param nY Number of pixels in the y direction.
     * @param j The pixel's column index.
     * @param i The pixel's row index.
     * @return The constructed Ray with DoF effect.
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
     * Writes the rendered image to the output using the configured {@link ImageWriter}.
     *
     * <p>This method calls the {@link ImageWriter#writeToImage()} method to save the image to the specified output.
     * If no {@link ImageWriter} has been set for the camera, an exception will be thrown.</p>
     *
     * @throws MissingResourceException If the {@link ImageWriter} has not been set for the camera.
     * @see ImageWriter#writeToImage()
     */
    public void writeToImage() {
        if (imageWriter == null) {
            throw new MissingResourceException("Missing parameter", "Camera", "imageWriter");
        }
        imageWriter.writeToImage();
    }


    /**
     * Inner static Builder class for creating instances of Camera.
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
         * Sets the number of threads to be used for rendering.
         *
         * @param thread The number of threads to use.
         *               -2: automatically determine the number of threads based on available processors.
         *               -1: use a range/stream approach for multithreading.
         *                0: disable multithreading.
         *                1+: specify the exact number of threads to use.
         * @return The builder instance.
         * @throws IllegalArgumentException if the number of threads is less than -2.
         */
        public Builder setMultithreading(int thread)
        {
            if(thread < -2) throw new IllegalArgumentException("Multithreading must be -2 or higher");
            if(thread >=-1) camera.threadsCount = thread;
            else{ // -2
                int cores = Runtime.getRuntime().availableProcessors() - camera.SPARE_THREADS;
                camera.threadsCount = cores <= 2 ? 1 : cores;
            }
            return this;
        }

        /**
         * Sets the interval for printing debug progress.
         *
         * @param printInterval The interval at which to print progress, in percentage.
         * @return The builder instance.
         */
        public Builder setDebugPrint(double printInterval) {
            camera.printInterval = printInterval;
            return this;
        }

        /**
         * Rotates the camera around the Z-axis by a specified angle.
         *
         * @param angle The angle in degrees by which to rotate the camera around the Z-axis.
         * @return The current Builder instance for chaining.
         */
        public Builder rotateAroundZAxis(double angle) {
            double radAngle = Math.toRadians(angle);
            double x = Math.cos(radAngle) * camera.distance;
            double y = Math.sin(radAngle) * camera.distance;

            camera.location = new Point(x, y, 100);
            camera.vTo = new Vector(-x, -y, 0).normalize();
            camera.vUp = new Vector(0, 0, 1);
            camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();
            return this;
        }

        /**
         * Rotates the camera around the X-axis by a specified angle.
         *
         * @param angle The angle in degrees by which to rotate the camera around the X-axis.
         * @return The current Builder instance for chaining.
         */
        public Builder rotateAroundXAxis(double angle) {
            double radAngle = Math.toRadians(angle);
            double y = Math.cos(radAngle) * camera.distance;
            double z = Math.sin(radAngle) * camera.distance;

            camera.location = new Point(100, y, z);
            camera.vTo = new Vector(0, -y, -z).normalize();
            camera.vRight = new Vector(1, 0, 0);
            camera.vUp = camera.vRight.crossProduct(camera.vTo).normalize();
            return this;
        }

        /**
         * Rotates the camera around the Y-axis by a specified angle.
         *
         * @param angle The angle in degrees by which to rotate the camera around the Y-axis.
         * @return The current Builder instance for chaining.
         */
        public Builder rotateAroundYAxis(double angle) {
            double radAngle = Math.toRadians(angle);
            double x = Math.cos(radAngle) * camera.distance;
            double z = Math.sin(radAngle) * camera.distance;

            camera.location = new Point(x, 100, z);
            camera.vTo = new Vector(-x, 0, -z).normalize();
            camera.vUp = new Vector(0, 1, 0);
            camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();
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