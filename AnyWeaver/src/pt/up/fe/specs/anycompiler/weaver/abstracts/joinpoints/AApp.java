package pt.up.fe.specs.anycompiler.weaver.abstracts.joinpoints;

import org.lara.interpreter.weaver.interf.events.Stage;
import java.util.Optional;
import org.lara.interpreter.exception.ActionException;
import pt.up.fe.specs.anycompiler.weaver.abstracts.AAnyWeaverJoinPoint;
import java.util.List;
import org.lara.interpreter.weaver.interf.JoinPoint;
import java.util.stream.Collectors;
import java.util.Arrays;

/**
 * Auto-Generated class for join point AApp
 * This class is overwritten by the Weaver Generator.
 * 
 * Top-level node
 * @author Lara Weaver Generator
 */
public abstract class AApp extends AAnyWeaverJoinPoint {

    /**
     * Adds an AST to the current program, returns the inserted join point
     * @param ast 
     */
    public AJoinPoint addAstImpl(AJoinPoint ast) {
        throw new UnsupportedOperationException(get_class()+": Action addAst not implemented ");
    }

    /**
     * Adds an AST to the current program, returns the inserted join point
     * @param ast 
     */
    public final AJoinPoint addAst(AJoinPoint ast) {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAction(Stage.BEGIN, "addAst", this, Optional.empty(), ast);
        	}
        	AJoinPoint result = this.addAstImpl(ast);
        	if(hasListeners()) {
        		eventTrigger().triggerAction(Stage.END, "addAst", this, Optional.ofNullable(result), ast);
        	}
        	return result;
        } catch(Exception e) {
        	throw new ActionException(get_class(), "addAst", e);
        }
    }

    /**
     * 
     */
    @Override
    public final List<? extends JoinPoint> select(String selectName) {
        List<? extends JoinPoint> joinPointList;
        switch(selectName) {
        	default:
        		joinPointList = super.select(selectName);
        		break;
        }
        return joinPointList;
    }

    /**
     * 
     */
    @Override
    public final void defImpl(String attribute, Object value) {
        switch(attribute){
        default: throw new UnsupportedOperationException("Join point "+get_class()+": attribute '"+attribute+"' cannot be defined");
        }
    }

    /**
     * 
     */
    @Override
    protected final void fillWithAttributes(List<String> attributes) {
        super.fillWithAttributes(attributes);
    }

    /**
     * 
     */
    @Override
    protected final void fillWithSelects(List<String> selects) {
        super.fillWithSelects(selects);
    }

    /**
     * 
     */
    @Override
    protected final void fillWithActions(List<String> actions) {
        super.fillWithActions(actions);
        actions.add("joinpoint addAst(joinpoint)");
    }

    /**
     * Returns the join point type of this class
     * @return The join point type
     */
    @Override
    public final String get_class() {
        return "app";
    }
    /**
     * 
     */
    protected enum AppAttributes {
        GETVALUE("getValue"),
        AST("ast"),
        CHILDREN("children"),
        DESCENDANTS("descendants");
        private String name;

        /**
         * 
         */
        private AppAttributes(String name){
            this.name = name;
        }
        /**
         * Return an attribute enumeration item from a given attribute name
         */
        public static Optional<AppAttributes> fromString(String name) {
            return Arrays.asList(values()).stream().filter(attr -> attr.name.equals(name)).findAny();
        }

        /**
         * Return a list of attributes in String format
         */
        public static List<String> getNames() {
            return Arrays.asList(values()).stream().map(AppAttributes::name).collect(Collectors.toList());
        }

        /**
         * True if the enum contains the given attribute name, false otherwise.
         */
        public static boolean contains(String name) {
            return fromString(name).isPresent();
        }
    }
}
