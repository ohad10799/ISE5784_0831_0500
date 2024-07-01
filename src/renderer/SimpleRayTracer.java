package renderer;

import lighting.Light;
import primitives.*;
import scene.Scene;
import geometries.Intersectable.GeoPoint;
import lighting.LightSource;
import java.util.List;

import static primitives.Util.alignZero;

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
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(ray);
        if (intersections == null)
            return scene.background;
        else {
            GeoPoint closestPoint = ray.findClosestGeoPoint(intersections);
            return calcColor(closestPoint, ray);
        }
    }

    /**
     * Calculates the color at a given point.
     * This method combines the emission color of the geometry at the point with the ambient light intensity of the scene.
     *
     * @param geoPoint the point at which to calculate the color
     * @return the combined color at the given point
     */
    private Color calcColor(GeoPoint geoPoint, Ray ray) {
        return scene.ambientLight.getIntensity().add(geoPoint.geometry.getEmission())
                .add(calcLocalEffects(geoPoint, ray));

    }

    private Color calcLocalEffects(GeoPoint gp, Ray ray) {
        Vector v = ray.getDirection();
        Vector n = gp.geometry.getNormal(gp.point);
        double nv = alignZero(n.dotProduct(v));
        if (nv == 0)
            return Color.BLACK;

        int shininess = gp.geometry.getMaterial().shininess;
        Double3 kd = gp.geometry.getMaterial().kd, ks = gp.geometry.getMaterial().ks;

        Color color = Color.BLACK;
        for (LightSource lightSource : scene.lights) {
            Vector l = lightSource.getL(gp.point);
            double nl = alignZero(n.dotProduct(l));

            if (nl * nv > 0) { // sign(nl) == sign(nv)
                Color lightIntensity = lightSource.getIntensity(gp.point);
                color = color.add(calcDiffusive(kd, nl, lightIntensity),
                        calcSpecular(ks, l, n, nl, v, shininess, lightIntensity));
            }
        }
        return color;
    }

    private Color calcDiffusive(Double3 kd, double nl, Color lightIntensity) {
        return lightIntensity.scale(kd.scale(Math.abs(nl)));
    }

    private Color calcSpecular(Double3 ks, Vector l, Vector n, double nl, Vector v, int shininess, Color lightIntensity) {
        Vector r = l.add(n.scale(-2 * nl)); // nl must not be zero!
        double minusVR = -alignZero(r.dotProduct(v));
        if (minusVR <= 0) {
            return new primitives.Color(Color.BLACK.getColor()); // View from direction opposite to r vector
        }
        return lightIntensity.scale(ks.scale(Math.pow(minusVR, shininess)));
    }


}
