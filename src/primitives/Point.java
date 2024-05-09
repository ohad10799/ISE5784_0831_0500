package primitives;

public class Point {
    protected final Double3 xyz;
    /** Zero triad (0,0,0) */
    public static final Point ZERO = new Point(0, 0, 0); //final static public Point ZERO;

    public Point(double d1, double d2, double d3) {
        xyz=new Double3(d1,d2,d3);
    }

    public Point(Double3 xyz) {
       this.xyz = xyz;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Point other)
                && this.xyz.equals(other.xyz);
    }

    @Override
    public String toString() {
        return xyz.toString();
    }

    public Vector subtract(Point p1){
        return new Vector(xyz.subtract(p1.xyz));

    }

    public Point add(Vector v1) {
        return new Point(xyz.add(v1.xyz));

    }

    public double distanceSquared(Point p1){
        return ( (xyz.d1-p1.xyz.d1) * (xyz.d1-p1.xyz.d1) + (xyz.d2-p1.xyz.d2) * (xyz.d2-p1.xyz.d2) + (xyz.d3-p1.xyz.d3) * (xyz.d3-p1.xyz.d3));
    }

    public double distance(Point p1){
        return Math.sqrt(distanceSquared(p1));
    }



}
