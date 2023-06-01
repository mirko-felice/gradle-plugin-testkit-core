/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirkofelice.plugin.dsl.test

import io.github.mirkofelice.structure.ExistingFile
import io.github.mirkofelice.structure.Permission

@TestkitTestDSL
class TestkitExistingFile(val name: String) : Convertable<ExistingFile> {

    var content: String = ""
    var permissions: List<Permission> = emptyList()
    var contentRegex: List<String> = emptyList()

    override fun toDataClass(): ExistingFile = ExistingFile(name, content, permissions, contentRegex)
}
