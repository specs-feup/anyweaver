/**
 * Copyright 2024 SPeCS.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.anycompiler.ast;

import java.util.Collection;

import pt.up.fe.specs.util.treenode.ATreeNode;

public abstract class AnyNode extends ATreeNode<AnyNode> {

    public AnyNode(Collection<? extends AnyNode> children) {
        super(children);
    }

    public abstract String getKind();

    public abstract Object getValue(String attribute);

    public abstract Object putValue(String attribute, Object value);

    public abstract Collection<String> getAttributes();

}
