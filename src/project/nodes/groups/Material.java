package project.nodes.groups;

import static org.lwjgl.opengl.GL11.*;

/**
 * Třída pro materiál
 */

public class Material extends NodeGroup {

    private float[] ambient;
    private float[] diffuse;
    private float[] specular;
    private float shininess;
    private float dissolve;
    private String name;

    public Material(float[] ambient, float[] diffuse, float[] specular, float shininess, String name, float dissolve) {
        if(ambient.length != 4)
            throw new IllegalArgumentException("Material ambient parameter must be a vector with 4 components.");
        if(diffuse.length != 4)
            throw new IllegalArgumentException("Material diffuse parameter must be a vector with 4 components.");
        if(specular.length != 4)
            throw new IllegalArgumentException("Material specular parameter must be a vector with 4 components.");

        this.ambient   = ambient;
        this.diffuse   = diffuse;
        this.specular  = specular;
        this.shininess = shininess;
        this.name      = name;
        this.dissolve  = dissolve;
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

    public float getShininess() {
        return shininess;
    }

    public void setShininess(float shininess) {
        this.shininess = shininess;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAmbient(float[] ambient) {
        this.ambient = ambient;
    }

    public void setDiffuse(float[] diffuse) {
        this.diffuse = diffuse;
    }

    public void setSpecular(float[] specular) {
        this.specular = specular;
    }

    public float getDissolve() {
        return dissolve;
    }

    public void setDissolve(float dissolve) {
        this.dissolve = dissolve;
    }

    @Override
    public void render() {

        glMaterialfv(GL_FRONT, GL_AMBIENT, ambient);
        glMaterialfv(GL_FRONT, GL_DIFFUSE, diffuse);
        glMaterialfv(GL_FRONT, GL_SPECULAR, specular);
        glMaterialf(GL_FRONT, GL_SHININESS, shininess);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);

        super.render();

        glDisable(GL_BLEND);
    }
}
