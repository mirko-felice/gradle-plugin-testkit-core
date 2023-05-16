/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirkofelice.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.registering
import org.gradle.kotlin.dsl.repositories
import org.gradle.kotlin.dsl.withType

/**
 * [Plugin] for gradle projects to facilitate use of the testkit.
 */
open class TestkitPlugin : Plugin<Project> {

    /**
     * @see [Plugin.apply]
     */
    override fun apply(target: Project) {
        if (!target.pluginManager.hasPlugin("java-gradle-plugin")) {
            target.pluginManager.apply("java-gradle-plugin")
        }
        target.repositories {
            mavenCentral()
        }
        val configuration = target.configurations.create("testkit") { conf ->
            conf.defaultDependencies {
                it.add(target.dependencies.create(core))
            }
        }
        val extension = target.extensions.create<TestkitExtension>("testkit")
        target.tasks.withType<TestkitTask>().configureEach {
            it.group = "verification"
            val processResources = target.tasks.getByName("processResources")
            val processTestResources = target.tasks.getByName("processTestResources")
            val pluginUnderTestMetadata = target.tasks.getByName("pluginUnderTestMetadata")
            it.dependencies(processResources, processTestResources, pluginUnderTestMetadata)
            it.checkerType.set(extension.checkerType)
            it.forwardOutput.set(extension.forwardOutput)
            it.classpath.from(configuration.resolve())
        }
        val testFromFolders by target.tasks.registering(TestkitFoldersTask::class) {
            description = "Runs the tests for the plugin, using the DSL `folders` configuration."
            onlyIf { extension.folders.isPresent }
            folders.set(extension.folders)
        }
        val testFromDSL by target.tasks.registering(TestkitTestsTask::class) {
            description = "Runs the tests for the plugin, using the DSL `tests` configuration."
            onlyIf { extension.tests.isPresent }
            tests.set(extension.tests)
        }
        target.tasks.register<DefaultTask>("tests") {
            group = "verification"
            description = "Runs all the testkit task."
            dependsOn(testFromFolders, testFromDSL)
            mustRunAfter(testFromFolders, testFromDSL)
        }
    }

    private fun TestkitTask.dependencies(vararg tasks: Task) {
        tasks.forEach {
            it.outputs.upToDateWhen { false }
            this.dependsOn(it)
        }
    }

    private companion object {
        private const val core = "io.github.mirko-felice.testkit:core:+"
    }
}
