/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.sling.scripting.javascript.wrapper;

import java.util.Iterator;

import javax.jcr.Node;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.scripting.javascript.helper.SlingWrapper;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Undefined;

/**
 * Resource in JavaScript has following signature: [Object] getData(); [Object]
 * data [Item] getItem(); [Item] item [String] getResourceType(); [String] type
 * [String] getPath(); [String] path
 */
public class ScriptableResource extends ScriptableObject implements SlingWrapper {

    public static final String CLASSNAME = "Resource";
    public static final Class<?> [] WRAPPED_CLASSES = { Resource.class };

    private Resource resource;

    public ScriptableResource() {
    }

    public ScriptableResource(Resource resource) {
        this.resource = resource;
    }

    public void jsConstructor(Object res) {
        this.resource = (Resource) res;
    }

    public Class<?> [] getWrappedClasses() {
        return WRAPPED_CLASSES;
    }
    
    @Override
    public String getClassName() {
        return CLASSNAME;
    }

    public Object jsFunction_getObject() {
        Object object = resource.adaptTo(Object.class);
        return (object != null) ? object : Undefined.instance;
    }

    public Object jsFunction_getNode() {
        Node node = resource.adaptTo(Node.class);
        Object result = Undefined.instance;
        if(node != null) {
            result = ScriptRuntime.toObject(this,node);
        }
        return result;
    }

    /** alias for getNode */
    public Object jsGet_node() {
        return jsFunction_getNode();
    }

    public String jsFunction_getResourceType() {
        return resource.getResourceType();
    }

    public String jsGet_type() {
        return this.jsFunction_getResourceType();
    }

    public Object jsFunction_getPath() {
        return Context.javaToJS(resource.getPath(), this);
    }

    public Object jsGet_path() {
        return this.jsFunction_getPath();
    }

    public Object jsFunction_getMetadata() {
        return resource.getResourceMetadata();
    }

    public Object jsGet_meta() {
        return resource.getResourceMetadata();
    }
    
    // TODO a wrapper would be more convenient than an Iterator,
    // but in my tests ScriptableItemMap didn't seem to allow
    // proper wrapping of its elements: javascript constructor
    // not found when scope = ScriptableItemMap
    public Iterator<Resource> jsGet_children() {
        return resource.getResourceProvider().listChildren(resource);
    }

    @Override
    public Object getDefaultValue(Class typeHint) {
        return resource.getPath();
    }

    public void setResource(Resource entry) {
        this.resource = entry;
    }

    // ---------- Wrapper interface --------------------------------------------

    // returns the wrapped resource
    public Object unwrap() {
        return resource;
    }

}
