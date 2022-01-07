/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mock.networking

import tech.antibytes.mediawiki.networking.NetworkingContract
import tech.antibytes.util.test.MockContract
import tech.antibytes.util.test.MockError

internal class PluginConfiguratorStub<FeatureConfiguration : Any, SubConfiguration>(
    var configure: ((FeatureConfiguration, SubConfiguration) -> Unit)? = null
) : NetworkingContract.PluginConfigurator<FeatureConfiguration, SubConfiguration>, MockContract.Mock {
    override fun configure(pluginConfiguration: FeatureConfiguration, subConfiguration: SubConfiguration) {
        configure?.invoke(pluginConfiguration, subConfiguration)
            ?: throw MockError.MissingStub("Missing sideeffect configure")
    }

    override fun clear() {
        configure = null
    }
}
