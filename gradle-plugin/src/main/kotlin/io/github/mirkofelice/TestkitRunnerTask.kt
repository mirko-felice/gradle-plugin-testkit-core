/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirkofelice

import io.github.mirkofelice.api.CheckerType
import io.github.mirkofelice.api.TestkitRunner
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.property

/**
 * Main [org.gradle.api.Task] of the plugin able to run the testkit according to the provided configuration.
 */
open class TestkitRunnerTask : DefaultTask() {

    /**
     * [Property] describing ...
     */
    @Input
    val forwardOutput: Property<Boolean> = project.objects.property()

    /**
     * [Property] describing ...
     */
    @Input
    val testFolderName: Property<String> = project.objects.property()

    /**
     * [Property] describing ...
     */
    @Input
    val checkerType: Property<String> = project.objects.property()

    private val realCheckerType: Provider<CheckerType> = checkerType.map { CheckerType.valueOf(it) }

    /**
     * Run the testkit.
     */
    @TaskAction
    fun run() {
        TestkitRunner.runTests(testFolderName.get(), realCheckerType.get(), forwardOutput.get())
    }
}
