/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import tech.antibytes.wikidata.app.ui.atom.AliasEditField
import tech.antibytes.wikidata.app.ui.atom.MultiLineEditableText
import tech.antibytes.wikidata.app.ui.atom.SingleLineEditableText
import tech.antibytes.wikidata.app.ui.theme.WikidataMobileTheme

val aliases = listOf("Lorem", "ipsum", "dolor", "sit", "amet")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WikidataMobileTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = 20.dp,
                                bottom = 10.dp,
                                start = 10.dp,
                                end = 10.dp,
                            )
                            .fillMaxSize(1F)
                    ) {
                        val scope = this

                        item {
                            SingleLineEditableText(
                                label = "Label",
                                value = "",
                                onChange = {},
                                underlineIndicator = true
                            )

                            Spacer(modifier = Modifier.height(15.dp))


                            MultiLineEditableText(
                                "Description",
                                "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.",
                                {},
                                underlineIndicator = true,
                            )

                            Spacer(modifier = Modifier.height(15.dp))

                            Text("Aliases")
                        }

                        itemsIndexed(aliases) { idx, value ->
                            val new = if (value.isEmpty()) {
                                "new alias"
                            } else {
                                null
                            }

                            AliasEditField(
                                label = new,
                                value = value,
                                onChange = {},
                            )
                        }
                    }
                }
            }
        }
    }
}
