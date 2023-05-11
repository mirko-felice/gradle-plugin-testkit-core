/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirkofelice.plugin

import io.github.mirkofelice.api.CheckerType
import io.github.mirkofelice.api.TestkitRunner
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.property

/**
 * Main [Task][org.gradle.api.Task] of the plugin able to run the testkit according to the provided configuration.
 */
open class TestkitRunnerTask : DefaultTask() {

    /**
     * [Property] describing the name of the folder containing the yaml file.
     */
    @Input
    val testFolderName: Property<String> = project.objects.property()

    /**
     * [Property] describing the [CheckerType] to use.
     */
    @Input
    val checkerType: Property<CheckerType> = project.objects.property()

    /**
     * [Property] describing if the user wants to see the output of the gradle build.
     */
    @Input
    val forwardOutput: Property<Boolean> = project.objects.property()

    /**
     * Run the testkit.
     */
    @TaskAction
    fun run() {
        TestkitRunner.runTests(testFolderName.get(), checkerType.get(), forwardOutput.get())
    }
}
