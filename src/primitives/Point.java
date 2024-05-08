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


}
