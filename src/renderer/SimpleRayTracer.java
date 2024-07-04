package renderer;

import geometries.Intersectable.GeoPoint;
import lighting.LightSource;
import primitives.*;
import scene.Scene;

import java.util.List;

import static primitives.Util.alignZero;

/**
 * The SimpleRayTracer class implements a basic ray tracing algorithm for rendering scenes.
 * It computes the color of rays intersecting with geometries in the scene, considering
 * ambient light, diffuse reflection, and specular reflection from light sources.
 */
public class SimpleRayTracer extends RayTracerBase {

    /**
     * Constructs a SimpleRayTracer with the specified scene.
     *
     * @param scene the scene to render
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    @Override
    public Color traceRay(Ray ray) {

        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(ray);

        if (intersections == null) {
            return scene.background;
        }
        GeoPoint closestPoint = ray.findClosestGeoPoint(intersections);

        return calcColor(closestPoint,ray);
    }

    /**
     * Calculates the color at a specific intersection point, considering ambient light and local effects.
     *
     * @param geoPoint the intersection point and associated geometry
     * @param ray      the ray that intersected with the geometry
     * @return the computed color at the intersection point
     */
    private Color calcColor(GeoPoint geoPoint, Ray ray) {
        return scene.ambientLight.
                getIntensity().
                add(geoPoint.geometry.getEmission())
                .add(calcLocalEffects(geoPoint, ray));
    }

    /**
     * Calculates the local effects (diffuse and specular reflections) at an intersection point.
     *
     * @param gp  the intersection point and associated geometry
     * @param ray the ray that intersected with the geometry
     * @return the computed color due to local effects
     */
    private Color calcLocalEffects(GeoPoint gp, Ray ray) {
        Color color = Color.BLACK;
        Vector v = ray.getDirection();
        Vector n = gp.geometry.getNormal(gp.point);

        double nv = alignZero(n.dotProduct(v));
        if (nv == 0)
            return Color.BLACK;

        Material material = gp.geometry.getMaterial();
        int shininess = material.shininess;

        Double3 kd = material.kd;
        Double3 ks = material.ks;

        for (LightSource lightSource : scene.lights) {
            Vector l = lightSource.getL(gp.point);
            if (l == null)
                continue;
            double nl = alignZero(n.dotProduct(l));

            if (nl * nv > 0) { // sign(nl) == sign(nv)
                Color lightIntensity = lightSource.getIntensity(gp.point);
                Double3 factor =
                        calcDiffusive(kd, nl)
                                .add(calcSpecular(ks, l, n, nl, v, shininess));
                color = color.add(lightIntensity.scale(factor));
            }
        }
        return color;
    }

    /**
     * Calculates the diffuse reflection contribution based on the material's diffuse coefficient and light direction.
     *
     * @param kd the diffuse reflection coefficient of the material
     * @param nl the dot product of normal vector and light direction vector
     * @return the diffuse reflection contribution
     */
    private Double3 calcDiffusive(Double3 kd, double nl) {
        return kd.scale(Math.abs(nl));
    }

    /**
     * Calculates the specular reflection contribution based on the material's specular coefficient,
     * light direction, view direction, normal vector, and shininess.
     *
     * @param ks        the specular reflection coefficient of the material
     * @param l         the direction vector from the light source to the point
     * @param n         the normal vector at the intersection point
     * @param nl        the dot product of normal vector and light direction vector
     * @param v         the view direction vector
     * @param shininess the shininess exponent of the material
     * @return the specular reflection contribution
     */
    private Double3 calcSpecular(Double3 ks, Vector l, Vector n, double nl, Vector v, int shininess) {
        Vector r = l.add(n.scale(-2 * nl)); // nl must not be zero!
        double minusVR = -alignZero(v.dotProduct(r));
        if (minusVR <= 0) {
            return Double3.ZERO; // View from direction opposite to r vector
        }

        return ks.scale(Math.pow(minusVR, shininess));

    }




}
