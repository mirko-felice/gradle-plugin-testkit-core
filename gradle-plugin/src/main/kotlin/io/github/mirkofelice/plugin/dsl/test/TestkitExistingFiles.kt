/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirkofelice.plugin.dsl.test

@TestkitTestDSL
class TestkitExistingFiles {

    internal val files: MutableList<TestkitExistingFile> = mutableListOf()

    fun file(name: String, lambda: TestkitExistingFile.() -> Unit) {
        files.add(TestkitExistingFile(name).apply(lambda))
    }
}
