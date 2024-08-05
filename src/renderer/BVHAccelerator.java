package renderer;

import primitives.Point;
import primitives.Ray;
import geometries.Geometry;
import geometries.Intersectable.GeoPoint;
import java.util.ArrayList;
import java.util.List;

public class BVHAccelerator {
    private final BVHNode root;
    private static final int MIN_OBJECTS = 2;
    private static final int MAX_DEPTH = 20;

    public BVHAccelerator(List<Geometry> geometries) {
        root = buildBVH(geometries, 0);
    }

    private BVHNode buildBVH(List<Geometry> geometries, int depth) {
        if (geometries.isEmpty()) {
            return null;
        }

        if (geometries.size() <= MIN_OBJECTS || depth >= MAX_DEPTH) {
            return new BVHLeafNode(geometries);
        }

        BoundingBox boundingBox = calculateBoundingBox(geometries);
        BVHSplitResult splitResult = splitGeometries(geometries);

        if (splitResult.leftGeometries.isEmpty() || splitResult.rightGeometries.isEmpty()) {
            return new BVHLeafNode(geometries);
        }

        return new BVHInnerNode(
                boundingBox,
                buildBVH(splitResult.leftGeometries, depth + 1),
                buildBVH(splitResult.rightGeometries, depth + 1)
        );
    }

    private BoundingBox calculateBoundingBox(List<Geometry> geometries) {
        if (geometries.isEmpty()) {
            throw new IllegalArgumentException("Geometry list cannot be empty");
        }

        double minX = Double.POSITIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double minZ = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;
        double maxZ = Double.NEGATIVE_INFINITY;

        for (Geometry geometry : geometries) {
            BoundingBox box = geometry.getBoundingBox();
            minX = Math.min(minX, box.getMin().getX());
            minY = Math.min(minY, box.getMin().getY());
            minZ = Math.min(minZ, box.getMin().getZ());
            maxX = Math.max(maxX, box.getMax().getX());
            maxY = Math.max(maxY, box.getMax().getY());
            maxZ = Math.max(maxZ, box.getMax().getZ());
        }

        Point min = new Point(minX, minY, minZ);
        Point max = new Point(maxX, maxY, maxZ);

        return new BoundingBox(min, max);
    }

    private BVHSplitResult splitGeometries(List<Geometry> geometries) {
        if (geometries.size() <= 1) {
            // No need to split if there's 0 or 1 geometry
            return new BVHSplitResult(new ArrayList<>(geometries), new ArrayList<>());
        }

        // Calculate the median x-coordinate
        List<Double> xCoordinates = new ArrayList<>();
        for (Geometry geometry : geometries) {
            BoundingBox box = geometry.getBoundingBox();
            xCoordinates.add(box.getMin().getX());
            xCoordinates.add(box.getMax().getX());
        }

        xCoordinates.sort(Double::compareTo);
        double medianX = xCoordinates.get(xCoordinates.size() / 2);

        // Partition the geometries into two groups
        List<Geometry> leftGeometries = new ArrayList<>();
        List<Geometry> rightGeometries = new ArrayList<>();

        for (Geometry geometry : geometries) {
            BoundingBox box = geometry.getBoundingBox();
            if (box.getMax().getX() <= medianX) {
                leftGeometries.add(geometry);
            } else if (box.getMin().getX() >= medianX) {
                rightGeometries.add(geometry);
            } else {
                // If the geometry overlaps the median, it can be added to both sides
                leftGeometries.add(geometry);
                rightGeometries.add(geometry);
            }
        }

        return new BVHSplitResult(leftGeometries, rightGeometries);
    }

    private int getLongestAxis(BoundingBox box) {
        double dx = box.getMax().getX() - box.getMin().getX();
        double dy = box.getMax().getY() - box.getMin().getY();
        double dz = box.getMax().getZ() - box.getMin().getZ();

        if (dx > dy && dx > dz) return 0;
        if (dy > dz) return 1;
        return 2;
    }

    public GeoPoint findClosestIntersection(Ray ray) {
        List<GeoPoint> intersections = new ArrayList<>();
        findIntersections(root, ray, intersections);
        if (intersections.isEmpty()) {
            return null;
        }
        return ray.findClosestGeoPoint(intersections);
    }

    private void findIntersections(BVHNode node, Ray ray, List<GeoPoint> intersections) {
        if (node == null || !node.boundingBox.intersects(ray)) {
            return;
        }

        if (node instanceof BVHLeafNode) {
            BVHLeafNode leafNode = (BVHLeafNode) node;
            for (Geometry geometry : leafNode.geometries) {
                List<GeoPoint> geometryIntersections = geometry.findGeoIntersections(ray);
                if (geometryIntersections != null) {
                    intersections.addAll(geometryIntersections);
                }
            }
        } else {
            BVHInnerNode innerNode = (BVHInnerNode) node;
            findIntersections(innerNode.left, ray, intersections);
            findIntersections(innerNode.right, ray, intersections);
        }
    }

    private static abstract class BVHNode {
        BoundingBox boundingBox;

        BVHNode(BoundingBox boundingBox) {
            this.boundingBox = boundingBox;
        }
    }

    private class BVHLeafNode extends BVHNode {
        List<Geometry> geometries;

        BVHLeafNode(List<Geometry> geometries) {
            super(calculateBoundingBox(geometries));
            this.geometries = geometries;
        }
    }

    private class BVHInnerNode extends BVHNode {
        BVHNode left;
        BVHNode right;

        BVHInnerNode(BoundingBox boundingBox, BVHNode left, BVHNode right) {
            super(boundingBox);
            this.left = left;
            this.right = right;
        }
    }

    private class BVHSplitResult {
        List<Geometry> leftGeometries;
        List<Geometry> rightGeometries;

        BVHSplitResult(List<Geometry> leftGeometries, List<Geometry> rightGeometries) {
            this.leftGeometries = leftGeometries;
            this.rightGeometries = rightGeometries;
        }
    }

    public void renderBoundingBoxes() {
        // Traverse the BVH and render each bounding box
        renderBoundingBox(root);
    }

    private void renderBoundingBox(BVHNode node) {
        if (node == null) return;

        // Render the current node's bounding box
        renderBox(node.boundingBox);

        if (node instanceof BVHInnerNode) {
            BVHInnerNode innerNode = (BVHInnerNode) node;
            renderBoundingBox(innerNode.left);
            renderBoundingBox(innerNode.right);
        }
    }

    private void renderBox(BoundingBox box) {


    }
}