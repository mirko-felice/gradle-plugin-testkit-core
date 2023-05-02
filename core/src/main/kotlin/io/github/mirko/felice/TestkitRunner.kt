/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirko.felice

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.gradle.internal.impldep.org.junit.rules.TemporaryFolder
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import java.io.File

/**
 * Entity able to run the tests contained in the yaml files.
 */
abstract class TestkitRunner(
    private val forwardOutput: Boolean,
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

    /**
     * Extension method for [BuildResult]] that returns the [org.gradle.testkit.runner.TaskOutcome]
     * of the task given as parameter.
     * @param name name of the task to extract outcome by
     */
    protected fun BuildResult.outcomeOf(name: String) = checkNotNull(task(":$name")?.outcome) {
        "Task $name was not present among the executed tasks"
    }

    /**
     * test.
     */
    protected abstract fun checkOutputContains(output: String, partOfOutput: String)

    protected abstract fun checkOutputDoesNotContain(output: String, notPartOfOutput: String)

    protected abstract fun BuildResult.checkSuccessOutcomeOf(taskName: String)

    protected abstract fun BuildResult.checkFailureOutcomeOf(taskName: String)

    protected abstract fun ExistingFile.checkExistingFile(fileToCheck: File)

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
            val output = result.output
            test.expectation.outputContains.forEach { checkOutputContains(output, it) }
            test.expectation.outputDoesntContain.forEach { checkOutputDoesNotContain(output, it) }
            test.expectation.success.forEach { result.checkSuccessOutcomeOf(it) }
            test.expectation.failure.forEach { result.checkFailureOutcomeOf(it) }
            test.expectation.fileToExists.forEach {
                it.checkExistingFile(File("${temporaryFolder.root.absolutePath}/${it.name}"))
            }
        }
        println("\nTerminate executing tests")
        if (!testFolder.delete()) println("Temporary folder with path ${testFolder.path} has not been deleted.")
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
