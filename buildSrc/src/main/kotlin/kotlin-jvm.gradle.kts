import com.lordcodes.turtle.shellRun
import java.util.*

plugins {
    id("org.jetbrains.kotlin.jvm")
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

val previousVersion = shellRun {
    git.gitCommand(listOf("tag", "-l")).split("\n").takeLast(2).first()
}

val currentVersion = shellRun {
    git.gitCommand(listOf("tag", "-l")).split("\n").last()
}

val file = file(project.rootDir.path + File.separator + "gradle.properties")
val key = "previousProjectVersion"
val gradleProps = Properties().also {
    it.load(file.inputStream())
}
if (!gradleProps.containsKey(key) || gradleProps.getProperty(key) != previousVersion) {
    gradleProps.setProperty(key, previousVersion)
    gradleProps.store(file.outputStream(), null)
}
