/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirko.felice

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.TaskOutcome
import java.io.File

/**
 * Empty doc.
 */
class BaseRunner(forwardOutput: Boolean) : TestkitRunner(forwardOutput) {

    override fun checkOutputContains(output: String, partOfOutput: String) {
        assert(output.contains(partOfOutput)) {
            "Output \"$output\" should contain \"$partOfOutput\"."
        }
    }

    override fun checkOutputDoesNotContain(output: String, notPartOfOutput: String) {
        assert(!output.contains(notPartOfOutput)) {
            "Output \"$output\" should not contain \"$notPartOfOutput\"."
        }
    }

    override fun BuildResult.checkSuccessOutcomeOf(taskName: String) {
        checkOutcome(TaskOutcome.SUCCESS, this.outcomeOf(taskName), taskName)
    }

    override fun BuildResult.checkFailureOutcomeOf(taskName: String) {
        checkOutcome(TaskOutcome.FAILED, this.outcomeOf(taskName), taskName)
    }

    override fun ExistingFile.checkExistingFile(fileToCheck: File) {
        assert(fileToCheck.exists())
        assert(fileToCheck.isFile)
        this.validate(fileToCheck)
    }

    private fun checkOutcome(expectedOutcome: TaskOutcome, actualOutcome: TaskOutcome, taskName: String) {
        assert(actualOutcome == expectedOutcome) {
            "Outcome of task '$taskName' should be $expectedOutcome, instead it is $actualOutcome."
        }
    }
}
