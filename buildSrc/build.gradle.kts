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
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.5.31")
    implementation(Dependency.gradle.coverage)
    implementation(Dependency.gradle.spotless)
    implementation(Dependency.gradle.projectConfig)
}
