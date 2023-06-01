/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirkofelice.plugin.dsl.test

import io.github.mirkofelice.structure.Output

@TestkitTestDSL
class TestkitOutput : Convertable<Output> {

    var contains: List<String> = emptyList()
    var doesntContain: List<String> = emptyList()

    override fun toDataClass(): Output = Output(contains, doesntContain)
}
