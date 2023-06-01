/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirkofelice.dsl.test

import io.github.mirkofelice.structure.Configuration

/**
 * Represents a test configuration, mirroring the [Configuration] of the core module.
 */
@TestkitTestDSL
class TestkitConfiguration : Convertable<Configuration> {

    /**
     * Represents the tasks to execute.
     */
    lateinit var tasks: List<String>

    /**
     * Represents the options of the build to execute.
     */
    var options: List<String> = emptyList()

    override fun convert(): Configuration = Configuration(tasks, options)
}
