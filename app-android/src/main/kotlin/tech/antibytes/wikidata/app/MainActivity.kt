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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import tech.antibytes.mediawiki.MwClient
import tech.antibytes.mediawiki.PublicApi
import tech.antibytes.wikibase.store.database.WikibaseDataBase
import tech.antibytes.wikibase.store.page.domain.PageStore
import tech.antibytes.wikidata.app.termsearch.TermSearchScreen
import tech.antibytes.wikidata.app.termsearch.TermSearchViewModel
import tech.antibytes.wikidata.app.ui.theme.WikidataMobileTheme
import tech.antibytes.wikidata.app.util.DatabaseFactory
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WikidataMobileTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    TermSearchScreen(
                        viewModel = TermSearchViewModel(
                            store = PageStore.getInstance(
                                MwClient.getInstance(
                                    "test.wikidata.org",
                                    object : PublicApi.Logger {
                                        override fun info(message: String) { }

                                        override fun warn(message: String) { }

                                        override fun error(exception: Throwable, message: String?) { }

                                        override fun log(message: String) { }
                                    },
                                    { true },
                                    { CoroutineScope(Dispatchers.IO) }
                                ),
                                DatabaseFactory.create(
                                    WikibaseDataBase.Schema,
                                    applicationContext
                                ).pageQueries,
                                { CoroutineScope(Dispatchers.IO) },
                                { CoroutineScope(Dispatchers.Default) }
                            ),
                            currentLanguage = MutableStateFlow(Locale.GERMAN)
                        )
                    )
                }
            }
        }
    }
}
