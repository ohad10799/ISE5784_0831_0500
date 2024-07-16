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


    private static final int MAX_CALC_COLOR_LEVEL = 10;
    private static final double MIN_CALC_COLOR_K = 0.001;
    private static final Double3 INITIAL_K = Double3.ONE;


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
        GeoPoint closestPoint = findClosestIntersection(ray);

        return closestPoint == null ? scene.background
                : calcColor(closestPoint, ray);
    }



    private Color calcColor(GeoPoint geoPoint, Ray ray) {
        return calcColor(geoPoint, ray, MAX_CALC_COLOR_LEVEL, INITIAL_K).
                add(scene.ambientLight.getIntensity());
    }


    private Color calcColor(GeoPoint gp, Ray ray , int level, Double3 k){
        Color color = calcLocalEffects(gp, ray, k);
        return 1 == level ? color
                : color.add(calcGlobalEffects(gp, ray, level, k));
    }



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



    private Double3 calcDiffusive(Material material, double nl) {
        return material.kD.scale(Math.abs(nl));
    }


    /**
     * calculate the specular component of the reflection
     *
     * @param material for the shininess and specular factor of the material
     * @param n        the normal vector
     * @param l        the direction vector from light to object
     * @param nl       cosine of angle between normal and vector from light to object
     * @param v        the direction of the ray from the camera
     * @return the specular component of the reflection
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
     * Checks if the point is unshaded by other geometries towards the light source.
     *
     * @param gp          the intersection point and associated geometry
     * @param l           the direction vector from the light source to the point
     * @param n           the normal vector at the intersection point
     * @param lightSource the light source to check shading against
     * @return true if the point is unshaded, false otherwise
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


    private Color calcGlobalEffects(GeoPoint gp, Ray ray, int level, Double3 k) {
        Material material = gp.geometry.getMaterial();
        return calcGlobalEffect(constructRefractedRay(gp, ray), material.kT, level, k)
                .add(calcGlobalEffect(constructReflectedRay(gp, ray), material.kR, level, k));
    }

    private Color calcGlobalEffect(Ray ray, Double3 kx, int level, Double3 k) {

        Double3 kkx = kx.product(k);

        if (kkx.lowerThan(MIN_CALC_COLOR_K))
            return Color.BLACK;

        GeoPoint gp = findClosestIntersection(ray);
        return (gp == null ? scene.background : calcColor(gp, ray, level-1, kkx))
                .scale(kx);
    }


    private Ray constructRefractedRay(GeoPoint gp, Ray ray) {
        Vector n = gp.geometry.getNormal(gp.point);
        Vector direction = ray.getDirection();
        return new Ray(gp.point, direction, n);
    }

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

    private GeoPoint findClosestIntersection(Ray ray) {
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(ray);
        if (intersections == null) {
            return null;
        }
        return ray.findClosestGeoPoint(intersections);
    }

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

