package renderer;
import primitives.*;
import geometries.*;
import lighting.*;
import renderer.*;
import scene.Scene;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static java.awt.Color.*;

public class BVHTest {

    private final Scene scene = new Scene("Complex Test Scene");
    private final Random random = new Random(42); // Seed for reproducibility

    @Test
    public void renderComplexSceneWithoutBVH() {
        buildComplexScene();
        renderScene("ComplexScene_NoBVH");
    }

    @Test
    public void renderComplexSceneWithBVH() {
        CBR sceneGeometries = new CBR();
        buildComplexSceneBVH(sceneGeometries);
        sceneGeometries.buildHierarchy();
        scene.geometries.add(sceneGeometries);
        renderScene("ComplexScene_WithBVH");
    }

    private void buildComplexScene() {
        // Ground and background planes
        scene.geometries.add(new Plane(new Point(0, 0, -30), new Vector(0, 0, 1))
                .setEmission(new Color(47, 79, 79))
                .setMaterial(new Material().setkD(0.8).setkS(0.2).setShininess(30)));
        scene.geometries.add(new Plane(new Point(0, -700, 0), new Vector(0, 1, 0))
                .setEmission(new Color(128, 128, 128))
                .setMaterial(new Material().setkD(0.8).setkS(0.2).setShininess(30)));
        scene.geometries.add(new Plane(new Point(-1000, 0, 0), new Vector(1, 0, 0))
                .setEmission(new Color(10, 100, 150))
                .setMaterial(new Material().setkD(0.8).setkS(0.2).setShininess(30)));

        // Add pyramid of spheres
        addPyramidOfSpheres();

        // Add multiple trees
        for (int i = 0; i < 50; i++) {
            addTree(-500 + i * 100, -300 + (i % 3) * 200, -30);
        }

        // Add random spheres
        for (int i = 0; i < 400; i++) {
            addRandomSphere();
        }

        // Add random triangles
        for (int i = 0; i < 400; i++) {
            addRandomTriangle();
        }

        setupLights();
    }

    private void buildComplexSceneBVH(CBR sceneGeometries) {
        // Ground and background planes
        sceneGeometries.add(new Plane(new Point(0, 0, -30), new Vector(0, 0, 1))
                .setEmission(new Color(47, 79, 79))
                .setMaterial(new Material().setkD(0.8).setkS(0.2).setShininess(30)));
        sceneGeometries.add(new Plane(new Point(0, -700, 0), new Vector(0, 1, 0))
                .setEmission(new Color(128, 128, 128))
                .setMaterial(new Material().setkD(0.8).setkS(0.2).setShininess(30)));
        sceneGeometries.add(new Plane(new Point(-1000, 0, 0), new Vector(1, 0, 0))
                .setEmission(new Color(10, 100, 150))
                .setMaterial(new Material().setkD(0.8).setkS(0.2).setShininess(30)));

        // Add pyramid of spheres
        addPyramidOfSpheresBVH(sceneGeometries);

        // Add multiple trees
        for (int i = 0; i < 10; i++) {
            addTreeBVH(sceneGeometries, -500 + i * 100, -300 + (i % 3) * 200, -30);
        }

        // Add random spheres
        for (int i = 0; i < 100; i++) {
            addRandomSphereBVH(sceneGeometries);
        }

        // Add random triangles
        for (int i = 0; i < 100; i++) {
            addRandomTriangleBVH(sceneGeometries);
        }

        setupLights();
    }

