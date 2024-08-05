package renderer;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

public class BoundingBox {
    private final Point min;
    private final Point max;

    public BoundingBox(Point min, Point max) {
        this.min = min;
        this.max = max;
    }

    public Point getMin() {
        return min;
    }

    public Point getMax() {
        return max;
    }

    public boolean intersects(Ray ray) {
        // Implement the intersection logic between the ray and the bounding box
        Vector dir = ray.getDirection();
        Point origin = ray.getHead();
        double tMin = (min.getX() - origin.getX()) / dir.getX();
        double tMax = (max.getX() - origin.getX()) / dir.getX();
        if (tMin > tMax) {
            double temp = tMin;
            tMin = tMax;
            tMax = temp;
        }

        double tyMin = (min.getY() - origin.getY()) / dir.getY();
        double tyMax = (max.getY() - origin.getY()) / dir.getY();
        if (tyMin > tyMax) {
            double temp = tyMin;
            tyMin = tyMax;
            tyMax = temp;
        }

        if ((tMin > tyMax) || (tyMin > tMax)) {
            return false;
        }

        if (tyMin > tMin) {
            tMin = tyMin;
        }

        if (tyMax < tMax) {
            tMax = tyMax;
        }

        double tzMin = (min.getZ() - origin.getZ()) / dir.getZ();
        double tzMax = (max.getZ() - origin.getZ()) / dir.getZ();
        if (tzMin > tzMax) {
            double temp = tzMin;
            tzMin = tzMax;
            tzMax = temp;
        }

        if ((tMin > tzMax) || (tzMin > tMax)) {
            return false;
        }

        return true;
    }

    public Point getCenter() {
        double centerX = (min.getX() + max.getX()) / 2;
        double centerY = (min.getY() + max.getY()) / 2;
        double centerZ = (min.getZ() + max.getZ()) / 2;
        return new Point(centerX, centerY, centerZ);
    }
}
