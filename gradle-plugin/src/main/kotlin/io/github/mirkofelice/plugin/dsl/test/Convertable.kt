/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirkofelice.plugin.dsl.test

internal interface Convertable<T> {

    fun toDataClass(): T
}
