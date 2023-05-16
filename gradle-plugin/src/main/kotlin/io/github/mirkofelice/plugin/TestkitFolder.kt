package io.github.mirkofelice.plugin

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
