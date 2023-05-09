/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirkofelice.core

/**
 * Represents the single test to execute.
 * @property description description to understand what this test does
 * @property configuration [Configuration] of the test
 * @property expectation [Expectation] of the test
 */
internal data class Test(
    val description: String,
    val configuration: Configuration,
    val expectation: Expectation,
)
