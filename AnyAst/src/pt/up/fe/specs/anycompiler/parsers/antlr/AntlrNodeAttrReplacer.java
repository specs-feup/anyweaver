/**
 * Copyright 2023 SPeCS.
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

package pt.up.fe.specs.anycompiler.parsers.antlr;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.ParseTree;
import pt.up.fe.specs.anycompiler.ast.AnyNode;
import pt.up.fe.specs.anycompiler.ast.visit.PreorderJmmVisitor;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AntlrNodeAttrReplacer extends PreorderJmmVisitor<Void, Void> {

    private final Map<ParseTree, AnyNode> antlrToJmm;
    private final Set<String> ignoreList;

    public AntlrNodeAttrReplacer(Map<ParseTree, AnyNode> antlrToJmm, Parser parser) {
        this.antlrToJmm = antlrToJmm;
        this.ignoreList = new HashSet<>(AntlrParser.getIgnoreList(parser));
    }

    @Override
    protected void buildVisitor() {
        setDefaultVisit(this::replaceAntlrNode);
    }

    private Void replaceAntlrNode(AnyNode node, Void dummy) {

        // Iterate over attributes, looking for ParseTree instances
        for (var attr : node.getAttributes()) {
            var value = node.getObject(attr);

            if (!(value instanceof ParseTree)) {
                continue;
            }

            var jmmNode = antlrToJmm.get(value);

            if (jmmNode == null) {
                System.out.println("Could not find JmmNode for ANTLR node " + value);
                continue;
            }

            jmmNode = processJmmNode(jmmNode);

            node.putObject(attr, jmmNode);

        }

        return null;
    }

    private AnyNode processJmmNode(AnyNode jmmNode) {
        // If node is in ignore list, use parent instead
        while (ignoreList.contains(jmmNode.getKind())) {

            if (jmmNode.getNumChildren() != 1) {
                System.out.println(
                        "Using as attribute, node in ignore list of kind '" + jmmNode.getKind() + "' which has '"
                                + jmmNode.getNumChildren() + "' children instead of 1, no changes made.");
                return jmmNode;
            }

            var newJmmNode = jmmNode.getChild(0);

            jmmNode = newJmmNode;
        }

        return jmmNode;
    }

}
