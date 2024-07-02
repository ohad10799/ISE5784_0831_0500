package primitives;

/**
 * Represents a 3D vector with x, y, and z components.
 * Extends the Point class to reuse its methods for vector operations.
 */
public class Vector extends Point {

    public static final Vector Y = new Vector(0, 1, 0);
    public static final Vector Z = new Vector(0, 0, -1);
    public static final Vector X = new Vector(1, 0, 0);


    /**
     * Constructs a new vector with the specified x, y, and z components.
     *
     * @param d1 The x component of the vector.
     * @param d2 The y component of the vector.
     * @param d3 The z component of the vector.
     * @throws IllegalArgumentException If the vector is a zero vector.
     */
    public Vector(double d1, double d2, double d3) {
        this(new Double3(d1, d2, d3));
    }

    /**
     * Constructs a new vector from a Double3 object.
     *
     * @param xyz The Double3 object containing the x, y, and z components of the vector.
     * @throws IllegalArgumentException If the vector is a zero vector.
     */
    public Vector(Double3 xyz) {
        super(xyz);
        if (xyz.equals(Double3.ZERO)) {
            throw new IllegalArgumentException("Vector can't be zero!");
        }
    }

    /**
     * Adds another vector to this vector.
     *
     * @param v1 The vector to add.
     * @return A new vector that is the result of the addition.
     */
    public Vector add(Vector v1) {
        Double3 val = xyz.add(v1.xyz);

        if (val.equals(Double3.ZERO)) {
            throw new IllegalArgumentException("adding the opposite vectors get vector ZERO!");
        }

        return new Vector(val);
    }

    /**
     * Scales this vector by a scalar value.
     *
     * @param rhs The scalar value to scale the vector by.
     * @return A new vector that is the result of the scaling.
     */
    public Vector scale(double rhs) {
        if (rhs == 0) {
            throw new IllegalArgumentException("The scale is zero, the vector is the zero vector!");
        }
        return new Vector(xyz.scale(rhs));
    }

    /**
     * Computes the dot product of this vector and another vector.
     *
     * @param v1 The other vector.
     * @return The dot product of the two vectors.
     */
    public double dotProduct(Vector v1) {
        return (xyz.d1 * v1.xyz.d1) + (xyz.d2 * v1.xyz.d2) + (xyz.d3 * v1.xyz.d3);
    }

    /**
     * Computes the cross product of this vector and another vector.
     *
     * @param v1 The other vector.
     * @return A new vector that is the result of the cross product.
     */
    public Vector crossProduct(Vector v1) {
        double x = (xyz.d2 * v1.xyz.d3 - xyz.d3 * v1.xyz.d2);
        double y = (xyz.d3 * v1.xyz.d1 - xyz.d1 * v1.xyz.d3);
        double z = (xyz.d1 * v1.xyz.d2 - xyz.d2 * v1.xyz.d1);

        // Check if the resulting vector is a zero vector
        if (x == 0 && y == 0 && z == 0) {
            throw new IllegalArgumentException("The vectors are parallel");
        }

        return new Vector(x, y, z);
    }


    /**
     * Computes the squared length of this vector.
     *
     * @return The squared length of the vector.
     */
    public double lengthSquared() {
        return dotProduct(this);
    }

    /**
     * Computes the length of this vector.
     *
     * @return The length of the vector.
     */
    public double length() {
        return Math.sqrt(lengthSquared());
    }

    /**
     * Returns a new vector that is the normalized version of this vector.
     *
     * @return The normalized vector.
     */
    public Vector normalize() {
        return new Vector(xyz.d1 / length(), xyz.d2 / length(), xyz.d3 / length());
    }

    /**
     * Returns a string representation of this vector.
     *
     * @return A string representation of the vector.
     */
    public String toString() {
        return super.toString();
    }

    /**
     * Checks if this vector is equal to another object.
     * Two vectors are equal if their x, y, and z components are equal.
     *
     * @param obj The object to compare with.
     * @return True if the objects are equal, false otherwise.
     */
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Vector vector)) return false;
        return xyz.equals(vector.xyz);
    }
}
