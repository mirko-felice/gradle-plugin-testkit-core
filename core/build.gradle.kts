@file:Suppress("UNSTABLE_API_USAGE")

import org.gradle.jvm.tasks.Jar
import java.util.regex.Pattern

buildscript {
    dependencies {
        classpath(libs.turtle)
    }
}

plugins {
    `java-library`
    `maven-publish`
    signing
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.qa)
    alias(libs.plugins.dokka)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(gradleApi())
    implementation(gradleKotlinDsl())
    implementation(gradleTestKit())
    implementation(libs.kotest)
    implementation(libs.bundles.jackson)
    dokkaHtmlPlugin(libs.dokka.versioning)
}

val javaVersion: String by project

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
            dependsOn(dokkaJavadoc)
            from(dokkaJavadoc.get().outputDirectory)
        }
    }

    withType<PublishToMavenRepository>().configureEach {
        onlyIf { Pattern.matches("(([0-9])+(\\.?([0-9]))*)+(-SNAPSHOT)?", project.version.toString()) }
    }

    withType<Test>().configureEach {
        useJUnitPlatform()
        testLogging {
            showStandardStreams = true
            showCauses = true
            showStackTraces = true
            events(*org.gradle.api.tasks.testing.logging.TestLogEvent.values())
            exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        }
    }
}

val organization: String by project
val githubUrl = "://github.com/$organization/${rootProject.name}"
val projectDescription: String by project
val publicationName = "testkit-core"

publishing {
    publications {
        create<MavenPublication>(publicationName) {
            from(components["java"])
            version = project.version.toString()
            pom {
                groupId = "$group.testkit"
                name.set("$groupId:$artifactId")
                description.set(projectDescription)
                url.set("https$githubUrl")
                packaging = "jar"
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                developers {
                    developer {
                        id.set("mirko-felice")
                        name.set("Mirko Felice")
                        email.set("mirko.felice@studio.unibo.it")
                    }
                }
                scm {
                    connection.set("scm:git:git$githubUrl.git")
                    developerConnection.set("scm:git:ssh$githubUrl.git")
                    url.set("https$githubUrl")
                }
            }
        }
        repositories {
            maven {
                val releasesUrl = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
                val snapshotsUrl = "https://s01.oss.sonatype.org/content/repositories/snapshots/"
                url = uri(if (project.version.toString().endsWith("SNAPSHOT")) snapshotsUrl else releasesUrl)
                credentials {
                    val mavenUsername: String? by project
                    username = mavenUsername
                    val mavenPassword: String? by project
                    password = mavenPassword
                }
            }
        }
    }
}

signing {
    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKey, signingPassword)
    sign(publishing.publications[publicationName])
}
