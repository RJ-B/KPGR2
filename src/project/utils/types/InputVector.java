package project.utils.types;

/**
 * třída pro vektory pro pohyb kamerou
 *
 * @author Rostislav Jirák
 * @version 2024
 */

public class InputVector {
    private float forward;
    private float strafe;
    private float vertical;

    public int keyFp = 0, keyFn = 0, keySp = 0, keySn = 0, keyVp = 0, keyVn = 0;

    public InputVector() {
        this.forward = 0;
        this.strafe = 0;
        this.vertical = 0;
    }

    public float getForward() {
        return forward;
    }

    public float getStrafe() {
        return strafe;
    }

    public float getVertical() {
        return vertical;
    }

    public void update(long deltaTime) {
        double a = Math.atan2(keyFp + keyFn, keySp + keySn);
        this.forward = (keyFp + keyFn == 0) ? 0 : (float) Math.sin(a) * deltaTime;
        this.strafe = (keySp + keySn == 0) ? 0 : (float) Math.cos(a) * deltaTime;
        this.vertical = (keyVp + keyVn) * deltaTime;
    }
}
