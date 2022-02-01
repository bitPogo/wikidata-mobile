/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.util

import java.util.Locale

class MwLocale(languageTag: String) : UtilContract.MwLocale {
    private val mwLanguageTag: String = languageTag.lowercase()

    private val locale = Locale.forLanguageTag(
        UtilContract.MwLocale.MW_MAPPING.getOrElse(mwLanguageTag) { mwLanguageTag }
    )
    override val displayLanguage: String = locale.displayLanguage

    override fun toLanguageTag(): String = mwLanguageTag

    override fun asLocale(): Locale = locale
}
