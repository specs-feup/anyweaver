package pt.up.fe.specs.anycompiler.weaver.abstracts;

import org.lara.interpreter.weaver.interf.JoinPoint;
import org.lara.interpreter.weaver.interf.SelectOp;
import pt.up.fe.specs.anycompiler.weaver.AnyJoinpoints;
import pt.up.fe.specs.anycompiler.weaver.abstracts.joinpoints.AJoinPoint;

import java.util.List;
import java.util.stream.Stream;

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
        return getNode().getChildrenStream().map(c -> AnyJoinpoints.create(c, joinPointClass)).toList();
    }

    @Override
    public String getAstImpl() {
        return getNode().toTree();
    }

    @Override
    public Object getValueImpl(String name) {
        return getNode().getObject(name);
    }

    @Override
    public Object setValueImpl(String name, Object value) {
        return getNode().putObject(name, value);
    }

    @Override
    public String getJoinPointType() {
        return getNode().getKind();
    }

    @Override
    public AJoinPoint[] getChildrenArrayImpl() {
        return getNode().getChildrenStream().map(n -> (AJoinPoint) AnyJoinpoints.create(n))
                .toArray(size -> new AJoinPoint[size]);
    }

    @Override
    public AJoinPoint[] getDescendantsArrayImpl() {
        return getNode().getDescendantsStream().map(n -> (AJoinPoint) AnyJoinpoints.create(n))
                .toArray(size -> new AJoinPoint[size]);
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

    @Override
    public AJoinPoint getParentImpl() {
        return AnyJoinpoints.create(getNode().getParent());
    }

}
