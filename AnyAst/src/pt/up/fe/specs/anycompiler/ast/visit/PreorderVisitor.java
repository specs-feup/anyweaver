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
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.anycompiler.ast.visit;

import pt.up.fe.specs.anycompiler.ast.AnyNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Visitor that automatically applies a preorder, top-down traversal (first current node, then children).
 *
 * @author JBispo
 *
 */
public abstract class PreorderVisitor<D, R> extends AllNodesVisitor<D, R> {

    @Override
    public R visit(AnyNode node, D data) {
        Objects.requireNonNull(node, () -> "Node should not be null");

        var visit = getVisit(node);

        // Preorder: 1st visit the node
        var nodeResult = visit.apply(node, data);

        // Preorder: then, visit each children
        List<R> childrenResults = new ArrayList<>();
        for (var child : node.getChildren()) {
            childrenResults.add(visit(child, data));
        }

        var reduceFunction = getReduce();

        // No reduce function, just return result of the node
        if (reduceFunction == null) {
            return nodeResult;
        }

        return reduceFunction.apply(nodeResult, childrenResults);
    }
}
