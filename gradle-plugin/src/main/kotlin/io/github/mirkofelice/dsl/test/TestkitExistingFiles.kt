/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirkofelice.dsl.test

/**
 * Represents the list of the existing files.
 */
@TestkitTestDSL
class TestkitExistingFiles {

    internal val files: MutableList<TestkitExistingFile> = mutableListOf()

    /**
     * Add a new file to the configuration.
     * @param name name of the file
     * @param lambda configuration of the [TestkitExistingFile] to apply
     */
    fun file(name: String, lambda: TestkitExistingFile.() -> Unit) {
        files.add(TestkitExistingFile(name).apply(lambda))
    }
}
