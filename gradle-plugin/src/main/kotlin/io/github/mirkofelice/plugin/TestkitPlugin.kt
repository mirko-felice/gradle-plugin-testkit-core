/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirkofelice.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.register
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
        val configuration = target.configurations.create("testkit") { conf ->
            conf.defaultDependencies {
                it.add(target.dependencies.create(core))
            }
        }
        val extension = target.extensions.create<TestkitExtension>("testkit")
        target.tasks.register<TestkitRunnerTask>("runTestkit") {
            tests.set(extension.tests)
        }
        target.tasks.withType<TestkitRunnerTask>().configureEach {
            val processResources = target.tasks.getByName("processResources")
            val processTestResources = target.tasks.getByName("processTestResources")
            val pluginUnderTestMetadata = target.tasks.getByName("pluginUnderTestMetadata")
            it.dependencies(processResources, processTestResources, pluginUnderTestMetadata)
            it.classpath.from(configuration)
        }
    }

    private fun TestkitRunnerTask.dependencies(vararg tasks: Task) {
        tasks.forEach {
            it.outputs.upToDateWhen { false }
            this.dependsOn(it)
        }
    }

    private companion object {
        private const val core = "io.github.mirko-felice.testkit:core:+"
    }
}
