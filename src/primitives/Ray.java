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
     * Constructs a new Ray with the specified head and direction.
     *
     * @param head      The starting point of the ray.
     * @param direction The direction of the ray.
     */
    public Ray(Point head, Vector direction) {
        this.head = head;
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
     * Calculates a point on the line of the ray, at a distance
     * given by the head of the ray
     *
     * @param t The distance from the head of the ray
     */
    public Point getPoint(double t) {
        if (isZero(t)) {
            return head;
        }
        return head.add(direction.scale(t));
    }

    /**
     * Finds the closest point to the head point from a list of points.
     *
     * @param points a list of points to search from
     * @return the point closest to the head point, or null if the list is empty
     */
    public Point findClosestPoint(List<Point> points) {
        return points == null || points.isEmpty() ? null
                : findClosestGeoPoint(points
                .stream()
                .map(p -> new GeoPoint(null, p))
                .toList())
                .point;
    }

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
