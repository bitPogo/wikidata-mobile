/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.mock

import tech.antibytes.wikidata.app.util.UtilContract
import java.util.Locale

data class MwLocaleAndroidStub(
    override val displayLanguage: String,
    override val displayName: String,
    var language: Locale
) : UtilContract.MwLocale {
    override fun toLanguageTag(): String = language.toLanguageTag()

    override fun asLocale(): Locale = language
}
