/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirkofelice.plugin.dsl.test

import io.github.mirkofelice.core.Configuration

/**
 * Represents a test configuration, mirroring the [Configuration] of the core module.
 */
@TestkitTestDSL
class TestkitConfiguration : Convertable<Configuration> {

    lateinit var tasks: List<String>

    var options: List<String> = emptyList()

    override fun toDataClass(): Configuration = Configuration(tasks, options)
}
