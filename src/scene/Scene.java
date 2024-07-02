package scene;

import geometries.Geometries;
import lighting.AmbientLight;
import lighting.LightSource;
import primitives.Color;

import java.util.LinkedList;
import java.util.List;

public class Scene {

    public final String sceneName;
    public Geometries geometries = new Geometries();
    public AmbientLight ambientLight = AmbientLight.NONE;
    public Color background = Color.BLACK;
    public List<LightSource> lights = new LinkedList<>();

    public Scene(String sceneName) {
        this.sceneName = sceneName;
    }

    public Scene setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight = ambientLight;
        return this;
    }

    public Scene setBackground(Color backgroundColor) {
        this.background = backgroundColor;
        return this;
    }

    public Scene setGeometries(Geometries geometries) {
        this.geometries = geometries;
        return this;
    }

    public Scene setLights(List<LightSource> lights) {
        this.lights = lights;
        return this;
    }
}
