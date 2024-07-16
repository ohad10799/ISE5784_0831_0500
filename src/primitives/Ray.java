package primitives;

import geometries.Intersectable.GeoPoint;

import java.util.List;

import static primitives.Util.isZero;

/**
 * Represents a ray in 3D space, defined by a starting point (head) and a direction.
 */
public class Ray {

    /**
     * The starting point of the ray.
     */
    final Point head;

    /**
     * The direction of the ray, normalized to unit length.
     */
    final Vector direction;

    /**
     * Small constant used for offsetting rays slightly from intersection points to prevent self-shadowing artifacts.
     * Adjusting this value can affect the smoothness of shadows and specular highlights in the rendered scene.
     */
    private static final double DELTA = 0.1;

    /**
     * Constructs a new Ray with the specified head and direction.
     *
     * @param head      The starting point of the ray.
     * @param direction The direction of the ray.
     */
    public Ray(Point head, Vector direction) {
        this.head = head;
        this.direction = direction.normalize();
    }

    public Ray(Point head, Vector direction, Vector normal) {

        if (isZero(normal.dotProduct(direction)))
            this.head = head.add(normal.scale(DELTA));
        else if (normal.dotProduct(direction) > 0)
            this.head = head.add(normal.scale(DELTA));
        else
            this.head = head.add(normal.scale(-DELTA));

        this.direction = direction.normalize();
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Ray other)
                && this.head.equals(other.head)
                && this.direction.equals(other.direction);
    }


    @Override
    public String toString() {
        return "head:" + head.xyz.toString() + " direction: " + direction.xyz.toString();
    }

    public Point getHead() {
        return head;
    }

    public Vector getDirection() {
        return direction;
    }

    /**
     * Calculates a point on the Ray at a distance given by parameter t.
     *
     * @param t the distance from the head of the Ray
     * @return the point on the Ray at distance t from the head
     */
    public Point getPoint(double t) {
        if (isZero(t)) {
            return head;
        }
        try {
            return head.add(direction.scale(t));
        } catch (IllegalArgumentException dontcare) {
            return head;
        }
    }

    /**
     * Finds the closest point to the head of the Ray from a list of points.
     *
     * @param points a list of points to search from
     * @return the point closest to the head of the Ray, or null if the list is empty
     */
    public Point findClosestPoint(List<Point> points) {
        return points == null || points.isEmpty() ? null
                : findClosestGeoPoint(points
                .stream()
                .map(p -> new GeoPoint(null, p))
                .toList())
                .point;
    }

    /**
     * Finds the closest GeoPoint to the head of the Ray from a list of GeoPoints.
     *
     * @param points a list of GeoPoints to search from
     * @return the GeoPoint closest to the head of the Ray, or null if the list is empty
     */
    public GeoPoint findClosestGeoPoint(List<GeoPoint> points) {
        GeoPoint closest = null;
        double minDistance = Double.POSITIVE_INFINITY;
        for (GeoPoint gp : points) {
            double distance = head.distanceSquared(gp.point);
            if (distance < minDistance) {
                minDistance = distance;
                closest = gp;
            }
        }
        return closest;
    }
}
