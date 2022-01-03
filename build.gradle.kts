/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

import tech.antibytes.gradle.wikidata.dependency.addCustomRepositories

plugins {
    id("tech.antibytes.gradle.wikidata.dependency")

    id("tech.antibytes.gradle.dependency")

    id("tech.antibytes.gradle.wikidata.script.quality-spotless")
}

allprojects {
    repositories {
        addCustomRepositories()
        mavenCentral()
        google()
    }
}

tasks.named<Wrapper>("wrapper") {
    gradleVersion = "7.3.2"
    distributionType = Wrapper.DistributionType.ALL
}
