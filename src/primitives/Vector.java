package primitives;

public class Vector extends Point{

    public Vector(double d1, double d2, double d3) {
        super(d1, d2, d3);

        if(equals(ZERO))
        {
            throw new IllegalArgumentException("Vector can't be zero!");
        }

    }

    public Vector(Double3 xyz) {
        super(xyz);

        if(equals(ZERO))
        {
            throw new IllegalArgumentException("Vector can't be zero!");
        }
    }

    public Vector add(Vector v1) {
        return new Vector(xyz.add(v1.xyz));

    }

    public Vector scale(double rhs) {
        return new Vector(xyz.scale(rhs));

    }

    public double dotProduct(Vector v1) {
        return (xyz.d1 * v1.xyz.d1) + (xyz.d2 * v1.xyz.d2) + (xyz.d3 * v1.xyz.d3);

    }

    public Vector crossProduct(Vector v1){
        return new Vector( (xyz.d2 * v1.xyz.d3 - xyz.d3 * v1.xyz.d2) , (xyz.d3 * v1.xyz.d1 - xyz.d1 * v1.xyz.d3) , (xyz.d1 * v1.xyz.d2 - xyz.d2 * v1.xyz.d1));

    }
    public double lengthSquared(){
       return dotProduct(this);
    }

    public double length(){
        return Math.sqrt(lengthSquared());
    }

    public Vector normalize(){
        return new Vector( xyz.d1/length() , xyz.d2/length(), xyz.d3/length());
    }

    public String toString(){
        return super.toString();
    }

    public boolean equals(Object obj) {
        return super.equals(obj);
    }


}
