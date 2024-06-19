package renderer;

import primitives.*;
import scene.Scene;

public abstract class RayTracerBase {

    protected final Scene scene;

    public RayTracerBase(Scene scene) {
        this.scene = scene;
    }

    public void renderImage(ImageWriter imageWriter) {
    }

    public Color traceRay(Ray ray) {
        return Color.BLACK;
    }
}
