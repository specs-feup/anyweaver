/**
 * Copyright 2016 SPeCS.
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

package pt.up.fe.specs.anycompiler.weaver.tests;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import pt.up.fe.specs.anycompiler.weaver.AnyWeaverTester;
import pt.up.fe.specs.util.SpecsSystem;

public class AnyWeaverTest {

    @BeforeClass
    public static void setupOnce() {
        SpecsSystem.programStandardInit();
        AnyWeaverTester.clean();
    }

    @After
    public void tearDown() {
        AnyWeaverTester.clean();
    }

    private static AnyWeaverTester newTester() {
        return new AnyWeaverTester("anyweaver/test/weaver/")
                .setSrcPackage("src/")
                .setResultPackage("results/");
    }

    @Test
    public void testBasic() {
        newTester().test("Srl.js", "srl-ast.json");
    }


}
