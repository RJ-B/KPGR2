package project.scene;

import project.scene.model.PlaneModel;
import project.nodes.groups.lights.DirectionalLight;
import project.nodes.groups.lights.PointLight;


/**
 * Třída reprezentující scénu pro letadlo.
 */

public class PlaneScene extends Scene {

    private DirectionalLight light1, light2;
    private PlaneModel planeModel;

    @Override
    protected void initScene() {
        //vytvoření dvou světel ve scéně
        light1 = createDirectionalLight(new float[] { 0, 1, 0 });
        light2 = createDirectionalLight(new float[] { 0, -1, 0 });

        //vytvoření modelu
        planeModel = new PlaneModel();

        //uložení světel do nodes
        light1.add(light2);
        light2.add(planeModel);

        addNode(light1);
    }

    @Override
    public void update(float deltaTime) {
        planeModel.update(deltaTime);
    }

    private DirectionalLight createDirectionalLight(float[] direction) {
        return new DirectionalLight(
                direction,
                new float[] { 0f, 0f, 0f, 1 },
                new float[] { 1f, 1f, 1f, 1 },
                new float[] { 0f, 0f, 0f, 0 }
        );
    }

    private PointLight createPointLight(float[] position) {
        return new PointLight(
                position,
                new float[] { 0f, 0f, 0f, 1 },
                new float[] { 1f, 1f, 1f, 1 },
                new float[] { 0f, 0f, 0f, 0 }
        );
    }
}
