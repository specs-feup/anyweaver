package pt.up.fe.specs.anycompiler.ast.visit;

import pt.up.fe.specs.anycompiler.ast.AnyNode;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

/**
 * @param <D>
 * @param <R>
 * @author Joao Bispo
 */
public abstract class AVisitor<D, R> implements Visitor<D, R> {

    private final Map<String, BiFunction<AnyNode, D, R>> visitMap;
    private BiFunction<AnyNode, D, R> defaultVisit;

    public AVisitor() {
        this.visitMap = new HashMap<>();

        // Initialize visitors
        buildVisitor();
    }

    protected abstract void buildVisitor();

    @Override
    public void addVisit(String kind, BiFunction<AnyNode, D, R> method) {
        this.visitMap.put(kind, method);
    }

    @Override
    public void setDefaultVisit(BiFunction<AnyNode, D, R> defaultVisit) {
        this.defaultVisit = defaultVisit;
    }

    /**
     * @param kind
     * @return the visit method to use, or default if no visit method was found
     */
    protected BiFunction<AnyNode, D, R> getVisit(AnyNode node) {

        // Iterate over node hierarchy, in order, until a visitor is found
        for (var kind : node.getHierarchy()) {
            var visitMethod = visitMap.get(kind);

            if (visitMethod != null) {
                return visitMethod;
            }
        }

        Objects.requireNonNull(defaultVisit,
                () -> "Could not find a suitable visit method for node of kind " + node.getKind()
                        + ", and no default visitor is set");

        return defaultVisit;
    }

    @Override
    public R visit(AnyNode node, D data) {
        Objects.requireNonNull(node, () -> "Node should not be null");

        return getVisit(node).apply(node, data);
    }

    protected R visitAllChildren(AnyNode node, D data) {
        for (var child : node.getChildren()) {
            visit(child, data);
        }

        return null;

    }
}