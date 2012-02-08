/*
 * Copyright 2011 the original author or authors.
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
package org.gradle.integtests.resolve;


import org.gradle.integtests.fixtures.AbstractIntegrationSpec
import org.gradle.integtests.fixtures.HttpServer
import org.gradle.integtests.fixtures.IvyRepository
import org.junit.Rule

public class CacheResilienceDependencyResolutionIntegrationTest extends AbstractIntegrationSpec {
    @Rule
    public final HttpServer server = new HttpServer()

    def "setup"() {
        requireOwnUserHomeDir()
    }

    public void "cache handles manual deletion of cached artifacts"() {
        server.start()

        given:
        def repo = new IvyRepository(file('ivy-repo'))
        def module = repo.module('group', 'projectA', '1.2')
        module.publish()

        def cacheDir = distribution.userHomeDir.file('caches')

        and:
        buildFile << """
repositories {
    ivy { url "http://localhost:${server.port}/repo" }
}
configurations { compile }
dependencies { compile 'group:projectA:1.2' }
task listJars << {
    assert configurations.compile.collect { it.name } == ['projectA-1.2.jar']
}
task deleteCacheFiles(type: Delete) {
    delete fileTree(dir: '${cacheDir}', includes: ['**/projectA/**'])
}
"""

        and:
        server.allowGet("/repo", repo.rootDir)

        and:
        succeeds('listJars')
        succeeds('deleteCacheFiles')
        
        when:
        server.resetExpectations()
        server.expectGet('/repo/group/projectA/1.2/ivy-1.2.xml', module.ivyFile)
        server.expectGet('/repo/group/projectA/1.2/projectA-1.2.jar', module.jarFile)

        then:
        succeeds('listJars')
    }
}