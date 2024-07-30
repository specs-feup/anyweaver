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
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import pt.up.fe.specs.anycompiler.ast.AnyNode;
import pt.up.fe.specs.anycompiler.ast.GenericAnyNode;
import pt.up.fe.specs.util.SpecsCheck;

import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

public class AntlrToAnyNodeConverter {

    private final Map<ParseTree, AnyNode> antlrToAny;

    public AntlrToAnyNodeConverter() {
        this.antlrToAny = new HashMap<>();
    }

    public static AnyNode convert(ParseTree antlrNode, Parser parser) {
        var converter = new AntlrToAnyNodeConverter();
        var node = converter.convertPrivate(antlrNode, parser);

        // Now that all nodes have been converted, replace attributes that are ANTLR nodes with the equivalent JmmNode
        converter.replaceAntlrNodeAttrs(node, parser);

        return node;
    }

    private void replaceAntlrNodeAttrs(AnyNode root, Parser parser) {
        new AntlrNodeAttrReplacer(antlrToAny, parser).visit(root);
    }

    private AnyNode convertPrivate(ParseTree antlrNode, Parser parser) {
        // Get kind
        var kind = getKind(antlrNode, parser);

        var node = new GenericAnyNode(kind);

        // Add to map
        antlrToAny.put(antlrNode, node);

        // Get hierarchy
        addHierarchy(node, antlrNode, parser);

        // Get attributes
        addAttributes(node, antlrNode, parser);

        // Get children
        addChildren(node, antlrNode, parser);

        return node;
    }

    @SuppressWarnings("unchecked")
    private void addHierarchy(AnyNode jmmNode, ParseTree node, Parser parser) {
        // If terminal node, it has no hierarchy
        if (node instanceof TerminalNode) {
            return;
        }

        // If terminal node, it has no hierarchy
        if (!(node instanceof ParserRuleContext)) {
            System.out.println("Don't know how to handle nodes of this class: " + node.getClass());
            return;
        }

        // Get hierarchy
        var classes = getNodeClasses(node);

        var hierarchy = classes.stream()
                .map(aClass -> getKind((Class<? extends ParserRuleContext>) aClass))
                .collect(Collectors.toList());

        jmmNode.setHierarchy(hierarchy);
    }

    private void addChildren(AnyNode anyNode, ParseTree antlrNode, Parser parser) {

        for (int i = 0; i < antlrNode.getChildCount(); i++) {
            var child = antlrNode.getChild(i);

            // Ignore terminal nodes that do not have a symbolic name
            if (child instanceof TerminalNode) {
                continue;
                /*
                var token = ((TerminalNode) child).getSymbol();
                if (parser.getVocabulary().getSymbolicName(token.getType()) == null) {
                    continue;
                }
                */
            }

            anyNode.addChild(convertPrivate(child, parser));
        }
    }

    private void addAttributes(AnyNode anyNode, ParseTree antlrNode, Parser parser) {

        // System.out.println("S NAME: " + parser.getSourceName());
        // Add line and column
        var startPosition = parser.getTokenStream().get(antlrNode.getSourceInterval().a);
        var endPosition = parser.getTokenStream().get(antlrNode.getSourceInterval().b);

        anyNode.putObject(NodePosition.LINE_START.getKey(), Integer.toString(startPosition.getLine()));
        anyNode.putObject(NodePosition.COL_START.getKey(), Integer.toString(startPosition.getCharPositionInLine()));

        anyNode.putObject(NodePosition.LINE_END.getKey(), Integer.toString(endPosition.getLine()));
        anyNode.putObject(NodePosition.COL_END.getKey(), Integer.toString(endPosition.getCharPositionInLine()));

        if (antlrNode instanceof TerminalNode) {
            var token = ((TerminalNode) antlrNode).getSymbol();
            anyNode.putObject("value", token.getText());
            return;
        }

        SpecsCheck.checkArgument(antlrNode instanceof ParserRuleContext,
                () -> "Expected node '" + antlrNode.getClass() + "' to be an instance of " + ParserRuleContext.class);

        // Get all classes up to ParserRuleContext
        var nodeClasses = getNodeClasses(antlrNode);

        var fields = nodeClasses.stream()
                // Get declaring fields of node classes
                .flatMap(nodeClass -> Arrays.asList(nodeClass.getDeclaredFields()).stream())
                // Only those that are public
                .filter(field -> Modifier.isPublic(field.getModifiers()))
                .collect(Collectors.toList());

        for (var field : fields) {

            var name = field.getName();

            try {
                // for (var field : node.getClass().getFields()) {
                if (!field.getType().isAssignableFrom(Token.class)) {
                    var value = processValue(field.get(antlrNode));
                    anyNode.putObject(name, value);
                    continue;
                }

                var token = (Token) field.get(antlrNode);

                // If no token for the given field, skip
                if (token == null) {
                    continue;
                }

                var literalValue = token.getText();

                SpecsCheck.checkNotNull(literalValue, () -> "Could not extract value from token");

                anyNode.putObject(name, literalValue);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Could not access field '" + name + "' from node " + antlrNode);
            }
        }

    }

    private Object processValue(Object value) {
        // If Token, convert to String
        if (value instanceof Token) {
            return ((Token) value).getText();
        }

        // If List, convert elements
        if (value instanceof List) {
            return ((List<?>) value).stream()
                    .map(element -> processValue(element))
                    .collect(Collectors.toList());
        }

        // Return as-is
        return value;
    }

    private List<Class<?>> getNodeClasses(ParseTree node) {
        var nodeClasses = new ArrayList<Class<?>>();
        Class<?> currentNodeClass = node.getClass();
        while (!currentNodeClass.equals(ParserRuleContext.class)) {
            nodeClasses.add(currentNodeClass);
            currentNodeClass = currentNodeClass.getSuperclass();
        }
        return nodeClasses;
    }

    private String getKind(ParseTree node, Parser parser) {
        // Tokens are terminal nodes
        if (node instanceof TerminalNode) {
            var token = ((TerminalNode) node).getSymbol();
            return parser.getVocabulary().getSymbolicName(token.getType());
        }

        if (!(node instanceof ParserRuleContext)) {
            throw new RuntimeException("Expected node to be of class '" + ParserRuleContext.class
                    + "', but got '" + node.getClass() + "'");
        }

        return getKind(((ParserRuleContext) node).getClass());
    }

    private String getKind(Class<? extends ParserRuleContext> nodeClass) {

        String className = nodeClass.getSimpleName();

        // Rules end with context
        if (!className.endsWith("Context")) {
            throw new RuntimeException("Expected classname to end with 'Context' " + nodeClass.getSimpleName());
        }

        return className.substring(0, className.length() - "Context".length());
    }

}
