package project.scene.model;

import project.nodes.Node;
import project.nodes.groups.transform.TransformNode;
import project.nodes.leaf.Geometry3D;
import project.nodes.groups.NodeGroup;
import project.utils.loaders.ModelLoader;

import java.io.IOException;

import project.nodes.groups.textures.Texture;


/**
 * Třída reprezentující síť modelu letadla.
 */

public class PlaneModel extends NodeGroup {

    private Node propeller, planeBody;
    private TransformNode propellerTransform;
    private Texture planeBodyTexture;

    public static boolean isAccelerating = false;
    public static boolean isDecelerating = false;

    public static float elapsedTime = 0; // celkový čas pro animaci
    private float angularVelocity = 0; // aktuální úhlová rychlost

    private final float [] propellerPivot = {-0.00176071f, 0.03878575f, 0};


    /**
     * Konstruktor načtení modelu
     */

    public PlaneModel() {
        try {
            setNodes(ModelLoader.loadModel("/obj/plane.obj")); //načtení modelu

            for (Node part : getNodes()) { //načtení vrtule
                if (part.any(node -> node instanceof Geometry3D n && n.getName().equals("propeller"))) {
                    propeller = part;
                    break;
                }
            }

            for (Node part1 : getNodes()) { //načtení trupu letadla
                if (part1.any(node -> node instanceof Geometry3D n && n.getName().equals("planeBody"))) {
                    planeBody = part1;
                    break;
                }
            }

            //přidání textury pro hlavní část letadla a uložení do nodes
            planeBodyTexture = new Texture("textures/army.jpg");
            planeBodyTexture.add(planeBody);
            remove(planeBody);
            add(planeBodyTexture);

            //přidání transformační matice pro vrtuli a uložení do nodes
            propellerTransform = new TransformNode();
            propellerTransform.add(propeller);
            remove(propeller);
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
        propellerTransform.translate(-propellerPivot[0], -propellerPivot[1], -propellerPivot[2]);
        propellerTransform.rotateZ(angularVelocity * deltaTimeInSeconds);// Aplikace rotace na základě aktuální úhlové rychlosti
        propellerTransform.translate(propellerPivot[0], propellerPivot[1], propellerPivot[2]);
    }
}
