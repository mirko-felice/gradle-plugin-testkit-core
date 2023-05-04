/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirko.felice.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.github.mirko.felice.api.CheckerType.KOTLIN
import io.github.mirko.felice.core.KotlinChecker
import io.github.mirko.felice.core.Test
import io.github.mirko.felice.core.TestkitChecker
import io.github.mirko.felice.core.Tests
import org.gradle.internal.impldep.org.junit.rules.TemporaryFolder
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import java.io.File

/**
 * Object able to run the tests contained in the yaml files.
 */
object TestkitRunner {

    private const val baseFolder = "build/resources/test/"
    private val mapper = ObjectMapper(YAMLFactory()).registerKotlinModule()

    /**
     * Runs all the tests.
     * @param testFolderName name of the folder containing yaml file in `resources`
     * @param checkerType [CheckerType] to use. Default to [CheckerType.KOTLIN]
     * @param forwardOutput true if user wants to see the tasks output, false otherwise. Default to false
     */
    fun runTests(testFolderName: String, checkerType: CheckerType = KOTLIN, forwardOutput: Boolean = false) {
        val testFolder = File(baseFolder + testFolderName)
        testFolder.walk()
            .filter { it.name.endsWith(".yaml") }
            .forEach { runTest(it, testFolder, checkerType, forwardOutput) }
    }

    private fun runTest(yamlFile: File, testFolder: File, checkerType: CheckerType, forwardOutput: Boolean) {
        val tests = mapper.readValue(yamlFile, Tests::class.java)
        val temporaryFolder = generateTempFolder(testFolder)

        println(
            "Executing tests of configuration file: " +
                "${testFolder.relativeTo(File(baseFolder)).invariantSeparatorsPath}/${yamlFile.name}\n",
        )
        try {
            tests.tests.forEachIndexed { index, test ->
                val options = if (test.configuration.options.isEmpty()) {
                    ""
                } else {
                    " ${test.configuration.options.replaceWithSpaces()}"
                }
                println(
                    "\tExecuting test n. ${index + 1}:\n\t\t'${test.description}' -> " +
                        "Running `gradlew ${test.configuration.tasks.replaceWithSpaces()}$options`",
                )
                val result = executeTest(
                    temporaryFolder.root,
                    test.configuration.tasks,
                    test.configuration.options,
                    test.expectation.result == TaskOutcome.FAILED.name,
                    forwardOutput,
                )
                val checker: TestkitChecker = when (checkerType) {
                    KOTLIN -> KotlinChecker(result)
                }
                executeChecks(checker, test, result, temporaryFolder.root)
            }
        } finally {
            println("\nTerminate executing tests\n")
            temporaryFolder.delete()
        }
    }

    private fun executeTest(
        testFolder: File,
        tasks: List<String>,
        options: List<String>,
        hasToFail: Boolean,
        forwardOutput: Boolean,
    ): BuildResult {
        val testkitProperties = javaClass.classLoader.getResource("testkit-gradle.properties")?.readText()
        if (testkitProperties != null) {
            File(testFolder, "gradle.properties").writeText(testkitProperties)
        }
        val gradleRunner = GradleRunner.create()
            .withProjectDir(testFolder)
            .withPluginClasspath()
            .withArguments(tasks + options)
        if (forwardOutput) gradleRunner.forwardOutput()
        return gradleRunner.run { if (hasToFail) buildAndFail() else build() }
    }

    private fun executeChecks(checker: TestkitChecker, test: Test, result: BuildResult, root: File) {
        val output = result.output
        test.expectation.outputContains.forEach { checker.checkOutputContains(output, it) }
        test.expectation.outputDoesntContain.forEach { checker.checkOutputDoesNotContain(output, it) }

        test.expectation.nonExistent.forEach { checker.checkTaskNonExistence(it) }
        val allExistingTasks = test.expectation.success + test.expectation.upToDate + test.expectation.failure
        allExistingTasks.forEach { checker.checkTaskExistence(it) }

        test.expectation.success.forEach { checker.checkSuccessOutcomeOf(it) }
        test.expectation.upToDate.forEach { checker.checkUpToDateOutcomeOf(it) }
        test.expectation.failure.forEach { checker.checkFailureOutcomeOf(it) }

        test.expectation.existingFiles.forEach {
            val fileToCheck = File("${root.absolutePath}/${it.name}")
            checker.checkFileExistence(it, fileToCheck)
            checker.checkFileContent(it, fileToCheck)
            checker.checkFilePermissions(it, fileToCheck)
        }
    }

    private fun generateTempFolder(testFolder: File) = TemporaryFolder().apply {
        create()
        testFolder.copyRecursively(this.root)
    }

    private fun List<String>.replaceWithSpaces() =
        this.toString()
            .replace("[", "")
            .replace("]", "")
            .replace(",", " ")
}
