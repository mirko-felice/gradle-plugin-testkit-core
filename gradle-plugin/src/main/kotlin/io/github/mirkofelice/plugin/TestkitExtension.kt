/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirkofelice.plugin

import io.github.mirkofelice.api.CheckerType
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.newInstance
import org.gradle.kotlin.dsl.property
import java.io.Serializable

/**
 * Extension for the plugin.
 */
@TestkitFolderDSL
@TestkitTestDSL
open class TestkitExtension(private val objects: ObjectFactory) : Serializable {

    /**
     * [Property] describing the [CheckerType] to use. Default to [CheckerType.KOTLIN]
     */
    val checkerType: Property<CheckerType> = objects.property<CheckerType>().convention(CheckerType.KOTLIN)

    /**
     * [Property] describing if the user wants to see the output of the gradle build. Default to false.
     */
    val forwardOutput: Property<Boolean> = objects.property<Boolean>().convention(false)

    /**
     * [Property] describing the [TestkitFolders] to retrieve tests from.
     */
    internal val folders: Property<TestkitFolders> = objects.property()

    /**
     * [Property] describing the [TestkitTests] to use.
     */
    internal val tests: Property<TestkitTests> = objects.property()

    /**
     * Sets the [TestkitFolders].
     * @param configuration configuration of the [TestkitFolders] to apply
     */
    fun folders(configuration: TestkitFolders.() -> Unit) {
        folders.set(objects.newInstance(TestkitFolders::class).apply(configuration))
    }

//    /**
//     * Sets the [TestkitTests].
//     * @param configuration configuration of the [TestkitTests] to apply
//     */
//    fun tests(configuration: TestkitTests.() -> Unit) {
//        tests.set(objects.newInstance(TestkitTests::class).apply(configuration))
//    }

    private companion object {
        private const val serialVersionUID = 1L
    }
}
