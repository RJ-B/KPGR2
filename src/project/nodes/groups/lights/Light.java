package project.nodes.groups.lights;

import project.nodes.groups.NodeGroup;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

/**
 * Abstraktní třída reprezentující světlo v grafu scény
 */

public abstract class Light extends NodeGroup {

    private final float[] ambient;
    private final float[] diffuse;
    private final float[] specular;

    private static final List<Integer> lightIds = new ArrayList<>(8);

    private final int lightMaxId = 7;

    private int lightId = -1;

    public Light(float[] ambient, float[] diffuse, float[] specular) {
        if(ambient.length != 4)
            throw new IllegalArgumentException("Light ambient parameter must be a vector with 4 components.");
        if(diffuse.length != 4)
            throw new IllegalArgumentException("Light diffuse parameter must be a vector with 4 components.");
        if(specular.length != 4)
            throw new IllegalArgumentException("Light specular parameter must be a vector with 4 components.");


        for(int i = 0; i < lightMaxId; i++) {
            if (!lightIds.contains(i)) {
                lightId = i;
                lightIds.add(lightId);
                break;
            }
        }

        if (lightId == -1)
            throw new IllegalStateException("Maximum number of lights has been reached.");

        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
    }

    protected int getLightId() {
        return switch (lightId) {
            case 0 -> GL_LIGHT0;
            case 1 -> GL_LIGHT1;
            case 2 -> GL_LIGHT2;
            case 3 -> GL_LIGHT3;
            case 4 -> GL_LIGHT4;
            case 5 -> GL_LIGHT5;
            case 6 -> GL_LIGHT6;
            case 7 -> GL_LIGHT7;
            default -> throw new IllegalStateException("Maximum number of lights has been reached.");
        };
    }

    public float[] getAmbient() {
        return ambient;
    }

    public float[] getDiffuse() {
        return diffuse;
    }

    public float[] getSpecular() {
        return specular;
    }

    @Override
    public void dispose() {
        super.dispose();

        lightIds.remove(lightId);
        lightId = -1;
    }

    protected void renderNodes() {
        super.render();
    }

    @Override
    public void render() {
        glLightfv(getLightId(), GL_AMBIENT, getAmbient());
        glLightfv(getLightId(), GL_DIFFUSE, getDiffuse());
        glLightfv(getLightId(), GL_SPECULAR, getSpecular());
    }
}
