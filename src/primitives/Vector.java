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


}
