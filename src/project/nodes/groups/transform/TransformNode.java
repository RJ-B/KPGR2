package project.nodes.groups.transform;

import project.nodes.groups.NodeGroup;
import transforms.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * Reprezentuje vrchol (uzel) pro transformaci části grafu scény. Transformace je aplikována na všechny uzly v podstromu daného uzlu.
 */

public class TransformNode extends NodeGroup {

    Mat4 matrix = new Mat4Identity();

    @Override
    public void render() {
        glMatrixMode(GL_MODELVIEW);

        glPushMatrix();
        glMultMatrixf(matrix.floatArray());

        super.render();

        glMatrixMode(GL_MODELVIEW);
        glPopMatrix();
    }


    public void scale(float val){
        Mat4Scale scale = new Mat4Scale(val);
        matrix = matrix.mul(scale);
    }

    public void rotateX(float angle){
        Mat4RotX rotateX = new Mat4RotX(angle);
        matrix = matrix.mul(rotateX);
    }

    public void rotateY(float angle){
        Mat4RotY rotateY = new Mat4RotY(angle);
        matrix = matrix.mul(rotateY);
    }

    public void rotateZ(float angle) {
        Mat4RotZ rotateZ = new Mat4RotZ(angle);
        matrix = matrix.mul(rotateZ);
    }

    public void translate(float x, float y, float z) {
        Mat4Transl transl = new Mat4Transl(x, y, z);
        matrix = matrix.mul(transl);
    }
}
