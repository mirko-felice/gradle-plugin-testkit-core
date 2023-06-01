/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirkofelice.structure

/**
 * Represents the expectations of a [Test].
 * @property result expected [BuildResult]
 * @property outcomes expected [Outcomes] of the tasks
 * @property output expected [Output] of the build
 * @property files expectations on [Files]
 */
data class Expectation(
    val result: BuildResult = BuildResult.SUCCESS,
    val outcomes: Outcomes = Outcomes(),
    val output: Output = Output(),
    val files: Files = Files(),
)
