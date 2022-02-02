/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.util

object SupportedWikibaseLanguages : UtilContract.SupportedWikibaseLanguages {
    override fun get(): List<UtilContract.MwLocale> {
        return UtilContract.SupportedWikibaseLanguages.LANGUAGES
            .map { tag -> MwLocale(tag) }
            .distinctBy { locale -> locale.displayName }
            .sortedBy { locale -> locale.displayName }
    }
}
