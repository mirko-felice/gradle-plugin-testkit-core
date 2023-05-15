package io.github.mirkofelice.plugin

import io.github.mirkofelice.api.CheckerType
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.property
import java.io.File
import java.io.Serializable
import javax.inject.Inject

/**
 * Entity describing a test.
 */
open class TestkitTest @Inject constructor(objects: ObjectFactory) : Serializable {

    /**
     * [Property] describing the name of the folder containing the yaml file. Default to 'src/main/resources'
     */
    val testFolderName: Property<String> = objects.property<String>().convention(defaultFolder)

    /**
     * [Property] describing the [CheckerType] to use. Default to [CheckerType.KOTLIN]
     */
    val checkerType: Property<CheckerType> = objects.property<CheckerType>().convention(CheckerType.KOTLIN)

    /**
     * [Property] describing if the user wants to see the output of the gradle build. Default to false.
     */
    val forwardOutput: Property<Boolean> = objects.property<Boolean>().convention(false)

    private companion object {
        private const val serialVersionUID = 1L
        private val sep = File.separator
        private val defaultFolder = "src${sep}main${sep}resources$sep"
    }
}
