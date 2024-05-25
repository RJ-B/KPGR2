package project.nodes.groups.lights;

import static org.lwjgl.opengl.GL11.*;

/**
 * Třída pro směrové světlo
 */

public class DirectionalLight extends Light {

    private final float[] direction;

    public DirectionalLight(float[] direction, float[] ambient, float[] diffuse, float[] specular) {
        super(ambient, diffuse, specular);

        if (direction.length != 3)
            throw new IllegalArgumentException("Light direction parameter must be a vector with 3 components.");

        this.direction = new float[] {direction[0], direction[1], direction[2], 0};
    }

    @Override
    public void render() {
        super.render();

        glLightfv(getLightId(), GL_POSITION, direction);

        glEnable(GL_LIGHTING);
        glEnable(getLightId());

        super.renderNodes();

        glDisable(GL_LIGHTING);
        glDisable(getLightId());
    }
}
