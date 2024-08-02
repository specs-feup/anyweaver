package pt.up.fe.specs.anycompiler.parsers.antlr;

import pt.up.fe.specs.anycompiler.ast.AnyNode;
import pt.up.fe.specs.anycompiler.ast.visit.PostorderVisitor;

import java.util.List;


public class NodeCleanup extends PostorderVisitor<Void, Void> {
    private final List<String> ignoreList;

    public NodeCleanup(List<String> ignoreList) {
        this.ignoreList = ignoreList;
    }

    @Override
    public void buildVisitor() {
        setDefaultVisit((node, dummy) -> this.defaultVisit(node, dummy));
    }

    private Void defaultVisit(AnyNode node, Void dummy) {

        if (!this.ignoreList.contains(node.getKind())) {
            return null;
        }

        var parent = node.getParent();
        if (parent == null) {
            System.err.println(
                    "Warning: the root node cannot be used in the ingnore list. The AST will keep this node.");
            return null;
        }

        var children = node.getChildren();
        var position = parent.getChildren().indexOf(node);
        for (var child : children) {
            parent.addChild(position++, child);
        }
        node.detach();
        return null;
    }

}
