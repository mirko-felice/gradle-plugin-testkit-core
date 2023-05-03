plugins {
    id("kotlin-jvm")
    `java-gradle-plugin`
    jacoco
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

tasks {

    withType<Test> {
        useJUnitPlatform()
        testLogging {
            showStandardStreams = true
        }
    }

    test {
        finalizedBy(jacocoTestReport)
    }

    jacocoTestReport {
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

sonarqube.properties {
    val coreSrcPath = project(":core").sourceSets.main.get().allSource.srcDirs.first { it.endsWith("kotlin") }
    val sources = properties["sonar.sources"].toString().replace("]", "").replace("[", "")
    property("sonar.sources", "$sources,$coreSrcPath")
}
