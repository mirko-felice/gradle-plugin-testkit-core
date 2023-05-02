/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirko.felice

import org.gradle.internal.impldep.org.apache.commons.lang.StringUtils
import java.io.File

/**
 * Represents the yaml root.
 * @property tests [List] of [Test]
 */
data class YamlTests(val tests: List<Test>)

/**
 * Represents the single test to execute.
 * @property description description to understand what this test does
 * @property configuration
 * @property expectation
 */
data class Test(
    val description: String,
    val configuration: Configuration,
    val expectation: Expectation,
)

/**
 * Represents the single test to execute.
 * @property tasks description to understand what this test does
 * @property options a
 */
data class Configuration(val tasks: List<String>, val options: List<String> = emptyList())

/**
 * Represents the single test to execute.
 * @property fileToExists description to understand what this test does
 * @property success
 * @property failure
 * @property outputContains
 * @property outputDoesntContain
 */
data class Expectation(
    val fileToExists: List<ExistingFile> = emptyList(),
    val success: List<String> = emptyList(),
    val failure: List<String> = emptyList(),
    val outputContains: List<String> = emptyList(),
    val outputDoesntContain: List<String> = emptyList(),
)

/**
 * Represents the single test to execute.
 */
enum class Permission(private val hasPermission: File.() -> Boolean) {
    R(File::canRead), W(File::canWrite), X(File::canExecute);

    /**
     *
     */
    fun requireOnFile(file: File) = require(file.hasPermission()) {
        "File ${file.absolutePath} must have permission $name, but it does not."
    }
}

/**
 * Represents the single test to execute.
 * @property name description to understand what this test does
 * @property findRegex
 * @property content
 * @property trim
 * @property permissions
 */
data class ExistingFile(
    val name: String,
    val findRegex: List<String> = emptyList(),
    val content: String? = null,
    val trim: Boolean = false,
    val permissions: List<Permission> = emptyList(),
    ) {
    /**
     *
     */
    fun validate(actualFile: File): Unit = with(actualFile) {
        require(exists()) {
            "File $name does not exist."
        }
        if (content != null) {
            val text = readText()
            require(text == content) {
                """
                Content of $name does not match expectations.
                
                Expected:
                $content
                
                Actual:
                $text
                
                Difference starts at index ${StringUtils.indexOfDifference(content, text)}:
                ${StringUtils.difference(content, text)}
                """.trimIndent()
            }
        }
        findRegex.forEach { regexString ->
            val regex = Regex(regexString)
            requireNotNull(readLines().find { regex.matches(it) }) {
                """
                None of the lines in $name matches the regular expression $findRegex. File content:
                ${readText()}
                """.trimIndent()
            }
        }
        permissions.forEach { it.requireOnFile(this) }
    }
}
