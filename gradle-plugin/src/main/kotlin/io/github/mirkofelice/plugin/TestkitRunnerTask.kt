/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirkofelice.plugin

import io.github.mirkofelice.api.TestkitRunner
import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.provider.ListProperty
import org.gradle.api.tasks.Classpath
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.listProperty
import java.io.File

/**
 * Main [Task][org.gradle.api.Task] of the plugin able to run the testkit according to the provided configuration.
 */
open class TestkitRunnerTask : DefaultTask() {

    /**
     * Property describing the classpath of the testkit.
     */
    @Classpath
    val classpath: ConfigurableFileCollection = project.objects.fileCollection()

    /**
     * [ListProperty] of [TestkitTest] describing the tests to run.
     */
    @Input
    val tests: ListProperty<TestkitTest> = project.objects.listProperty()

    /**
     * Run the tests.
     */
    @TaskAction
    fun run() {
        tests.get().forEach {
            TestkitRunner.runTests(
                project.name,
                project.projectDir.path + File.separator + it.testFolderName.get(),
                it.checkerType.get(),
                it.forwardOutput.get(),
            )
        }
    }
}
