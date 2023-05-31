/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirkofelice.plugin.dsl.test

import io.github.mirkofelice.core.Outcomes

@TestkitTestDSL
class TestkitOutcomes : Convertable<Outcomes> {

    var success: List<String> = emptyList()
    var failed: List<String> = emptyList()
    var upToDate: List<String> = emptyList()
    var skipped: List<String> = emptyList()
    var fromCache: List<String> = emptyList()
    var noSource: List<String> = emptyList()
    var notExecuted: List<String> = emptyList()

    override fun toDataClass() = Outcomes(success, failed, upToDate, skipped, fromCache, noSource, notExecuted)
}
