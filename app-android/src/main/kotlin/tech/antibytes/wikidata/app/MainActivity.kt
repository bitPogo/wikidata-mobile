/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import tech.antibytes.mediawiki.MwClient
import tech.antibytes.mediawiki.PublicApi
import tech.antibytes.wikibase.store.database.WikibaseDataBase
import tech.antibytes.wikibase.store.entity.domain.EntityStore
import tech.antibytes.wikibase.store.page.domain.PageStore
import tech.antibytes.wikidata.app.termbox.TermboxScreen
import tech.antibytes.wikidata.app.termbox.TermboxViewModel
import tech.antibytes.wikidata.app.ui.theme.WikidataMobileTheme
import tech.antibytes.wikidata.app.util.ConnectivityManager
import tech.antibytes.wikidata.app.util.DatabaseFactory
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WikidataMobileTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {

                }
            }
        }
    }
}
