package renderer;

import primitives.Color;
import primitives.Ray;
import scene.Scene;

/**
 * Abstract base class for ray tracers in a rendering engine.
 * This class provides the basic structure and common functionality
 * for ray tracing algorithms.
 */
public abstract class RayTracerBase {

    protected final Scene scene;

    /**
     * Constructs a new RayTracerBase with the specified scene.
     *
     * @param scene the scene to be rendered
     */
    public RayTracerBase(Scene scene) {
        this.scene = scene;
    }

    /**
     * Renders the image using the provided image writer.
     * This method should be overridden by subclasses to provide specific
     * rendering behavior.
     *
     * @param imageWriter the image writer used to render the image
     */
    public void renderImage(ImageWriter imageWriter) {
    }

    /**
     * Traces a ray and determines the color at the intersection point.
     * This method should be overridden by subclasses to provide specific
     * ray tracing behavior.
     *
     * @param ray the ray to be traced
     * @return the color at the intersection point, or black if no intersection
     */
    public Color traceRay(Ray ray) {
        return Color.BLACK;
    }

    /**
     * Returns the scene associated with this ray tracer.
     *
     * @return the scene associated with this ray tracer
     */
    public Scene getScene() {
        return scene;
    }
}
