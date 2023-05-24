plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation(libs.kotlin)
    implementation(libs.sonarqube)
    implementation(libs.turtle)
    implementation(libs.dokka)
    implementation(libs.kotlin.qa)
    implementation(libs.jacoco.testkit)
}
