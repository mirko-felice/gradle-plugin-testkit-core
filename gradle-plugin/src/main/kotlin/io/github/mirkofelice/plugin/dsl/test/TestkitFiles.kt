/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirkofelice.plugin.dsl.test

import io.github.mirkofelice.structure.Files

@TestkitTestDSL
class TestkitFiles : Convertable<Files> {

    private val existing: TestkitExistingFiles = TestkitExistingFiles()

    fun existing(lambda: TestkitExistingFiles.() -> Unit) {
        existing.apply(lambda)
    }

    override fun toDataClass(): Files = Files(existing.files.map { it.toDataClass() })
}
