/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirkofelice

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.property
import java.io.Serializable

/**
 * Extension for the plugin.
 */
open class TestkitExtension(objects: ObjectFactory) : Serializable {

    /**
     * [Property] describing the name of the test folder.
     */
    val testFolderName: Property<String> = objects.property()

    /**
     * [Property] describing ...
     */
    val checkerType: Property<String> = objects.property()

    /**
     * [Property] describing ...
     */
    val forwardOutput: Property<Boolean> = objects.property<Boolean>().convention(false)

    companion object {
        private const val serialVersionUID = 1L
    }
}
