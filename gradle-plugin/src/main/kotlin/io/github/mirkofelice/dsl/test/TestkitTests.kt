/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirkofelice.dsl.test

import io.github.mirkofelice.structure.Tests
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import org.gradle.kotlin.dsl.listProperty
import org.gradle.kotlin.dsl.newInstance
import org.gradle.kotlin.dsl.property
import java.io.File
import java.io.Serializable
import javax.inject.Inject

/**
 * Represents the tests' configuration, mirroring the [Tests] of the core module.
 */
@TestkitTestDSL
open class TestkitTests @Inject constructor(private val objects: ObjectFactory) : Serializable, Convertable<Tests> {

    private lateinit var _folder: File
    private val tests: ListProperty<TestkitTest> = objects.listProperty()

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

    /**
     * Represents the root folder of the tests.
     */
    var folder: File
        get() = this._folder
        set(value) {
            require(value.isDirectory && value.walk().any { it.name.endsWith("build.gradle.kts") }) {
                "File with path ${value.path} has to be a folder and must contain build.gradle.kts!"
            }
            this._folder = value
        }

    override fun convert(): Tests = Tests(tests.get().map { it.convert() })

    private companion object {
        private const val serialVersionUID = 1L
    }
}
