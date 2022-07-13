import tech.antibytes.gradle.wikidata.dependency.Dependency
import tech.antibytes.gradle.wikidata.dependency.addCustomRepositories

plugins {
    `kotlin-dsl`

    id("tech.antibytes.gradle.wikidata.dependency")
}

repositories {
    gradlePluginPortal()
    mavenCentral()
    google()
    addCustomRepositories()
}

dependencies {
    implementation(Dependency.gradle.dependency)
    implementation(Dependency.gradle.serialization)
    implementation(Dependency.gradle.coverage)
    implementation(Dependency.gradle.spotless)
    implementation(Dependency.gradle.projectConfig)
    implementation(Dependency.gradle.runtimeConfig)
    implementation(Dependency.gradle.sqldelight)
    implementation(Dependency.gradle.hilt)
}
