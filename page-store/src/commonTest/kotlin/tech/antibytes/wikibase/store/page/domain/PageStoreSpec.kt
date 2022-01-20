/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.page.domain

import tech.antibytes.util.test.fulfils
import tech.antibytes.wikibase.store.page.PageStoreContract
import kotlin.test.Test

class PageStoreSpec {
    @Test
    fun `It fulfils PageStoreFactory`() {
        PageStore fulfils PageStoreContract.PageStoreFactory
    }

    @Test
    fun `Given getInstance is called it retuns a PageStore`() {

    }
}
