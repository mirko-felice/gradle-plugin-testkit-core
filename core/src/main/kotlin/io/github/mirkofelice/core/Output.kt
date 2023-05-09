/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirkofelice.core

/**
 * Represents the expectations of the output.
 * @property contains [List] of contents that should be contained in the output
 * @property doesntContain [List] of contents that should not be contained in the output
 */
internal data class Output(
    val contains: List<String> = emptyList(),
    val doesntContain: List<String> = emptyList(),
)
