package pt.up.fe.specs.anycompiler.weaver.abstracts;

import java.util.List;
import java.util.stream.Stream;

import org.lara.interpreter.weaver.interf.JoinPoint;
import org.lara.interpreter.weaver.interf.SelectOp;

import pt.up.fe.specs.anycompiler.weaver.AnyJoinpoints;
import pt.up.fe.specs.anycompiler.weaver.abstracts.joinpoints.AJoinPoint;

/**
 * Abstract class which can be edited by the developer. This class will not be overwritten.
 * 
 * @author Lara Weaver Generator
 */
public abstract class AAnyWeaverJoinPoint extends AJoinPoint {

    /**
     * Compares the two join points based on their node reference of the used compiler/parsing tool.<br>
     * This is the default implementation for comparing two join points. <br>
     * <b>Note for developers:</b> A weaver may override this implementation in the editable abstract join point, so the
     * changes are made for all join points, or override this method in specific join points.
     */
    @Override
    public boolean compareNodes(AJoinPoint aJoinPoint) {
        return this.getNode().equals(aJoinPoint.getNode());
    }

    /**
     * Generic select function, used by the default select implementations.
     */
    @Override
    public <T extends AJoinPoint> List<? extends T> select(Class<T> joinPointClass, SelectOp op) {
        throw new RuntimeException(
                "Generic select function not implemented yet. Implement it in order to use the default implementations of select");
    }

    @Override
    public String getAstImpl() {
        return getNode().toTree();
    }

    @Override
    public Object getValueImpl(String name) {
        return getNode().getValue(name);
    }

    @Override
    public String getJoinPointType() {
        return getNode().getKind();
    }

    /*
    @Override
    public List<JoinPoint> getJpChildren() {
        return getNode().getChildren().stream().map(n -> (JoinPoint) AnyJoinpoints.create(n)).toList();
    }
    */

    @Override
    public Stream<JoinPoint> getJpChildrenStream() {
        return getNode().getChildrenStream().map(c -> (JoinPoint) AnyJoinpoints.create(c));
    }

    /**
     * Implement this method and getJpChildrenStream() in order to obtain tree-like functionality (descendants, etc).
     * 
     * @return
     */
    @Override
    public JoinPoint getJpParent() {
        return AnyJoinpoints.create(getNode().getParent());
    }

}
