/**
 * Copyright 2021 SPeCS.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package pt.up.fe.specs.anycompiler.ast.visit;

import pt.up.fe.specs.anycompiler.ast.AnyNode;

import java.util.function.BiFunction;

/**
 * Represents all visitors of AnyNode instances.
 *
 * @param <D>
 * @param <R>
 * @author JBispo
 */
public interface Visitor<D, R> {

    R visit(AnyNode node, D data);

    default R visit(AnyNode node) {
        return visit(node, null);
    }

    /**
     * Adds a visit for the node with the given kind name.
     *
     * @param kind
     * @param method
     */
    void addVisit(String kind, BiFunction<AnyNode, D, R> method);

    /**
     * Overload that accepts any object as kind, and calls .toString() to determine the kind of node.
     *
     * @param kind
     * @param method
     */
    default void addVisit(Object kind, BiFunction<AnyNode, D, R> method) {
        addVisit(kind.toString(), method);
    }

    void setDefaultVisit(BiFunction<AnyNode, D, R> method);
}
