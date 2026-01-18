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
 * specific language governing permissions and limitations under the License.
 */

package pt.up.fe.specs.anycompiler.parsers;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.google.gson.Gson;

import pt.up.fe.specs.anycompiler.AnyParser;
import pt.up.fe.specs.anycompiler.ast.AnyNode;
import pt.up.fe.specs.anycompiler.ast.GenericAnyNode;
import pt.up.fe.specs.util.SpecsLogs;

public class JsonParser implements AnyParser {

    private final String kindAttr;
    private final String childrenAttr;
    private final boolean isFlat;

    private int currentId;

    public JsonParser(String kindAttr, String childrenAttr, boolean isFlat) {
        this.kindAttr = kindAttr;
        this.childrenAttr = childrenAttr;
        this.isFlat = isFlat;

        currentId = 0;
    }

    public JsonParser() {
        this("kind", "children", false);
    }

    @Override
    public AnyNode parse(String code) {

        @SuppressWarnings("unchecked")
        Map<String, Object> jsonAst = new Gson().fromJson(code, Map.class);

        if (isFlat) {
            return flatParser(jsonAst);
        }

        return hierarchicalParser(jsonAst);
    }

    private AnyNode hierarchicalParser(Map<String, Object> code) {
        // In hierarchical mode, there is a single object which is a node, and children are inside the corresponding
        // nodes

        // Reset id
        currentId = 0;

        var root = parseNodeRecursive(code);

        // Reset id again
        currentId = 0;

        return root;
    }

    private String nextId() {
        var id = "id_" + currentId;
        currentId++;
        return id;
    }

    private AnyNode flatParser(Map<String, Object> jsonAst) {
        // In flat mode, each element is a key with the id of the node, and a value representing the node
        // Create map of ids -> nodes
        var nodes = new HashMap<String, AnyNode>();
        var children = new HashMap<String, List<String>>();
        for (var key : jsonAst.keySet()) {
            @SuppressWarnings("unchecked")
            var value = (Map<String, Object>) jsonAst.get(key);

            parseNode(key, value, nodes, children);
            // System.out.println("Key: " + key);
            // System.out.println("Value: " + jsonAst.get(key));
        }

        // Set children
        for (var key : children.keySet()) {
            var childrenIds = children.get(key);

            var childrenNodes = childrenIds.stream()
                    .map(id -> nodes.get(id))
                    .toList();

            nodes.get(key).setChildren(childrenNodes);

        }

        // Get the root node
        var randomNode = nodes.values().stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Could not find any node"));

        while (randomNode.hasParent()) {
            randomNode = randomNode.getParent();
        }

        return randomNode;
    }

    private void parseNode(String id, Map<String, Object> value, HashMap<String, AnyNode> nodes,
            HashMap<String, List<String>> children) {

        var kind = value.get(kindAttr).toString();
        Objects.requireNonNull(kind, () -> "Node '" + id + "' does not have attribute '" + kindAttr + "'");

        var node = new GenericAnyNode(kind);
        nodes.put(id, node);

        // Set id
        node.putObject("id", id);

        // Set attributes
        for (var key : value.keySet()) {

            // Skip children
            // if (key.equals(children)) {
            if (key.equals(childrenAttr)) {
                continue;
            }

            Object attrValue = value.get(key);

            if (attrValue instanceof List<?> list) {
                attrValue = list.toArray(new Object[list.size()]);
            }

            node.putObject(key, attrValue);
        }

        var childrenIds = getChildrenIds(id, value);

        children.put(id, childrenIds);

    }

    private GenericAnyNode parseNodeRecursive(Map<String, Object> value) {
        var id = nextId();

        var kind = value.get(kindAttr).toString();
        Objects.requireNonNull(kind, () -> "Node '" + id + "' does not have attribute '" + kindAttr + "'");

        var node = new GenericAnyNode(kind);

        // Set id
        node.putObject("id", id);

        // Set attributes
        for (var key : value.keySet()) {

            // Skip children
            if (key.equals(childrenAttr)) {
                continue;
            }

            Object attrValue = value.get(key);

            if (attrValue instanceof List<?> list) {
                attrValue = list.toArray(new Object[list.size()]);
            }

            node.putObject(key, attrValue);
        }

        // Parse children
        @SuppressWarnings("unchecked")
        var childrenUnparsed = (List<Map<String, Object>>) value.get(childrenAttr);
        Objects.requireNonNull(childrenUnparsed,
                () -> "Node '" + id + "' does not have attribute '" + childrenAttr + "'");

        var children = childrenUnparsed.stream().map(child -> parseNodeRecursive(child)).toList();
        node.setChildren(children);

        return node;
    }

    private List<String> getChildrenIds(String id, Map<String, Object> value) {
        // Create children ids
        if (!value.containsKey(childrenAttr)) {
            SpecsLogs.info(
                    "Node '" + id + "' does not have an attribute '" + childrenAttr + "' for children, assuming empty");
            return Collections.emptyList();
        }

        @SuppressWarnings("unchecked")
        var children = (List<Object>) value.get(childrenAttr);

        return children.stream()
                .map(c -> c.toString())
                .toList();
    }

}
