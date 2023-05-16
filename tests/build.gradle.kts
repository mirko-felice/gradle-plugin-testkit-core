@file:Suppress("UNSTABLE_API_USAGE")

import org.apache.tools.ant.taskdefs.condition.Os

plugins {
    id("kotlin-jvm")
    `java-gradle-plugin`
    alias(libs.plugins.jacoco.testkit)
    id("io.github.mirko-felice.testkit")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(gradleKotlinDsl())
    testImplementation(project(":core"))
    testImplementation(libs.kotest)
}

gradlePlugin {
    plugins {
        create("hello") {
            id = "io.github.mirko-felice.plugins.hello"
            implementationClass = "io.github.mirkofelice.plugins.hello.HelloGradle"
        }
        create("generateFile") {
            id = "io.github.mirko-felice.plugins.file"
            implementationClass = "io.github.mirkofelice.plugins.file.GenerateFilePlugin"
        }
    }
}

inline fun <reified T : Task> Project.disableTrackStateOnWindows() {
    tasks.withType<T>().configureEach {
        doNotTrackState("Windows is a mess and JaCoCo does not work correctly")
    }
}

if (Os.isFamily(Os.FAMILY_WINDOWS)) {
    disableTrackStateOnWindows<Test>()
    disableTrackStateOnWindows<JacocoReport>()
}

tasks {

    withType<Test> {
        dependsOn(generateJacocoTestKitProperties)
        useJUnitPlatform()
        testLogging {
            showStandardStreams = true
        }
    }

    test {
        finalizedBy(jacocoTestReport)
    }

    jacocoTestReport {
        dependsOn(test)
        val coreProject = project(":core")
        val pluginProject = project(":gradle-plugin")
        dependsOn(pluginProject.tasks.getByName("compileKotlin"))

        val coreSrcPath = getSrc(coreProject)
        val pluginSrcPath = getSrc(pluginProject)
        val testsSrcPath = getSrc(project)
        sourceDirectories.setFrom(coreSrcPath, testsSrcPath, pluginSrcPath)

        val coreClassesPath = getClassesFromSrc(coreProject)
        val pluginClassesPath = getClassesFromSrc(pluginProject)
        val testsClassesPath = getClassesFromSrc(project)
        classDirectories.setFrom(coreClassesPath, testsClassesPath, pluginClassesPath)

        reports.html.required.set(true)
        reports.xml.required.set(true)
    }
}

fun getSrc(project: Project) = project.sourceSets.main.get().allSource.srcDirs

fun getClassesFromSrc(project: Project): String {
    val sep = File.separator
    val srcPartialPath = "src${sep}main${sep}kotlin"
    val classesPartialPath = "build${sep}classes${sep}kotlin${sep}main"
    return getSrc(project).first { it.endsWith("kotlin") }.path.replace(srcPartialPath, classesPartialPath)
}
