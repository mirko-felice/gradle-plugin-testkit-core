/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirkofelice.plugins.file

import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.listProperty
import org.gradle.kotlin.dsl.property
import org.gradle.kotlin.dsl.register
import java.io.File
import java.io.Serializable

enum class Permission {
    R, W, X
}

open class GenerateFileTask : DefaultTask() {

    @Input
    val fileName: Property<String> = project.objects.property()

    @Input
    @Optional
    val content: Property<String> = project.objects.property()

    @Input
    @Optional
    val permissions: ListProperty<Permission> = project.objects.listProperty()

    @OutputFile
    val file: Provider<File> = fileName.map { File(project.rootDir.path + File.separator + it) }

    @TaskAction
    fun generateFile() {
        val file = file.get()
        file.writeText(content.get())
        permissions.get().setPermissions(file, true)
        val difference = Permission.values().toList().minus(permissions.get().toSet())
        difference.setPermissions(file, false)
    }

    private fun List<Permission>.setPermissions(file: File, setter: Boolean) {
        forEach {
            logger.quiet("File set " +
                    when (it) {
                        Permission.R -> "readable: ${file.setReadable(setter, true)}"
                        Permission.W -> "writeable: ${file.setWritable(setter, true)}"
                        Permission.X -> "executable: ${file.setExecutable(setter, true)}"
                    }
            )
        }
    }
}

open class GenerateFileExtension(objects: ObjectFactory) : Serializable {

    val fileName: Property<String> = objects.property()

    val content: Property<String> = objects.property<String>().convention("example")

    val permissions: ListProperty<Permission> = objects.listProperty<Permission>().convention(listOf(Permission.R))

    private companion object {
        private const val serialVersionUID = 1L
    }
}

open class GenerateFilePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        val extension = target.extensions.create<GenerateFileExtension>("generateFile")
        target.tasks.register<GenerateFileTask>("generateFile") {
            fileName.set(extension.fileName)
            content.set(extension.content)
            permissions.set(extension.permissions)
        }
    }
}
