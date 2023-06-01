/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirkofelice.dsl.test

import io.github.mirkofelice.structure.ExistingFile
import io.github.mirkofelice.structure.Permission

/**
 * Represents an existing file, mirroring the [ExistingFile] of the core module.
 */
@TestkitTestDSL
class TestkitExistingFile(private val name: String) : Convertable<ExistingFile> {

    /**
     * Represents the content of the file.
     */
    var content: String = ""

    /**
     * Represents the permissions list of the file.
     */
    var permissions: List<Permission> = emptyList()

    /**
     * Represents the regex list of the content.
     */
    var contentRegex: List<String> = emptyList()

    override fun convert(): ExistingFile = ExistingFile(name, content, permissions, contentRegex)
}
