plugins {
    id("com.gradle.enterprise") version "3.13.1"
    id("org.danilopianini.gradle-pre-commit-git-hooks") version "1.1.7"
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
