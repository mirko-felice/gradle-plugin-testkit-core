@file:Suppress("UNSTABLE_API_USAGE")

plugins {
    id("kotlin-jvm")
    `java-gradle-plugin`
    `jacoco-report-aggregation`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(gradleKotlinDsl())
    testImplementation(project(":core"))
    testImplementation(libs.kotest)
    jacocoAggregation(project(":gradle-plugin"))
    jacocoAggregation(project(":core"))
}

gradlePlugin {
    plugins {
        create("hello") {
            id = "io.github.mirko-felice.plugins.hello"
            implementationClass = "io.github.mirkofelice.plugins.hello.HelloGradle"
        }
        create("generateFile") {
            id = "io.github.mirko-felice.plugins.file"
            implementationClass = "io.github.mirkofelice.plugins.file.GenerateFilePlugin"
        }
    }
}

tasks {

    test {
        finalizedBy(testCodeCoverageReport)
    }

    testCodeCoverageReport {
        doFirst {
            Thread.sleep(5000) // Wait to finish writing jacoco/test.exec
        }
    }
}
