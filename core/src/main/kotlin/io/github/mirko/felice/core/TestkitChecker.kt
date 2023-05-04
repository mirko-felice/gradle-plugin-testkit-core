/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirko.felice.core

import org.gradle.testkit.runner.BuildResult
import java.io.File

/**
 * Entity able to check the assertions of the tests.
 */
internal interface TestkitChecker {

    /**
     * Represents the [BuildResult] of the executed task.
     */
    val buildResult: BuildResult

    /**
     * Checks that the actual content of the output contains an expected part of it.
     * @param output actual content
     * @param partOfOutput expected part of content
     */
    fun checkOutputContains(output: String, partOfOutput: String)

    /**
     * Checks that the actual content does not contain an expected part of it.
     * @param output actual content
     * @param notPartOfOutput expected not part of content
     */
    fun checkOutputDoesNotContain(output: String, notPartOfOutput: String)

    /**
     * Checks that a task outcome is [org.gradle.testkit.runner.TaskOutcome.SUCCESS].
     * @param taskName name of the task to check
     */
    fun checkSuccessOutcomeOf(taskName: String)

    /**
     * Checks that a task outcome is [org.gradle.testkit.runner.TaskOutcome.UP_TO_DATE].
     * @param taskName name of the task to check
     */
    fun checkUpToDateOutcomeOf(taskName: String)

    /**
     * Checks that a task outcome is [org.gradle.testkit.runner.TaskOutcome.FAILED].
     * @param taskName name of the task to check
     */
    fun checkFailureOutcomeOf(taskName: String)

    /**
     * Checks that a file exists.
     * @param existingFile [ExistingFile] to extract info from
     * @param file [File] to check
     */
    fun checkFileExistence(existingFile: ExistingFile, file: File)

    /**
     * Checks that a file has correct permissions.
     * @param existingFile [ExistingFile] to extract info from
     * @param file [File] to check
     */
    fun checkFilePermissions(existingFile: ExistingFile, file: File)

    /**
     * Checks that a file has correct permissions.
     * @param existingFile [ExistingFile] to extract info from
     * @param file [File] to check
     */
    fun checkFileContent(existingFile: ExistingFile, file: File)

    /**
     * Checks that a task does not exist.
     * @param taskName task name to check
     */
    fun checkTaskNonExistence(taskName: String)

    /**
     * Checks that a task does exist.
     * @param taskName task name to check
     */
    fun checkTaskExistence(taskName: String)
}
