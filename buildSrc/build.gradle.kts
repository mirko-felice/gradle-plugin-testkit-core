plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation(libs.kotlin)

    implementation(libs.kotlinqa)
    implementation(libs.turtle)
    implementation(libs.sonarqube)

    implementation(libs.dokka)

    implementation(libs.jacocotestkit)
}
