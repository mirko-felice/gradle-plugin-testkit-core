/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

class PluginTest {

    @TempDir
    lateinit var tempDir: File
    private lateinit var buildFile: File

    @BeforeEach
    fun setup() {
        buildFile = tempDir.resolve("build.gradle.kts")
    }

    @Test
    fun pluginTest() {
        val buildContent = """
            plugins {
                id("io.github.mirko-felice.testkit")
            }
            
            testkit {
                folders {
                    withMainDefault()
                    withTestDefault()
                }
                tests {
                    folder = file(System.getProperty("user.dir") + "/src/test/resources")
                    test("example") {
                        configuration {
                            tasks = emptyList()
                            options = emptyList()
                            println(options)
                        }
                    }
                }
            }
            
        """.trimIndent()
        runGradleBuild(buildContent)
    }

    @Test
    fun testWithJavaGradlePlugin() {
        val buildContent = """
            plugins {
                `java-gradle-plugin`
                id("io.github.mirko-felice.testkit")
            }
        """.trimIndent()
        runGradleBuild(buildContent)
    }

    private fun runGradleBuild(buildContent: String) {
        buildFile.writeText(buildContent)

        val testkitProperties = javaClass.classLoader.getResource("testkit-gradle.properties")?.readText()
        if (testkitProperties != null) {
            tempDir.resolve("gradle.properties").writeText(testkitProperties)
        }
        GradleRunner.create()
            .withProjectDir(tempDir)
            .withPluginClasspath()
            .withArguments("testkit")
            .build()
    }
}
