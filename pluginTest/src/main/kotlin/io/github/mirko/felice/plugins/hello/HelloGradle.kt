/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirko.felice.plugins.hello

import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.property
import org.gradle.kotlin.dsl.register
import java.io.File
import java.io.Serializable

open class HelloTask : DefaultTask() {

    @Input
    val author: Property<String> = project.objects.property()

    @Internal
    val message: Provider<String> = author.map { "hello from $it" }

    @OutputFile
    val testFile: RegularFileProperty = project.objects.fileProperty().apply { set(File("test.txt")) }

    @TaskAction
    fun print() {
        logger.quiet(message.get())
        val file = testFile.asFile.get()
        file.writeText("example")
        if (!file.setReadOnly()) logger.quiet("Cannot set file read only.")
    }
}

open class HelloExtension(objects: ObjectFactory) : Serializable {

    val author: Property<String> = objects.property()

    companion object {
        private const val serialVersionUID = 1L
    }
}

open class HelloGradle : Plugin<Project> {

    override fun apply(target: Project) {
        val extension = target.extensions.create<HelloExtension>("hello")
        target.tasks.register<HelloTask>("hello") {
            author.set(extension.author)
        }
    }
}
