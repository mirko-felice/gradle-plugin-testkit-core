/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirkofelice.dsl.test

import io.github.mirkofelice.structure.Outcomes

/**
 * Represents the outcomes of the test, mirroring the [Outcomes] of the core module.
 */
@TestkitTestDSL
class TestkitOutcomes : Convertable<Outcomes> {

    /**
     * Represents the [List] of tasks that should succeed.
     */
    var success: List<String> = emptyList()

    /**
     * Represents the [List] of tasks that should fail.
     */
    var failed: List<String> = emptyList()

    /**
     * Represents the [List] of tasks that should be up-to-date.
     */
    var upToDate: List<String> = emptyList()

    /**
     * Represents the [List] of tasks that should be skipped.
     */
    var skipped: List<String> = emptyList()

    /**
     * Represents the [List] of tasks that should be from-cache.
     */
    var fromCache: List<String> = emptyList()

    /**
     * Represents the [List] of tasks that should be no-source.
     */
    var noSource: List<String> = emptyList()

    /**
     * Represents the [List] of tasks that should not exist.
     */
    var notExecuted: List<String> = emptyList()

    override fun convert() = Outcomes(success, failed, upToDate, skipped, fromCache, noSource, notExecuted)
}
