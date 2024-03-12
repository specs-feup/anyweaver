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

package pt.up.fe.specs.anycompiler.weaver.joinpoints;

import pt.up.fe.specs.anycompiler.ast.AnyNode;
import pt.up.fe.specs.anycompiler.weaver.AnyJoinpoints;
import pt.up.fe.specs.anycompiler.weaver.abstracts.joinpoints.AApp;
import pt.up.fe.specs.anycompiler.weaver.abstracts.joinpoints.AJoinPoint;

public class AppJp extends AApp {

    private final AnyNode app;

    public AppJp(AnyNode app) {
        this.app = app;
    }

    @Override
    public AnyNode getNode() {
        return app;
    }

    @Override
    public AJoinPoint addAstImpl(AJoinPoint ast) {
        return AnyJoinpoints.create(app.addChild(ast.getNode()));
    }

}
