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

subprojects {
    version = rootProject.version
    if (!name.equals("tests"))
        apply(plugin = "org.jetbrains.dokka")
}

val organization: String by project
val githubUrl: String by project
val projectDescription: String by project

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
    property("sonar.projectDescription", projectDescription)
    property("sonar.projectVersion", project.version.toString())
    property("sonar.scm.provider", "git")
    property("sonar.verbose", "true")
    property("sonar.links.homepage", githubUrl)
    property("sonar.links.ci", "$githubUrl/actions")
    property("sonar.links.scm", githubUrl)
    property("sonar.links.issue", "$githubUrl/issues")
}

val javaVersion: String by project

tasks.dokkaHtml {
    dokkaSourceSets {
        configureEach {
            moduleName.set("Gradle Plugin Testkit")
            reportUndocumented.set(true)
            documentedVisibilities.set(
                setOf(
                    DokkaConfiguration.Visibility.PUBLIC,
                    DokkaConfiguration.Visibility.PROTECTED,
                    DokkaConfiguration.Visibility.INTERNAL,
                ),
            )
            jdkVersion.set(javaVersion.toInt())
//            println(languageVersion.get())
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
