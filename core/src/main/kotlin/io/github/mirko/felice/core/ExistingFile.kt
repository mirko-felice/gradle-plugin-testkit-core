/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirko.felice.core

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
