package project.scene.model;

import lwjglutils.OGLTexture2D;
import project.nodes.Node;
import project.nodes.groups.transform.TransformNode;
import project.nodes.leaf.Geometry3D;
import project.nodes.groups.NodeGroup;
import project.utils.loaders.ModelLoader;
import project.utils.loaders.mtl.MtlLoader;

import java.io.IOException;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import project.nodes.groups.textures.Texture;


/**
 * Třída reprezentující síť modelu letadla.
 */

public class PlaneModel extends NodeGroup {

    private Node propeller;
    private Node planeBody;
    private TransformNode propellerTransform;

    public static boolean isAccelerating = false;
    public static boolean isDecelerating = false;

    public static float elapsedTime = 0; // celkový čas pro animaci
    private float angularVelocity = 0; // aktuální úhlová rychlost

    private OGLTexture2D texture;


    /**
     * Konstruktor načtení modelu
     */

    public PlaneModel() {
        try {
            setNodes(ModelLoader.loadModel("/obj/plane.obj")); //načtení modelu

            for (Node part : getNodes()) { //načtení a uložení vrtule
                if (part.any(node -> node instanceof Geometry3D n && n.getName().equals("propeller"))) {
                    propeller = part;
                    break;
                }
            }
            for (Node part1 : getNodes()) { //načtení a uložení vrtule
                if (part1.any(node -> node instanceof Geometry3D n && n.getName().equals("planeBody"))) {
                    planeBody = part1;
                    break;
                }
            }
            // Nastavení textur
            System.out.println("Loading texture...");
            try {
                texture = new OGLTexture2D("textures/army.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }

            glEnable(GL_TEXTURE_2D);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
            glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);


            glActiveTexture(GL_TEXTURE0);
            texture.bind();

            glPushMatrix();

            glPopMatrix();
            glDisable(GL_TEXTURE_2D);


            //transformace pro vrtuli
            propellerTransform = new TransformNode();
            propellerTransform.add(planeBody);
            remove(planeBody);
            add(propellerTransform);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update(float delta) {
        float deltaTimeInSeconds = delta / 1000.0f;
        float maxAngularVelocity = 3000 * (float) Math.PI / 30;// maximální úhlová rychlost (3000 RPM)
        float k = 10;// konstanta pro sigmoidní funkci
        elapsedTime += deltaTimeInSeconds;

        if (isAccelerating) {
            float accelerationTime = 5;// čas na zrychlení v sekundách
            float normalizedTime = elapsedTime / accelerationTime;
            float sigmoidFactor = (float) (1 / (1 + Math.exp(-k * (normalizedTime - 0.5))));// Sigmoidní funkce pro nelineární zrychlení
            angularVelocity = maxAngularVelocity * sigmoidFactor;// Aktualizace úhlové rychlosti - zrychlení do maximální rychlosti
        } else if(isDecelerating){
            float decelerationTime = 5;// čas na zpomalení v sekundách
            float normalizedDecelerationTime = elapsedTime / decelerationTime;
            float sigmoidFactor = (float) (1 / (1 + Math.exp(-k * (normalizedDecelerationTime - 0.5))));// Sigmoidní funkce pro nelineární zpomalení
            angularVelocity = angularVelocity * (1 - sigmoidFactor);// Aktualizace úhlové rychlosti - zpomalení z aktuální rychlosti

            //úhlová rychlost nepůjde do záporu
            if (angularVelocity < 0) {
                angularVelocity = 0;
            }
        }
        propellerTransform.rotateZ(angularVelocity * deltaTimeInSeconds);// Aplikace rotace na základě aktuální úhlové rychlosti
    }
}
