plugins {
    id("library-conventions")
    id("com.gradle.plugin-publish") version "1.2.0"
}

dependencies {
    implementation(project(":core"))
    implementation(gradleKotlinDsl())
}

val projectDescription = "Gradle Plugin to facilitate use of the testkit."
val githubUrl: String by project

gradlePlugin {
    plugins {
        website.set(githubUrl)
        vcsUrl.set("$githubUrl.git")
        create("testkit-plugin") {
            id = "$group"
            implementationClass = "io.github.mirkofelice.TestkitPlugin"
            displayName = "Testkit Gradle Plugin"
            description = projectDescription
            tags.set(listOf("test", "testing", "testkit", "plugin-testing", "gradle-plugin-testing"))
        }
    }
}
