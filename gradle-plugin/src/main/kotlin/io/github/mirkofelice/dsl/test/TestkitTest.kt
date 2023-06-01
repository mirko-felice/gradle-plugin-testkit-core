/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirkofelice.dsl.test

import io.github.mirkofelice.structure.Test
import org.gradle.api.provider.Property
import java.io.Serializable
import javax.inject.Inject

/**
 * Represents a test configuration, mirroring the [Test] of the core module.
 * @property description [Property] to describe the test
 */
@TestkitTestDSL
open class TestkitTest @Inject constructor(private val description: Property<String>) :
    Serializable, Convertable<Test> {

    private val configuration: TestkitConfiguration = TestkitConfiguration()
    private val expectation: TestkitExpectation = TestkitExpectation()

    /**
     * Sets the configuration of the test.
     * @param configuration configuration of the [TestkitConfiguration] to apply
     */
    fun configuration(configuration: TestkitConfiguration.() -> Unit) {
        this.configuration.apply(configuration)
    }

    /**
     * Sets the expectation of the test.
     * @param configuration configuration of the [TestkitExpectation] to apply
     */
    fun expectation(configuration: TestkitExpectation.() -> Unit) {
        this.expectation.apply(configuration)
    }

    override fun convert(): Test =
        Test(this.description.get(), this.configuration.convert(), this.expectation.convert())

    private companion object {
        private const val serialVersionUID = 1L
    }
}
