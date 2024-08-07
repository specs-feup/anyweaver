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

package pt.up.fe.specs.anycompiler;


import org.junit.Assert;
import org.junit.Test;
import pt.up.fe.specs.anycompiler.parsers.JsonParser;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsStrings;

public class JsonParserTest {

    @Test
    public void testSrlAst() {
        var code = SpecsIo.getResource("pt/up/fe/specs/anycompiler/srl-ast.json");
        var parser = new JsonParser("type", "children", true);
        var ast = parser.parse(code);

        Assert.assertTrue(SpecsStrings.check(SpecsIo.getResource("pt/up/fe/specs/anycompiler/srl-ast.json.txt"), ast.toTree()));
    }
}
