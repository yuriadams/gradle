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
package org.gradle.api.internal.file.archive.decompressors;


import org.gradle.api.tasks.bundling.Compression
import spock.lang.Specification

/**
 * by Szczepan Faber, created at: 11/17/11
 */
public class DecompressorFactoryTest extends Specification {

    def factory = new DecompressorFactory();

    def "creates decompressors"() {
        expect:
        factory.decompressor(Compression.BZIP2.extension) instanceof Bzip2Decompressor
        factory.decompressor(Compression.GZIP.extension)  instanceof GzipDecompressor
        factory.decompressor(Compression.NONE.extension)  instanceof NoOpDecompressor
        factory.decompressor("foo")  instanceof NoOpDecompressor
    }
}