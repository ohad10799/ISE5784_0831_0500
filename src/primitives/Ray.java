package primitives;

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

    public Point getHead(){
        return head;
    }

    public Vector getDirection() {
        return direction;
    }
}
