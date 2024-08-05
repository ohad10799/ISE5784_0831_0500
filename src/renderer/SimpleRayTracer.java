package renderer;

import geometries.Intersectable.GeoPoint;
import lighting.LightSource;
import primitives.*;
import scene.Scene;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * The SimpleRayTracer class implements a basic ray tracing algorithm for rendering scenes.
 * It computes the color of rays intersecting with geometries in the scene, considering
 * ambient light, diffuse reflection, and specular reflection from light sources.
 */
public class SimpleRayTracer extends RayTracerBase {


    private static final int MAX_CALC_COLOR_LEVEL = 10; // Maximum recursion depth for color calculation
    private static final double MIN_CALC_COLOR_K = 0.001; // Minimum scaling factor for color calculations
    private static final Double3 INITIAL_K = Double3.ONE; // Initial coefficient for color calculations

    /**
     * Constructor for SimpleRayTracer.
     *
     * @param scene The scene to be rendered.
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    @Override
    public Color traceRay(Ray ray) {
        if (bvhAccelerator != null) {
            if (bvhAccelerator.findClosestIntersection(ray) != null) {
                return scene.background;
            }
        }
        GeoPoint closestPoint = findClosestIntersection(ray);
        return closestPoint == null ? scene.background : calcColor(closestPoint, ray);
    }

    /**
     * Calculates the color at a given intersection point, adding ambient light.
     *
     * @param geoPoint The intersection point in the scene.
     * @param ray      The ray that intersected.
     * @return The calculated color including ambient light.
     */
    private Color calcColor(GeoPoint geoPoint, Ray ray) {
        return calcColor(geoPoint, ray, MAX_CALC_COLOR_LEVEL, INITIAL_K).
                add(scene.ambientLight.getIntensity());
    }

    /**
     * Calculates the color at a given intersection point with support for recursive reflection and refraction.
     *
     * @param gp    The intersection point.
     * @param ray   The ray that intersected.
     * @param level The recursion level for reflection/refraction.
     * @param k    The attenuation coefficient.
     * @return The calculated color.
     */
    private Color calcColor(GeoPoint gp, Ray ray , int level, Double3 k){
        Color color = calcLocalEffects(gp, ray, k);
        return 1 == level ? color
                : color.add(calcGlobalEffects(gp, ray, level, k));
    }

    /**
     * Calculates local effects (lighting) at a given intersection point.
     *
     * @param gp  The intersection point.
     * @param ray The ray that intersected.
     * @param k   The attenuation coefficient.
     * @return The calculated color from local effects.
     */
    private Color calcLocalEffects(GeoPoint gp, Ray ray, Double3 k) {

        Color color = gp.geometry.getEmission();
        Vector v = ray.getDirection();
        Vector n = gp.geometry.getNormal(gp.point);
        Material material = gp.geometry.getMaterial();

        double nv = alignZero(n.dotProduct(v));

        if (isZero(nv))
            return color;

        for (LightSource lightSource : scene.lights) {

            Vector l = lightSource.getL(gp.point);

            if (l == null)
                continue;

            double nl = alignZero(n.dotProduct(l));

            if (nl * nv > 0) { // sign(nl) == sign(nv)

                Double3 ktr = transparency(gp, lightSource, l, n);

                if (!ktr.product(k).lowerThan(MIN_CALC_COLOR_K)) {
                    Color iL = lightSource.getIntensity(gp.point).scale(ktr);
                    color = color.add(
                            iL.scale(calcDiffusive(material, nl)),
                            iL.scale(calcSpecular(material, n, l, nl, v)));
                }
            }
        }
        return color;
    }

    /**
     * Calculates the diffuse reflection component.
     *
     * @param material The material of the geometry.
     * @param nl       The dot product of the normal and light vector.
     * @return The diffuse component of color.
     */
    private Double3 calcDiffusive(Material material, double nl) {
        return material.kD.scale(Math.abs(nl));
    }

    /**
     * Calculates the specular reflection component.
     *
     * @param material The material of the geometry.
     * @param n       The normal vector at the intersection point.
     * @param l       The light vector.
     * @param nl      The dot product of the normal and light vector.
     * @param v       The view vector.
     * @return The specular component of color.
     */
    private Double3 calcSpecular(Material material, Vector n, Vector l, double nl, Vector v) {

        Vector r = l.add(n.scale(-2 * nl)); // nl must not be zero!
        double minusVR = -alignZero(v.dotProduct(r));

        //if deflection < 0  - more than 90 degrees there will be not specular component
        double max = minusVR > 0 ? minusVR : 0;

        if (minusVR > 0)
            for (int i = 0; i < material.shininess; i++)
                max *= minusVR;

        return material.kS.scale(max);
    }

