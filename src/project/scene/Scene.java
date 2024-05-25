package project.scene;

import project.utils.types.Disposable;
import project.nodes.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Tato abstraktní třída reprezentuje scénu.
 */

public abstract class Scene implements Disposable {

    public Scene() {
        initScene();
    }

    protected abstract void initScene();

    private final List<Node> nodes = new ArrayList<>();

    /**
     *Metoda pro přidání uzlu do nodes
     * @param node
     */

    public void addNode(Node node){
        nodes.add(node);
    }

    /**
     * Metoda aktualizující scénu.
     * @param deltaTime což je rozdíl času od posledního vykreslení.
     */

    public abstract void update(float deltaTime);

    /**
     * Metoda vykreslující scénu.
     */

    public void draw() {
        for (Node node : nodes) {
            node.render();
        }
    }

    /**
     * Metoda, která uvolní alokované zdroje.
     */

    @Override
    public void dispose() {
        for (Node node : nodes){
            node.dispose();
        }
    }
}
