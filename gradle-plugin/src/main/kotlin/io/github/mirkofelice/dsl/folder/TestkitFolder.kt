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
 * @property inProject true if the path has to be in the target project, false otherwise
 */
@TestkitFolderDSL
open class TestkitFolder @Inject constructor(internal val path: Property<String>, internal val inProject: Boolean) :
    Serializable {

    private companion object {
        private const val serialVersionUID = 1L
    }
}
