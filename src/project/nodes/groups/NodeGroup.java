package project.nodes.groups;

import project.nodes.Node;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Reprezentuje vrchol (uzel) grafu scény, který může obsahovat více vrcholů (uzlů)
 */

public abstract class NodeGroup extends Node {

    protected List<Node> nodes;

    public NodeGroup() {
        nodes = new ArrayList<>();
    }

    public boolean add(Node node){
        return nodes.add(node);
    }

    public boolean remove(Node node) {
        return nodes.remove(node);
    }

    public List<Node> getNodes() {
        return nodes;
    }

    protected void setNodes(List<Node> nodes){
        this.nodes = nodes;
    }

    @Override
    public void render() {
        for (Node node : nodes) {
            node.render();
        }
    }

    public int size() {
        return nodes.size();
    }

    @Override
    public boolean any(Predicate<Node> predicate) {
        if (predicate.test(this)) return true;

        for (Node node : nodes) {
            if (node.any(predicate)) return true;
        }

        return false;
    }

    @Override
    public void dispose() {
        for (Node node : nodes) {
            node.dispose();
        }
    }
}
