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
import io.github.mirkofelice.runners.TestRunner
import io.github.mirkofelice.structure.Tests
import java.io.File

/**
 * Object able to run the tests contained in the yaml files.
 */
object Testkit {

    /**
     * Constant representing the system property to set the test mode.
     */
    const val TEST_MODE = "TESTKIT_TEST_MODE"
    private val sep = File.separator
    private val mapper = JsonMapper
        .builder(YAMLFactory())
        .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS, true)
        .build()
        .registerKotlinModule()

    /**
     * Default folder containing the tests, as 'currentDir/src/test/resources/'.
     */
    val DEFAULT_TEST_FOLDER = "${System.getProperty("user.dir")}${sep}src${sep}test${sep}resources$sep"

    /**
     * Default folder containing the tests, as 'currentDir/src/main/resources/'.
     */
    val DEFAULT_MAIN_FOLDER = "${System.getProperty("user.dir")}${sep}src${sep}main${sep}resources$sep"

    /**
     * Run all the tests, designed to be run by the user.
     * @param projectName name of the project, used to search for plugin classpath
     * @param projectFolderPath path of the folder containing yaml file and `build.gradle.kts` applying the plugin. Default to [DEFAULT_TEST_FOLDER]
     * @param checkerType [CheckerType] to use. Default to [CheckerType.KOTLIN]
     * @param forwardOutput true if user wants to see the tasks output, false otherwise. Default to false
     */
    fun test(
        projectName: String,
        projectFolderPath: String = DEFAULT_TEST_FOLDER,
        checkerType: CheckerType = KOTLIN,
        forwardOutput: Boolean = false,
    ) {
        val projectFolder = File(projectFolderPath)
        val buildFolder = projectFolderPath.replaceAfterLast(projectName, "") + sep + "build"
        if (requireFolder(projectFolder) && requireBuildGradleKts(projectFolder) && !isTestMode()) {
            val yamlFile = requireYaml(projectFolder)
            val tests = mapper.readValue(yamlFile, Tests::class.java)
            println("Executing tests of configuration file: '${yamlFile.name}' in dir '${projectFolder.path}'\n")
            TestRunner.runTests(tests, yamlFile.parentFile, buildFolder, checkerType, forwardOutput)
        }
    }

    /**
     * Runs all the tests, designed to be run by the gradle-plugin.
     * @param tests [Tests] to run
     * @param projectFolderPath path of the folder containing the `build.gradle.kts` applying the plugin
     * @param pluginBuildFolder path of the build folder containing the plugin under test
     * @param checkerType [CheckerType] to use
     * @param forwardOutput true if user wants to see the tasks output, false otherwise
     */
    fun test(
        tests: Tests,
        projectFolderPath: String,
        pluginBuildFolder: String,
        checkerType: CheckerType,
        forwardOutput: Boolean,
    ) {
        val projectFolder = File(projectFolderPath)
        if (requireFolder(projectFolder) && requireBuildGradleKts(projectFolder)) {
            println("Executing tests configured in plugin DSL of folder dir '${projectFolder.path}'\n")
            TestRunner.runTests(tests, projectFolder, pluginBuildFolder, checkerType, forwardOutput)
        }
    }

    private fun requireFolder(projectFolder: File): Boolean {
        return checkCondition(projectFolder.isDirectory, "File '$projectFolder' does not exist or is not a folder!")
    }

    private fun requireBuildGradleKts(projectFolder: File): Boolean {
        return checkCondition(
            projectFolder.walk().any { it.name.endsWith("build.gradle.kts") },
            "Folder '$projectFolder' does not contain any 'build.gradle.kts'!",
        )
    }

    private fun requireYaml(projectFolder: File): File {
        try {
            return projectFolder.walk().maxDepth(1).first { it.name.endsWith(".yaml") }
        } catch (e: NoSuchElementException) {
            throw IllegalArgumentException("Folder '$projectFolder' does not contain any yaml file.", e)
        }
    }

    private fun checkCondition(condition: Boolean, message: String): Boolean {
        return if (isTestMode()) {
            if (!condition) println(message)
            true
        } else {
            require(condition) { message }
            condition
        }
    }

    private fun isTestMode() = System.getProperty(TEST_MODE).toBoolean()
}
