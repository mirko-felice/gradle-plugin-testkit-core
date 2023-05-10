@file:Suppress("UNSTABLE_API_USAGE")

plugins {
    id("library-conventions")
}

dependencies {
    implementation(gradleKotlinDsl())
    implementation(gradleTestKit())
    implementation(libs.bundles.jackson)
}

val githubUrl: String by project
val projectDescription: String by project

publishing {
    publications {
        create<MavenPublication>(project.name) {
            from(components["java"])
            version = project.version.toString()
            pom {
                groupId = group.toString()
                name.set("$groupId:$artifactId")
                description.set(projectDescription)
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
                    connection.set("scm:git:git$githubUrl.git") // TODO da controllare
                    developerConnection.set("scm:git:ssh$githubUrl.git")
                    url.set(githubUrl)
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
    sign(publishing.publications[project.name])
}