    /**
     * Checks if a point is unshaded by a light source.
     *
     * @param gp          The intersection point.
     * @param l           The light vector.
     * @param n           The normal vector.
     * @param lightSource The light source.
     * @return True if the point is unshaded, false otherwise.
     */
    private boolean unshaded(GeoPoint gp, Vector l, Vector n, LightSource lightSource) {

        Vector lightDirection = l.scale(-1); // from point to light source

        Ray lightRay = new Ray(gp.point, lightDirection, n);
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(lightRay);

        if (intersections == null)
            return true;
        if (lightSource != null)
        {
            double lightDistance = lightSource.getDistance(gp.point);
            for (GeoPoint geoPoint : intersections) {
                if (alignZero(geoPoint.point.distance(gp.point) - lightDistance) <= 0 && geoPoint.geometry.getMaterial().kT.equals(Double3.ZERO))
                    return false;
            }
        }
        return true;
    }

    /**
     * Calculates global effects (reflection and refraction) at a given intersection point.
     *
     * @param gp    The intersection point.
     * @param ray   The ray that intersected.
     * @param level The recursion level for global effects.
     * @param k    The attenuation coefficient.
     * @return The calculated color from global effects.
     */
    private Color calcGlobalEffects(GeoPoint gp, Ray ray, int level, Double3 k) {
        Material material = gp.geometry.getMaterial();
        return calcGlobalEffect(constructRefractedRay(gp, ray), material.kT, level, k)
                .add(calcGlobalEffect(constructReflectedRay(gp, ray), material.kR, level, k));
    }

    /**
     * Calculates the effect of either reflection or refraction at a given ray.
     *
     * @param ray   The ray to trace.
     * @param kx    The material's reflection/refraction coefficient.
     * @param level The recursion level.
     * @param k     The attenuation coefficient.
     * @return The calculated color from the global effect.
     */
    private Color calcGlobalEffect(Ray ray, Double3 kx, int level, Double3 k) {

        Double3 kkx = kx.product(k);

        if (kkx.lowerThan(MIN_CALC_COLOR_K))
            return Color.BLACK;

        GeoPoint gp = findClosestIntersection(ray);
        return (gp == null ? scene.background : calcColor(gp, ray, level-1, kkx))
                .scale(kx);
    }

    /**
     * Constructs a refracted ray from a given intersection point and ray.
     *
     * @param gp  The intersection point.
     * @param ray The ray that intersected.
     * @return The refracted ray.
     */
    private Ray constructRefractedRay(GeoPoint gp, Ray ray) {
        Vector n = gp.geometry.getNormal(gp.point);
        Vector direction = ray.getDirection();
        return new Ray(gp.point, direction, n);
    }

    /**
     * Constructs a reflected ray from a given intersection point and ray.
     *
     * @param gp  The intersection point.
     * @param ray The ray that intersected.
     * @return The reflected ray.
     */
    private Ray constructReflectedRay(GeoPoint gp, Ray ray) {
        Vector n = gp.geometry.getNormal(gp.point);
        Vector v = ray.getDirection();

        // Calculate the reflection direction
        double nv = alignZero(n.dotProduct(v));
        if (nv == 0) {
            return null; // No reflection if the direction is perpendicular to the normal
        }

        Vector r = v.subtract(n.scale(2 * nv));

        return new Ray(gp.point, r, n);
    }

    /**
     * Finds the closest intersection point for a given ray.
     *
     * @param ray The ray to check for intersections.
     * @return The closest intersection point, or null if no intersection exists.
     */
    private GeoPoint findClosestIntersection(Ray ray) {
        if (bvhAccelerator != null) {
            return bvhAccelerator.findClosestIntersection(ray);
        }
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(ray);
        if (intersections == null) {
            return null;
        }
        return ray.findClosestGeoPoint(intersections);
    }

    /**
     * Computes the transparency coefficient for a point considering occlusions.
     *
     * @param geoPoint    The point being evaluated for transparency.
     * @param ls          The light source being considered.
     * @param l           The light vector.
     * @param n           The normal vector at the point.
     * @return The transparency coefficient.
     */
    private Double3 transparency(GeoPoint geoPoint, LightSource ls, Vector l, Vector n) {
        Vector lightDirection = l.scale(-1); // from point to light source

        double maxDistance = ls.getDistance(geoPoint.point);
        Ray lightRay = new Ray(geoPoint.point, lightDirection, n);
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(lightRay, maxDistance);

        if (intersections == null)
            return Double3.ONE;

        Double3 ktr = Double3.ONE;
        double lightDistance = ls.getDistance(geoPoint.point);

        for (GeoPoint intersectionPoint  : intersections) {
            if (alignZero(intersectionPoint.point.distance(geoPoint.point) - lightDistance) <= 0){
                ktr = ktr.product(intersectionPoint.geometry.getMaterial().kT);
                if (ktr.lowerThan(MIN_CALC_COLOR_K))
                    return Double3.ZERO;
            }

        }
        return ktr;
    }


}

