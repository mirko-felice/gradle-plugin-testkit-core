/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirko.felice.core

import org.gradle.internal.impldep.org.apache.commons.lang.StringUtils
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.TaskOutcome
import java.io.File

/**
 * Implementation of [TestkitChecker] that uses basic kotlin assertions.
 * @param buildResult [BuildResult] used to check
 * @constructor Creates the checker based on the result.
 */
internal class KotlinChecker(override val buildResult: BuildResult) : TestkitChecker {

    override fun checkOutputContains(output: String, partOfOutput: String) {
        assert(output.contains(partOfOutput)) {
            "Output '$output' should contain '$partOfOutput'."
        }
    }

    override fun checkOutputDoesNotContain(output: String, notPartOfOutput: String) {
        assert(!output.contains(notPartOfOutput)) {
            "Output '$output' should not contain '$notPartOfOutput'."
        }
    }

    override fun checkSuccessOutcomeOf(taskName: String) {
        checkOutcome(TaskOutcome.SUCCESS, taskName)
    }

    override fun checkUpToDateOutcomeOf(taskName: String) {
        checkOutcome(TaskOutcome.UP_TO_DATE, taskName)
    }

    override fun checkFailureOutcomeOf(taskName: String) {
        checkOutcome(TaskOutcome.FAILED, taskName)
    }

    override fun checkFileExistence(existingFile: ExistingFile, file: File) {
        assert(file.exists()) {
            "File '${file.name}' does not exist."
        }
        assert(file.isFile) {
            "File '${file.name}' is not a real file."
        }
        existingFile.findRegex.forEach { regexString ->
            val regex = Regex(regexString)
            requireNotNull(file.readLines().find { regex.matches(it) }) {
                """
                None of the lines in ${file.name} matches the regular expression ${existingFile.findRegex}. File content:
                ${file.readText()}
                """.trimIndent()
            }
        }
    }

    override fun checkFilePermissions(existingFile: ExistingFile, file: File) {
        existingFile.permissions.forEach {
            assert(it.hasPermission(file)) {
                "File ${file.absolutePath} must have permission $it, but it does not."
            }
        }
    }

    override fun checkFileContent(existingFile: ExistingFile, file: File) {
        if (existingFile.content != null) {
            val actualContent = file.readText()
            assert(existingFile.content == actualContent) {
                """
                Content of ${existingFile.name} does not match expectations.
                
                Expected:
                ${existingFile.content}
                
                Actual:
                $actualContent
                
                Difference starts at index ${StringUtils.indexOfDifference(existingFile.content, actualContent)}:
                ${StringUtils.difference(existingFile.content, actualContent)}
                """.trimIndent()
            }
        }
    }

    override fun checkTaskNonExistence(taskName: String) {
        assert(buildResult.task(":$taskName") == null) {
            "Task with name '$taskName' should not exist."
        }
    }

    override fun checkTaskExistence(taskName: String) {
        assert(buildResult.task(":$taskName") != null) {
            "Task with name '$taskName' should exist."
        }
    }

    private fun checkOutcome(expectedOutcome: TaskOutcome, taskName: String) {
        val actualOutcome = buildResult.outcomeOf(taskName)
        assert(actualOutcome == expectedOutcome) {
            "Outcome of task '$taskName' should be $expectedOutcome, instead it is $actualOutcome."
        }
    }

    private fun BuildResult.outcomeOf(name: String) = checkNotNull(task(":$name")?.outcome) {
        "Task $name was not present among the executed tasks"
    }
}
