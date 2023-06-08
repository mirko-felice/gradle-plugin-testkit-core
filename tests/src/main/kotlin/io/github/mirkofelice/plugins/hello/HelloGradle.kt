/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirkofelice.plugins.hello

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.property
import org.gradle.kotlin.dsl.register
import java.io.Serializable

open class HelloTask : DefaultTask() {

    @Input
    val author: Property<String> = project.objects.property()

    @TaskAction
    fun print() {
        logger.quiet("hello from ${author.get()}")
    }
}

open class FailTask : DefaultTask() {

    @TaskAction
    fun fail() {
        throw GradleException("Just to fail the task")
    }
}

open class HelloExtension(objects: ObjectFactory) : Serializable {

    val author: Property<String> = objects.property()

    private companion object {
        private const val serialVersionUID = 1L
    }
}

open class HelloGradle : Plugin<Project> {

    override fun apply(target: Project) {
        val extension = target.extensions.create<HelloExtension>("hello")
        target.tasks.register<HelloTask>("hello") {
            author.set(extension.author)
        }
        target.tasks.register<FailTask>("fail")
    }
}
