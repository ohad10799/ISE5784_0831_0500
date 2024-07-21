package renderer;

import primitives.*;
import geometries.*;
import lighting.*;
import org.junit.jupiter.api.Test;
import scene.Scene;

import static java.awt.Color.*;

public class myPictures {

    private final Scene          scene         = new Scene("Test scene");
    /** Camera builder for the tests with triangles */
    private final Camera.Builder cameraBuilder = Camera.getBuilder()
            .setDirection(Vector.Z, Vector.Y)
            .setRayTracer(new SimpleRayTracer(scene));

    @Test
    public void render3DPyramidOfSpheres() {
        double radius = 5d;
        double spacing = 2 * radius; // distance between sphere centers
        double heightStep = 1.5 * radius; // height difference between layers

        // Create the plane (floor)
        scene.geometries.add(
                new Plane(new Point(0, -20, 0), new Vector(0, 1, 0))
                        .setEmission(new Color(YELLOW))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setShininess(30))
        );

        // First layer (16 spheres in a 4x4 grid)
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                double x = (i - 1.5) * spacing;
                double z = (j - 1.5) * spacing + 400;
                scene.geometries.add(
                        new Sphere(radius, new Point(x, -20 + radius, z))
                                .setEmission(new Color(BLUE))
                                .setMaterial(new Material().setkD(0.4).setkS(0.3).setShininess(100).setkT(0.3))
                );
            }
        }

        // Second layer (8 spheres in a 3x3 grid)
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                double x = (i - 1) * spacing;
                double z = (j - 1) * spacing + 400;
                scene.geometries.add(
                        new Sphere(radius, new Point(x, -20 + radius + heightStep, z))
                                .setEmission(new Color(RED))
                                .setMaterial(new Material().setkD(0.4).setkS(0.3).setShininess(100).setkT(0.3))
                );
            }
        }

        // Third layer (4 spheres in a 2x2 grid)
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                double x = (i - 0.5) * spacing;
                double z = (j - 0.5) * spacing + 400;
                scene.geometries.add(
                        new Sphere(radius, new Point(x, -20 + radius + 2 * heightStep, z))
                                .setEmission(new Color(GREEN))
                                .setMaterial(new Material().setkD(0.4).setkS(0.3).setShininess(100).setkT(0.3))
                );
            }
        }

        // Fourth layer (1 sphere at the top)
        scene.geometries.add(
                new Sphere(radius , new Point(0, -20 + radius + 3 * heightStep, 400))
                        .setEmission(new Color(BLUE)) //
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setShininess(30))
        );


        // Add a light source
        scene.setAmbientLight(new AmbientLight(new Color(WHITE), 0.15));
        scene.lights.add(
                new SpotLight(new Color(700, 400, 400), new Point(100, 40, 115), new Vector(-1, -1, -4)) //
                        .setKl(4E-4).setKq(2E-5));



        // Set up the camera and render the image
        cameraBuilder.setLocation(new Point(0, 0, 1000))
                .setVpSize(200, 200)
                .setImageWriter(new ImageWriter("pyramidOfSpheres3D", 500, 500))
                .setVpDistance(1500)
                .build()
                .renderImage()
                .writeToImage();
    }
}
