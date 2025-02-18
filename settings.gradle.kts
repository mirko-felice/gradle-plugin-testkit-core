pluginManagement.resolutionStrategy.eachPlugin {
    if (requested.id.id.startsWith("io.github.mirko-felice.testkit")) {
        val currentVersion: String by settings
        useVersion(currentVersion)
    }
}

plugins {
    id("com.gradle.enterprise") version "3.19.2"
    id("org.danilopianini.gradle-pre-commit-git-hooks") version "1.1.9"
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.6.0"
}

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
        publishOnFailure()
    }
}

gitHooks {
    preCommit {
        tasks("ktlintCheck")
    }
    commitMsg { conventionalCommits() }
    createHooks(true)
}

rootProject.name = "gradle-plugin-testkit"

include("core")
include("tests")
include("gradle-plugin")
