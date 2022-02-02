/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.util

import org.junit.Test
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import java.util.Locale

class MwLocaleSpec {
    @Test
    fun `It fulfils MwLocale`() {
        MwLocale("en") fulfils UtilContract.MwLocale::class
    }

    @Test
    fun `Given asLocale is called maps custom MwLanguageTags to IETF BCP 47`() {
        UtilContract.MwLocale.MW_MAPPING.forEach { (mwTag, ietfTag) ->
            MwLocale(mwTag).asLocale() mustBe Locale.forLanguageTag(ietfTag)
        }
    }

    @Test
    fun `Given toLanguageTag is called returns custom MwLanguageTags`() {
        UtilContract.MwLocale.MW_MAPPING.forEach { (mwTag, _) ->
            MwLocale(mwTag).toLanguageTag() mustBe mwTag
        }
    }

    @Test
    fun `Given toLanguageTag is called returns normalized LanguageTags`() {
        // Given
        val tag = "en_US"

        // When
        val actual = MwLocale(tag).toLanguageTag()

        // Then
        actual mustBe tag.lowercase().replace('_', '-')
    }

    @Test
    fun `Given displayLanguage is called returns custom IETF BCP 47 defined displayLanguage`() {
        UtilContract.MwLocale.MW_MAPPING.forEach { (mwTag, ietfTag) ->
            MwLocale(mwTag).displayLanguage mustBe Locale.forLanguageTag(ietfTag).displayLanguage
        }
    }

    @Test
    fun `Given displayName is called returns custom IETF BCP 47 defined displayName`() {
        UtilContract.MwLocale.MW_MAPPING.forEach { (mwTag, ietfTag) ->
            MwLocale(mwTag).displayName mustBe Locale.forLanguageTag(ietfTag).displayName
        }
    }
}
