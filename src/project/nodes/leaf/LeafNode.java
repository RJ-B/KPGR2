package project.nodes.leaf;

import project.nodes.Node;

import java.util.function.Predicate;

/**
 * Reprezentuje vrchol (uzel) scény, který nemůže obsahovat více vrcholů uzlů
 */

public abstract class LeafNode extends Node {

    @Override
    public boolean add(Node node) {
        return false;
    }

    @Override
    public boolean remove(Node node) {
        return false;
    }

    @Override
    public boolean any(Predicate<Node> predicate) {
        return predicate.test(this);
    }
}