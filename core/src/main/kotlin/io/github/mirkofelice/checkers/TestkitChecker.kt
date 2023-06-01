/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirkofelice.checkers

import io.github.mirkofelice.structure.Files
import io.github.mirkofelice.structure.Outcomes
import io.github.mirkofelice.structure.Output
import io.github.mirkofelice.structure.Permission
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.TaskOutcome
import java.io.File

/**
 * Entity able to check the assertions of the tests.
 */
internal interface TestkitChecker {

    /**
     * Checks that a task has been executed.
     * @param taskName name of the task to check
     * @param result [BuildResult] to check from
     */
    fun checkExecutedTask(taskName: String, result: BuildResult)

    /**
     * Checks that a task has not been executed.
     * @param taskName name of the task to check
     * @param result [BuildResult] to check from
     */
    fun checkNonExecutedTask(taskName: String, result: BuildResult)

    /**
     * Checks an expected [TaskOutcome] of a particular task.
     * @param expectedOutcome [TaskOutcome] to check
     * @param taskName name of the task to check
     * @param result [BuildResult] to check from
     */
    fun checkOutcome(expectedOutcome: TaskOutcome, taskName: String, result: BuildResult)

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
     * Checks that a file exists.
     * @param file [File] to check
     */
    fun checkFileExistence(file: File)

    /**
     * Checks that a file has correct permission.
     * @param permission [Permission] to check
     * @param file [File] to check
     */
    fun checkFilePermission(permission: Permission, file: File)

    /**
     * Checks that a file has correct content.
     * @param content content to check
     * @param file [File] to check
     */
    fun checkFileContent(content: String, file: File)

    /**
     * Checks that a file has at least one line according to a regex.
     * @param contentRegex [Regex] to check
     * @param file [File] to check
     */
    fun checkFileContentRegex(contentRegex: Regex, file: File)

    /**
     * Checks all the expected [Outcomes].
     * @param expectedOutcomes expected [Outcomes] to check
     * @param result [BuildResult] to check from
     */
    fun checkOutcomes(expectedOutcomes: Outcomes, result: BuildResult) {
        expectedOutcomes.allExecutedTasks().forEach { checkExecutedTask(it, result) }
        expectedOutcomes.success.forEach { checkOutcome(TaskOutcome.SUCCESS, it, result) }
        expectedOutcomes.failed.forEach { checkOutcome(TaskOutcome.FAILED, it, result) }
        expectedOutcomes.upToDate.forEach { checkOutcome(TaskOutcome.UP_TO_DATE, it, result) }
        expectedOutcomes.fromCache.forEach { checkOutcome(TaskOutcome.FROM_CACHE, it, result) }
        expectedOutcomes.skipped.forEach { checkOutcome(TaskOutcome.SKIPPED, it, result) }
        expectedOutcomes.noSource.forEach { checkOutcome(TaskOutcome.NO_SOURCE, it, result) }

        expectedOutcomes.notExecuted.forEach { checkNonExecutedTask(it, result) }
    }

    /**
     * Checks all the expectations of the [Output].
     * @param expectedOutput expected [Output] to check
     * @param actualOutput actual output
     */
    fun checkOutput(expectedOutput: Output, actualOutput: String) {
        expectedOutput.contains.forEach { checkOutputContains(actualOutput, it) }
        expectedOutput.doesntContain.forEach { checkOutputDoesNotContain(actualOutput, it) }
    }

    /**
     * Checks the expectations on [Files].
     * @param files expectations on [Files]
     * @param root root folder to check from
     */
    fun checkFiles(files: Files, root: File) {
        files.existing.forEach {
            val fileToCheck = File("${root.absolutePath}/${it.name}")
            checkFileExistence(fileToCheck)
            it.permissions.forEach { permission -> checkFilePermission(permission, fileToCheck) }
            if (it.content != "") checkFileContent(it.content, fileToCheck)
            if (it.contentRegex.isNotEmpty()) {
                it.contentRegex.forEach { regex -> checkFileContentRegex(Regex(regex), fileToCheck) }
            }
        }
    }
}
