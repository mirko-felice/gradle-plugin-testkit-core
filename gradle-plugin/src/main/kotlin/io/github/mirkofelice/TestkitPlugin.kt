/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirkofelice

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.register

/**
 * [Plugin] for gradle projects to facilitate use of the testkit.
 */
open class TestkitPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        val extension = target.extensions.create<TestkitExtension>("testkit")
        target.tasks.register<TestkitRunnerTask>("runTestkit") {
            testFolderName.set(extension.testFolderName)
            checkerType.set(extension.checkerType)
            forwardOutput.set(extension.forwardOutput)
        }
    }
}
