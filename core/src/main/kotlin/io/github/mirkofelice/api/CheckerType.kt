/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirkofelice.api

/**
 * Represents the type of the [TestkitChecker][io.github.mirkofelice.core.TestkitChecker]
 * used to check the assertions of the tests.
 */
enum class CheckerType {

    /**
     * Represents the [KotlinChecker][io.github.mirkofelice.core.KotlinChecker] which uses kotlin assertions.
     */
    KOTLIN,
}
