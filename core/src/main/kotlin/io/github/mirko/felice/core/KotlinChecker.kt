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
 */
internal class KotlinChecker : TestkitChecker {

    override fun checkTaskExistence(taskName: String, result: BuildResult) {
        assert(result.task(":$taskName") != null) {
            "Task with name '$taskName' should exist."
        }
    }

    override fun checkTaskNonExistence(taskName: String, result: BuildResult) {
        assert(result.task(":$taskName") == null) {
            "Task with name '$taskName' should not exist."
        }
    }

    override fun checkOutcome(expectedOutcome: TaskOutcome, taskName: String, result: BuildResult) {
        val actualOutcome = result.outcomeOf(taskName)
        assert(actualOutcome == expectedOutcome) {
            "Outcome of task '$taskName' should be $expectedOutcome, instead it is $actualOutcome."
        }
    }

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

    override fun checkFileExistence(file: File) {
        assert(file.exists()) {
            "File '${file.name}' does not exist."
        }
        assert(file.isFile) {
            "File '${file.name}' is not a real file."
        }
    }

    override fun checkFilePermissions(permissions: List<Permission>, file: File) {
        permissions.forEach {
            assert(it.hasPermission(file)) {
                "File ${file.absolutePath} must have permission $it, but it does not."
            }
        }
    }

    override fun checkFileContent(content: String, file: File) {
        val actualContent = file.readText()
        assert(content == actualContent) {
            """
            Content of ${file.name} does not match expectations.
            
            Expected:
            $content
            
            Actual:
            $actualContent
            
            Difference starts at index ${StringUtils.indexOfDifference(content, actualContent)}:
            ${StringUtils.difference(content, actualContent)}
            """.trimIndent()
        }
    }

    private fun BuildResult.outcomeOf(name: String) = checkNotNull(task(":$name")?.outcome) {
        "Task $name was not present among the executed tasks"
    }
}
