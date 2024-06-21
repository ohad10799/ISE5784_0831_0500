package renderer;

import primitives.*;
import scene.Scene;
import java.util.List;

/**
 * A simple implementation of a ray tracer.
 * This class extends the RayTracerBase and provides a basic ray tracing algorithm.
 */
public class SimpleRayTracer extends RayTracerBase {

    /**
     * Constructs a new SimpleRayTracer with the specified scene.
     *
     * @param scene the scene to be rendered
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    @Override
    public Color traceRay(Ray ray) {
        List<Point> intersections = scene.geometries.findIntersections(ray);
        if (intersections == null)
            return scene.background;
        else
            return calcColor(ray.findClosestPoint(intersections));
}

    /**
     * Calculates the color at a given point.
     * Currently, this method returns the ambient light intensity of the scene.
     *
     * @param point the point at which to calculate the color
     * @return the color at the given point
     */
    private Color calcColor(Point point) {
         return scene.ambientLight.getIntensity();
}

}
