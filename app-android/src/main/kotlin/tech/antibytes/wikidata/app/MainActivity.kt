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
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import tech.antibytes.mediawiki.MwClient
import tech.antibytes.mediawiki.PublicApi
import tech.antibytes.wikibase.store.user.domain.UserStore
import tech.antibytes.wikidata.app.languageselector.LanguageSelectorScreen
import tech.antibytes.wikidata.app.languageselector.LanguageSelectorViewModel
import tech.antibytes.wikidata.app.login.LoginScreen
import tech.antibytes.wikidata.app.login.LoginViewModel
import tech.antibytes.wikidata.app.ui.theme.WikidataMobileTheme
import tech.antibytes.wikidata.app.util.SupportedWikibaseLanguages
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WikidataMobileTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    LanguageSelectorScreen(
                        viewModel = LanguageSelectorViewModel(
                            MutableStateFlow(Locale.GERMAN),
                            SupportedWikibaseLanguages.get(),
                            CoroutineScope(Dispatchers.Default)
                        )
                    )
                }
            }
        }
    }
}
