/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirkofelice.runners

import org.gradle.plugin.devel.tasks.PluginUnderTestMetadata
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.internal.PluginUnderTestMetadataReading
import java.io.File
import java.util.*

/**
 * Object able to run the gradle build.
 */
internal object BuildRunner {

    private const val PROPERTIES_FILE_NAME = PluginUnderTestMetadataReading.PLUGIN_METADATA_FILE_NAME
    private val sep = File.separator
    private val propertiesFolder =
        PluginUnderTestMetadata::class.simpleName.toString().replaceFirstChar { it.lowercase(Locale.getDefault()) }

    /**
     * Executes a build starting from the parameters.
     * @param pluginBuildFolder path of the build folder containing the plugin under test
     * @param projectFolder folder containing the `build.gradle.kts` applying the plugin
     * @param tasks [List] of tasks to run
     * @param options [List] of options to apply
     * @param hasToFail true if the user expects the build to fail, false otherwise
     * @param forwardOutput true if user wants to see the tasks output, false otherwise. Default to false
     */
    internal fun build(
        pluginBuildFolder: String,
        projectFolder: File,
        tasks: List<String>,
        options: List<String>,
        hasToFail: Boolean,
        forwardOutput: Boolean,
    ): BuildResult {
        val testkitProperties = javaClass.classLoader.getResource("testkit-gradle.properties")?.readText()
        if (testkitProperties != null) {
            File(projectFolder, "gradle.properties").writeText(testkitProperties)
        }
        val gradleRunner = GradleRunner.create()
            .withProjectDir(projectFolder)
            .withPluginClasspath(getPluginClasspath(pluginBuildFolder))
            .withArguments(tasks + options)
        if (forwardOutput) gradleRunner.forwardOutput()
        return gradleRunner.run { if (hasToFail) buildAndFail() else build() }
    }

    private fun getPluginClasspath(buildFolder: String): List<File> {
        val pluginClasspathFile = File(buildFolder + sep + propertiesFolder + sep + PROPERTIES_FILE_NAME)
        return if (pluginClasspathFile.exists()) {
            pluginClasspathFile.inputStream().use { s ->
                Properties().also { it.load(s) }
                    .getProperty(PluginUnderTestMetadataReading.IMPLEMENTATION_CLASSPATH_PROP_KEY)
                    .split(File.pathSeparator)
                    .map { File(it) }
            }
        } else {
            emptyList()
        }
    }
}
