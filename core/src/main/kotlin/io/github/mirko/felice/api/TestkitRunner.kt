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
import io.github.mirko.felice.core.YamlTests
import org.gradle.internal.impldep.org.junit.rules.TemporaryFolder
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import java.io.File

/**
 * Entity able to run the tests contained in the yaml files.
 * @param checkerType [CheckerType] to use
 * @param forwardOutput true if user wants to see the tasks output, false otherwise. Default to false
 * @param testFolder path of the folder to test. Default to "build/resources/test"
 */
class TestkitRunner(
    private val checkerType: CheckerType,
    private val forwardOutput: Boolean = false,
    private val testFolder: String = "build/resources/test",
) {

    /**
     * Runs all the tests.
     */
    fun runTests() {
        val testFolder = File(testFolder)
        testFolder.walk()
            .filter { it.name.endsWith(".yaml") }
            .forEach { runTest(it, testFolder) }
    }

    private fun runTest(yamlFile: File, testFolder: File) {
        val yamlTests = mapper.readValue(yamlFile, YamlTests::class.java)
        val temporaryFolder = generateTempFolder(testFolder)

        println("Executing tests of configuration file: ${yamlFile.name}\n")
        yamlTests.tests.forEachIndexed { index, test ->
            println("\tExecuting test n. ${index + 1} -> \'${test.description}\'")
            val options =
                if (test.configuration.options.isEmpty()) "" else " ${test.configuration.options.replaceWithSpaces()}"
            println("\tRunning `gradlew ${test.configuration.tasks.replaceWithSpaces()}$options`")
            val result = executeTest(
                temporaryFolder.root,
                test.configuration.tasks,
                test.configuration.options,
                test.expectation.failure.isNotEmpty(),
            )
            val checker: TestkitChecker = when (checkerType) {
                KOTLIN -> KotlinChecker(result)
            }
            executeChecks(checker, test, result, temporaryFolder.root)
        }
        println("\nTerminate executing tests")
        temporaryFolder.delete()
    }

    private fun executeTest(
        testFolder: File,
        tasks: List<String>,
        options: List<String>,
        hasToFail: Boolean,
    ): BuildResult {
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
        test.expectation.success.forEach { checker.checkSuccessOutcomeOf(it) }
        test.expectation.failure.forEach { checker.checkFailureOutcomeOf(it) }
        test.expectation.fileToExists.forEach {
            checker.checkFileExistence(it, File("${root.absolutePath}/${it.name}"))
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

    companion object {

        private val mapper = ObjectMapper(YAMLFactory()).registerKotlinModule()
    }
}
