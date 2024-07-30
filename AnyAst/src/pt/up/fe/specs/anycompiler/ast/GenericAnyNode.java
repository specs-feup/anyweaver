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

package pt.up.fe.specs.anycompiler.ast;

import java.util.*;

public class GenericAnyNode extends AnyNode {

    private String kind;

    private final Map<String, Object> attributes;
    private Collection<String> hierarchy;

    private GenericAnyNode(String kind, Map<String, Object> attributes, Collection<? extends AnyNode> children) {
        super(children);
        this.kind = kind;
        this.attributes = attributes;
        this.hierarchy = Arrays.asList(kind);
    }

    public GenericAnyNode(String kind, Collection<? extends AnyNode> children) {
        this(kind, new HashMap<String, Object>(), children);
    }

    public GenericAnyNode(String kind) {
        this(kind, Collections.emptyList());
    }

    @Override
    public Collection<String> getHierarchy() {
        return hierarchy;
    }

    public void setHierarchy(Collection<String> hierarchy) {
        this.hierarchy = hierarchy;
    }

    @Override
    public String toContentString() {
        return kind + ": " + attributes.toString();
    }

    @Override
    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    @Override
    public Object getObject(String attribute) {
        // System.out.println("GENERIC: " + attributes);
        // System.out.println("GET: " + attributes.get(attribute));
        return attributes.get(attribute);
    }

    @Override
    public Object putObject(String attribute, Object value) {
        return attributes.put(attribute, value);
    }

    @Override
    public Collection<String> getAttributes() {
        return attributes.keySet();
    }

    @Override
    protected AnyNode copyPrivate() {
        // Copy children
        var childrenCopy = getChildren().stream()
                .map(c -> c.copy())
                .toList();

        return new GenericAnyNode(kind, new HashMap<>(attributes), childrenCopy);
    }

    /*
    public static final DataKey<String> KIND = KeyFactory.string("kind");
    
    public static final DataKey<Map<String, Object>> ATTRIBUTES = KeyFactory.generic("attributes",
            () -> new HashMap<String, Object>());
    
    private GenericAnyNode(DataStore data, Collection<? extends AnyNode> children) {
        super(data, children);
    }
    
    public GenericAnyNode() {
        super(DataStore.newInstance("GenericAnyNode Data"), Collections.emptyList());
    }
    */

}
