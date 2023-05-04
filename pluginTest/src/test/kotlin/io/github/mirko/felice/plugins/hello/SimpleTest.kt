/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirko.felice.plugins.hello

import io.github.mirko.felice.api.TestkitRunner
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec

class SimpleTest : StringSpec({

    "Set of positive tests" {
        TestkitRunner.runTests("positiveTests")
    }

    "Wrong output test with forward output" {
        shouldThrow<AssertionError> {
            TestkitRunner.runTests("wrongOutput", forwardOutput = true)
        }
    }
})
