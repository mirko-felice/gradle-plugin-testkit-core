/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirkofelice.plugin.dsl.test

import io.github.mirkofelice.core.BuildResult
import io.github.mirkofelice.core.Expectation

@TestkitTestDSL
class TestkitExpectation : Convertable<Expectation> {

    var result: BuildResult = BuildResult.SUCCESS
    private val outcomes: TestkitOutcomes = TestkitOutcomes()
    private val output: TestkitOutput = TestkitOutput()
    private val files: TestkitFiles = TestkitFiles()

    fun outcomes(outcomes: TestkitOutcomes.() -> Unit) {
        this.outcomes.apply(outcomes)
    }

    fun output(output: TestkitOutput.() -> Unit) {
        this.output.apply(output)
    }

    fun files(files: TestkitFiles.() -> Unit) {
        this.files.apply(files)
    }

    override fun toDataClass(): Expectation =
        Expectation(result, outcomes.toDataClass(), output.toDataClass(), files.toDataClass())
}
