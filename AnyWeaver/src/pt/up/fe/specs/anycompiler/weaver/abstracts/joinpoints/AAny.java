package pt.up.fe.specs.anycompiler.weaver.abstracts.joinpoints;

import pt.up.fe.specs.anycompiler.weaver.abstracts.AAnyWeaverJoinPoint;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Arrays;

/**
 * Auto-Generated class for join point AAny
 * This class is overwritten by the Weaver Generator.
 * 
 * Any node
 * @author Lara Weaver Generator
 */
public abstract class AAny extends AAnyWeaverJoinPoint {

    /**
     * Returns the join point type of this class
     * @return The join point type
     */
    @Override
    public final String get_class() {
        return "any";
    }
    /**
     * 
     */
    protected enum AnyAttributes {
        AST("ast"),
        CHILDREN("children"),
        DESCENDANTS("descendants"),
        GETVALUE("getValue"),
        PARENT("parent");
        private String name;

        /**
         * 
         */
        private AnyAttributes(String name){
            this.name = name;
        }
        /**
         * Return an attribute enumeration item from a given attribute name
         */
        public static Optional<AnyAttributes> fromString(String name) {
            return Arrays.asList(values()).stream().filter(attr -> attr.name.equals(name)).findAny();
        }

        /**
         * Return a list of attributes in String format
         */
        public static List<String> getNames() {
            return Arrays.asList(values()).stream().map(AnyAttributes::name).collect(Collectors.toList());
        }

        /**
         * True if the enum contains the given attribute name, false otherwise.
         */
        public static boolean contains(String name) {
            return fromString(name).isPresent();
        }
    }
}
