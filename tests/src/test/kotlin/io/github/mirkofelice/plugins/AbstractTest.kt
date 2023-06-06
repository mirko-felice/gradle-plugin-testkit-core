package io.github.mirkofelice.plugins

import io.github.mirkofelice.api.CheckerType
import io.github.mirkofelice.api.Testkit
import io.kotest.core.spec.style.StringSpec

abstract class AbstractTest(body: StringSpec.() -> Unit) : StringSpec(body)

private const val projectName = "tests"

internal fun Testkit.runTestsInsideProject(
    testFolder: String,
    checkerType: CheckerType = CheckerType.KOTLIN,
    forwardOutput: Boolean = false
) {
    test(projectName, DEFAULT_TEST_FOLDER.replace("main", "test") + testFolder, checkerType, forwardOutput)
}
