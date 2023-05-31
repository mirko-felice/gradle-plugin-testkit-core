/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirkofelice.plugin.tasks

import io.github.mirkofelice.api.CheckerType
import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Classpath
import org.gradle.api.tasks.Input
import org.gradle.kotlin.dsl.property

/**
 * Abstract base task providing [checkerType] and [forwardOutput] properties to set for all the tests.
 */
open class TestkitTask : DefaultTask() {

    /**
     * Property describing the classpath of the testkit.
     */
    @Classpath
    val classpath: ConfigurableFileCollection = project.objects.fileCollection()

    /**
     * [Property] describing the [CheckerType] to use. Default to [CheckerType.KOTLIN]
     */
    @Input
    val checkerType: Property<CheckerType> = project.objects.property()

    /**
     * [Property] describing if the user wants to see the output of the gradle build. Default to false.
     */
    @Input
    val forwardOutput: Property<Boolean> = project.objects.property()
}
