/**
 * Copyright 2023 SPeCS.
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

package pt.up.fe.specs.anycompiler.parsers.antlr;

import pt.up.fe.specs.util.providers.StringProvider;

public enum NodePosition implements StringProvider {

    FILE("file"),
    LINE_START("lineStart"),
    LINE_END("lineEnd"),
    COL_START("colStart"),
    COL_END("colEnd");

    private final String string;

    private NodePosition(String string) {
        this.string = string;
    }

    @Override
    public String getString() {
        return string;
    }

}
