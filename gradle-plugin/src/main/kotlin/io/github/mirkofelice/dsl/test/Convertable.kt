/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirkofelice.dsl.test

/**
 * Represents an entity able to be converted in another entity.
 */
internal fun interface Convertable<T> {

    /**
     * Converts the object into the other object.
     */
    fun convert(): T
}
