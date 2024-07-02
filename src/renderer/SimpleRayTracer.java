package renderer;

import geometries.Intersectable.GeoPoint;
import lighting.LightSource;
import primitives.*;
import scene.Scene;

import java.util.List;

import static primitives.Util.alignZero;

public class SimpleRayTracer extends RayTracerBase {


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

    private Color calcColor(GeoPoint geoPoint, Ray ray) {
        return scene.ambientLight.
                getIntensity().
                add(geoPoint.geometry.getEmission())
                .add(calcLocalEffects(geoPoint, ray));
    }

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

    private Double3 calcDiffusive(Double3 kd, double nl) {
        return kd.scale(Math.abs(nl));
    }

    private Double3 calcSpecular(Double3 ks, Vector l, Vector n, double nl, Vector v, int shininess) {
        Vector r = l.add(n.scale(-2 * nl)); // nl must not be zero!
        double minusVR = -alignZero(v.dotProduct(r));
        if (minusVR <= 0) {
            return Double3.ZERO; // View from direction opposite to r vector
        }

        return ks.scale(Math.pow(minusVR, shininess));

    }




}
