/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirkofelice.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
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
        val configuration = target.configurations.create("testkit") { conf ->
            conf.defaultDependencies {
                it.add(target.dependencies.create(core))
            }
        }
        val extension = target.extensions.create<TestkitExtension>("testkit")
        target.tasks.register<TestkitRunnerTask>("runTestkit") {
            testFolderName.set(extension.testFolderName)
            checkerType.set(extension.checkerType)
            forwardOutput.set(extension.forwardOutput)
        }
        target.tasks.withType<TestkitRunnerTask>().configureEach {
            it.classpath.from(configuration)
        }
    }

    private companion object {
        private const val core = "io.github.mirko-felice.testkit:core:0.5.2"
    }
}
