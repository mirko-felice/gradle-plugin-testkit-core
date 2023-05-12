@file:Suppress("UNSTABLE_API_USAGE")

plugins {
    id("library-conventions")
}

dependencies {
    implementation(gradleTestKit())
    implementation(libs.bundles.jackson)
}

val description: String by project
val projectDescription by extra(description)

publishing {
    publications {
        create<MavenPublication>(project.name) {
            from(components["java"])
        }
    }
}

signing {
    sign(publishing.publications[project.name])
}
