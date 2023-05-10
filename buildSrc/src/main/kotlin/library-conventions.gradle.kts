import java.util.regex.Pattern

plugins {
    id("kotlin-jvm")
    id("org.danilopianini.gradle-kotlin-qa")
    id("org.jetbrains.dokka")
    `java-library`
    `maven-publish`
    signing
}

val javaVersion: String by project
val githubUrl: String by project

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(javaVersion))
    }
    withSourcesJar()
    withJavadocJar()
}

tasks {

    withType<Jar> {
        if (archiveClassifier.get() == "javadoc") {
            dependsOn(dokkaHtml)
            from(dokkaHtml.get().outputDirectory)
        }
    }

    withType<PublishToMavenRepository> {
        onlyIf { Pattern.matches("(([0-9])+(\\.?([0-9]))*)", project.version.toString()) }
    }
}

signing {
    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKey, signingPassword)
}
