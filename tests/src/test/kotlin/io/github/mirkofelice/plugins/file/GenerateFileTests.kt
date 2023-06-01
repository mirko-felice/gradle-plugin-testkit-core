/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirkofelice.plugins.file

import io.github.mirkofelice.api.TestkitRunner
import io.github.mirkofelice.plugins.AbstractTest
import io.github.mirkofelice.plugins.runTestsInsideProject
import io.kotest.assertions.throwables.shouldThrow

class GenerateFileTests : AbstractTest({

    "Basic Test" {
        TestkitRunner.runTestsInsideProject("generateFile/basic")
    }

    "Content Test" {
        TestkitRunner.runTestsInsideProject("generateFile/content")
    }

    "Permissions Test" {
        TestkitRunner.runTestsInsideProject("generateFile/permissions")
    }

    "Regex Test" {
        TestkitRunner.runTestsInsideProject("generateFile/regex")
    }

    "Wrong permissions Test" {
        shouldThrow<IllegalStateException> {
            TestkitRunner.runTestsInsideProject("generateFile/wrongPermissions")
        }
    }

    "Wrong content Test" {
        shouldThrow<IllegalStateException> {
            TestkitRunner.runTestsInsideProject("generateFile/wrongContent")
        }
    }

    "Wrong content regex Test" {
        shouldThrow<IllegalStateException> {
            TestkitRunner.runTestsInsideProject("generateFile/wrongContentRegex")
        }
    }

})
