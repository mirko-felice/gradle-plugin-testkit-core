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
import org.gradle.plugin.devel.tasks.PluginUnderTestMetadata
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.internal.PluginUnderTestMetadataReading
import java.io.File
import java.util.*
import kotlin.io.path.createTempDirectory
import io.github.mirkofelice.core.BuildResult as Result

/**
 * Object able to run the tests contained in the yaml files.
 */
object TestkitRunner {

    private const val PROPERTIES_FILE_NAME = PluginUnderTestMetadataReading.PLUGIN_METADATA_FILE_NAME
    private val sep = File.separator
    private val mapper = JsonMapper
        .builder(YAMLFactory())
        .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS, true)
        .build()
        .registerKotlinModule()
    private val propertiesFolder =
        PluginUnderTestMetadata::class.simpleName.toString().replaceFirstChar { it.lowercase(Locale.getDefault()) }

    /**
     * Default folder containing the tests.
     */
    val DEFAULT_TEST_FOLDER = "${System.getProperty("user.dir")}${sep}src${sep}main${sep}resources$sep"

    /**
     * Run all the tests.
     * @param tests [Tests] to run
     * @param rootFolder folder containing the `build.gradle.kts`
     * @param checkerType [CheckerType] to use. Default to [CheckerType.KOTLIN]
     * @param forwardOutput true if user wants to see the tasks output, false otherwise. Default to false
     */
    fun runTests(
        tests: Tests,
        rootFolder: File,
        buildFolder: String,
        checkerType: CheckerType = KOTLIN,
        forwardOutput: Boolean = false,
    ) {
        require(rootFolder.isDirectory && rootFolder.walk().any { it.name.endsWith("build.gradle.kts") })
        println(
            "Executing tests configured in plugin DSL of folder dir '${rootFolder.path}'\n",
        )
        runTests(tests, buildFolder, rootFolder, checkerType, forwardOutput)
    }

    /**
     * Run all the tests.
     * @param projectName name of the project, used to search for plugin classpath
     * @param rootFolderName name of the folder containing yaml file. Default to [DEFAULT_TEST_FOLDER]
     * @param checkerType [CheckerType] to use. Default to [CheckerType.KOTLIN]
     * @param forwardOutput true if user wants to see the tasks output, false otherwise. Default to false
     */
    fun runTests(
        projectName: String,
        rootFolderName: String = DEFAULT_TEST_FOLDER,
        checkerType: CheckerType = KOTLIN,
        forwardOutput: Boolean = false,
    ) {
        val rootFolder = File(rootFolderName)
        val buildFolder = rootFolderName.replaceAfter(projectName, "") + File.separator + "build"
        val yamlFile = rootFolder.walk().firstOrNull { it.name.endsWith(".yaml") }
        if (yamlFile != null) {
            val tests = mapper.readValue(yamlFile, Tests::class.java)
            println(
                "Executing tests of configuration file: '${yamlFile.name}' in dir '${rootFolder.path}'\n",
            )
            runTests(tests, buildFolder, yamlFile.parentFile, checkerType, forwardOutput)
        }
    }

    private fun runTests(
        tests: Tests,
        buildFolder: String,
        rootFolder: File,
        checkerType: CheckerType,
        forwardOutput: Boolean,
    ) {
        val temporaryFolder = generateTempFolder(rootFolder)

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
                    buildFolder,
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
        buildFolder: String,
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
            .withPluginClasspath(getPluginClasspath(buildFolder))
            .withArguments(tasks + options)
        if (forwardOutput) gradleRunner.forwardOutput()
        return gradleRunner.run { if (hasToFail) buildAndFail() else build() }
    }

    private fun executeChecks(checker: TestkitChecker, test: Test, result: BuildResult, rootFolder: File) {
        checker.checkOutcomes(test.expectation.outcomes, result)
        checker.checkOutput(test.expectation.output, result.output)
        checker.checkFiles(test.expectation.files, rootFolder)
    }

    private fun generateTempFolder(testFolder: File) = createTempDirectory("testkit").apply {
        testFolder.copyRecursively(this.toFile())
    }.toFile()

    private fun List<String>.replaceWithSpaces() =
        this.toString()
            .replace("[", "")
            .replace("]", "")
            .replace(",", " ")

    private fun getPluginClasspath(buildFolder: String): List<File> {
        val pluginClasspathFile = File(buildFolder + sep + propertiesFolder + sep + PROPERTIES_FILE_NAME)
        return Properties().also { it.load(pluginClasspathFile.inputStream()) }
            .getProperty(PluginUnderTestMetadataReading.IMPLEMENTATION_CLASSPATH_PROP_KEY)
            .split(File.pathSeparator)
            .map { File(it) }
    }
}
