import com.lordcodes.turtle.shellRun
import java.util.*

plugins {
    id("org.sonarqube")
}

rootProject.version = shellRun {
    git.gitCommand(listOf("describe", "--tags", "--always"))
}.let {
    if (it.contains("-")) it.substringBefore("-") + "-SNAPSHOT" else it
}

subprojects.forEach { it.version = rootProject.version }

val organization: String by project
val githubUrl = "://github.com/$organization/${rootProject.name}"
val projectDescription: String by project

sonarqube.properties {
    val httpUrl = "https$githubUrl"
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
    property("sonar.links.homepage", httpUrl)
    property("sonar.links.ci", "$httpUrl/actions")
    property("sonar.links.scm", httpUrl)
    property("sonar.links.issue", "$httpUrl/issues")
}
