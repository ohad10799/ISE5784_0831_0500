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
            .setDirection(Vector.Z, Vector.Y).
            setLocation(new Point(0, 0, 10)).
            setVpDistance(100)
            .setRayTracer(new SimpleRayTracer(scene));

    @Test
    public void renderTwoColorTest2() {
        scene.geometries.add(
                new Plane(new Point(0,-20,0),new Vector(0,1,0)).setEmission(new Color(YELLOW))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setShininess(30)),
                new Sphere(5d, new Point(-1.2,20,400)).setEmission(new Color(BLUE))
                        .setMaterial(new Material().setkD(0.4).setkS(0.3).setShininess(100).setkT(0.3))
                );

        scene.lights.add(
                new SpotLight(new Color(1000, 600, 0), new Point(-100, -100, 500), new Vector(-1, -1, -2))
                        .setKl(0.0004).setKq(0.0000006));

        cameraBuilder.setLocation(new Point(0, 0, 1000)).setVpDistance(1000)
                .setVpSize(150, 150)
                .setImageWriter(new ImageWriter("custom", 500, 500))
                .build()
                .renderImage()
                .writeToImage();
    }
}
