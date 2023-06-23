/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

import io.github.mirkofelice.api.Testkit
import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

class PluginTest {

    @TempDir
    lateinit var tempDir: File
    private lateinit var buildFile: File
    private lateinit var buildContent: String

    @BeforeEach
    fun setup() {
        buildFile = tempDir.resolve("build.gradle.kts")
        buildContent = """
            plugins {
                id("io.github.mirko-felice.testkit")
            }
            
        """.trimIndent()
    }

    @Test
    fun completeFolders() {
        buildContent += """
            testkit {
                folders {
                    projectFolder("emptyDir")
                    genericFolder(System.getProperty("user.dir") + "/src/test/resources")
                    subFoldersOfProject("emptyDir")
                    subFoldersOf(System.getProperty("user.dir") + "/src/test/resources")
                }
            }
        """.trimIndent()

        tempDir.resolve("emptyDir").also {
            it.mkdir()
            it.resolve("build.gradle.kts").createNewFile()
            val content = """
                tests:
                  - description: "Empty"
                    configuration:
                      tasks:
                        - tasks
                    expectation:
                      result: SUCCESS
            """.trimIndent()
            it.resolve("empty.yaml").writeText(content)
        }

        runGradleBuild(testMode = true)
    }

    @Test
    fun completeTests() {
        buildContent = """
            import io.github.mirkofelice.structure.BuildResult
            import io.github.mirkofelice.structure.Permission
            
        """.trimIndent() + buildContent + """
            testkit {
                tests {
                    folder = "emptyDir"
                    println(folder)
                    test("example") {
                        configuration {
                            tasks = listOf("tasks")
                            println(tasks)
                            options = emptyList()
                            println(options)
                        }
                        expectation {
                            result = BuildResult.SUCCESS
                            println(result)
                            outcomes {
                                success = emptyList()
                                println(success)
                                failed = emptyList()
                                println(failed)
                                skipped = emptyList()
                                println(skipped)
                                noSource = emptyList()
                                println(noSource)
                                fromCache = emptyList()
                                println(fromCache)
                                upToDate = emptyList()
                                println(upToDate)
                                notExecuted = emptyList()
                                println(notExecuted)
                            }
                            output {
                                contains = emptyList()
                                println(contains)
                                doesntContain = emptyList()
                                println(doesntContain)
                            }
                            files {
                                existing {
                                    file("build.gradle.kts") {
                                        content = ""
                                        println(content)
                                        contentRegex = emptyList()
                                        println(contentRegex)
                                        permissions = listOf(Permission.R)
                                        println(permissions)
                                    }
                                }
                            }
                        }
                    }
                    test("required") {
                        id = "test"
                        configuration {
                            tasks = listOf("tasks")
                        }
                    }
                    test("requiring") {
                        requires = "test"
                        configuration {
                            tasks = listOf("tasks")
                        }
                    }
                }
            }
        """.trimIndent()

        tempDir.resolve("emptyDir").also {
            it.mkdir()
            it.resolve("build.gradle.kts").createNewFile()
            val content = """
                tests:
                  - description: "Empty"
                    configuration:
                      tasks: 
                        - tasks
                    expectation:
                      result: SUCCESS
            """.trimIndent()
            it.resolve("empty.yaml").writeText(content)
        }

        runGradleBuild()
    }

    @Test
    fun withJavaGradlePlugin() {
        buildContent = """
            plugins {
                `java-gradle-plugin`
                id("io.github.mirko-felice.testkit")
            }
        """.trimIndent()
        runGradleBuild()
    }

    @Test
    fun wrongTestsFolder() {
        buildContent += """
            testkit {
                tests {
                    folder = System.getProperty("user.dir") + "/src/main"
                }
            }
        """.trimIndent()
        runGradleBuild(failing = true)
    }

    @Test
    fun wrongTestsFolderAsFile() {
        buildContent += """
            testkit {
                tests {
                    folder = System.getProperty("user.dir") + "/src/main/kotlin/io/github/mirkofelice/plugin/TestkitPlugin.kt"
                }
            }
        """.trimIndent()
        runGradleBuild(failing = true)
    }

    @Test
    fun wrongFolder() {
        buildContent += """
            testkit {
                tests {
                    folder = System.getProperty("user.dir") + "/src/main"
                }
            }
        """.trimIndent()
        runGradleBuild(failing = true)
    }

    @Test
    fun wrongFolderAsFile() {
        buildContent += """
            testkit {
                tests {
                    folder = System.getProperty("user.dir") + "/src/main/kotlin/io/github/mirkofelice/plugin/TestkitPlugin.kt"
                }
            }
        """.trimIndent()
        runGradleBuild(failing = true)
    }

    @Test
    fun wrongSubFolder() {
        buildContent += """
            testkit {
                folders {
                    subFoldersOf(file(System.getProperty("user.dir")).parentFile.path + "/tests/src/test/resources/generateFile/basic")
                }
            }
        """.trimIndent()
        runGradleBuild(failing = true)
    }

    @Test
    fun wrongSubFolderAsFile() {
        buildContent += """
            testkit {
                folders {
                    subFoldersOfProject("src/test/resources/build.gradle.kts")
                }
            }
        """.trimIndent()
        runGradleBuild(failing = true)
    }

    @Test
    fun wrongSubFolderMissingGradle() {
        buildContent += """
            testkit {
                folders {
                    subFoldersOfProject("src/test/resources/missGradle")
                }
            }
        """.trimIndent()
        runGradleBuild(failing = true)
    }

    @Test
    fun wrongSubFolderMissingYaml() {
        buildContent += """
            testkit {
                folders {
                    subFoldersOfProject("src/test/resources/missYaml")
                }
            }
        """.trimIndent()
        runGradleBuild(failing = true)
    }

    @Test
    fun withMainAndTestFolders() {
        buildContent += """
            testkit {
                folders {
                    withMainDefault()
                    withTestDefault()
                }
            }
        """.trimIndent()

        runGradleBuild(testMode = true)
    }

    private fun runGradleBuild(failing: Boolean = false, testMode: Boolean = false) {
        buildFile.writeText(buildContent)

        val testkitProperties = javaClass.classLoader.getResource("testkit-gradle.properties")?.readText()
        if (testkitProperties != null) {
            tempDir.resolve("gradle.properties").writeText(testkitProperties)
        }
        val runner = GradleRunner.create()
            .withProjectDir(tempDir)
            .withPluginClasspath()
            .withArguments("testkit")
        if (testMode) runner.withArguments(runner.arguments + "-D${Testkit.TEST_MODE}=true")
        if (failing) runner.buildAndFail() else runner.build()
    }
}
