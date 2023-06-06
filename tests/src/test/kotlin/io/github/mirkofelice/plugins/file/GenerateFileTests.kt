/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirkofelice.plugins.file

import io.github.mirkofelice.api.Testkit
import io.github.mirkofelice.plugins.AbstractTest
import io.github.mirkofelice.plugins.runTestsInsideProject
import io.kotest.assertions.throwables.shouldThrow

class GenerateFileTests : AbstractTest({

    "Basic Test" {
        Testkit.runTestsInsideProject("generateFile/basic")
    }

    "Content Test" {
        Testkit.runTestsInsideProject("generateFile/content")
    }

    "Permissions Test" {
        Testkit.runTestsInsideProject("generateFile/permissions")
    }

    "Regex Test" {
        Testkit.runTestsInsideProject("generateFile/regex")
    }

    "Wrong permissions Test" {
        shouldThrow<IllegalStateException> {
            Testkit.runTestsInsideProject("generateFile/wrongPermissions")
        }
    }

    "Wrong content Test" {
        shouldThrow<IllegalStateException> {
            Testkit.runTestsInsideProject("generateFile/wrongContent")
        }
    }

    "Wrong content regex Test" {
        shouldThrow<IllegalStateException> {
            Testkit.runTestsInsideProject("generateFile/wrongContentRegex")
        }
    }

})
