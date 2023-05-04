/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirko.felice.plugins.file

import io.github.mirko.felice.api.TestkitRunner
import io.kotest.core.spec.style.StringSpec

class GenerateFileTests : StringSpec({

    "Basic Test" {
        TestkitRunner.runTests("generateFile/basic")
    }

    "Content Test" {
        TestkitRunner.runTests("generateFile/content")
    }

    "Permissions Test" {
        TestkitRunner.runTests("generateFile/permissions")
    }
})
