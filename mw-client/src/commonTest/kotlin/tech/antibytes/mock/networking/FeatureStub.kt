/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mock.networking

import io.ktor.client.HttpClient
import io.ktor.client.features.HttpClientFeature
import io.ktor.util.AttributeKey

class FeatureStub {
    class Config

    companion object Feature : HttpClientFeature<Config, FeatureStub> {
        override val key: AttributeKey<FeatureStub> = AttributeKey("FeatureStub")

        override fun install(feature: FeatureStub, scope: HttpClient) = Unit

        override fun prepare(block: Config.() -> Unit): FeatureStub {
            Config().apply(block)
            return FeatureStub()
        }
    }
}
