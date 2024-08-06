package renderer;

import primitives.*;
import geometries.*;
import lighting.*;
import org.junit.jupiter.api.Test;
import scene.Scene;

import static java.awt.Color.*;

/**
 * The class is responsible for rendering a 3D scene containing various geometric shapes.
 * The scene includes a pyramid of spheres, two trees made of spheres, and a pyramid of triangles on top of each tree.
 * Additionally, there are planes representing the ground and background, and a mirror for reflective effects.
 */
public class myPictures {

    // Scene to render
    private final Scene scene = new Scene("Test scene");

    // Camera builder for the tests with triangles
    private final Camera.Builder cameraBuilder = Camera.getBuilder()
            .setRayTracer(new SimpleRayTracer(scene));

    @Test
    public void render3DPyramidOfSpheres() {
        double radius = 15d;
        double spacing = 2 * radius ;
        double heightStep = 25;

        // ***** SHAPES *****

        // Add a large plane as the ground
        scene.geometries.add(
                new Plane(new Point(0, 0, -30), new Vector(0, 0, 1))
                        .setEmission(new Color(47,79,79))
                        .setMaterial(new Material().setkD(0.8).setkS(0.2).setShininess(30))
        );

        // Add background planes
        scene.geometries.add(
                new Plane(new Point(0, -700, 0), new Vector(0, 1, 0))
                        .setEmission(new Color(128, 128, 128))
                        .setMaterial(new Material().setkD(0.8).setkS(0.2).setShininess(30))
        );


        scene.geometries.add(
                new Plane(new Point(-1000, 0, 0), new Vector(1, 0, 0))
                        .setEmission(new Color(10, 100, 150))
                        .setMaterial(new Material().setkD(0.8).setkS(0.2).setShininess(30))
        );


        // First layer (16 spheres in a 4x4 grid)
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                double x = (i - 1.5) * spacing;
                double y = (j - 1.5) * spacing;
                scene.geometries.add(
                        new Sphere(radius, new Point(x, y, -30 + radius))  // Changed Z to -30 + radius
                                .setEmission(new Color(BLUE))
                                .setMaterial(new Material().setkD(0.4).setkS(0.3).setShininess(100).setkT(0.1).setkR(0.1))
                );
            }
        }

        // Second layer (9 spheres in a 3x3 grid)
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                double x = (i - 1) * spacing;
                double y = (j - 1) * spacing;
                scene.geometries.add(
                        new Sphere(radius, new Point(x, y, -30 + radius + heightStep))  // Adjusted Z
                                .setEmission(new Color(RED))
                                .setMaterial(new Material().setkD(0.4).setkS(0.3).setShininess(100).setkT(0.1).setkR(0.1))
                );
            }
        }

        // Third layer (4 spheres in a 2x2 grid)
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                double x = (i - 0.5) * spacing;
                double y = (j - 0.5) * spacing;
                scene.geometries.add(
                        new Sphere(radius, new Point(x, y, -30 + radius + 2 * heightStep))  // Adjusted Z
                                .setEmission(new Color(GREEN))
                                .setMaterial(new Material().setkD(0.4).setkS(0.3).setShininess(100).setkT(0.1).setkR(0.1))
                );
            }
        }

        // Fourth layer (1 sphere at the top)
        scene.geometries.add(
                new Sphere(radius, new Point(0, 0, -30 + radius + 3 * heightStep))  // Adjusted Z
                        .setEmission(new Color(BLUE))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setShininess(30).setkT(0.1).setkR(0.1))
        );


        double floorSphereRadius = 20d;

        // Add right tree (8 spheres in a vertical line)
        for (int i = 0 ; i<8 ; i++){
            scene.geometries.add(
                    new Sphere(floorSphereRadius, new Point(-500, 100, -30 + floorSphereRadius + 20 * i))
                            .setEmission(new Color(83, 49, 24))  // Light red color
                            .setMaterial(new Material().setkD(0.4).setkS(0.3).setShininess(100).setkT(0.1).setkR(0.1))
            );

        }

        double baseOffset = 20; // size of the pyramid base
        double pyramidHeight = 60;

        // Add pyramid of triangles on top of the right tree
        double rightTopX = -500d;
        double rightTopY = 100d;
        double rightTopZ = -32 + floorSphereRadius + 20 * 8;

        Point baseR1 = new Point(rightTopX - baseOffset, rightTopY - baseOffset, rightTopZ);
        Point baseR2 = new Point(rightTopX + baseOffset, rightTopY - baseOffset, rightTopZ);
        Point baseR3 = new Point(rightTopX, rightTopY + baseOffset, rightTopZ);
        Point apexR = new Point(rightTopX, rightTopY, rightTopZ + pyramidHeight);

        scene.geometries.add(new Triangle(baseR1, baseR2, apexR)
                .setEmission(new Color(GREEN))
                .setMaterial(new Material().setkD(0.5).setkS(0.5).setShininess(100).setkT(0).setkR(0)));
        scene.geometries.add(new Triangle(baseR2, baseR3, apexR)
                .setEmission(new Color(GREEN))
                .setMaterial(new Material().setkD(0.5).setkS(0.5).setShininess(100).setkT(0).setkR(0)));
        scene.geometries.add(new Triangle(baseR3, baseR1, apexR)
                .setEmission(new Color(GREEN))
                .setMaterial(new Material().setkD(0.5).setkS(0.5).setShininess(100).setkT(0).setkR(0)));


        // Add left tree (8 spheres in a vertical line)
        for (int i = 0 ; i<8 ; i++) {
            scene.geometries.add(
                    new Sphere(floorSphereRadius, new Point(-400, -300, -30 + floorSphereRadius + 20 * i))
                            .setEmission(new Color(83, 49, 24))  // Light green color
                            .setMaterial(new Material().setkD(0.4).setkS(0.3).setShininess(100).setkT(0.1).setkR(0.1))
            );
        }


        // Add pyramid of triangles on top of the left tree
        double leftTopX = -410d;
        double leftTopY = -300d;
        double leftTopZ = -32 + floorSphereRadius + 20 * 8;

        Point base1 = new Point(leftTopX - baseOffset, leftTopY - baseOffset, leftTopZ);
        Point base2 = new Point(leftTopX + baseOffset, leftTopY - baseOffset, leftTopZ);
        Point base3 = new Point(leftTopX, leftTopY + baseOffset, leftTopZ);
        Point apex = new Point(leftTopX, leftTopY, leftTopZ + pyramidHeight);

        scene.geometries.add(new Triangle(base1, base2, apex)
                .setEmission(new Color(GREEN))
                .setMaterial(new Material().setkD(0.5).setkS(0.5).setShininess(100).setkT(0).setkR(0)));
        scene.geometries.add(new Triangle(base2, base3, apex)
                .setEmission(new Color(GREEN))
                .setMaterial(new Material().setkD(0.5).setkS(0.5).setShininess(100).setkT(0.2).setkR(0)));
        scene.geometries.add(new Triangle(base3, base1, apex)
                .setEmission(new Color(GREEN))
                .setMaterial(new Material().setkD(0.5).setkS(0.5).setShininess(100).setkT(0).setkR(0)));


        // Create the mirror
        Point p1 = new Point(-999, -500, 200);  // left point
        Point p2 = new Point(-999, 150, 200);     // right point
        Point p3 = new Point(-999, -200, -500); // top point

        scene.geometries.add(new Triangle(p1, p2, p3)
                .setEmission(Color.BLACK) // No emission color
                .setMaterial(new Material().setkR(1.0))); // Reflective mirror effect



        // ***** LIGHTS *****

        // Set ambient light (keep it very low to emphasize shadows)
        scene.setAmbientLight(new AmbientLight(new Color(WHITE), 0.02));

        // Main strong light from above and to the side
        scene.lights.add(
                new SpotLight(new Color(255, 255, 255), new Point(200, 200, 200), new Vector(-1, -1, -1))
                        .setKl(4E-4).setKq(2E-6));

        // Secondary light for fill
        scene.lights.add(
                new PointLight(new Color(50, 50, 100), new Point(-100, -100, 200))
                        .setKl(4E-4).setKq(2E-5));


        // ***** CAMERA *****

        // Define camera parameters
        double distance = 400; // Distance from the origin
        Point baseLocation = new Point(0, -distance, 100);
        Vector baseDirection = new Vector(0, 1, 0);
        Vector baseUp = new Vector(0, 0, 1);

        // Create the camera with depth of field
        Camera camera1 = Camera.getBuilder()
                .setLocation(baseLocation)
                .setDirection(baseDirection, baseUp)
                .setVpSize(300, 300)
                .setVpDistance(distance)
                .setUseDepthOfField(true)
                .setFocalLength(400)
                .setApertureSize(10)
                .setRayTracer(new SimpleRayTracer(scene))
                .setImageWriter(new ImageWriter("pyramidOfSpheres_DoF", 800, 800))
                .build();

        // Apply rotation to the camera
        camera1.rotateAroundZAxis(15);

        // Render the image
        camera1.renderImage().writeToImage();

        // Create the camera without depth of field
        Camera camera2 = Camera.getBuilder()
                .setLocation(baseLocation)
                .setDirection(baseDirection, baseUp)
                .setVpSize(300, 300)
                .setVpDistance(distance)
                .setUseDepthOfField(false)
                .setRayTracer(new SimpleRayTracer(scene))
                .setImageWriter(new ImageWriter("pyramidOfSpheres_No_DoF", 800, 800))
                .build();

        // Apply rotation to the camera
        camera2.rotateAroundZAxis(15);

        // Render the image
        camera2.renderImage().writeToImage();
    }

}