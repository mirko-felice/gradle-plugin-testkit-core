/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirkofelice.plugin

import org.gradle.api.provider.Property
import java.io.Serializable
import javax.inject.Inject

/**
 * Represents a test configuration, mirroring the [Test][io.github.mirkofelice.core.Test] of the core.
 * @property description [Property] to describe the test
 */
@TestkitTestDSL
open class TestkitTest @Inject constructor(internal val description: Property<String>) : Serializable {

//    private var shouldSucceed: Boolean = true
//    private var output: String = ""
//    private var outputContain: String = ""
//    private var filesToExist: List<String> = listOf()
//    private var filesToNotExist: List<String> = listOf()

//    fun shouldSucceed(shouldSucceed: () -> Boolean) {
//        this.shouldSucceed = shouldSucceed()
//    }
//
//    fun output(output: () -> String) {
//        this.output = output()
//    }
//
//    infix fun String.shouldContain(outputContain: String) {
//        this@TestkitTest.outputContain = outputContain
//    }
//
//    fun filesToExist(filesToExist: () -> List<String>) {
//        this.filesToExist = filesToExist()
//    }
//
//    fun filesToNotExist(filesToNotExist: () -> List<String>) {
//        this.filesToNotExist = filesToNotExist()
//    }

//    fun runTest(): TestResult {
//        println(description)
//        Logger.getLogger(this.toString()).info { "Test Description is: $description" }
//        val location = File("build/resources/test")
//        val testFolder = folder {
//            location.copyRecursively(this.root)
//        }
//
//        val result = GradleRunner
//            .create()
//            .withProjectDir(testFolder.root)
//            .withPluginClasspath()
//            .withArguments(taskName)
//            .run { if (shouldSucceed) build() else buildAndFail() }
//        val testResult = TestResult(result, taskName)

    // TODO all checks

//        if (this.shouldSucceed)
//            assert(testResult.isSucceeded) // OR using Kotest
//        else
//            assert(testResult.isFailed)

//        testResult.output kotestShould this.outputContain
//
//        // Other examples of check
//        // TODO assert files to exist
//        // TODO assert files to not exist
//        return testResult
//    }

//    private fun MyTest.assertExistingFiles(orElse: () -> Unit) {
//        if (!this.filesToExist.map { File("$location/$it") }.all { it.exists() && it.isFile })
//            orElse()
//    }

    private companion object {
        private const val serialVersionUID = 1L
    }
}
