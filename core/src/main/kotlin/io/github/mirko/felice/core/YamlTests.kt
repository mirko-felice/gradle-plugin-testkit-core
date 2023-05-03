/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirko.felice.core

import java.io.File

/**
 * Represents the yaml root.
 * @property tests [List] of [Test]
 */
internal data class YamlTests(val tests: List<Test>)

/**
 * Represents the single test to execute.
 * @property description description to understand what this test does
 * @property configuration [Configuration] of the test
 * @property expectation [Expectation] of the test
 */
internal data class Test(
    val description: String,
    val configuration: Configuration,
    val expectation: Expectation,
)

/**
 * Record containing the configuration of a [Test].
 * @property tasks [List] of tasks to execute
 * @property options [List] of options to add in executing the tests
 */
internal data class Configuration(val tasks: List<String>, val options: List<String> = emptyList())

/**
 * Represents the expectations of a [Test].
 * @property fileToExists [List] of [ExistingFile]
 * @property nonExistent [List] of tasks that should not exist
 * @property success [List] of tasks that should succeed
 * @property upToDate [List] of tasks that should be up-to-date
 * @property failure [List] of tasks that should fail
 * @property outputContains [List] of contents that should be contained in the output
 * @property outputDoesntContain [List] of contents that should not be contained in the output
 */
internal data class Expectation(
    val fileToExists: List<ExistingFile> = emptyList(),
    val nonExistent: List<String> = emptyList(),
    val success: List<String> = emptyList(),
    val upToDate: List<String> = emptyList(),
    val failure: List<String> = emptyList(),
    val outputContains: List<String> = emptyList(),
    val outputDoesntContain: List<String> = emptyList(),
)

/**
 * Represents the permission of a file.
 * @property hasPermission
 */
internal enum class Permission(val hasPermission: File.() -> Boolean) {

    /**
     * Represents the READ permission.
     */
    R(File::canRead),

    /**
     * Represents the WRITE permission.
     */
    W(File::canWrite),

    /**
     * Represents the EXECUTE permission.
     */
    X(File::canExecute),
}

/**
 * Represents the single test to execute.
 * @property name description to understand what this test does
 * @property findRegex
 * @property content
 * @property trim
 * @property permissions
 */
internal data class ExistingFile(
    val name: String,
    val findRegex: List<String> = emptyList(),
    val content: String? = null,
    val trim: Boolean = false,
    val permissions: List<Permission> = emptyList(),
)
