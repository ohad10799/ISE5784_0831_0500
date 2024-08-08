package primitives;

import java.util.List;

/**
 * Represents a point in 3D space.
 */
public class Point {
    /**
     * Zero triad (0,0,0)
     */
    public static final Point ZERO = new Point(0, 0, 0);

    /**
     * The coordinates of the point.
     */
    protected final Double3 xyz;

    /**
     * Represents a point with positive infinite coordinates.
     */
    public static final Point POSITIVE_INFINITE = new Point(Double3.POSITIVE_INFINITE);

    /**
     * Represents a point with negative infinite coordinates.
     */
    public static final Point NEGATIVE_INFINITE = new Point(Double3.NEGATIVE_INFINITE);

    /**
     * Constructs a new Point with the given coordinates.
     *
     * @param d1 The x-coordinate of the point.
     * @param d2 The y-coordinate of the point.
     * @param d3 The z-coordinate of the point.
     */
    public Point(double d1, double d2, double d3) {
        this(new Double3(d1, d2, d3));
    }

    /**
     * Constructs a new Point with the given coordinates in a Double3 object.
     *
     * @param xyz The coordinates of the point.
     */
    Point(Double3 xyz) {
        this.xyz = xyz;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point point)) return false;
        return xyz.equals(point.xyz);
    }


    @Override
    public String toString() {
        return xyz.toString();
    }

    /**
     * Computes the vector from this point to another point.
     *
     * @param p1 The other point.
     * @return The vector from this point to the other point.
     */
    public Vector subtract(Point p1) {
        Double3 val = xyz.subtract(p1.xyz);
        if (val.equals(Double3.ZERO)) {
            throw new IllegalArgumentException("subtracting the same point get vector ZERO!");
        }
        return new Vector(val);
    }

    /**
     * Computes the point resulting from adding a vector to this point.
     *
     * @param v1 The vector to add.
     * @return The new point after adding the vector.
     */
    public Point add(Vector v1) {
        return new Point(xyz.add(v1.xyz));
    }


    /**
     * Computes the point resulting from adding another point to this point.
     *
     * @param p1 The point to add.
     * @return The new point after adding the other point.
     */
    public Point add(Point p1) {
        return new Point(xyz.add(p1.xyz));
    }

    /**
     * Scales the coordinates of the point by a given factor.
     *
     * @param v The scaling factor.
     * @return The new point after scaling.
     */
    public Point scale(double v) {
        return new Point(xyz.scale(v));
    }

    /**
     * Computes the squared distance between this point and another point.
     *
     * @param p1 The other point.
     * @return The squared distance between this point and the other point.
     */
    public double distanceSquared(Point p1) {
        return ((xyz.d1 - p1.xyz.d1) * (xyz.d1 - p1.xyz.d1)
                + (xyz.d2 - p1.xyz.d2) * (xyz.d2 - p1.xyz.d2)
                + (xyz.d3 - p1.xyz.d3) * (xyz.d3 - p1.xyz.d3));
    }

    /**
     * Computes the distance between this point and another point.
     *
     * @param p1 The other point.
     * @return The distance between this point and the other point.
     */
    public double distance(Point p1) {
        return Math.sqrt(distanceSquared(p1));
    }

    /**
     * Finds the point with the minimum coordinates from a list of points.
     *
     * @param points The list of points.
     * @return The point with the minimum coordinates.
     */
    public static Point findMinimum(List<Point> points) {
        double xMin = Double.POSITIVE_INFINITY;
        double yMin = Double.POSITIVE_INFINITY;
        double zMin = Double.POSITIVE_INFINITY;
        for (Point point : points) {
            xMin = Math.min(point.getX(), xMin);
            yMin = Math.min(point.getY(), yMin);
            zMin = Math.min(point.getZ(), zMin);
        }

        return new Point(xMin, yMin, zMin);
    }

    /**
     * Finds the point with the maximum coordinates from a list of points.
     *
     * @param points The list of points.
     * @return The point with the maximum coordinates.
     */
    public static Point findMaximum(List<Point> points) {
        double xMax = Double.NEGATIVE_INFINITY;
        double yMax = Double.NEGATIVE_INFINITY;
        double zMax = Double.NEGATIVE_INFINITY;
        for (Point point : points) {
            xMax = Math.max(point.getX(), xMax);
            yMax = Math.max(point.getY(), yMax);
            zMax = Math.max(point.getZ(), zMax);
        }
        return new Point(xMax, yMax, zMax);
    }

    /**
     * Gets the x-coordinate of the point.
     *
     * @return The x-coordinate.
     */
    public double getX() {
        return xyz.d1;
    }

    /**
     * Gets the y-coordinate of the point.
     *
     * @return The y-coordinate.
     */
    public double getY() {
        return xyz.d2;
    }

    /**
     * Gets the z-coordinate of the point.
     *
     * @return The z-coordinate.
     */
    public double getZ() {
        return xyz.d3;
    }


}
