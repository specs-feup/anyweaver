/**
 * Copyright 2016 SPeCS.
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

package pt.up.fe.specs.anycompiler.weaver;

import pt.up.fe.specs.anycompiler.ast.AnyNode;
import pt.up.fe.specs.anycompiler.ast.GenericAnyNode;
import pt.up.fe.specs.anycompiler.weaver.abstracts.AAnyWeaverJoinPoint;
import pt.up.fe.specs.anycompiler.weaver.abstracts.joinpoints.AJoinPoint;
import pt.up.fe.specs.anycompiler.weaver.joinpoints.AnyJp;
import pt.up.fe.specs.anycompiler.weaver.joinpoints.AppJp;
import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.classmap.FunctionClassMap;

public class AnyJoinpoints {

    private static final FunctionClassMap<AnyNode, AAnyWeaverJoinPoint> JOINPOINT_FACTORY;
    static {
        JOINPOINT_FACTORY = new FunctionClassMap<>();
        JOINPOINT_FACTORY.put(GenericAnyNode.class, AnyJoinpoints::jpBuilder);
        // JOINPOINT_FACTORY.put(Placeholder.class, node -> new PlaceholderJp(node));
    }

    private static AAnyWeaverJoinPoint jpBuilder(AnyNode node) {

        if (node.getKind().equals("app")) {
            return new AppJp(node);
        }

        // Default
        return new AnyJp(node);
    }

    public static AAnyWeaverJoinPoint createFromLara(Object node) {
        if (!(node instanceof AnyNode)) {
            throw new RuntimeException(
                    "Expected input to be a AnyNode, is " + node.getClass().getSimpleName() + ": " + node);
        }

        return create((AnyNode) node);
    }

    public static AAnyWeaverJoinPoint create(AnyNode node) {
        if (node == null) {
            SpecsLogs.debug("CxxJoinpoints: tried to create join point from null node, returning undefined");
            return null;
        }

        return JOINPOINT_FACTORY.apply(node);
    }

    public static <T extends AJoinPoint> T create(AnyNode node, Class<T> targetClass) {
        if (targetClass == null) {
            throw new RuntimeException("Check if you meant to call 'create' with a single argument");
        }

        return targetClass.cast(create(node));
    }

}
