
import com.lordcodes.turtle.shellRun
import java.util.*

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("pl.droidsonroids.jacoco.testkit")
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

val currentVersion = shellRun {
    git.gitCommand(listOf("tag", "-l")).split("\n").last()
}

val file = file(project.rootDir.path + File.separator + "gradle.properties")
val key = "currentVersion"
val gradleProps = Properties().also {
    it.load(file.inputStream())
}
if (!gradleProps.containsKey(key) || gradleProps.getProperty(key) != currentVersion) {
    gradleProps.setProperty(key, currentVersion)
    gradleProps.store(file.outputStream(), null)
}

inline fun <reified T : Task> Project.disableTrackStateOnWindows() {
    tasks.withType<T>().configureEach {
        doNotTrackState("Windows is a mess and JaCoCo does not work correctly")
    }
}

disableTrackStateOnWindows<Test>()
disableTrackStateOnWindows<JacocoReport>()

tasks {

    withType<Test> {
        dependsOn(generateJacocoTestKitProperties)
        useJUnitPlatform()
        testLogging {
            showStandardStreams = true
        }
    }
}
