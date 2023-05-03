/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirko.felice.plugins.hello

import io.github.mirko.felice.api.CheckerType
import io.github.mirko.felice.api.TestkitRunner
import io.kotest.core.spec.style.StringSpec

class SimpleTest : StringSpec({

    "Simple Test" {
        TestkitRunner(CheckerType.KOTLIN, false).runTests()
    }
})
