/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirko.felice.core

/**
 * Represents the expectation of an existing file.
 * @property name name of the file to check
 * @property content content of the file
 * @property permissions [List] of [Permission]
 * @property contentRegex [List] of regex to match in the file content
 */
internal data class ExistingFile(
    val name: String,
    val content: String = "",
    val permissions: List<Permission> = emptyList(),
    val contentRegex: List<String> = emptyList(),
)
