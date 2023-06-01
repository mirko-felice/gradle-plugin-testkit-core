/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirkofelice.core

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.TaskOutcome
import java.io.File

/**
 * Implementation of [TestkitChecker] that uses basic kotlin assertions.
 */
internal class KotlinChecker : TestkitChecker {

    override fun checkExecutedTask(taskName: String, result: BuildResult) {
        checkNotNull(result.task(":$taskName")) {
            "Task with name '$taskName' should have been executed."
        }
    }

    override fun checkNonExecutedTask(taskName: String, result: BuildResult) {
        check(result.task(":$taskName") == null) {
            "Task with name '$taskName' should have not been executed."
        }
    }

    override fun checkOutcome(expectedOutcome: TaskOutcome, taskName: String, result: BuildResult) {
        val actualOutcome = result.outcomeOf(taskName)
        check(actualOutcome == expectedOutcome) {
            "Outcome of task '$taskName' should be $expectedOutcome, instead it is $actualOutcome."
        }
    }

    override fun checkOutputContains(output: String, partOfOutput: String) {
        check(output.contains(partOfOutput)) {
            "Output '$output' should contain '$partOfOutput'."
        }
    }

    override fun checkOutputDoesNotContain(output: String, notPartOfOutput: String) {
        check(!output.contains(notPartOfOutput)) {
            "Output '$output' should not contain '$notPartOfOutput'."
        }
    }

    override fun checkFileExistence(file: File) {
        check(file.exists()) {
            "File '${file.name}' does not exist."
        }
        check(file.isFile) {
            "File '${file.name}' is not a real file."
        }
    }

    override fun checkFilePermissions(permissions: List<Permission>, file: File) {
        permissions.forEach {
            check(it.hasPermission(file)) {
                "File ${file.absolutePath} must have permission $it, but it does not."
            }
        }
    }

    override fun checkFileContent(content: String, file: File) {
        val actualContent = file.readText()
        check(content == actualContent) {
            """
            Content of ${file.name} does not match expectations.
            
            Expected:
            $content
            
            Actual:
            $actualContent
            """.trimIndent()
        }
    }

    override fun checkFileContentRegex(contentRegex: Regex, file: File) {
        check(file.readLines().any { contentRegex.matches(it) }) {
            """
            None of the lines in ${file.name} matches the regular expression $contentRegex. 
            File content:
            ${file.readText()}
            """.trimIndent()
        }
    }

    private fun BuildResult.outcomeOf(name: String) = checkNotNull(task(":$name")) {
        "Task $name was not present among the executed tasks"
    }.outcome
}
