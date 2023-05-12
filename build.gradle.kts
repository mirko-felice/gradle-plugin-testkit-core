import com.lordcodes.turtle.shellRun
import org.jetbrains.dokka.DokkaConfiguration
import java.net.URL
import java.util.*

plugins {
    id("org.sonarqube")
    id("org.jetbrains.dokka")
}

repositories {
    mavenCentral()
}

rootProject.version = shellRun {
    git.gitCommand(listOf("describe", "--tags", "--always"))
}.let {
    if (it.contains("-")) it.substringBefore("-") + "-SNAPSHOT" else it
}

val javaVersion: String by project

subprojects {
    version = rootProject.version
    if (!name.equals("tests")) {
        apply(plugin = "org.jetbrains.dokka")
        tasks.dokkaHtml {
            dependsOn(project(":core").tasks.getByName("compileKotlin"))
            dokkaSourceSets {
                configureEach {
                    reportUndocumented.set(true)
                    documentedVisibilities.set(
                        setOf(
                            DokkaConfiguration.Visibility.PUBLIC,
                            DokkaConfiguration.Visibility.PROTECTED,
                            DokkaConfiguration.Visibility.INTERNAL,
                        ),
                    )
                    includes.setFrom("module.md")
                    jdkVersion.set(javaVersion.toInt())
                    externalDocumentationLink {
                        url.set(URL("https://docs.gradle.org/current/javadoc/"))
                    }
                    sourceLink {
                        localDirectory.set(projectDir.resolve("src"))
                        remoteUrl.set(URL("$githubUrl/tree/master/src"))
                    }
                }
            }
        }
    }
}

val organization: String by project
val githubUrl: String by project
val description: String by project

sonarqube.properties {
    val token = System.getenv()["SONAR_TOKEN"] ?: file("sonar.properties").inputStream().use {
        val sonarProperties = Properties()
        sonarProperties.load(it)
        sonarProperties.getProperty("token")
    }
    property("sonar.login", token)
    property("sonar.organization", organization)
    property("sonar.host.url", "https://sonarcloud.io")
    property("sonar.projectName", rootProject.name)
    property("sonar.projectKey", "${organization}_${rootProject.name}")
    property("sonar.projectDescription", description)
    property("sonar.projectVersion", project.version.toString())
    property("sonar.scm.provider", "git")
    property("sonar.verbose", "true")
    property("sonar.links.homepage", githubUrl)
    property("sonar.links.ci", "$githubUrl/actions")
    property("sonar.links.scm", githubUrl)
    property("sonar.links.issue", "$githubUrl/issues")
}
