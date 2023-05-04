/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirko.felice.core

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.TaskOutcome

/**
 * Abstract implementation of [TestkitChecker].
 * @param result [BuildResult] used to check
 */
internal abstract class AbstractTestkitChecker(protected val result: BuildResult) : TestkitChecker {

    /**
     * Returns the [org.gradle.testkit.runner.TaskOutcome] of the task given as parameter.
     * @param name name of the task to extract the outcome by
     */
    protected fun outcomeOf(name: String) = result.task(":$name")?.outcome ?: TaskOutcome.FAILED
}
