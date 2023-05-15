/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirkofelice.plugin

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import org.gradle.kotlin.dsl.listProperty
import org.gradle.kotlin.dsl.newInstance
import java.io.Serializable

/**
 * Main extension for the plugin.
 */
open class TestkitExtension(private val objects: ObjectFactory) : Serializable {

    private val defaultTest = objects.newInstance(TestkitTest::class)

    /**
     * [ListProperty] of [TestkitTest] describing the tests to run. Default to 'emptyList()'.
     */
    val tests: ListProperty<TestkitTest> = objects.listProperty<TestkitTest>().convention(emptyList())

    /**
     * Adds a new [TestkitTest].
     * @param configuration configuration of the [TestkitTest] to apply
     */
    open infix fun test(configuration: TestkitTest.() -> Unit) {
        tests.add(objects.newInstance(TestkitTest::class).apply(configuration))
    }

    /**
     * Automatically adds the default test.
     */
    open fun withDefault() {
        withDefault { }
    }

    /**
     * Automatically adds the default test.
     * @param configuration configuration of the [TestkitTest] to apply
     */
    open fun withDefault(configuration: TestkitTest.() -> Unit) {
        tests.add(defaultTest.apply(configuration))
    }

    private companion object {
        private const val serialVersionUID = 1L
    }
}
