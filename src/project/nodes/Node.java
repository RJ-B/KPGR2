package project.nodes;

import project.utils.types.Disposable;

import java.util.function.Predicate;

public abstract class Node implements Disposable {

    public abstract boolean add(Node node);

    public abstract boolean remove(Node node);

    public abstract void render();

    public abstract boolean any(Predicate<Node> predicate);
}
