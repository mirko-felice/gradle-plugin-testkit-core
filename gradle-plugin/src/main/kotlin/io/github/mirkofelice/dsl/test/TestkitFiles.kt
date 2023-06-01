/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirkofelice.dsl.test

import io.github.mirkofelice.structure.Files

/**
 * Represents the files of the test, mirroring the [Files] of the core module.
 */
@TestkitTestDSL
class TestkitFiles : Convertable<Files> {

    private val existing: TestkitExistingFiles = TestkitExistingFiles()

    /**
     * Sets the existing files of the test.
     * @param lambda configuration of the [TestkitExistingFiles] to apply
     */
    fun existing(lambda: TestkitExistingFiles.() -> Unit) {
        existing.apply(lambda)
    }

    override fun convert(): Files = Files(existing.files.map { it.convert() })
}
