/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirko.felice.core

/**
 * Represents the expectations of a [Test].
 * @property result expected result of the build
 * @property existingFiles [List] of [ExistingFile]
 * @property nonExistent [List] of tasks that should not exist
 * @property success [List] of tasks that should succeed
 * @property upToDate [List] of tasks that should be up-to-date
 * @property failure [List] of tasks that should fail
 * @property outputContains [List] of contents that should be contained in the output
 * @property outputDoesntContain [List] of contents that should not be contained in the output
 */
internal data class Expectation(
    val result: ExpectedResult = ExpectedResult.SUCCESS,
    val existingFiles: List<ExistingFile> = emptyList(),
    val nonExistent: List<String> = emptyList(),
    val success: List<String> = emptyList(),
    val upToDate: List<String> = emptyList(),
    val failure: List<String> = emptyList(),
    val outputContains: List<String> = emptyList(),
    val outputDoesntContain: List<String> = emptyList(),
)
