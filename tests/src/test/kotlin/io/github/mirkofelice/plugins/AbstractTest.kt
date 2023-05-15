package io.github.mirkofelice.plugins

import io.github.mirkofelice.api.CheckerType
import io.github.mirkofelice.api.TestkitRunner
import io.kotest.core.spec.style.StringSpec

abstract class AbstractTest(body: StringSpec.() -> Unit) : StringSpec(body)

private const val projectName = "tests"

internal fun TestkitRunner.runTestsInsideProject(
    testFolder: String,
    checkerType: CheckerType = CheckerType.KOTLIN,
    forwardOutput: Boolean = false
) {
    runTests(projectName, DEFAULT_TEST_FOLDER.replace("main", "test") + testFolder, checkerType, forwardOutput)
}
