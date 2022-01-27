/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.util

import org.junit.Test
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import tech.antibytes.wikidata.app.util.UtilContract.SupportedWikibaseLanguages.Companion.LANGUAGES
import java.util.Locale

class SupportedWikibaseLanguagesSpec {
    @Test
    fun `It fulfils SupportedWikibaseLanguages`() {
        SupportedWikibaseLanguages fulfils UtilContract.SupportedWikibaseLanguages::class
    }

    @Test
    fun `Given get is called, it returns a List of the supported Languages`() {
        // Given
        val expected = LANGUAGES.map { tag -> Locale.forLanguageTag(tag) }

        // When
        val actual = SupportedWikibaseLanguages.get()

        // Then
        actual mustBe expected
    }
}