    private void addPyramidOfSpheres() {
        double radius = 15d;
        double spacing = 2 * radius;
        double heightStep = 25;

        // First layer (16 spheres in a 4x4 grid)
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                double x = (i - 1.5) * spacing;
                double y = (j - 1.5) * spacing;
                scene.geometries.add(new Sphere(radius, new Point(x, y, -30 + radius))
                        .setEmission(new Color(BLUE))
                        .setMaterial(new Material().setkD(0.4).setkS(0.3).setShininess(100).setkT(0.1).setkR(0.1)));
            }
        }

        // Second layer (9 spheres in a 3x3 grid)
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                double x = (i - 1) * spacing;
                double y = (j - 1) * spacing;
                scene.geometries.add(new Sphere(radius, new Point(x, y, -30 + radius + heightStep))
                        .setEmission(new Color(RED))
                        .setMaterial(new Material().setkD(0.4).setkS(0.3).setShininess(100).setkT(0.1).setkR(0.1)));
            }
        }

        // Third layer (4 spheres in a 2x2 grid)
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                double x = (i - 0.5) * spacing;
                double y = (j - 0.5) * spacing;
                scene.geometries.add(new Sphere(radius, new Point(x, y, -30 + radius + 2 * heightStep))
                        .setEmission(new Color(GREEN))
                        .setMaterial(new Material().setkD(0.4).setkS(0.3).setShininess(100).setkT(0.1).setkR(0.1)));
            }
        }

        // Fourth layer (1 sphere at the top)
        scene.geometries.add(new Sphere(radius, new Point(0, 0, -30 + radius + 3 * heightStep))
                .setEmission(new Color(BLUE))
                .setMaterial(new Material().setkD(0.5).setkS(0.5).setShininess(30).setkT(0.1).setkR(0.1)));
    }

    private void addPyramidOfSpheresBVH(CBR sceneGeometries) {
        double radius = 15d;
        double spacing = 2 * radius;
        double heightStep = 25;

        // First layer (16 spheres in a 4x4 grid)
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                double x = (i - 1.5) * spacing;
                double y = (j - 1.5) * spacing;
                sceneGeometries.add(new Sphere(radius, new Point(x, y, -30 + radius))
                        .setEmission(new Color(BLUE))
                        .setMaterial(new Material().setkD(0.4).setkS(0.3).setShininess(100).setkT(0.1).setkR(0.1)));
            }
        }

        // Second layer (9 spheres in a 3x3 grid)
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                double x = (i - 1) * spacing;
                double y = (j - 1) * spacing;
                sceneGeometries.add(new Sphere(radius, new Point(x, y, -30 + radius + heightStep))
                        .setEmission(new Color(RED))
                        .setMaterial(new Material().setkD(0.4).setkS(0.3).setShininess(100).setkT(0.1).setkR(0.1)));
            }
        }

        // Third layer (4 spheres in a 2x2 grid)
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                double x = (i - 0.5) * spacing;
                double y = (j - 0.5) * spacing;
                sceneGeometries.add(new Sphere(radius, new Point(x, y, -30 + radius + 2 * heightStep))
                        .setEmission(new Color(GREEN))
                        .setMaterial(new Material().setkD(0.4).setkS(0.3).setShininess(100).setkT(0.1).setkR(0.1)));
            }
        }

        // Fourth layer (1 sphere at the top)
        sceneGeometries.add(new Sphere(radius, new Point(0, 0, -30 + radius + 3 * heightStep))
                .setEmission(new Color(BLUE))
                .setMaterial(new Material().setkD(0.5).setkS(0.5).setShininess(30).setkT(0.1).setkR(0.1)));
    }

    private void addTree(double x, double y, double z) {
        double floorSphereRadius = 20d;

        // Add tree trunk (8 spheres in a vertical line)
        for (int i = 0; i < 8; i++) {
            scene.geometries.add(new Sphere(floorSphereRadius, new Point(x, y, z + floorSphereRadius + 20 * i))
                    .setEmission(new Color(83, 49, 24))
                    .setMaterial(new Material().setkD(0.4).setkS(0.3).setShininess(100).setkT(0.1).setkR(0.1)));
        }

        // Add tree top (pyramid of triangles)
        double baseOffset = 20;
        double pyramidHeight = 60;
        double topZ = z + floorSphereRadius + 20 * 8;

        Point base1 = new Point(x - baseOffset, y - baseOffset, topZ);
        Point base2 = new Point(x + baseOffset, y - baseOffset, topZ);
        Point base3 = new Point(x, y + baseOffset, topZ);
        Point apex = new Point(x, y, topZ + pyramidHeight);

        scene.geometries.add(new Triangle(base1, base2, apex)
                .setEmission(new Color(GREEN))
                .setMaterial(new Material().setkD(0.5).setkS(0.5).setShininess(100)));
        scene.geometries.add(new Triangle(base2, base3, apex)
                .setEmission(new Color(GREEN))
                .setMaterial(new Material().setkD(0.5).setkS(0.5).setShininess(100)));
        scene.geometries.add(new Triangle(base3, base1, apex)
                .setEmission(new Color(GREEN))
                .setMaterial(new Material().setkD(0.5).setkS(0.5).setShininess(100)));
    }

    private void addTreeBVH(CBR sceneGeometries, double x, double y, double z) {
        double floorSphereRadius = 20d;

        // Add tree trunk (8 spheres in a vertical line)
        for (int i = 0; i < 8; i++) {
            sceneGeometries.add(new Sphere(floorSphereRadius, new Point(x, y, z + floorSphereRadius + 20 * i))
                    .setEmission(new Color(83, 49, 24))
                    .setMaterial(new Material().setkD(0.4).setkS(0.3).setShininess(100).setkT(0.1).setkR(0.1)));
        }

        // Add tree top (pyramid of triangles)
        double baseOffset = 20;
        double pyramidHeight = 60;
        double topZ = z + floorSphereRadius + 20 * 8;

        Point base1 = new Point(x - baseOffset, y - baseOffset, topZ);
        Point base2 = new Point(x + baseOffset, y - baseOffset, topZ);
        Point base3 = new Point(x, y + baseOffset, topZ);
        Point apex = new Point(x, y, topZ + pyramidHeight);

        sceneGeometries.add(new Triangle(base1, base2, apex)
                .setEmission(new Color(GREEN))
                .setMaterial(new Material().setkD(0.5).setkS(0.5).setShininess(100)));
        sceneGeometries.add(new Triangle(base2, base3, apex)
                .setEmission(new Color(GREEN))
                .setMaterial(new Material().setkD(0.5).setkS(0.5).setShininess(100)));
        sceneGeometries.add(new Triangle(base3, base1, apex)
                .setEmission(new Color(GREEN))
                .setMaterial(new Material().setkD(0.5).setkS(0.5).setShininess(100)));
    }

    private void addRandomSphere() {
        double x = random.nextDouble() * 1000 - 500;
        double y = random.nextDouble() * 1000 - 500;
        double z = random.nextDouble() * 200 - 100;
        double radius = random.nextDouble() * 10 + 5;

        scene.geometries.add(new Sphere(radius, new Point(x, y, z))
                .setEmission(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)))
                .setMaterial(new Material().setkD(0.4).setkS(0.3).setShininess(100).setkT(0.1).setkR(0.1)));
    }

    private void addRandomSphereBVH(CBR sceneGeometries) {
        double x = random.nextDouble() * 1000 - 500;
        double y = random.nextDouble() * 1000 - 500;
        double z = random.nextDouble() * 200 - 100;
        double radius = random.nextDouble() * 10 + 5;

        sceneGeometries.add(new Sphere(radius, new Point(x, y, z))
                .setEmission(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)))
                .setMaterial(new Material().setkD(0.4).setkS(0.3).setShininess(100).setkT(0.1).setkR(0.1)));
    }

    private void addRandomTriangle() {
        Point p1 = new Point(random.nextDouble() * 1000 - 500, random.nextDouble() * 1000 - 500, random.nextDouble() * 200 - 100);
        Point p2 = new Point(random.nextDouble() * 1000 - 500, random.nextDouble() * 1000 - 500, random.nextDouble() * 200 - 100);
        Point p3 = new Point(random.nextDouble() * 1000 - 500, random.nextDouble() * 1000 - 500, random.nextDouble() * 200 - 100);

        scene.geometries.add(new Triangle(p1, p2, p3)
                .setEmission(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)))
                .setMaterial(new Material().setkD(0.5).setkS(0.5).setShininess(100)));
    }

    private void addRandomTriangleBVH(CBR sceneGeometries) {
        Point p1 = new Point(random.nextDouble() * 1000 - 500, random.nextDouble() * 1000 - 500, random.nextDouble() * 200 - 100);
        Point p2 = new Point(random.nextDouble() * 1000 - 500, random.nextDouble() * 1000 - 500, random.nextDouble() * 200 - 100);
        Point p3 = new Point(random.nextDouble() * 1000 - 500, random.nextDouble() * 1000 - 500, random.nextDouble() * 200 - 100);

        sceneGeometries.add(new Triangle(p1, p2, p3)
                .setEmission(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)))
                .setMaterial(new Material().setkD(0.5).setkS(0.5).setShininess(100)));
    }

    private void setupLights() {
        scene.lights.add(new DirectionalLight(new Color(WHITE), new Vector(1, 1, -1)));
        scene.lights.add(new SpotLight(new Color(WHITE), new Point(-100, -100, 400), new Vector(1, 1, -2))
                .setKl(0.0001).setKq(0.00005));
        scene.lights.add(new PointLight(new Color(255, 215, 0), new Point(500, -500, -100))
                .setKl(0.0005).setKq(0.0001));
    }

    private void renderScene(String imageName) {

        double distance = 500; // Distance from the origin
        Point baseLocation = new Point(0, -distance, 100);
        Vector baseDirection = new Vector(0, 1, 0);
        Vector baseUp = new Vector(0, 0, 1);
        Camera camera1 = Camera.getBuilder()
                .setLocation(baseLocation)
                .setDirection(baseDirection, baseUp)
                .setVpSize(300, 300)
                .setVpDistance(distance)
                .setUseDepthOfField(false)
                .setRayTracer(new SimpleRayTracer(scene))
                .setImageWriter(new ImageWriter(imageName, 800, 800))
                .build();

        // Render the image
        camera1.renderImage().writeToImage();
    }
}