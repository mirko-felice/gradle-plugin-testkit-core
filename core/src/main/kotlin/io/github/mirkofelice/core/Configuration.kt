/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirkofelice.core

/**
 * Record containing the configuration of a [Test].
 * @property tasks [List] of tasks to execute
 * @property options [List] of options to add in executing the tests
 */
data class Configuration(
    val tasks: List<String>,
    val options: List<String> = emptyList(),
)
