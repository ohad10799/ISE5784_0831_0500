package renderer;

import org.junit.jupiter.api.Test;
import lighting.*;
import geometries.*;
import primitives.*;
import scene.Scene;

import java.util.Random;
import java.util.function.BiConsumer;

import static java.awt.Color.*;

/**
 * The BVHTest class contains tests for rendering scenes with and without Bounding Volume Hierarchy (BVH).
 * It uses JUnit 5 to run tests and compares the performance of rendering scenes with many objects
 * with and without BVH optimization.
 */
public class BVHTest {

    private static final boolean RUN_BVH_TEST = false;

    /**
     * Tests rendering a scene with many trees, either using BVH or not based on the {@link #RUN_BVH_TEST} flag.
     */
    @Test
    public void testWithManyTrees(){
        if (RUN_BVH_TEST) {
            testBVHWithManyTrees();
        } else {
            testNonBVHWithManyTrees();
        }
    }

    /**
     * Tests rendering a scene with many trees without using Bounding Volume Hierarchy (BVH).
     * Creates a scene with ground and background planes and 200 randomly placed trees.
     * Renders the scene using a camera and saves the image.
     */
    public void testNonBVHWithManyTrees() {
        Scene scene = new Scene("BVH Test Scene");

        // Add ground plane
        scene.geometries.add(
                new Plane(new Point(0, 0, -30), new Vector(0, 0, 1))
                        .setEmission(new Color(47, 79, 79))
                        .setMaterial(new Material()
                                .setkD(0.8)
                                .setkS(0.2)
                                .setShininess(30))
        );

        // Add background planes
        scene.geometries.add(
                new Plane(new Point(0, -1000, 0), new Vector(0, 1, 0))
                        .setEmission(new Color(128, 128, 128))
                        .setMaterial(new Material()
                                .setkD(0.8)
                                .setkS(0.2)
                                .setShininess(30))
        );

        scene.geometries.add(
                new Plane(new Point(-2000, 0, 0), new Vector(1, 0, 0))
                        .setEmission(new Color(10, 100, 150))
                        .setMaterial(new Material()
                                .setkD(0.8)
                                .setkS(0.2)
                                .setShininess(30))
        );

        // Function to create a tree
        BiConsumer<Double, Double> createTree = (x, y) -> {
            double floorSphereRadius = 20d;
            for (int i = 0; i < 8; i++) {
                scene.geometries.add(
                        new Sphere(floorSphereRadius, new Point(x, y, -30 + floorSphereRadius + 20 * i))
                                .setEmission(new Color(83, 49, 24))
                                .setMaterial(new Material()
                                        .setkD(0.4)
                                        .setkS(0.3)
                                        .setShininess(100)
                                        .setkT(0.1)
                                        .setkR(0.1))
                );
            }

            double baseOffset = 20;
            double pyramidHeight = 60;
            double topZ = -32 + floorSphereRadius + 20 * 8;

            Point base1 = new Point(x - baseOffset, y - baseOffset, topZ);
            Point base2 = new Point(x + baseOffset, y - baseOffset, topZ);
            Point base3 = new Point(x, y + baseOffset, topZ);
            Point apex = new Point(x, y, topZ + pyramidHeight);

            scene.geometries.add(new Triangle(base1, base2, apex)
                    .setEmission(new Color(GREEN))
                    .setMaterial(new Material()
                            .setkD(0.5)
                            .setkS(0.5)
                            .setShininess(100)
                            .setkT(0)
                            .setkR(0)));
            scene.geometries.add(new Triangle(base2, base3, apex)
                    .setEmission(new Color(GREEN))
                    .setMaterial(new Material()
                            .setkD(0.5)
                            .setkS(0.5)
                            .setShininess(100)
                            .setkT(0.2)
                            .setkR(0)));
            scene.geometries.add(new Triangle(base3, base1, apex)
                    .setEmission(new Color(GREEN))
                    .setMaterial(new Material()
                            .setkD(0.5)
                            .setkS(0.5)
                            .setShininess(100)
                            .setkT(0)
                            .setkR(0)));
        };

        // Create 200 trees
        Random random = new Random(42);
        for (int i = 0; i < 200; i++) {
            double x = random.nextDouble() * 900 - 700;
            double y = random.nextDouble() * 900 - 700;
            createTree.accept(x, y);
        }

        // Lights
        scene.setAmbientLight(new AmbientLight(new Color(WHITE), 0.02));

        scene.lights.add(
                new SpotLight(new Color(255, 255, 255), new Point(200, 200, 200), new Vector(-1, -1, -1))
                        .setKl(4E-4)
                        .setKq(2E-6));

        scene.lights.add(
                new PointLight(new Color(50, 50, 100), new Point(-100, -100, 200))
                        .setKl(4E-4)
                        .setKq(2E-5));

        // Create a camera
        Camera camera = Camera.getBuilder()
                .setLocation(new Point(0, -200, 1500))
                .setDirection(new Vector(0, 0.8, -0.6), new Vector(0, 0.6, 0.8))  // Looking down
                .setVpSize(300, 300)
                .setVpDistance(2000)
                .setUseDepthOfField(false)
                .setMultithreading(-1)
                .setDebugPrint(0.1)
                .rotateAroundXAxis(-5)
                .rotateAroundZAxis(10)
                .setRayTracer(new SimpleRayTracer(scene))
                .setImageWriter(new ImageWriter("BVHTestWithManyTrees", 800, 800))
                .build();

        // Render the image
        camera.renderImage().writeToImage();
    }

