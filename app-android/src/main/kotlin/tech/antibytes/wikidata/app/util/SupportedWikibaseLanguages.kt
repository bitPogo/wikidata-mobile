/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.util

import java.util.Locale

object SupportedWikibaseLanguages : UtilContract.SupportedWikibaseLanguages {
    override fun get(): List<Locale> {
        return UtilContract.SupportedWikibaseLanguages.LANGUAGES.map { tag -> Locale.forLanguageTag(tag) }
    }
}
