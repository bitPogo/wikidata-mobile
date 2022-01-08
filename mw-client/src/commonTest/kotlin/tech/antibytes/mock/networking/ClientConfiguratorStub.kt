/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

import io.ktor.client.HttpClientConfig
import tech.antibytes.mediawiki.networking.NetworkingContract
import tech.antibytes.util.test.MockContract
import tech.antibytes.util.test.MockError

internal class ClientConfiguratorStub(
    var configure: ((HttpClientConfig<*>, Set<NetworkingContract.Plugin<in Any, in Any?>>?) -> Unit)? = null
) : NetworkingContract.ClientConfigurator, MockContract.Mock {
    override fun configure(
        httpConfig: HttpClientConfig<*>,
        installers: Set<NetworkingContract.Plugin<in Any, in Any?>>?
    ) {
        configure?.invoke(httpConfig, installers) ?: throw MockError.MissingStub("Missing sideeffect configure")
    }

    override fun clear() {
        configure = null
    }
}
