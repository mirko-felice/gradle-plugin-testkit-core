/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirkofelice.plugin

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.property
import java.io.Serializable

/**
 * Extension for the plugin.
 */
open class TestkitExtension(objects: ObjectFactory) : Serializable {

    /**
     * [Property] describing the name of the folder containing the yaml file.
     */
    val testFolderName: Property<String> = objects.property()

    /**
     * [Property] describing the [CheckerType][io.github.mirkofelice.api.CheckerType] to use.
     */
    val checkerType: Property<String> = objects.property()

    /**
     * [Property] describing if the user wants to see the output of the gradle build. Default to false.
     */
    val forwardOutput: Property<Boolean> = objects.property<Boolean>().convention(false)

    private companion object {
        private const val serialVersionUID = 1L
    }
}
