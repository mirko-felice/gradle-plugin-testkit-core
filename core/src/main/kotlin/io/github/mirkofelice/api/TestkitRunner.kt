/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirkofelice.api

import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.github.mirkofelice.api.CheckerType.KOTLIN
import io.github.mirkofelice.core.KotlinChecker
import io.github.mirkofelice.core.Test
import io.github.mirkofelice.core.TestkitChecker
import io.github.mirkofelice.core.Tests
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import java.io.File
import kotlin.io.path.createTempDirectory
import io.github.mirkofelice.core.BuildResult as Result

/**
 * Object able to run the tests contained in the yaml files.
 */
object TestkitRunner {

    private const val baseFolder = "build/resources/test/"
    private val mapper = JsonMapper
        .builder(YAMLFactory())
        .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS, true)
        .build()
        .registerKotlinModule()

    /**
     * Run all the tests inside this project.
     * @param testFolderName name of the folder containing yaml file in `resources`. Default to `""`
     * @param checkerType [CheckerType] to use. Default to [CheckerType.KOTLIN]
     * @param forwardOutput true if user wants to see the tasks output, false otherwise. Default to false
     */
    fun runTests(
        testFolderName: String = "",
        checkerType: CheckerType = KOTLIN,
        forwardOutput: Boolean = false,
    ) {
        val testFolder = File(baseFolder + testFolderName)
        testFolder.walk()
            .filter { it.name.endsWith(".yaml") }
            .forEach { runTest(it, testFolder, checkerType, forwardOutput) }
    }

    /**
     * Runs all the tests.
     * @param projectFolder folder of the project to run into
     * @param testFolderName name of the folder containing yaml file in `resources`. Default to `""`
     * @param checkerType [CheckerType] to use. Default to [CheckerType.KOTLIN]
     * @param forwardOutput true if user wants to see the tasks output, false otherwise. Default to false
     */
    fun runTests(
        projectFolder: String,
        testFolderName: String = "",
        checkerType: CheckerType = KOTLIN,
        forwardOutput: Boolean = false,
    ) {
        val testFolder = File(projectFolder + File.separator + baseFolder + testFolderName)
        testFolder.walk()
            .filter { it.name.endsWith(".yaml") }
            .forEach { runTest(it, testFolder, checkerType, forwardOutput) }
    }

    private fun runTest(yamlFile: File, testFolder: File, checkerType: CheckerType, forwardOutput: Boolean) {
        val tests = mapper.readValue(yamlFile, Tests::class.java)
        val temporaryFolder = generateTempFolder(testFolder)

        println(
            "Executing tests of configuration file: ${yamlFile.name} in temp folder ${temporaryFolder.absolutePath}\n" +
                "using base folder ${testFolder.absolutePath}\n",
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
                    temporaryFolder,
                    test.configuration.tasks,
                    test.configuration.options,
                    test.expectation.result == Result.FAILED,
                    forwardOutput,
                )
                val checker: TestkitChecker = when (checkerType) {
                    KOTLIN -> KotlinChecker()
                }
                executeChecks(checker, test, result, temporaryFolder)
            }
        } finally {
            println("\nTerminate executing tests\n")
            if (!temporaryFolder.deleteRecursively()) println("Error deleting the temp folder.")
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
        checker.checkOutcomes(test.expectation.outcomes, result)
        checker.checkOutput(test.expectation.output, result.output)
        checker.checkFiles(test.expectation.files, root)
    }

    private fun generateTempFolder(testFolder: File) = createTempDirectory("testkit").apply {
        testFolder.copyRecursively(this.toFile())
    }.toFile()

    private fun List<String>.replaceWithSpaces() =
        this.toString()
            .replace("[", "")
            .replace("]", "")
            .replace(",", " ")
}
