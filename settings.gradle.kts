/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
    }
}

includeBuild("gradlePlugin/wikidata-dependency")

plugins {
    id("com.gradle.enterprise") version("3.7")
}

include(
    "mw-client",
    "mw-client-e2e",
    "entity-store",
    "utils-coroutine"
)

buildCache {
    local {
        isEnabled = false
        directory = File(rootDir, "build-cache")
        removeUnusedEntriesAfterDays = 30
    }
}

rootProject.name = "wikidata-mobile"
