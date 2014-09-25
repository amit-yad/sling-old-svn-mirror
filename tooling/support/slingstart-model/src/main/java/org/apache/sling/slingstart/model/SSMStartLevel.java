/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.sling.slingstart.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A start level holds a set of artifacts.
 * A valid start level is positive, start level 0 means the default OSGi start level.
 */
public class SSMStartLevel implements Comparable<SSMStartLevel> {

    private final int level;

    private final List<SSMArtifact> artifacts = new ArrayList<SSMArtifact>();

    public SSMStartLevel(final int level) {
        this.level = level;
    }

    public int getLevel() {
        return this.level;
    }

    public List<SSMArtifact> getArtifacts() {
        return this.artifacts;
    }

    /**
     * validates the object and throws an IllegalStateException
     *
     * @throws IllegalStateException
     */
    public void validate() {
        for(final SSMArtifact sl : this.artifacts) {
            sl.validate();
        }
        if ( level < 0 ) {
            throw new IllegalStateException("level");
        }
    }

    /**
     * Search an artifact with the same groupId, artifactId, version, type and classifier.
     * Version is not considered.
     */
    public SSMArtifact search(final SSMArtifact template) {
        SSMArtifact found = null;
        for(final SSMArtifact current : this.artifacts) {
            if ( current.getGroupId().equals(template.getGroupId())
              && current.getArtifactId().equals(template.getArtifactId())
              && current.getClassifier().equals(template.getClassifier())
              && current.getType().equals(template.getType()) ) {
                found = current;
                break;
            }
        }
        return found;
    }

    public void merge(final SSMStartLevel other) {
        for(final SSMArtifact a : other.artifacts) {
            final SSMArtifact found = this.search(a);
            if ( found != null ) {
                this.artifacts.remove(found);
            }
            this.artifacts.add(a);
        }
    }

    @Override
    public int compareTo(final SSMStartLevel o) {
        if ( this.level < o.level ) {
            return -1;
        } else if ( this.level > o.level ) {
            return 1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "SSMStartLevel [level=" + level + ", artifacts=" + artifacts
                + "]";
    }
}
