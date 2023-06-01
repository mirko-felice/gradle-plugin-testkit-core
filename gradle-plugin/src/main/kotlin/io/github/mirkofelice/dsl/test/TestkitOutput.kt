/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirkofelice.dsl.test

import io.github.mirkofelice.structure.Output

/**
 * Represents the output of the test, mirroring the [Output] of the core module.
 */
@TestkitTestDSL
class TestkitOutput : Convertable<Output> {

    /**
     * Represents the [List] of contents that should be contained in the output.
     */
    var contains: List<String> = emptyList()

    /**
     * Represents the [List] of contents that should not be contained in the output.
     */
    var doesntContain: List<String> = emptyList()

    override fun convert(): Output = Output(contains, doesntContain)
}
