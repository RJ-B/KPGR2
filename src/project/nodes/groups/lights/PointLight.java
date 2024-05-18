package project.nodes.groups.lights;

import java.util.Objects;

import static org.lwjgl.opengl.GL11.*;

/**
 * Bodové světlo
 */
public class PointLight extends Light {

    private final float[] position;

    public PointLight(float[] position, float[] ambient, float[] diffuse, float[] specular) {
        super(ambient, diffuse, specular);

        Objects.requireNonNull(position);

        if(position.length != 3)
            throw new IllegalArgumentException("Light position parameter must be a vector with 3 components.");

        this.position = new float[] {position[0], position[1], position[2], 1};
    }

    @Override
    public void render() {
        super.render();


        glLightfv(getLightId(), GL_POSITION, position);

        glEnable(GL_LIGHTING);
        glEnable(getLightId());

        super.renderNodes();

        glDisable(GL_LIGHTING);
        glDisable(getLightId());

    }

}
