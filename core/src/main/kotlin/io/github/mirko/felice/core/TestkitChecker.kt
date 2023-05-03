/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirko.felice.core

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.TaskOutcome
import java.io.File

/**
 * Entity able to check the assertions of the tests.
 * @param result [BuildResult] used to check
 */
internal abstract class TestkitChecker(protected val result: BuildResult) {

    /**
     * Returns the [org.gradle.testkit.runner.TaskOutcome] of the task given as parameter.
     * @param name name of the task to extract the outcome by
     */
    protected fun outcomeOf(name: String) = result.task(":$name")?.outcome ?: TaskOutcome.FAILED

    /**
     * Checks that the actual content of the output contains an expected part of it.
     * @param output actual content
     * @param partOfOutput expected part of content
     */
    abstract fun checkOutputContains(output: String, partOfOutput: String)

    /**
     * Checks that the actual content does not contain an expected part of it.
     * @param output actual content
     * @param notPartOfOutput expected not part of content
     */
    abstract fun checkOutputDoesNotContain(output: String, notPartOfOutput: String)

    /**
     * Checks that a task outcome is [org.gradle.testkit.runner.TaskOutcome.SUCCESS].
     * @param taskName name of the task to check
     */
    abstract fun checkSuccessOutcomeOf(taskName: String)

    /**
     * Checks that a task outcome is [org.gradle.testkit.runner.TaskOutcome.UP_TO_DATE].
     * @param taskName name of the task to check
     */
    abstract fun checkUpToDateOutcomeOf(taskName: String)

    /**
     * Checks that a task outcome is [org.gradle.testkit.runner.TaskOutcome.FAILED].
     * @param taskName name of the task to check
     */
    abstract fun checkFailureOutcomeOf(taskName: String)

    /**
     * Checks that a file exists.
     */
    abstract fun checkFileExistence(existingFile: ExistingFile, file: File)

    /**
     * Checks that a file has correct permissions.
     */
    abstract fun checkFilePermissions(existingFile: ExistingFile, file: File)

    /**
     * Checks that a file has correct permissions.
     */
    abstract fun checkFileContent(existingFile: ExistingFile, file: File)

    /**
     * Checks that a task does not exist.
     */
    abstract fun checkTaskNonExistence(nonExistingTask: String)
}
