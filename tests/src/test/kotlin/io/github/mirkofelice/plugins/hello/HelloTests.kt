/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirkofelice.plugins.hello

import io.github.mirkofelice.api.Testkit
import io.github.mirkofelice.plugins.AbstractTest
import io.github.mirkofelice.plugins.runTestsInsideProject
import io.kotest.assertions.throwables.shouldThrow

class HelloTests : AbstractTest({

    "Set of positive tests" {
        Testkit.runTestsInsideProject("hello/positiveTests")
    }

    "Wrong output test with forward output" {
        shouldThrow<IllegalStateException> {
            Testkit.runTestsInsideProject("hello/wrongOutput", forwardOutput = true)
        }
    }

    "Non existing task" {
        shouldThrow<IllegalStateException> {
            Testkit.runTestsInsideProject("hello/wrongExistingTask")
        }
    }
})
