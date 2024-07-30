/**
 * Copyright 2024 SPeCS.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.anycompiler.ast;

import pt.up.fe.specs.util.collections.Attributes;
import pt.up.fe.specs.util.treenode.ATreeNode;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

public abstract class AnyNode extends ATreeNode<AnyNode> implements Attributes {

    public AnyNode(Collection<? extends AnyNode> children) {
        super(children);
    }

    /**
     * @return the kind of this node (e.g. MethodDeclaration, ClassDeclaration, etc.)
     */
    public abstract String getKind();

    /**
     * @return a ordered collection of Strings with the hierarchy of the node, starting from the most specific class
     * (e.g. a node that is an expr and a binaryExpr will return ['binaryExpr', 'expr']
     */
    public Collection<String> getHierarchy() {
        return Arrays.asList(getKind());
    }

    public abstract void setHierarchy(Collection<String> hierarchy);

    /**
     * @param attribute
     * @returns the value of an attribute. To see all the attributes iterate the list provided by
     * {@link AnyNode#getAttributes()}
     * @deprecated use getObject() instead
     */
    public Object getValue(String attribute) {
        return getObject(attribute);
    }

    /**
     * Sets the value of an attribute.
     *
     * @param attribute
     * @param value
     * @deprecated use putObject instead
     */
    public Object putValue(String attribute, Object value) {
        return putObject(attribute, value);
    }

//    public abstract Collection<String> getAttributes();

    /**
     * This method receives a string, getAncestor methods that receive a class will not check the kind of the AnyNode.
     *
     * @param kind
     * @return the first ancestor of the given kind, or Optional.empty() if no ancestor of that kind was found
     */
    public Optional<AnyNode> getAncestor(String kind) {
        var currentParent = getParent();
        while (currentParent != null) {

            if (currentParent.isInstance(kind)) {
                return Optional.of(currentParent);
            }

            currentParent = currentParent.getParent();
        }

        return Optional.empty();
    }

    /**
     * @param kind
     * @return true if the node is an instance of the given kind
     */
    public boolean isInstance(String kind) {
        return getHierarchy().contains(kind);
    }

    /**
     * Helper method which calls .toString() over the argument 'kind'.
     *
     * @param kind
     * @return true if the node is an instance of the given kind
     */
    public boolean isInstance(Object kind) {
        return isInstance(kind.toString());
    }
}
