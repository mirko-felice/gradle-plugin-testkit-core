/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirko.felice.plugins.file

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

internal enum class Permission {
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
    val permissions: ListProperty<String> = project.objects.listProperty()

    private val realPermissions: Provider<List<Permission>> = permissions.map { it
        .map { permission ->
            if (Permission.values().map(Permission::name).contains(permission))
                Permission.valueOf(permission)
            else
                throw IllegalArgumentException("Permission with name $permission does not exist.")
        }
    }

    @OutputFile
    val file: Provider<File> = fileName.map { File(project.rootDir.path + File.separator + it) }

    @TaskAction
    fun generateFile() {
        val file = file.get()
        file.writeText(content.get())
        realPermissions.get().forEach {
            logger.quiet("File set " +
                when (it) {
                    Permission.R -> "readable: ${file.setReadable(true, true)}"
                    Permission.W -> "writeable: ${file.setWritable(true, true)}"
                    Permission.X -> "executable: ${file.setExecutable(true, true)}"
                }
            )
        }
        val difference = Permission.values().toList().minus(realPermissions.get())
        difference.forEach {
            println(it)
            when (it) {
                Permission.R -> "readable: ${file.setReadable(false, true)}"
                Permission.W -> "writeable: ${file.setWritable(false, true)}"
                Permission.X -> "executable: ${file.setExecutable(false, true)}"
            }
        }
    }
}

open class GenerateFileExtension(objects: ObjectFactory) : Serializable {

    val fileName: Property<String> = objects.property()

    val content: Property<String> = objects.property<String>().convention("example")

    val permissions: ListProperty<String> = objects.listProperty<String>().convention(emptyList())

    companion object {
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
