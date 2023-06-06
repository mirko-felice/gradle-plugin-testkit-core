/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirkofelice.runners

import io.github.mirkofelice.api.CheckerType
import io.github.mirkofelice.checkers.KotlinChecker
import io.github.mirkofelice.structure.BuildResult
import io.github.mirkofelice.structure.Tests
import java.io.File
import kotlin.io.path.createTempDirectory

/**
 * Object able to run the [Tests].
 */
internal object TestRunner {

    /**
     * Runs the tests, using appropriate parameters.
     * @param tests [Tests] to run
     * @param pluginBuildFolder path of the folder containing the plugin under test
     * @param projectFolder folder containing the `build.gradle.kts` applying the plugin
     * @param checkerType [CheckerType] to use
     * @param forwardOutput true if user wants to see the tasks output, false otherwise. Default to false
     */
    internal fun runTests(
        tests: Tests,
        projectFolder: File,
        pluginBuildFolder: String,
        checkerType: CheckerType,
        forwardOutput: Boolean,
    ) {
        val temporaryProjectFolder = generateTempFolder(projectFolder)

        try {
            tests.tests.forEachIndexed { index, test ->
                val tasks = test.configuration.tasks.joinToString(" ")
                val options = test.configuration.options.joinToString(" ")

                println(
                    "\tExecuting test n. ${index + 1}:\n\t\t'${test.description}' -> " +
                        "Running `gradlew $tasks $options`",
                )
                val result = BuildRunner.build(
                    pluginBuildFolder,
                    temporaryProjectFolder,
                    test.configuration.tasks,
                    test.configuration.options,
                    test.expectation.result == BuildResult.FAILED,
                    forwardOutput,
                )
                when (checkerType) {
                    CheckerType.KOTLIN -> KotlinChecker()
                }.check(test.expectation, result, temporaryProjectFolder)
            }
        } finally {
            println("\nTerminate executing tests\n")
            if (!temporaryProjectFolder.deleteRecursively()) println("Error deleting the temp folder.")
        }
    }

    private fun generateTempFolder(testFolder: File) = createTempDirectory("testkit").apply {
        testFolder.copyRecursively(this.toFile())
    }.toFile()
}
