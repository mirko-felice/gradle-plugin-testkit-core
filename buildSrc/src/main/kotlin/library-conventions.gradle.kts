import java.util.regex.Pattern

plugins {
    id("kotlin-jvm")
    id("org.danilopianini.gradle-kotlin-qa")
    id("org.jetbrains.dokka")
    `java-library`
    `maven-publish`
    signing
}

kotlin {
    target {
        compilations.all {
            kotlinOptions {
                allWarningsAsErrors = true
                freeCompilerArgs = listOf("-opt-in=kotlin.RequiresOptIn")
            }
        }
    }
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

afterEvaluate {
    publishing {
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
        publications {
            withType<MavenPublication>().configureEach {
                version = project.version.toString()
                pom {
                    groupId = group.toString()
                    name.set("$groupId:$artifactId")
                    description.set(project.extra["projectDescription"].toString())
                    url.set(githubUrl)
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
                        val newUrl = githubUrl.replace("https", "")
                        connection.set("scm:git:git$newUrl.git")
                        developerConnection.set("scm:git:ssh$newUrl.git")
                        url.set(githubUrl)
                    }
                }
            }
        }
    }
}
