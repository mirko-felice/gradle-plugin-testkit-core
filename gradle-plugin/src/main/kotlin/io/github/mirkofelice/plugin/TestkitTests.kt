/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirkofelice.plugin

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import org.gradle.kotlin.dsl.listProperty
import org.gradle.kotlin.dsl.newInstance
import org.gradle.kotlin.dsl.property
import java.io.Serializable
import javax.inject.Inject

/**
 * Represents the tests' configuration.
 */
@TestkitTestDSL
open class TestkitTests @Inject constructor(private val objects: ObjectFactory) : Serializable {

    internal val tests: ListProperty<TestkitTest> = objects.listProperty()

    /**
     * Adds a new test.
     * @param description describes the test
     * @param configuration configuration to apply
     */
    fun test(description: String, configuration: TestkitTest.() -> Unit) {
        val test = objects.newInstance(TestkitTest::class, objects.property(String::class).value(description))
            .apply(configuration)
        tests.add(test)
    }

    private companion object {
        private const val serialVersionUID = 1L
    }
}
