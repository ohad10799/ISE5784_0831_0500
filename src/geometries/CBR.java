package geometries;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static primitives.Util.*;
import primitives.*;

/**
 * Implements the CBR object.
 *
 */
public class CBR extends Geometries {
    /** minimum coordinates point of the bound */
    private Point min = Point.POSITIVE_INFINITE;

    /** minimum coordinates point of the bound */
    private Point max = Point.NEGATIVE_INFINITE;

    /**
     * Default CBR constructor.
     */
    public CBR() {}

    /** CBR constructor based on list of intersectables.
     *
     * @param geometries the list of intersectables
     */
    public CBR(Intersectable... geometries) {
        add(geometries);
        buildHierarchy();
    }

    /** CBR constructor based on one intersectable.
     *
     * @param geometry an intersectable object
     */
    public CBR(Intersectable geometry) {
        this.geometries.add(geometry);
        List<Point> minMax = geometry.minMaxPoints();
        this.min = minMax.get(0);
        this.max = minMax.get(1);
    }

    /**
     * Getter to the value of min.
     *
     * @return the minimum point of the CBR
     */
    public Point getMin() {
        return min;
    }

    /**
     * Getter to the value of max.
     *
     * @return the maximum point of the CBR
     */
    public Point getMax() {
        return max;
    }
    /**
     * A method to add geometries to the collection and update min and max points accordingly.
     *
     * @param  geometries	variable number of Intersectable objects to be added
     * @return         	void
     */
    @Override
    public void add(Intersectable... geometries) {
        for (Intersectable obj : geometries) {
            if (!(obj instanceof CBR))
                this.geometries.add(new CBR(obj));
            else
                this.geometries.add(obj);
        }

        // check possibility of preferred points
        for (Intersectable obj : geometries) {
            List<Point> objMinMax = obj.minMaxPoints();
            min = Point.findMinimum(List.of(min, objMinMax.get(0)));
            max = Point.findMaximum(List.of(max, objMinMax.get(1)));
        }
    }

    @Override
    public List<Point> minMaxPoints() {
        return List.of(min, max);
    }

    /**
     * Sum of coordinates of the bound's center.
     *
     * @return the value of one bound
     */
    private double boundValue(Point minPoint, Point maxPoint) {
        Point center = minPoint.add(maxPoint).scale(0.5); // center point
        return center.getX() + center.getY() + center.getZ();
    }
    /**
     * Generate the function comment for the given function body in a markdown code block with the correct language syntax.
     *
     * @param  ray         description of parameter
     * @param  maxDistance description of parameter
     * @return             description of return value
     */
    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance) {
        double tmin = Double.NEGATIVE_INFINITY;
        double tmax = Double.POSITIVE_INFINITY;

        Vector v = ray.getDirection();
        Point p0 = ray.getHead();
        if (!isZero(v.getX())) {
            double tx1 = (min.getX() - p0.getX()) / v.getX();
            double tx2 = (max.getX() - p0.getX()) / v.getX();

            tmin = Math.max(tmin, Math.min(tx1, tx2));
            tmax = Math.min(tmax, Math.max(tx1, tx2));
        }

        if (!isZero(v.getY())) {
            double ty1 = (min.getY() - p0.getY()) / v.getY();
            double ty2 = (max.getY() - p0.getY()) / v.getY();

            tmin = Math.max(tmin, Math.min(ty1, ty2));
            tmax = Math.min(tmax, Math.max(ty1, ty2));
        }

        if (!isZero(v.getZ())) {
            double tz1 = (min.getZ() - p0.getZ()) / v.getZ();
            double tz2 = (max.getZ() - p0.getZ()) / v.getZ();

            tmin = Math.max(tmin, Math.min(tz1, tz2));
            tmax = Math.min(tmax, Math.max(tz1, tz2));
        }

        if (tmax < tmin) return null;
        return super.findGeoIntersectionsHelper(ray, maxDistance);
    }

    /**
     * This method wraps the recursive helper to sort the list only once.
     *
     * @return the updated CBR
     */
    public CBR buildHierarchy() {
        Collections.sort(this.geometries, new Comparator<Intersectable>() {
            @Override
            public int compare(Intersectable obj1, Intersectable obj2) {
                List<Point> minMax1 = obj1.minMaxPoints();
                double value1 = boundValue(minMax1.get(0), minMax1.get(1));

                List<Point> minMax2 = obj2.minMaxPoints();
                double value2 = boundValue(minMax2.get(0), minMax2.get(1));

                if (value1 < value2) return 1;
                return -1;
            }
        });

        this.geometries = List.of(buildHierarchyHelper(this.geometries));
        return this;
    }

    /**
     * Recursive algorithm which builds a hierarchy tree of CBRs from a sorted list of CBRs.
     *
     * @param geometries the list of CBRs
     * @return the top of the tree
     */
    private Intersectable buildHierarchyHelper(List<Intersectable> geometries) {
        if (geometries.size() < 2)
            return geometries.get(0);

        int halfSize = geometries.size() / 2;

        List<Intersectable> leftPart = geometries.subList(0, halfSize);
        List<Intersectable> rightPart = geometries.subList(halfSize, geometries.size());

        Intersectable leftCbr = buildHierarchyHelper(leftPart);
        Intersectable rightCbr = buildHierarchyHelper(rightPart);

        CBR newCbr = new CBR();
        newCbr.geometries = List.of(leftCbr, rightCbr);
        List<Point> leftMinMax = leftCbr.minMaxPoints();
        List<Point> rightMinMax = rightCbr.minMaxPoints();

        newCbr.min = Point.findMinimum(List.of(leftMinMax.get(0), rightMinMax.get(0)));
        newCbr.max = Point.findMaximum(List.of(leftMinMax.get(1), rightMinMax.get(1)));
        return newCbr;
    }
}