/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirkofelice.plugin

import io.github.mirkofelice.api.CheckerType
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
     * Default to "": that means that the _src/test/resources_ path will be used.
     */
    val testFolderName: Property<String> = objects.property<String>().convention("")

    /**
     * [Property] describing the [CheckerType] to use. Default to [CheckerType.KOTLIN]
     */
    val checkerType: Property<CheckerType> = objects.property<CheckerType>().convention(CheckerType.KOTLIN)

    /**
     * [Property] describing if the user wants to see the output of the gradle build. Default to false.
     */
    val forwardOutput: Property<Boolean> = objects.property<Boolean>().convention(false)

    private companion object {
        private const val serialVersionUID = 1L
    }
}