    /**
     * Tests rendering a scene with many trees using Bounding Volume Hierarchy (BVH).
     * Creates a root CBR for the entire scene, adds ground and background planes, and 200 randomly placed trees.
     * Constructs a BVH hierarchy and sets it as the geometries of the scene. Renders the scene using a camera
     * and saves the image.
     */
    public void testBVHWithManyTrees() {
        Scene scene = new Scene("BVH Test Scene");

        // Create a root CBR for the entire scene
        CBR rootCBR = new CBR();

        // Add ground plane
        rootCBR.add(new Plane(new Point(0, 0, -30), new Vector(0, 0, 1))
                .setEmission(new Color(47, 79, 79))
                .setMaterial(new Material()
                        .setkD(0.8)
                        .setkS(0.2)
                        .setShininess(30)));


        // Add background planes
        rootCBR.add(new Plane(new Point(0, -1000, 0), new Vector(0, 1, 0))
                .setEmission(new Color(128, 128, 128))
                .setMaterial(new Material()
                        .setkD(0.8)
                        .setkS(0.2)
                        .setShininess(30)));


        rootCBR.add(new Plane(new Point(-2000, 0, 0), new Vector(1, 0, 0))
                .setEmission(new Color(10, 100, 150))
                .setMaterial(new Material()
                        .setkD(0.8)
                        .setkS(0.2)
                        .setShininess(30)));


        // Function to create a tree
        BiConsumer<Double, Double> createTree = (x, y) -> {
            CBR treeCBR = new CBR();
            double floorSphereRadius = 20d;
            for (int i = 0; i < 8; i++) {
                treeCBR.add(new Sphere(floorSphereRadius, new Point(x, y, -30 + floorSphereRadius + 20 * i))
                        .setEmission(new Color(83, 49, 24))
                        .setMaterial(new Material()
                                .setkD(0.4)
                                .setkS(0.3)
                                .setShininess(100)
                                .setkT(0.1)
                                .setkR(0.1)));

            }

            double baseOffset = 20;
            double pyramidHeight = 60;
            double topZ = -32 + floorSphereRadius + 20 * 8;

            Point base1 = new Point(x - baseOffset, y - baseOffset, topZ);
            Point base2 = new Point(x + baseOffset, y - baseOffset, topZ);
            Point base3 = new Point(x, y + baseOffset, topZ);
            Point apex = new Point(x, y, topZ + pyramidHeight);

            treeCBR.add(new Triangle(base1, base2, apex)
                    .setEmission(new Color(GREEN))
                    .setMaterial(new Material()
                            .setkD(0.5)
                            .setkS(0.5)
                            .setShininess(100)
                            .setkT(0)
                            .setkR(0)));

            treeCBR.add(new Triangle(base2, base3, apex)
                    .setEmission(new Color(GREEN))
                    .setMaterial(new Material()
                            .setkD(0.5)
                            .setkS(0.5).setShininess(100)
                            .setkT(0.2)
                            .setkR(0)));

            treeCBR.add(new Triangle(base3, base1, apex)
                    .setEmission(new Color(GREEN))
                    .setMaterial(new Material()
                            .setkD(0.5)
                            .setkS(0.5)
                            .setShininess(100)
                            .setkT(0)
                            .setkR(0)));

            rootCBR.add(treeCBR);
        };

        // Create 200 trees
        Random random = new Random(42);
        for (int i = 0; i < 200; i++) {
            double x = random.nextDouble() * 900 - 700;
            double y = random.nextDouble() * 900 - 700;
            createTree.accept(x, y);
        }

        // Build the BVH hierarchy
        rootCBR.buildHierarchy();

        // Set the scene geometries to the root CBR
        scene.geometries = rootCBR;

        // Lights
        scene.setAmbientLight(new AmbientLight(new Color(WHITE), 0.02));

        scene.lights.add(
                new SpotLight(new Color(255, 255, 255), new Point(200, 200, 200), new Vector(-1, -1, -1))
                        .setKl(4E-4).setKq(2E-6));

        scene.lights.add(
                new PointLight(new Color(50, 50, 100), new Point(-100, -100, 200))
                        .setKl(4E-4).setKq(2E-5));

        // Create a camera
        Camera camera = Camera.getBuilder()
                .setLocation(new Point(0, -200, 1500))
                .setDirection(new Vector(0, 0.8, -0.6), new Vector(0, 0.6, 0.8))
                .setVpSize(300, 300)
                .setVpDistance(2000)
                .setUseDepthOfField(false)
                .setMultithreading(-1)
                .setDebugPrint(0.1)
                .setRayTracer(new SimpleRayTracer(scene))
                .rotateAroundXAxis(-5)
                .rotateAroundZAxis(10)
                .setImageWriter(new ImageWriter("BVHTestWithManyTrees", 800, 800))
                .build();

        // Render the image
        camera.renderImage().writeToImage();
    }
}