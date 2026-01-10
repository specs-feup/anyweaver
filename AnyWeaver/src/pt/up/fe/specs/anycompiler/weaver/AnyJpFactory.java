package pt.up.fe.specs.anycompiler.weaver;

import pt.up.fe.specs.anycompiler.ast.GenericAnyNode;
import pt.up.fe.specs.anycompiler.weaver.abstracts.joinpoints.AAny;

public class AnyJpFactory {

    /**
     * Creates a new, empty Any join point.
     *
     * @param kind
     * @return
     */
    public static AAny anyNode(AnyWeaver weaver, String kind) {
        var node = new GenericAnyNode(kind);
        return AnyJoinpoints.create(node, weaver, AAny.class);
    }

}
