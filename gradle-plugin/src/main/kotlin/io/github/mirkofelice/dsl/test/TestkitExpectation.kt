/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirkofelice.dsl.test

import io.github.mirkofelice.structure.BuildResult
import io.github.mirkofelice.structure.Expectation

/**
 * Represents the expectation of the test, mirroring the [Expectation] of the core module.
 */
@TestkitTestDSL
class TestkitExpectation : Convertable<Expectation> {

    private val outcomes: TestkitOutcomes = TestkitOutcomes()
    private val output: TestkitOutput = TestkitOutput()
    private val files: TestkitFiles = TestkitFiles()

    /**
     * Represents the [BuildResult] of the test.
     */
    var result: BuildResult = BuildResult.SUCCESS

    /**
     * Sets the outcomes of the test.
     * @param outcomes configuration of the [TestkitOutcomes] to apply
     */
    fun outcomes(outcomes: TestkitOutcomes.() -> Unit) {
        this.outcomes.apply(outcomes)
    }

    /**
     * Sets the output of the test.
     * @param output configuration of the [TestkitOutput] to apply
     */
    fun output(output: TestkitOutput.() -> Unit) {
        this.output.apply(output)
    }

    /**
     * Sets the files of the test.
     * @param files configuration of the [TestkitFiles] to apply
     */
    fun files(files: TestkitFiles.() -> Unit) {
        this.files.apply(files)
    }

    override fun convert(): Expectation =
        Expectation(result, outcomes.convert(), output.convert(), files.convert())
}
