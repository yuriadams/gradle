/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.api.internal.artifacts.ivyservice

import org.apache.ivy.core.module.id.ModuleRevisionId
import spock.lang.Specification

/**
 * by Szczepan Faber, created at: 5/11/12
 */
class DefaultUnresolvedDependencySpec extends Specification {

    def "provides module details"() {
        when:
        def dep = new DefaultUnresolvedDependency(ModuleRevisionId.newInstance('org.foo', "foo", '1.0'), new RuntimeException("boo!"))

        then:
        dep.identifier.group == 'org.foo'
        dep.identifier.name == 'foo'
        dep.identifier.version == '1.0'
        dep.id == 'org.foo#foo;1.0'
        dep.toString() == 'org.foo:foo:1.0'
    }
}
