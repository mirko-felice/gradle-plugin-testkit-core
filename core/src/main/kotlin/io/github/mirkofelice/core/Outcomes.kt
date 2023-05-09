/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirkofelice.core

/**
 * Represents all the expected outcomes of the tasks.
 * @property success [List] of tasks that should succeed
 * @property failed [List] of tasks that should fail
 * @property upToDate [List] of tasks that should be up-to-date
 * @property skipped [List] of tasks that should be skipped
 * @property fromCache [List] of tasks that should be from-cache
 * @property noSource [List] of tasks that should be no-source
 * @property notExecuted [List] of tasks that should not exist
 */
internal data class Outcomes(
    val success: List<String> = emptyList(),
    val failed: List<String> = emptyList(),
    val upToDate: List<String> = emptyList(),
    val skipped: List<String> = emptyList(),
    val fromCache: List<String> = emptyList(),
    val noSource: List<String> = emptyList(),
    val notExecuted: List<String> = emptyList(),
) {
    internal fun allExecutedTasks() = success + failed + upToDate + skipped + fromCache + noSource
}
