/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirkofelice.dsl.folder

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import org.gradle.kotlin.dsl.listProperty
import org.gradle.kotlin.dsl.newInstance
import org.gradle.kotlin.dsl.property
import java.io.File
import java.io.Serializable
import javax.inject.Inject

/**
 * Represents the folders containing the tests files.
 */
@TestkitFolderDSL
open class TestkitFolders @Inject constructor(private val objects: ObjectFactory) : Serializable {

    internal val folders: ListProperty<TestkitFolder> = objects.listProperty()

    /**
     * Adds a new folder, that has to be a subfolder of the target project.
     * @param path path of the folder containing the tests files
     */
    fun projectFolder(path: String) {
        addFolder(path, true)
    }

    /**
     * Adds a new generic folder.
     * @param path path of the folder containing the tests files
     */
    fun genericFolder(path: String) {
        addFolder(path, false)
    }

    /**
     * Adds all the sub-folders, that have to exist in the target project.
     * @param path path of the main folder containing the sub-folders containing the tests files
     */
    fun subFoldersOfProject(path: String) {
        addSubFolders(path, true)
    }

    /**
     * Adds all the sub-folders starting from a generic folder.
     * @param path path of the main folder containing the sub-folders containing the tests files
     */
    fun subFoldersOf(path: String) {
        addSubFolders(path, false)
    }

    /**
     * Automatically adds the project folder with the default main path ('src/main/resources').
     */
    fun withMainDefault() {
        projectFolder(DEFAULT_MAIN_PATH)
    }

    /**
     * Automatically adds the project folder with the default test path ('src/test/resources').
     */
    fun withTestDefault() {
        projectFolder(DEFAULT_TEST_PATH)
    }

    private fun addSubFolders(path: String, inProject: Boolean) {
        val folder = File(path)
        require(folder.isDirectory) { "File '$folder' does not exist or is not a folder!" }
        val subFolders = folder.walk()
            .filter {
                it.isDirectory &&
                    it.walk().maxDepth(1).any { f -> f.name == "build.gradle.kts" } &&
                    it.walk().maxDepth(1).any { f -> f.name.endsWith(".yaml") }
            }
            .toList()
        require(subFolders.isNotEmpty()) {
            "Folder '$folder' does not contain any subfolder containing 'build.gradle.kts' and a yaml file!"
        }
        subFolders.forEach { addFolder(it.path, inProject) }
    }

    private fun addFolder(path: String, inProject: Boolean) =
        folders.add(
            objects.newInstance(TestkitFolder::class, objects.property(String::class).value(path), inProject),
        )

    private companion object {
        private const val serialVersionUID = 1L
        private val sep = File.separator
        private val DEFAULT_MAIN_PATH = path("main")
        private val DEFAULT_TEST_PATH = path("test")
        private fun path(folder: String) = "src$sep$folder${sep}resources$sep"
    }
}
