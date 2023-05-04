import org.apache.tools.ant.taskdefs.condition.Os

plugins {
    id("kotlin-jvm")
    `java-gradle-plugin`
    jacoco
    alias(libs.plugins.jacoco.testkit)
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
            implementationClass = "io.github.mirko.felice.plugins.hello.HelloGradle"
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
        val coreProjectDir = project(":core")

        val coreSrcPath = coreProjectDir.sourceSets.main.get().allSource.srcDirs
        val pluginSrcPath = project.sourceSets.main.get().allSource.srcDirs
        sourceDirectories.setFrom(coreSrcPath, pluginSrcPath)

        val coreClassesPath = getClassesFromSrc(coreProjectDir)
        val pluginClassesPath = getClassesFromSrc(project)
        classDirectories.setFrom(coreClassesPath, pluginClassesPath)

        reports.html.required.set(true)
        reports.xml.required.set(true)
    }
}

fun getClassesFromSrc(project: Project): String {
    val sep = File.separator
    val srcPartialPath = "src${sep}main${sep}kotlin"
    val classesPartialPath = "build${sep}classes${sep}kotlin${sep}main"
    return project.sourceSets.main.get().allSource.srcDirs.first { it.endsWith("kotlin") }.path.replace(srcPartialPath, classesPartialPath)
}
