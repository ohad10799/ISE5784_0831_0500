package primitives;

/**
 * Represents a point in 3D space.
 */
public class Point {
    /**
     * Zero triad (0,0,0)
     */
    public static final Point ZERO = new Point(0, 0, 0); // Final static public Point ZERO;
    protected final Double3 xyz; // The coordinates of the point.

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

    public double getX() {
        return xyz.d1;
    }

    public double getY() {
        return xyz.d2;
    }

    public double getZ() {
        return xyz.d3;
    }

    public double get(int axis) {
        switch (axis) {
            case 0:
                return getX();
            case 1:
                return getY();
            case 2:
                return getZ();
            default:
                throw new IllegalArgumentException("Invalid axis index");
        }

    }
}
