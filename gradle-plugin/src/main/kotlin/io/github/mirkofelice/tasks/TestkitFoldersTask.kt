/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirkofelice.tasks

import io.github.mirkofelice.api.Testkit
import io.github.mirkofelice.dsl.folder.TestkitFolders
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.property
import java.io.File

/**
 * [Task][org.gradle.api.Task] of the plugin able to run the testkit according to the provided configuration.
 */
open class TestkitFoldersTask : TestkitTask() {

    /**
     * [Property] describing the [TestkitFolders] to retrieve tests from.
     */
    @Input
    val folders: Property<TestkitFolders> = project.objects.property()

    /**
     * Runs the tests from the specified folders.
     */
    @TaskAction
    fun run() {
        folders.get().folders.get().forEach {
            Testkit.test(
                project.name,
                project.projectDir.path + File.separator + it.path.get(),
                this.checkerType.get(),
                this.forwardOutput.get(),
            )
        }
    }
}
