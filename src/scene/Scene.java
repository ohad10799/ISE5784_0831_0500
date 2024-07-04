package scene;

import geometries.Geometries;
import lighting.AmbientLight;
import lighting.LightSource;
import primitives.Color;

import java.util.LinkedList;
import java.util.List;

/**
 * The Scene class represents a scene in a ray tracing application.
 * It encapsulates the geometries, ambient light, background color, and light sources of the scene.
 */
public class Scene {

    public final String sceneName; // The name of the scene
    public Geometries geometries = new Geometries(); // The geometries in the scene
    public AmbientLight ambientLight = AmbientLight.NONE; // The ambient light in the scene
    public Color background = Color.BLACK; // The background color of the scene
    public List<LightSource> lights = new LinkedList<>(); // The light sources in the scene

    /**
     * Constructs a Scene object with the specified name.
     *
     * @param sceneName the name of the scene
     */
    public Scene(String sceneName) {
        this.sceneName = sceneName;
    }

    /**
     * Sets the ambient light of the scene.
     *
     * @param ambientLight the ambient light to set
     * @return the Scene object with the updated ambient light
     */
    public Scene setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight = ambientLight;
        return this;
    }

    /**
     * Sets the background color of the scene.
     *
     * @param backgroundColor the background color to set
     * @return the Scene object with the updated background color
     */
    public Scene setBackground(Color backgroundColor) {
        this.background = backgroundColor;
        return this;
    }

    /**
     * Sets the geometries of the scene.
     *
     * @param geometries the geometries to set
     * @return the Scene object with the updated geometries
     */
    public Scene setGeometries(Geometries geometries) {
        this.geometries = geometries;
        return this;
    }

    /**
     * Sets the light sources of the scene.
     *
     * @param lights the light sources to set
     * @return the Scene object with the updated light sources
     */
    public Scene setLights(List<LightSource> lights) {
        this.lights = lights;
        return this;
    }
}
