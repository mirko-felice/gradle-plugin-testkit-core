/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirkofelice.core

/**
 * Represents all the expectations on files.
 * @property existing [List] of [ExistingFile] to check
 */
data class Files(
    val existing: List<ExistingFile> = emptyList(),
)
