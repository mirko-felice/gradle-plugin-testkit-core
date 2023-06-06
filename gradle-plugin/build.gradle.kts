import java.util.regex.Pattern

plugins {
    id("library-conventions")
    alias(libs.plugins.gradlepublish)
}

dependencies {
    implementation(project(":core"))
    implementation(gradleKotlinDsl())
    testImplementation(gradleTestKit())
    testImplementation(libs.junit)
}

val projectDescription by extra("Gradle Plugin to help users to test own plugins using testkit.")
val githubUrl: String by project

gradlePlugin {
    plugins {
        website.set(githubUrl)
        vcsUrl.set("$githubUrl.git")
        create("testkit-plugin") {
            id = "$group"
            implementationClass = "io.github.mirkofelice.plugin.TestkitPlugin"
            displayName = "Testkit Gradle Plugin"
            description = projectDescription
            tags.set(listOf("test", "testing", "testkit", "plugin-testing", "gradle-plugin-testing"))
        }
    }
}

tasks {

    jacocoTestReport {
        enabled = false
    }

    publishPlugins {
        onlyIf { Pattern.matches("(([0-9])+(\\.?([0-9]))*)", project.version.toString()) }
    }
}
