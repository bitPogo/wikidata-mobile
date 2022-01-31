/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.util

import android.content.Context
import android.net.ConnectivityManager
import tech.antibytes.mediawiki.PublicApi

// TODO: Make checker for SDK 31 and above
class ConnectivityManager(
    private val context: Context
) : PublicApi.ConnectivityManager {
    override fun hasConnection(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        @Suppress("DEPRECATION")
        return connectivityManager.activeNetworkInfo?.isConnected ?: false
    }
}
