/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirkofelice.structure

/**
 * Represents the single test to execute.
 * @property id identifier of the test. Default to a random string
 * @property description description to understand what this test does
 * @property requires id of another test required by this. Default to null
 * @property configuration [Configuration] of the test
 * @property expectation [Expectation] of the test
 */
data class Test(
    val id: String = List(STRING_LENGTH) { charPool.random() }.joinToString(""),
    val description: String,
    val requires: String? = null,
    val configuration: Configuration,
    val expectation: Expectation,
) {
    private companion object {
        private const val STRING_LENGTH = 10
        private val charPool = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    }
}
