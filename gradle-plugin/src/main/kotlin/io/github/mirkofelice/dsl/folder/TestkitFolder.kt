/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirkofelice.dsl.folder

import org.gradle.api.provider.Property
import java.io.Serializable
import javax.inject.Inject

/**
 * Represents a folder containing a path to the tests files.
 * @property path [Property] describing the path of the folder containing the tests files
 */
@TestkitFolderDSL
open class TestkitFolder @Inject constructor(internal val path: Property<String>) : Serializable {

    private companion object {
        private const val serialVersionUID = 1L
    }
}
